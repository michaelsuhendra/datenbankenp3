package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Anzeige;
import de.unidue.inf.is.domain.Benutzer;
import de.unidue.inf.is.stores.StoreException;
import de.unidue.inf.is.stores.UserStore;

public final class ProfilServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Profil von einem Benutzer
		List<Anzeige> angeboten = new ArrayList<Anzeige>();
		List<Anzeige> gekauft = new ArrayList<Anzeige>();
		Benutzer benutzer = null;
		
		request.setAttribute("pagetitle", "User Profil");
		request.setAttribute("ersteller", request.getParameter("ersteller"));
		request.setAttribute("benutzer", request.getParameter("benutzer"));
		System.out.println("User profile viewed by " + String.valueOf(request.getParameter("benutzer")));
		
		String ersteller = String.valueOf(request.getParameter("ersteller"));
	
		try{
			UserStore userstore = new UserStore();
			benutzer = userstore.getBenutzer(ersteller);			
			angeboten = userstore.angeboteAnzeigen(ersteller);
			gekauft = userstore.gekaufteAnzeigen(ersteller);
	
			userstore.complete();
			userstore.close();
			
		} catch(StoreException e) {
			e.printStackTrace();
		}
		
		request.setAttribute("person", benutzer);
		request.setAttribute("angeboten", angeboten);
		request.setAttribute("anzahl", angeboten.size());
		request.setAttribute("gekauft", gekauft);
		
		try {
        	request.getRequestDispatcher("/user_profil.ftl").forward(request, response);
        } catch(Exception e) {
        	e.printStackTrace();
        }


    }

}

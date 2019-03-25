package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Anzeige;
import de.unidue.inf.is.domain.Kategorie;
import de.unidue.inf.is.stores.StoreException;
import de.unidue.inf.is.stores.UserStore;

public final class EditierenServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Anzeige anzToEdit = new Anzeige();
		List<Kategorie> kategorie = new ArrayList<Kategorie>();
		
    	//Form für Anzeige Editieren
    	request.setAttribute("pagetitle", "Anzeige editieren");
        request.setAttribute("ersteller", request.getParameter("ersteller"));
        
        // ID von Anzeige Details übertragen
        request.setAttribute("anzeigeid", request.getParameter("anzeigeid"));
        
        int anzeigeid = Integer.parseInt(request.getParameter("anzeigeid"));
        
        // Werte aus der ausgewählte Anzeige setzen
        try{
        	UserStore userstore = new UserStore();
        	anzToEdit = userstore.sucheAnzeige(anzeigeid);
        	
        	request.setAttribute("titel", anzToEdit.getTitel());
        	request.setAttribute("preis", anzToEdit.getPreis());
        	request.setAttribute("text", anzToEdit.getText());
        	kategorie = anzToEdit.getKategorie();
        	
        	request.setAttribute("checked1", "");
        	request.setAttribute("checked2", "");
        	request.setAttribute("checked3", "");
        	request.setAttribute("checked4", "");
        	
        	for(int i = 0; i < kategorie.size(); i++) {
        		if(kategorie.get(i).getName().equals("Digitale Waren")) {
        			request.setAttribute("checked1", " checked");
        		}
        		if(kategorie.get(i).getName().equals("Haus & Garten")) {
        			request.setAttribute("checked2", " checked");
        		}
        		if(kategorie.get(i).getName().equals("Mode & Kosmetik")) {
        			request.setAttribute("checked3", " checked");
        		}
        		if(kategorie.get(i).getName().equals("Multimedia & Elektronik")) {
        			request.setAttribute("checked4", " checked");
        		}
        	}
        	
        	userstore.complete();
        	userstore.close();
        } catch (StoreException e) {
        	e.printStackTrace();
        }
        
        
        try {
        	request.getRequestDispatcher("/anzeige_editieren.ftl").forward(request, response);
        } catch (Exception e) {
        	e.printStackTrace();
        }

    }

}

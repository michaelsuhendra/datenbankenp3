package de.unidue.inf.is;

import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import de.unidue.inf.is.domain.Anzeige;
//import de.unidue.inf.is.domain.Kategorie;
//import de.unidue.inf.is.stores.StoreException;
//import de.unidue.inf.is.domain.User;
//import de.unidue.inf.is.stores.UserStore;

public final class ErstellenServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	//Form für Anzeige Erstellen
    	request.setAttribute("pagetitle", "Anzeige erstellen");
		// request.setAttribute("ersteller", request.getParameter("ersteller"));
        request.setAttribute("ersteller", request.getParameter("ersteller"));
        try {
        	request.getRequestDispatcher("/anzeige_erstellen.ftl").forward(request, response);
        } catch (Exception e) {
        	e.printStackTrace();
        }

    }
	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	doGet(request, response);
	}
	
    
}

package de.unidue.inf.is;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Anzeige;
import de.unidue.inf.is.domain.Kategorie;
//import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.StoreException;
import de.unidue.inf.is.stores.UserStore;

public final class AnzeigeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		//String action = (request.getParameter("action") == null) ? "" : request.getParameter("action");
		
		// Hauptseite
			// Liste von alle Anzeigen
	        List<Anzeige> anzeigen = null;
	        
	        request.setAttribute("pagetitle", "Inserator");
	      
	        /*********************************************************************/
	        /** weil wir kein Login-Page haben, hier den Benutzername eintragen! */
	        request.setAttribute("benutzer", "m.nadine");
	        /*********************************************************************/
	        
	        String benutzer = String.valueOf(request.getAttribute("benutzer"));
	        
	        try {
	        	UserStore userstore = new UserStore();
	        	String link = "";
	        	// because id doesn't show in freemarker list 
	        	anzeigen = userstore.alleAnzeigen();
	        	for(Anzeige a : anzeigen) {
	        		
	        		String temp = "<h2><a href=\"/anzeige_details?id=" + a.getID()
	        		+ "&benutzer=" + benutzer + "\" title=\"Click for details\">" + a.getTitel()
	        		+ " </a> </h2>\r\n" + 
	        		"Seit: " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(a.getErstellungsdatum()) + "<b> Preis: " + a.getPreis()
	        		+ " EUR</b> </br>\r\n" + 
	        		"<a href=\"/user_profil?ersteller=" + a.getErsteller() + "&benutzer=" + benutzer + "\">" + a.getErsteller()
	        		+ " </a> </br>\r\n" + 
	        		"</br>";
	        		
	        		link = link + temp;
	        	}
	        	
	        	request.setAttribute("link", link);
	        	userstore.complete();
	        	userstore.close();
	        	
	        } catch(StoreException e) {
	        	e.printStackTrace();
	        }
	        
	        try {
	        	request.getRequestDispatcher("/hauptseite.ftl").forward(request, response);
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
        
		}
/*
    }*/
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Nach Erstellung einer Anzeige zurück zur Hauptseite
		
		// Fehlermeldung wenn preis negativ ist oder Länge der Titel überschreitet 100

        if(Double.parseDouble(request.getParameter("preis")) < 0 || String.valueOf(request.getParameter("titel")).length() > 100) {
        	request.setAttribute("errormessage", "Fehler bei der Eingabe!");
        	// Form für Anzeige Erstellen erneut zeigen
        	request.setAttribute("pagetitle", "Anzeige erstellen");
    		request.setAttribute("ersteller", request.getParameter("ersteller"));
        	try {
				request.getRequestDispatcher("/anzeige_erstellen.ftl").forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			}
        }
		
        else {
			request.setAttribute("pagetitle", "Inserator");
			// der Benutzer ist jetzt der Ersteller
		 // request.setAttribute("ersteller", request.getParameter("benutzer"));
			request.setAttribute("benutzer", request.getParameter("ersteller"));
			request.setAttribute("ersteller", request.getParameter("ersteller"));
			
			String titel = request.getParameter("titel");
			String text = request.getParameter("text");
			BigDecimal preis = new BigDecimal(request.getParameter("preis"));
			String ersteller = request.getParameter("ersteller");
			String[] katArray = request.getParameterValues("kategorie");
			String kat = "";
			for (int i = 0; i < katArray.length; i++) {
				kat = kat + katArray[i];
			}
			
			List<Kategorie> kategorie = new ArrayList<Kategorie>();
			if(kat.contains("Digitale Waren")) {
				kategorie.add(new Kategorie("Digitale Waren"));
			}
			if(kat.contains("Haus & Garten")) {
				kategorie.add(new Kategorie("Haus & Garten"));
			}
			if(kat.contains("Mode & Kosmetik")) {
				kategorie.add(new Kategorie("Mode & Kosmetik"));
			}
			if(kat.contains("Multimedia & Elektronik")) {
				kategorie.add(new Kategorie("Multimedia & Elektronik"));
			}
					
			Anzeige anzToAdd = new Anzeige(titel, text, preis, ersteller, kategorie);
		
			try {
				UserStore userstore = new UserStore();
				userstore.addAnzeige(anzToAdd);
				userstore.complete();
				userstore.close();
			} catch (StoreException e) {
				e.printStackTrace();
			}
			
			// doGet aus Hauptseite
			List<Anzeige> anzeigen = null;
			String benutzer = String.valueOf(request.getAttribute("benutzer"));
			
			try {
	        	UserStore userstore2 = new UserStore();
	        	String link = "";
	        	
	        	anzeigen = userstore2.alleAnzeigen();
	        	for(Anzeige a : anzeigen) {
	        		
	        		String temp = "<h2><a href=\"/anzeige_details?id=" + a.getID()
	        		+ "&benutzer=" + benutzer + "\" title=\"Click for details\">" + a.getTitel()
	        		+ " </a> </h2>\r\n" + 
	        		"Seit: " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(a.getErstellungsdatum()) + "<b> Preis: " + a.getPreis()
	        		+ " EUR</b> </br>\r\n" + 
	        		"<a href=\"/user_profil?ersteller=" + a.getErsteller() + "\">" + a.getErsteller()
	        		+ " </a> </br>\r\n" + 
	        		"</br>";
	        		
	        		link = link + temp;
	        	}
	        	
	        	request.setAttribute("link", link);
	        	userstore2.complete();
	        	userstore2.close();
	        	
	        } catch(StoreException e) {
	        	e.printStackTrace();
	        }
			
			try {
				request.getRequestDispatcher("/hauptseite.ftl").forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			}
			
        }
				
			doGet(request, response);
			
    }
	
}

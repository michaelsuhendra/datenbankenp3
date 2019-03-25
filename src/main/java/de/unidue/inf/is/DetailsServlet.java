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
import de.unidue.inf.is.domain.Kommentar;
import de.unidue.inf.is.stores.StoreException;
//import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.UserStore;

public final class DetailsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Anzeige, Details und Kommentare für eine bestimmte Anzeige anzeigen
		
        Anzeige anzeige = new Anzeige();
        String gekauft = new String();
        List<Kommentar> kommentare = new ArrayList<Kommentar>();
        
        request.setAttribute("pagetitle", "Inserator");
        
        //ID und aktuelle Benutzer von Hauptseite übertragen 
        request.setAttribute("benutzer", request.getParameter("benutzer"));
        request.setAttribute("id", request.getParameter("id"));
        
        int id = Integer.parseInt(request.getParameter("id"));
        
        try {
        	UserStore userstore = new UserStore();
        	anzeige = userstore.sucheAnzeige(id);
        	
        	System.out.println("Displaying Anzeige with id = " + anzeige.getID());
        	System.out.println(anzeige.getStatus());
        	
        	request.setAttribute("titel" , anzeige.getTitel());
        	request.setAttribute("erstellungsdatum" , anzeige.getErstellungsdatum());
        	request.setAttribute("ersteller" , anzeige.getErsteller());
        	request.setAttribute("text" , anzeige.getText());
        	request.setAttribute("preis" , anzeige.getPreis());
        	request.setAttribute("status" , anzeige.getStatus());
        	
        	if(anzeige.getStatus().equals("verkauft")) {
        		gekauft = userstore.verkauftAnzeige(id);
        		request.setAttribute("kaeufer", gekauft);
        	}
        	
        	kommentare = userstore.listKommentar(id);
        	request.setAttribute("allekommentare", kommentare);

        	userstore.complete();
        	userstore.close();
        	
        } catch(StoreException e) {
        	e.printStackTrace();
        }
        
        try {
        	request.getRequestDispatcher("/anzeige_details.ftl").forward(request, response);
        } catch(Exception e) {
        	e.printStackTrace();
        }

    }
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Editieren
        if(request.getParameter("action").equals("editieren")) {
        	
        	if(Double.parseDouble(request.getParameter("preis")) < 0 || String.valueOf(request.getParameter("titel")).length() > 100) {
            	request.setAttribute("errormessage", "Fehler bei der Eingabe!");
            	Anzeige anzToEdit = new Anzeige();
        		List<Kategorie> kategorie = new ArrayList<Kategorie>();
        		
            	//Form für Anzeige Editieren
            	request.setAttribute("pagetitle", "Anzeige editieren");
                request.setAttribute("ersteller", request.getParameter("ersteller"));
                System.out.println("Erstellen edited by: " + String.valueOf(request.getParameter("ersteller")));
                
                // ID von Anzeige Details übertragen
                request.setAttribute("anzeigeid", request.getParameter("id"));
                
                int anzeigeid = Integer.parseInt(request.getParameter("id"));
                
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
        	
        	else {
				String titel = request.getParameter("titel");
				String text = request.getParameter("text");
				BigDecimal preis = new BigDecimal(request.getParameter("preis"));
				String ersteller = request.getParameter("ersteller");
				String[] katArray = request.getParameterValues("kategorie");
				String kat = "";
				for (int i = 0; i < katArray.length; i++) {
					kat = kat + katArray[i];
				}
				System.out.println(kat);
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
						
				Anzeige anzToEdit = new Anzeige(titel, text, preis, ersteller, kategorie);
				anzToEdit.setID(Integer.parseInt(request.getParameter("id")));
				
				//doGet für Anzeige Details
				Anzeige anzeige = new Anzeige();
		        String gekauft = new String();
		        List<Kommentar> kommentare = new ArrayList<Kommentar>();
		        
		        request.setAttribute("pagetitle", "Inserator");
		        
		        //ID und aktuelle Benutzer von Hauptseite übertragen 
		        request.setAttribute("benutzer", request.getParameter("benutzer"));
		        request.setAttribute("id", request.getParameter("id"));
		        
		        int id = Integer.parseInt(request.getParameter("id"));
		        
		        try {
		        	UserStore userstore = new UserStore();
		        	userstore.updateAnzeige(anzToEdit);
		        	userstore.complete();
		        	userstore.close();
		        	
		        } catch(StoreException e) {
		        	e.printStackTrace();
		        }
		        
		        try {
		        	UserStore userstore2 = new UserStore();
		        	anzeige = userstore2.sucheAnzeige(id);
		        	
		        	if(anzeige.getStatus().equals("verkauft")) {
		        		gekauft = userstore2.verkauftAnzeige(id);
		        	}	        	
		        	
		        	kommentare = userstore2.listKommentar(id);
	
		        	userstore2.complete();
		        	userstore2.close();
		        
		        } catch(StoreException e) {
		        	e.printStackTrace();
		        }
		        
		        System.out.println("Displaying Anzeige with id = " + anzeige.getID());
	        	System.out.println(anzeige.getStatus());
	        	
	        	request.setAttribute("titel" , anzeige.getTitel());
	        	request.setAttribute("erstellungsdatum" , anzeige.getErstellungsdatum());
	        	request.setAttribute("ersteller" , anzeige.getErsteller());
	        	request.setAttribute("text" , anzeige.getText());
	        	request.setAttribute("preis" , anzeige.getPreis());
	        	request.setAttribute("status" , anzeige.getStatus());
	        	
	        	request.setAttribute("kaeufer", gekauft);
	        	request.setAttribute("allekommentare", kommentare);
				
				try {
					request.getRequestDispatcher("/anzeige_details.ftl").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				}
			
        	}
        	
        }
		
        // Kaufen
        else if(request.getParameter("action").equals("kaufen")) {
        	
        	String benutzername = String.valueOf(request.getParameter("benutzer"));
        	int anzeigeid = Integer.parseInt(request.getParameter("id"));
        	
        	//doGet aus Anzeige Details
			Anzeige anzeige = new Anzeige();
	        String gekauft = new String();
	        List<Kommentar> kommentare = new ArrayList<Kommentar>();
	        
	        request.setAttribute("pagetitle", "Inserator");
	        
	        //ID und aktuelle Benutzer von Hauptseite übertragen 
	        request.setAttribute("benutzer", request.getParameter("benutzer"));
	        request.setAttribute("id", request.getParameter("id"));
	        
        	try {
        		UserStore userstore = new UserStore();
        		userstore.kaufen(benutzername, anzeigeid);
        		userstore.complete();
        		userstore.close();
        	} catch(StoreException e) {
        		e.printStackTrace();
        	}
        	
        	try {
	        	UserStore userstore2 = new UserStore();
	        	anzeige = userstore2.sucheAnzeige(anzeigeid);
	        	
	        	if(anzeige.getStatus().equals("verkauft")) {
	        		gekauft = userstore2.verkauftAnzeige(anzeigeid);
	        	}	        	
	        	kommentare = userstore2.listKommentar(anzeigeid);

	        	userstore2.complete();
	        	userstore2.close();
	        
	        } catch(StoreException e) {
	        	e.printStackTrace();
	        }
	        
	        System.out.println("Displaying Anzeige with id = " + anzeige.getID());
        	System.out.println(anzeige.getStatus());
        	
        	request.setAttribute("titel" , anzeige.getTitel());
        	request.setAttribute("erstellungsdatum" , anzeige.getErstellungsdatum());
        	request.setAttribute("ersteller" , anzeige.getErsteller());
        	request.setAttribute("text" , anzeige.getText());
        	request.setAttribute("preis" , anzeige.getPreis());
        	request.setAttribute("status" , anzeige.getStatus());
        	
        	request.setAttribute("kaeufer", gekauft);
        	request.setAttribute("allekommentare", kommentare);
        	
        	
        	
        	try {
        		request.getRequestDispatcher("/anzeige_details.ftl").forward(request, response);
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        	
        }
        
        // Löschen
        else if(request.getParameter("action").equals("loeschen")) {
        	
        	int anzeigeid = Integer.parseInt(request.getParameter("id"));
        		
        	// refresh list of Anzeige
	        List<Anzeige> anzeigen = null;
	        
	        request.setAttribute("pagetitle", "Inserator");
	      
	        request.setAttribute("benutzer", request.getParameter("benutzer"));
	        
	        String benutzer = String.valueOf(request.getAttribute("benutzer"));
	        
	        //doGet aus Hauptseite
	        try {
	        	UserStore userstore = new UserStore();
	        	userstore.deleteAnzeige(anzeigeid);
	        	
	        	String link = "";
	        	// because id doesn't show in freemarker
	        	anzeigen = userstore.alleAnzeigen();
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
	        	userstore.complete();
	        	userstore.close();
	        	
	        } catch(StoreException e) {
	        	e.printStackTrace();
	        }
 
        	try {
        		request.getRequestDispatcher("/hauptseite.ftl").forward(request, response);
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        	
        }
        
        // Kommentar: Kommentartexte, die leer sind, wird nicht eingetragen
        else if(request.getParameter("action").equals("kommentar") && !request.getParameter("kommentartext").equals("")) {
        	
        	System.out.println("action kommentar");
        	String text = request.getParameter("kommentartext");
        	String benutzer = request.getParameter("benutzer");
        	int anzeigeid = Integer.parseInt(request.getParameter("id"));
        	Kommentar komToAdd = new Kommentar(0, text, benutzer);
        	
        	//doGet aus Anzeige Details
        	 Anzeige anzeige = new Anzeige();
             String gekauft = new String();
             List<Kommentar> kommentare = new ArrayList<Kommentar>();
             
             request.setAttribute("pagetitle", "Inserator");
             
             //ID und aktuelle Benutzer von Hauptseite übertragen 
             request.setAttribute("benutzer", request.getParameter("benutzer"));
             request.setAttribute("id", request.getParameter("id"));
             
             try{
               	UserStore userstore = new UserStore();
            	userstore.addKommentar(komToAdd, anzeigeid);
            	userstore.complete();
            	userstore.close();
            	
             } catch(StoreException e) {
            	 e.printStackTrace();
             }
             
             try {
               	UserStore userstore2 = new UserStore();
             	anzeige = userstore2.sucheAnzeige(anzeigeid);
             	
               	if(anzeige.getStatus().equals("verkauft")) {
             		gekauft = userstore2.verkauftAnzeige(anzeigeid);
             	}             	
             	
             	kommentare = userstore2.listKommentar(anzeigeid);
             	
             	userstore2.complete();
             	userstore2.close();
             	
             } catch(StoreException e) {
             	e.printStackTrace();
             }
             
            System.out.println("Displaying Anzeige with id = " + anzeige.getID());
          	System.out.println(anzeige.getStatus());
          	
          	request.setAttribute("titel" , anzeige.getTitel());
          	request.setAttribute("erstellungsdatum" , anzeige.getErstellungsdatum());
          	request.setAttribute("ersteller" , anzeige.getErsteller());
          	request.setAttribute("text" , anzeige.getText());
          	request.setAttribute("preis" , anzeige.getPreis());
          	request.setAttribute("status" , anzeige.getStatus());
          	request.setAttribute("kaeufer", gekauft);
          	request.setAttribute("allekommentare", kommentare);
        	
        	try {
        		request.getRequestDispatcher("/anzeige_details.ftl").forward(request, response);
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        	
        }
        
        	doGet(request, response);
        
    }

}

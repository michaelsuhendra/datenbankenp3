package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Nachricht;
import de.unidue.inf.is.stores.StoreException;
import de.unidue.inf.is.stores.UserStore;

public final class NachrichtServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ArrayList<Nachricht> nachrichten = new ArrayList<Nachricht>();
		
		request.setAttribute("pagetitle", "Nachrichten");
        request.setAttribute("absender", request.getParameter("benutzer"));
        request.setAttribute("empfaenger", request.getParameter("ersteller"));
        
        String absender = String.valueOf(request.getParameter("benutzer"));
        String empfaenger = String.valueOf(request.getParameter("ersteller"));
        
        try {
        	UserStore userstore = new UserStore();
        	nachrichten = userstore.listNachrichten(absender, empfaenger);
        	userstore.complete();
        	userstore.close();
        } catch(StoreException e) {
        	e.printStackTrace();
        }
        
        request.setAttribute("nachrichten", nachrichten);
        
        try {
        	request.getRequestDispatcher("/privatnachrichten.ftl").forward(request, response);
        } catch(Exception e) {
        	e.printStackTrace();
        }

    }
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ArrayList<Nachricht> nachrichten = new ArrayList<Nachricht>();
		
		request.setAttribute("pagetitle", "Nachrichten");
        request.setAttribute("absender", request.getParameter("absender"));
        request.setAttribute("empfaenger", request.getParameter("empfaenger"));
        
        String absender = String.valueOf(request.getParameter("absender"));
        String empfaenger = String.valueOf(request.getParameter("empfaenger"));
        String text = String.valueOf(request.getParameter("text"));
        System.out.println(text);
        System.out.println("absender " + absender + "empfänger " + empfaenger);
        
        Nachricht nachToAdd = new Nachricht(0, text, absender, empfaenger);
        
        try {
        	UserStore userstore = new UserStore();
        	userstore.addNachricht(nachToAdd);
        	nachrichten = userstore.listNachrichten(absender, empfaenger);
        	userstore.complete();
        	userstore.close();
        } catch(StoreException e) {
        	e.printStackTrace();
        }
        
        request.setAttribute("nachrichten", nachrichten);
        
        try {
        	request.getRequestDispatcher("/privatnachrichten.ftl").forward(request, response);
        } catch(Exception e) {
        	e.printStackTrace();
        }
        
        doGet(request, response);

    }

}

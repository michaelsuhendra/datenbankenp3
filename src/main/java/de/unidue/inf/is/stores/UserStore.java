package de.unidue.inf.is.stores;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.unidue.inf.is.domain.Anzeige;
import de.unidue.inf.is.domain.Benutzer;
import de.unidue.inf.is.domain.Kategorie;
import de.unidue.inf.is.domain.Kommentar;
import de.unidue.inf.is.domain.Nachricht;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.utils.DBUtil;


// Diese Klasse enthält alle Statements, stellt die Verbindung zum Datenbank her und auch trennt.

public final class UserStore implements Closeable {

    private Connection connection;
    private boolean complete;

    // Konstruktor, um eine Verbindung zur (entfernten) Datenbank mit getExternalConnection herzustellen.
    
    public UserStore() throws StoreException {
        try {
            connection = DBUtil.getExternalConnection("project");
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }
    
    // Hier alle Methoden.

    //Benutzer hinzufügen
    
    public void addUser(User userToAdd) throws StoreException {
        try {
            PreparedStatement preparedStatement = connection
                            .prepareStatement("insert into dbp34.benutzer (benutzername, name) values (?, ?)");
            preparedStatement.setString(1, userToAdd.getFirstname());
            preparedStatement.setString(2, userToAdd.getLastname());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }
    
    //Benutzer auswählen
    
    public User getUser(String benutzer) throws StoreException{
    	User user = null;
    	try {
    		PreparedStatement ps = connection
    				.prepareStatement("select benutzername, name from dbp34.benutzer where benutzername = ?");
    		ps.setString(1, benutzer);
    		ResultSet rs = ps.executeQuery();
    		if(rs.next()) {
    			user = new User(rs.getString(1), rs.getString(2));
    		}
    		
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
    	return user;
    }
    
    // User Profil: Benutzer anhand von Benutzername im User Profil zeigen
    
    public Benutzer getBenutzer(String benutzername) throws StoreException{
    	Benutzer benutzer = null;
    	try {
    		//System.out.println("Method getBenutzer is called");
    		PreparedStatement ps = connection
    				.prepareStatement("select * from dbp34.benutzer where benutzername = ?");
    		ps.setString(1, benutzername);
    		ResultSet rs = ps.executeQuery();
    		if(rs.next()) {
    			benutzer = new Benutzer(rs.getString(1), rs.getString(2), rs.getTimestamp(3));
    		}
    		
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
    	return benutzer;
    }
    
    // Hauptseite: Alle Anzeigen anzeigen, die aktiv (nicht verkauft) sind
    
    public ArrayList<Anzeige> alleAnzeigen() throws StoreException{
    	ArrayList<Anzeige> result = new ArrayList<Anzeige>();
    	try {
    		PreparedStatement ps = connection
    				.prepareStatement("select * from dbp34.anzeige where status = 'aktiv'");
    		ResultSet rs = ps.executeQuery();
    		while(rs.next()) {
    			Anzeige anz = new Anzeige(rs.getInt(1), rs.getString(2), 
						rs.getString(3), rs.getBigDecimal(4), rs.getTimestamp(5), 
						rs.getString(6), rs.getString(7));
    			
    			result.add(anz);

    		}
    		
    	} catch (SQLException e) {
    		throw new StoreException(e);
    	}
    	
    	return result;
    }
    
    // Anzeige erstellen: Insert Anzeige und die zugehörige Kategorie(n)
    
    public void addAnzeige(Anzeige anzToAdd) throws StoreException{
    	int idToAdd = 0;
    	List<Kategorie> kategorie = anzToAdd.getKategorie();
    	//System.out.println("No of categories" + kategorie.size());
    	//System.out.println("Kategorie =" + anzToAdd.getKategorie().get(2).getName());
    	
    	try {
    		PreparedStatement ps = connection
    				.prepareStatement("insert into dbp34.anzeige (titel, text, preis, ersteller, status) values (?, ?, ?, ?, ?)");
    		ps.setString(1, anzToAdd.getTitel());
    		ps.setString(2, anzToAdd.getText());
    		ps.setBigDecimal(3, anzToAdd.getPreis());
    		ps.setString(4, anzToAdd.getErsteller());
    		ps.setString(5, anzToAdd.getStatus());
    		ps.executeUpdate();
    		
    		PreparedStatement psSelect = connection
    				.prepareStatement("select id from dbp34.anzeige order by erstellungsdatum desc fetch first row only");
    		ResultSet rs = psSelect.executeQuery();
    		if(rs.next()) {
    			idToAdd = rs.getInt(1);
    			System.out.println("Kategorie for anzeige" + idToAdd);
    		}
    		
    		//insert Kategorie(n) für die letztgemachte Anzeige
    		int i = 0;
    		PreparedStatement ps2 = connection
    	    				.prepareStatement("insert into dbp34.hatkategorie (anzeigeid, kategorie) values (?, ?)");
    		if(idToAdd != 0) {
    			while(i < kategorie.size()) {
    				ps2.setInt(1, idToAdd);
    				System.out.println(kategorie.get(i).getName());
    				ps2.setString(2, kategorie.get(i).getName());
    				ps2.executeUpdate();
    				i++;
    			}
    		}
    		
    	}
    	catch(SQLException e) {
    		throw new StoreException(e);
    	} 
    }
    
    // Anzeige editieren: Attribute für diese Anzeige aktualisieren, auch die Kategorie(n)
    
    public void updateAnzeige(Anzeige anzToEdit) throws StoreException{
    	
    	try {
    		PreparedStatement ps = connection
    				.prepareStatement("update dbp34.anzeige set titel=?, text=?, preis=? where id=?");
    		ps.setString(1, anzToEdit.getTitel());
    		ps.setString(2, anzToEdit.getText());
    		ps.setBigDecimal(3, anzToEdit.getPreis());
    		ps.setInt(4, anzToEdit.getID());
    		ps.executeUpdate();
    		
    		// Update Kategorie
    		PreparedStatement ps2 = connection
    				.prepareStatement("delete from dbp34.hatkategorie where anzeigeid = ?");
    		ps2.setInt(1, anzToEdit.getID());
    		ps2.executeUpdate();
    		
    		PreparedStatement ps3 = connection
    				.prepareStatement("insert into dbp34.hatkategorie (anzeigeid, kategorie) values (?, ?)");
    		int i = 0;
    		while(i < anzToEdit.getKategorie().size()) {
    			ps3.setInt(1, anzToEdit.getID());
    			ps3.setString(2, anzToEdit.getKategorie().get(i).getName());
    			ps3.executeUpdate();
    			i++;
    		}
    		
    		System.out.println("Method updateAnzeige called for id " + anzToEdit.getID());
    		
    	}
    	catch(SQLException e) {
    		throw new StoreException(e);
    	} 
    }
    
    // Anzeige details: Suche anhand von Anzeige ID die anzuzeigende Anzeige, gibt die Anzeige zurück
    // Add categories (select from hatkategorie) then add to list of kategorie
    
    public Anzeige sucheAnzeige(int id) throws StoreException{
    	Anzeige anz = new Anzeige();
    	List<Kategorie> kategorie = new ArrayList<Kategorie>();
    	
    	try {
    		PreparedStatement ps = connection
    				.prepareStatement("select * from dbp34.anzeige where id = ?");
    		ps.setInt(1, id);
    		try(ResultSet res =  ps.executeQuery()) {
    			if(res.next()) {
    				anz = new Anzeige(res.getInt(1), res.getString(2), 
    						res.getString(3), res.getBigDecimal(4), res.getTimestamp(5), 
    						res.getString(6), res.getString(7));
    			}
    		
    		}
    		
    		PreparedStatement ps2 = connection
    				.prepareStatement("select kategorie from dbp34.hatkategorie where anzeigeid = ?");
    		ps2.setInt(1, id);
    		try(ResultSet res2 = ps2.executeQuery()){
    			while(res2.next()) {
    				Kategorie temp = new Kategorie(res2.getString(1));
    				kategorie.add(temp);
    			}
    		}
    		
    		anz.setKategorie(kategorie);
    		System.out.println(id + " " + kategorie.size());
    		//System.out.println("Anzeige gesucht mit titel" + anz.getTitel());
    		return anz;
    		
    	}
    	catch(SQLException e) {
    		throw new StoreException(e);
    	} 
    }
    
    // Anzeige Details: Suchen, ob eine Anzeige schon verkauft ist, dann wird der Käufer im Anzeige Details getragen
    // Wird nur aufgerufen wenn Status verkauft ist (siehe Servlet)
    
    public String verkauftAnzeige(int id) throws StoreException{
    	String kaeufer = null;
    	//System.out.println("Method verkauftAnzeige called");
    	
    	try {
    		PreparedStatement psv = connection.prepareStatement("select * from dbp34.kauft where anzeigeid = ?");
    		psv.setInt(1, id);
    		try(ResultSet res = psv.executeQuery()){
    			if(res.next()) {
    				kaeufer = res.getString(1);
    			}
    		}
    	} catch(SQLException e) {
    		throw new StoreException(e);
    	}
    	
    	return kaeufer;
    }
    
    // Anzeige Details: Insert into Kauft. Der Artikel ist automatisch zu "verkauft" gesetzt.
    
    public void kaufen(String ersteller, int anzeigeid) throws StoreException{
    	
    	try {
    		PreparedStatement ps = connection
    				.prepareStatement("insert into dbp34.kauft (benutzername, anzeigeID) values (?, ?)");
    		ps.setString(1, ersteller);
    		ps.setInt(2, anzeigeid);
    		ps.executeUpdate();
    		
    	} catch(SQLException e) {
    		throw new StoreException(e);
    	}
    		
    }
    
    // Anzeige Details: Anzeige anhand von Anzeige ID löschen
    
    public void deleteAnzeige(int anzeigeid) throws StoreException{
    	
    	try {
    		PreparedStatement ps = connection
    				.prepareStatement("delete from dbp34.anzeige where id= ? ");
    		ps.setInt(1, anzeigeid);
    		ps.executeUpdate();
    		System.out.println("Method deleteAnzeige called for id = " + anzeigeid);
    		
    	} catch(SQLException e) {
    		throw new StoreException(e);
    	}
    	   	
    }
     
    // Anzeige Details: Kommentar hinzufugen
    
    public void addKommentar(Kommentar komToAdd, int anzeigeid) throws StoreException {
        try {
        	int key = -1;
        	System.out.println("Method addKommentar called for id = " + anzeigeid);
        	
            PreparedStatement preparedStatement = connection
                            .prepareStatement("insert into dbp34.kommentar (text) values (?)");
            preparedStatement.setString(1, komToAdd.getText());
            preparedStatement.executeUpdate();
            
            // Der letzte Kommentar wählen und dann insert into hatkommentar
            PreparedStatement psSelect = connection
    				.prepareStatement("select id from dbp34.kommentar order by erstellungsdatum desc fetch first row only");
    		ResultSet rs = psSelect.executeQuery();
    		if(rs.next()) {
    			key = rs.getInt(1);
    			//System.out.println("Kategorie for anzeige" + idToAdd);
    		}
            
            PreparedStatement ps2 = connection
            		.prepareStatement("insert into dbp34.hatkommentar (kommentarid, benutzername, anzeigeid) values (?, ?, ?)");
            ps2.setInt(1, key);
            ps2.setString(2, komToAdd.getGeschriebenVon());
            ps2.setInt(3, anzeigeid);
            ps2.executeUpdate();
   
        }
        
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }
    
    // Anzeige Details: Alle kommentare für eine bestimmte Anzeige anzeigen, sortiert nach Datum
    
    public ArrayList<Kommentar> listKommentar(int anzeigeid) throws StoreException{
    	ArrayList<Kommentar> kommentare = new ArrayList<Kommentar>();
    	
    	try {
    		PreparedStatement ps = connection
    				.prepareStatement("select a.id, a.text, b.benutzername from dbp34.kommentar as a join"
    						+ " dbp34.hatkommentar as b on a.id = b.kommentarid where b.anzeigeid = ?"
    						+ " order by a.erstellungsdatum asc");
    		ps.setInt(1, anzeigeid);
    		try(ResultSet rs = ps.executeQuery()){
    			while(rs.next()) {
    				Kommentar temp = new Kommentar(rs.getInt(1), rs.getString(2), rs.getString(3));
    				//System.out.println(rs.getString(3));
    				kommentare.add(temp);
    			}
    		}
    		
    	} catch(SQLException e) {
    		throw new StoreException(e);
    	}
    	
    	return kommentare;
    	
    }
    
    // User Profil: Alle Anzeigen angeboten von einem Benutzer
    
    public ArrayList<Anzeige> angeboteAnzeigen(String ersteller) throws StoreException{
    	ArrayList<Anzeige> result = new ArrayList<Anzeige>();
    	
    	try {
    		PreparedStatement ps = connection
    				.prepareStatement("select * from dbp34.anzeige where ersteller = ?");
    		ps.setString(1, ersteller);
    		try(ResultSet rs = ps.executeQuery()){
    			while(rs.next()) {
    				Anzeige anz = new Anzeige(rs.getInt(1), rs.getString(2), 
    						rs.getString(3), rs.getBigDecimal(4), rs.getTimestamp(5), 
    						rs.getString(6), rs.getString(7));
    				result.add(anz);
    			}
    		}
    		
    	} catch(SQLException e) {
    		throw new StoreException(e);
    	}
    	
    	return result;
    }
    
    // User Profil: Alle Anzeigen von einem Benutzer gekauft
    
    public ArrayList<Anzeige> gekaufteAnzeigen(String benutzer) throws StoreException{
    	ArrayList<Anzeige> result = new ArrayList<Anzeige>();
    	
    	try {
    		PreparedStatement ps = connection
    				.prepareStatement("select * from dbp34.anzeige as a join dbp34.kauft as b on a.id = b.anzeigeid where b.benutzername = ?");
    		ps.setString(1, benutzer);
    		try(ResultSet rs = ps.executeQuery()){
    			while(rs.next()) {
    				Anzeige anz = new Anzeige(rs.getInt(1), rs.getString(2), 
    						rs.getString(3), rs.getBigDecimal(4), rs.getTimestamp(5), 
    						rs.getString(6), rs.getString(7));
    				result.add(anz);
    			}
    		}
    		
    	} catch(SQLException e) {
    		throw new StoreException(e);
    	}
    	
    	return result;
    }
    
    // Privatnachrichten: Liste von Nachrichten zwischen Absender und Empfänger (beide Richtungen!)
    // Annahme dass ältere Nachrichten kleiner IDs haben
    
    public ArrayList<Nachricht> listNachrichten(String absender, String empfaenger) throws StoreException{
    	ArrayList<Nachricht> result = new ArrayList<Nachricht>();
    	
    	try {
    		PreparedStatement ps = connection
    				.prepareStatement("select * from dbp34.nachricht where absender = ? and empfaenger = ? "
    						+ "or absender = ? and empfaenger = ? order by id asc");
    		ps.setString(1, absender);
    		ps.setString(2, empfaenger);
    		ps.setString(3, empfaenger);
    		ps.setString(4, absender);
    		
    		try(ResultSet rs = ps.executeQuery()){
    			while(rs.next()) {
    				Nachricht temp = new Nachricht(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
    				result.add(temp);
    			}
    		}
    		
    	} catch(SQLException e) {
    		throw new StoreException(e);
    	}
    	
    	return result;
    }
    
    // Privatnachrichten: Absender schickt eine neue Nachricht an den Empfänger
    
    public void addNachricht(Nachricht nachToAdd) {
    	try {
    		PreparedStatement ps = connection
    				.prepareStatement("insert into dbp34.nachricht (text, absender, empfaenger) values (?, ?, ?)");
    		ps.setString(1, nachToAdd.getText());
    		ps.setString(2, nachToAdd.getAbsender());
    		ps.setString(3, nachToAdd.getEmpfaenger());
    		ps.executeUpdate();
    		
    	} catch(SQLException e) {
    		throw new StoreException(e);
    	}
    	
    }
    
    // Bezeichne dass alle Statements durchgeführt werden (Ende der Transaktion)

    public void complete() {
        complete = true;
    }

    // Transaction commit/rollback und Connection schliessen

    @Override
    public void close() throws IOException {
        if (connection != null) {
            try {
                if (complete) {
                    connection.commit();
                }
                else {
                    connection.rollback();
                }
            }
            catch (SQLException e) {
                throw new StoreException(e);
            }
            finally {
                try {
                    connection.close();
                }
                catch (SQLException e) {
                    throw new StoreException(e);
                }
            }
        }
    }

}

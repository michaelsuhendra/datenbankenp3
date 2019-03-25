package de.unidue.inf.is.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public final class Anzeige {
	
	private int anzeigeid;
	private String titel;
	private String text;
	private BigDecimal preis;
	private Timestamp erstellungsdatum;
	private String ersteller;
	private String status;
	private List<Kategorie> kategorie;
	
	public Anzeige() {
	
	}
	
	public Anzeige(int id, String titel, String text, BigDecimal preis, Timestamp datum, String ersteller, String status) {
		this.anzeigeid = id;
		this.titel = titel;
		this.text = text;
		this.preis = preis;
		this.erstellungsdatum = datum;
		this.ersteller = ersteller;
		this.status = status;
	}
	
	// overloaded constructor to create a new Anzeige
	public Anzeige(String titel, String text, BigDecimal preis, String ersteller, List<Kategorie> kategorie) {
//		this.id = 0;
		this.titel = titel;
		this.text = text;
		this.preis = preis;
		this.erstellungsdatum = new Timestamp(System.currentTimeMillis());
		this.ersteller = ersteller;
		this.status = "aktiv";
		this.kategorie = kategorie;
	}
	
	public int getID() {
		return anzeigeid;
	}
	
	public void setID(int id) {
		this.anzeigeid = id;
	}
	
	public String getTitel() {
		return titel;
	}
	
	public String getText() {
		return text;
	}
	
	public BigDecimal getPreis() {
		return preis;
	}
	
	public Timestamp getErstellungsdatum() {
		return erstellungsdatum;
	}
	
	public String getErsteller() {
		return ersteller;
	}
	
	public String getStatus() {
		return status;
	}
	
	public List<Kategorie> getKategorie(){
		return kategorie;
	}
	
	public void setKategorie(List<Kategorie> kategorie) {
		this.kategorie = kategorie;
	}

}

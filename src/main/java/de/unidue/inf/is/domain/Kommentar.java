package de.unidue.inf.is.domain;

public final class Kommentar {
	
	private int id;
	private String text;
	// Benutzer, der der Kommentar schreibt (aus der Tabelle HatKommentar)
	// Should this be of type User?
	private String geschriebenVon;
	
	public Kommentar(int id, String text, String geschriebenVon) {
		this.id = id;
		this.text = text;
		this.geschriebenVon = geschriebenVon;
	}
	
	public int getID() {
		return id;
	}
	
	public String getText() {
		return text;
	}
	
	public String getGeschriebenVon() {
		return geschriebenVon;
	}

}

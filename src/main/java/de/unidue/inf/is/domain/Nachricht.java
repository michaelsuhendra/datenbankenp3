package de.unidue.inf.is.domain;

public final class Nachricht {
	
	private int id;
	private String text;
	private String absender;
	private String empfaenger;
	
	public Nachricht(int id, String text, String absender, String empfaenger) {
		this.id = id;
		this.text = text;
		this.absender = absender;
		this.empfaenger = empfaenger;
	}
	
	public int getID() {
		return id;
	}
	
	public String getText() {
		return text;
	}
	
	public String getAbsender() {
		return absender;
	}
	
	public String getEmpfaenger() {
		return empfaenger;
	}

}

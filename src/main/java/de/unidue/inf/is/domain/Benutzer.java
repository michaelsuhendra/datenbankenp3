package de.unidue.inf.is.domain;

import java.sql.Timestamp;

public final class Benutzer {
	
	private String benutzername;
	private String name;
	private Timestamp eintrittsdatum;
	
	public Benutzer(String benutzername, String name, Timestamp eintrittsdatum) {
		this.benutzername = benutzername;
		this.name = name;
		this.eintrittsdatum = eintrittsdatum;
	}
	
	public String getBenutzername() {
		return benutzername;
	}
	
	public String getName() {
		return name;
	}
	
	public Timestamp getEintrittsdatum() {
		return eintrittsdatum;
	}

}

package de.reneruck.tcd.datamodel;

public class User {

	private long id;
	private String forename;
	private String surname;
	
	public User(String forename, String surname) {
		super();
		this.forename = forename;
		this.surname = surname;
	}

	public long getId() {
		return id;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	
}

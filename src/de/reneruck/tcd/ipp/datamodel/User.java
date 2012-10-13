package de.reneruck.tcd.ipp.datamodel;

public class User {

	private long id;
	private String forename = "";
	private String surname = "";
	
	public User(String name) {
		super();
		String[] split = name.split(" ");
		if(split.length >= 1) {
			this.forename = split[0];
		} 
		if(split.length > 1) {
			this.surname = split[1];
		}
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

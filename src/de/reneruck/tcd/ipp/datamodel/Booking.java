package de.reneruck.tcd.ipp.datamodel;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.reneruck.tcd.ipp.datamodel.exceptions.NotAcceptedException;


public class Booking implements Serializable {

	private static final long serialVersionUID = 2638486391776355573L;
	private long id;
	private String requester;
	private Date travelDate;
	private Airport from;
	private Airport to;
	private boolean accepted = false;
	
	public Booking(String requester, Date travelDate, Airport from) {
		super();
		this.id = System.currentTimeMillis();
		this.requester = requester;
		this.travelDate = travelDate;
		this.from = from;
	}

	public long getId() {
		return id;
	}

	public String getRequester() {
		return requester;
	}

	public Date getTravelDate() {
		return travelDate;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public String generateTicket() throws NotAcceptedException {
		if(this.accepted) {
			DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			return "======== Ticket ======= \n" +
					"ID: " + this.id + "\n" +
					"Name: " + this.requester + "\n" +
					"Departing: " + df.format(this.travelDate) + "\n" +
					"=========================";
					
		} else {
			throw new NotAcceptedException();
		}
	}

	public Airport getFrom() {
		return from;
	}

	public void setFrom(Airport from) {
		this.from = from;
	}

	public Airport getTo() {
		return to;
	}

	public void setTo(Airport to) {
		this.to = to;
	}

	public String printOverview() {
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return "============================= \n" +
				"Name: " + this.requester + "\n" +
				"Departing: " + df.format(this.travelDate) + "\n" +
				"=============================";
	}

	public void storeToDB() {
		
	}
}

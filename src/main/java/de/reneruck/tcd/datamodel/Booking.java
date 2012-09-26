package de.reneruck.tcd.datamodel;

import java.sql.Date;

import de.reneruck.tcd.datamodel.exceptions.NotAcceptedException;


public class Booking {

	private long Id;
	private User requester;
	private Date travelDate;
	private boolean accepted;
	
	public Booking(User requester, Date travelDate) {
		super();
		this.Id = System.currentTimeMillis();
		this.requester = requester;
		this.travelDate = travelDate;
	}

	public long getId() {
		return Id;
	}

	public User getRequester() {
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
			return "======== Ticket ======= \n" +
					"Name: " + this.requester.getForename() + "\n" +
					"Surname: " + this.requester.getSurname() + "\n" +
					"Departing: " + travelDate.getTime() + "\n" +
					"=========================";
					
		} else {
			throw new NotAcceptedException();
		}
	}
}

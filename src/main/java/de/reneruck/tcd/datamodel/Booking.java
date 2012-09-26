package de.reneruck.tcd.datamodel;

import java.sql.Date;

import de.reneruck.tcd.datamodel.exceptions.NotAcceptedException;


public class Booking {

	private long Id;
	private User requester;
	private Date travelDate;
	private Location from;
	private Location to;
	private boolean accepted;
	
	public Booking(User requester, Date travelDate, Location from, Location to) {
		super();
		this.Id = System.currentTimeMillis();
		this.requester = requester;
		this.travelDate = travelDate;
		this.from = from;
		this.to = to;
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

	public Location getFrom() {
		return from;
	}

	public void setFrom(Location from) {
		this.from = from;
	}

	public Location getTo() {
		return to;
	}

	public void setTo(Location to) {
		this.to = to;
	}
}

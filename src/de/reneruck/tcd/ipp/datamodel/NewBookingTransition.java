package de.reneruck.tcd.ipp.datamodel;

import java.io.Serializable;
import java.net.ConnectException;
import java.util.Date;

import com.google.gson.Gson;

public class NewBookingTransition implements Transition, Serializable {

	private static final long serialVersionUID = -7340057807403227632L;
	private Booking booking;
	private long transitionId;
	private Date handlingDate;
	private TransitionState state = TransitionState.PENDING;
	private String reason;

	public NewBookingTransition(Booking booking) {
		this.booking = booking;
		this.transitionId = booking.getId();
	}

	public Date getProcessingDate() {
		return handlingDate;
	}

	public void setHandlingDate(Date handlingDate) {
		this.handlingDate = handlingDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		String json = this.getClass().getName() + "=" + gson.toJson(this);
		return json;
	}

	private String generateSql() {
		String sql = "INSERT INTO Bookings (booking_id, StartAirportId, User, Flight) " + "VALUES(" + this.booking.getId() + ", " + this.booking.getFrom().ordinal() + " ,'" + this.booking.getRequester()
				+ "', 19)";
		return sql;
	}

	@Override
	public void performTransition(DatabaseConnection connection) throws ConnectException {
		
		if (!connection.bookingExists(this.booking.getId())) { // if already exists we don't have to do anything
			
			int flightForDate = connection.getFlightForDate(this.booking.getTravelDate(), this.booking.getFrom());
			if(flightForDate > 0){
				if(Statics.MAX_PASSENGERS > connection.getBookingCountForFlight(flightForDate)) {
					int bookingId = connection.makeABooking(this.booking, flightForDate);
					if(bookingId > 0) {
						this.booking.setAccepted(true);
						this.handlingDate = new Date(System.currentTimeMillis());
					}
				} else {
					declineRequest("Flight already full");
				}
			} else {
				declineRequest("No flight at the desired time");
			}
		}
	}

	private void declineRequest(String reason) {
		this.reason = reason;
		this.handlingDate = new Date(System.currentTimeMillis());
		this.booking.setAccepted(false);
	}

	@Override
	public long getTransitionId() {
		return this.transitionId;
	}

	@Override
	public TransitionState getTransitionState() {
		return this.state;
	}

	@Override
	public void setTransitionState(TransitionState state) {
		this.state = state;
	}

	@Override
	public boolean equals(Object arg0) {
		return ((Transition)arg0).getTransitionId() == this.getTransitionId();
	}
	@Override
	public int hashCode() {
		return (int) (this.transitionId + this.booking.getId());
	}

	@Override
	public Booking getBooking() {
		return this.booking;
	}
}

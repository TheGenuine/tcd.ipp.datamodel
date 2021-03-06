package de.reneruck.tcd.ipp.datamodel.transition;

import java.io.Serializable;
import java.net.ConnectException;
import java.util.Date;

import com.google.gson.Gson;

import de.reneruck.tcd.ipp.datamodel.Booking;
import de.reneruck.tcd.ipp.datamodel.Statics;
import de.reneruck.tcd.ipp.datamodel.database.DatabaseConnection;

/**
 * {@link Transition} implementation create a new {@link Booking}.
 * 
 * @author Rene
 *
 */
public class NewBookingTransition implements Transition, Serializable {

	private static final long serialVersionUID = -7340057807403227632L;
	private Booking booking;
	private long transitionId;
	private Date handlingDate;
	private TransitionState state = TransitionState.PENDING;

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

	@Override
	public String toString() {
		Gson gson = new Gson();
		String json = this.getClass().getName() + "=" + gson.toJson(this);
		return json;
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
		this.booking.setReason(reason);
		this.booking.setAccepted(false);
		this.handlingDate = new Date(System.currentTimeMillis());
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

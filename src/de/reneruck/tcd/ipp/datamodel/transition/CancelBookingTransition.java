package de.reneruck.tcd.ipp.datamodel.transition;

import java.io.Serializable;
import java.net.ConnectException;
import java.util.Date;

import com.google.gson.Gson;

import de.reneruck.tcd.ipp.datamodel.Booking;
import de.reneruck.tcd.ipp.datamodel.database.DatabaseConnection;

public class CancelBookingTransition implements Transition, Serializable {

	private static final long serialVersionUID = 7911624815707534301L;
	private long transitionId;
	private Booking booking;
	private Date processingDate;
	private TransitionState transitionState = TransitionState.PENDING;

	public CancelBookingTransition(Booking booking) {
		this.booking = booking;
		this.transitionId = booking.getId();
	}

	@Override
	public long getTransitionId() {
		return this.transitionId;
	}

	@Override
	public Booking getBooking() {
		return this.booking;
	}

	@Override
	public Date getProcessingDate() {
		return this.processingDate;
	}

	@Override
	public TransitionState getTransitionState() {
		return this.transitionState;
	}

	@Override
	public void performTransition(DatabaseConnection connection) throws ConnectException {
		if(connection != null && connection.bookingExists(this.booking.getId())) {
			connection.removeBooking(this.booking.getId());
		}

	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		String json = this.getClass().getName() + "=" + gson.toJson(this);
		return json;
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
	public void setHandlingDate(Date handlingDate) {
		this.processingDate = handlingDate;
	}

	@Override
	public void setTransitionState(TransitionState state) {
		this.transitionState = state;
	}

}

package de.reneruck.tcd.ipp.datamodel.transition;

import java.io.Serializable;
import java.net.ConnectException;
import java.util.Date;

import de.reneruck.tcd.ipp.datamodel.Booking;
import de.reneruck.tcd.ipp.datamodel.database.DatabaseConnection;

public class CancelBookingTransition implements Transition, Serializable {

	private static final long serialVersionUID = 7911624815707534301L;
	private long transitionId;
	private Booking booking;
	private Date processingDate;
	private TransitionState transitionState = TransitionState.PENDING;
	private String reason;

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
	public String getReason() {
		return this.reason;
	}

	@Override
	public void performTransition(DatabaseConnection connection) throws ConnectException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHandlingDate(Date handlingDate) {
		this.processingDate = handlingDate;
	}

	@Override
	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public void setTransitionState(TransitionState state) {
		this.transitionState = state;
	}

}

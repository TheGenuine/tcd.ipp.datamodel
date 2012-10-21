package de.reneruck.tcd.ipp.datamodel;

import java.net.ConnectException;
import java.util.Date;

public interface Transition {
	
	public long getTransitionId();
	public Booking getBooking();
	public Date getProcessingDate();
	public TransitionState getTransitionState();
	public String getReason();
	public void performTransition(DatabaseConnection connection) throws ConnectException;
	public void setHandlingDate(Date handlingDate);
	public void setReason(String reason);
	public void setTransitionState(TransitionState state);
}

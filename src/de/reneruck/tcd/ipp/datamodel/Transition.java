package de.reneruck.tcd.ipp.datamodel;

import java.util.Date;

public interface Transition {
	
	public long getBookingId();
	public Date getHandlingDate();
	public String getReason();
	public String generateSql();
	public void setBookingId(long bookingId);
	public void setHandlingDate(Date handlingDate);
	public void setReason(String reason);
}

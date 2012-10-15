package de.reneruck.tcd.ipp.datamodel;

import java.net.ConnectException;
import java.util.Date;

import com.google.gson.Gson;

public class NewBookingTransition implements Transition {

	private Booking booking;

	private long bookingId;
	private Date handlingDate;
	private String reason;

	public NewBookingTransition(Booking booking) {
		this.booking = booking;
		setBookingId(booking.getId());
	}

	public long getBookingId() {
		return bookingId;
	}

	public void setBookingId(long bookingId) {
		this.bookingId = bookingId;
	}

	public Date getHandlingDate() {
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
		String sql = "INSERT INTO Bookings (booking_id, StartAirportId, User, Flight) " + "VALUES(" + this.bookingId + ", " + this.booking.getFrom().ordinal() + " ,'" + this.booking.getRequester()
				+ "', 19)";
		return sql;
	}

	@Override
	public void performTransition(DatabaseConnection connection) throws ConnectException {
		if (connection.bookingExists(this.bookingId)) {
			
		}
	}

}

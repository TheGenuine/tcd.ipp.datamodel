package de.reneruck.tcd.ipp.datamodel.exceptions;

public class NotAcceptedException extends Exception {

	public NotAcceptedException() {
		new Exception("Ticket has not been accepted");
	}
}

package de.reneruck.tcd.ipp.datamodel.exceptions;


public class DatabaseException extends Exception {

	public DatabaseException(String string, Throwable e) {
		new Exception(string, e);
	}

	
}

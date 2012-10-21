package de.reneruck.tcd.ipp.datamodel;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class DatabaseConnection {

	private static Connection dbConnection = null;

	// Hostname
	private static String dbHost = "127.0.0.1";
	// Port -- Standard: 3306
	private static String dbPort = "3306";
	// Datenbankname
	private static String database = "mydb";
	// Datenbankuser
	private static String dbUser = "root";
	// Datenbankpasswort
	private static String dbPassword = "password";

	public DatabaseConnection() throws ConnectException {
		connectToDB();
	}

	private void connectToDB() throws ConnectException {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			dbConnection = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + database + "?" + "user=" + dbUser + "&" + "password=" + dbPassword);
		} catch (ClassNotFoundException e) {
			throw new ConnectException("JDBC driver not found");
		} catch (SQLException e) {
			throw new ConnectException("No connection possible");
		}
	}

	private Connection getInstance() throws ConnectException {
		if (dbConnection == null)
			connectToDB();
		return dbConnection;
	}

	public boolean bookingExists(long id) throws ConnectException {
		if (getInstance() != null) {
			try {
				Statement query = dbConnection.createStatement();
				String sql = "SELECT * FROM bookings WHERE booking_id = " + id;
				ResultSet result = query.executeQuery(sql);
				if (result != null) {
					return result.first();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public int getBookingsForFlight(int flightId) throws ConnectException {
		if (getInstance() != null) {

		}
		return 0;
	}

	public int getFlightForDate(Date date, Airport from) throws ConnectException {
		long timestamp = date.getTime();
		String sql = "SELECT  idFlight FROM mydb.flight where Date = " + timestamp + " " + "OR Date+3600000 >=  " + timestamp + " " + "AND Date - 3600000 <= " + timestamp + " AND DepartureAirport = "
				+ from.ordinal() + " LIMIT 1";
		ResultSet result = executeQuery(sql);
		try {
			if (result != null && result.first()) {
				return result.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	private boolean execute(String sql) throws ConnectException {
		dbConnection = getInstance();

		if (dbConnection != null) {
			// Anfrage-Statement erzeugen.
			Statement query;
			try {
				query = dbConnection.createStatement();
				return query.execute(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			throw new ConnectException("Database unreachable");
		}
		return false;
	}

	private ResultSet executeQuery(String sql) throws ConnectException {
		dbConnection = getInstance();

		if (dbConnection != null) {
			// Anfrage-Statement erzeugen.
			Statement query;
			try {
				query = dbConnection.createStatement();
				return query.executeQuery(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			throw new ConnectException("Database unreachable");
		}
		return null;
	}

	public boolean executeSql(String sqlStatement) throws ConnectException {
		return execute(sqlStatement);
	}

	public void close() {
		if (dbConnection != null) {
			try {
				dbConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
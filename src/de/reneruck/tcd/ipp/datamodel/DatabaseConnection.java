package de.reneruck.tcd.ipp.datamodel;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

	/**
	 * Checks if a specific booking exists.
	 * 
	 * @param booking_id
	 *            the id of the desired booking
	 * @return true if a booking with the given booking_id exists
	 * @throws ConnectException
	 *             can occur if no connection to the database is available
	 */
	public boolean bookingExists(long booking_id) throws ConnectException {
		if (getInstance() != null) {
			try {
				Statement query = dbConnection.createStatement();
				String sql = "SELECT * FROM bookings WHERE booking_id = " + booking_id;
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

	/**
	 * Returns the count of bookings already made for the given flight
	 * 
	 * @param flightId
	 *            the flightId(DB id) of the desired flight
	 * @return the count of bookings for the given flight
	 * @throws ConnectException
	 *             can occur if no connection to the database is available
	 */
	public int getBookingCountForFlight(int flightId) throws ConnectException {
		if (getInstance() != null) {
			String sql = "SELECT COUNT(ID) FROM mydb.bookings WHERE Flight = " + flightId;
			ResultSet result = executeQuery(sql);
			try {
				if (result != null && result.first()) {
					return result.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * Looks for a flight at the given {@link Date} +/- 1 hour, from the given
	 * {@link Airport}
	 * 
	 * @param date
	 *            the date to look for a flight
	 * @param from
	 *            the airport to fly from
	 * @return the id of the flight if found one, otherwise 0 will be returned.
	 * @throws ConnectException
	 *             can occur if no connection to the database is available
	 */
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

	/**
	 * Inserts a new booking into the database.
	 * 
	 * @param booking
	 *            the {@link Booking} Object to be inserted.
	 * @param flightId
	 *            the flight to make this booking for
	 * @return the database id of the booking if successful or 0 if not.
	 * @throws ConnectException
	 *             can occur if no connection to the database is available
	 */
	public int makeABooking(Booking booking, long flightId) throws ConnectException {

		String query = "INSERT INTO mydb.bookings (booking_id, StartAirportId, User, Flight) "
		+ "values(" + booking.getId() + ", " + booking.getFrom().ordinal() + ", '" + booking.getRequester()+ "', " + flightId + ")";
		ResultSet result = executeInsert(query);

		try {
			if (result != null && result.first()) {
				return result.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
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

	/**
	 * Executes the given insert statement and returns all generated ID's that
	 * were made with this statement.
	 * 
	 * @param sql
	 *            insert statement
	 * @return a {@link ResultSet} of generated ID's or null if none have been
	 *         generated or an error ocured.
	 * @throws ConnectException
	 *             can occur if no connection to the database is available
	 */
	public ResultSet executeInsert(String sql) throws ConnectException {
		dbConnection = getInstance();

		if (dbConnection != null) {
			try {
				PreparedStatement prepareStatement = dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				prepareStatement.execute(sql, Statement.RETURN_GENERATED_KEYS);
				return prepareStatement.getGeneratedKeys();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			throw new ConnectException("Database unreachable");
		}
		return null;
	}
	
	/**
	 * Executes a given SQL statement without any check before.<br>
	 * Only useful for inserts. Will not return anything from the database.
	 * 
	 * @param sqlStatement
	 *            the statement to execute
	 * @return true if the execution of the statement was successful. false if
	 *         not.
	 * @throws ConnectException
	 *             can occur if no connection to the database is available
	 */
	public ResultSet executeSql(String sqlStatement) throws ConnectException {
		return executeQuery(sqlStatement);
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
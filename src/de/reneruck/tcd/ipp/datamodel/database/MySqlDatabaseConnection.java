package de.reneruck.tcd.ipp.datamodel.database;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import de.reneruck.tcd.ipp.datamodel.Airport;
import de.reneruck.tcd.ipp.datamodel.Booking;
import de.reneruck.tcd.ipp.datamodel.transition.Transition;

/**
 * This {@link DatabaseConnection} implementation constitutes the specific
 * implementation for the connection to a MySql Database.
 * 
 * @author Rene
 * 
 */
public class MySqlDatabaseConnection implements DatabaseConnection {

	private Connection dbConnection = null;

	private static String dbHost = "127.0.0.1";
	private static String dbPort = "3306";
	private static String database = "mydb";
	private static String dbUser = "root";
	private static String dbPassword = "password";

	public MySqlDatabaseConnection() throws ConnectException {
		connectToDB();
	}

	private void connectToDB() throws ConnectException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.dbConnection = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + database + "?" + "user=" + dbUser + "&" + "password=" + dbPassword);
		} catch (ClassNotFoundException e) {
			throw new ConnectException("JDBC driver not found");
		} catch (SQLException e) {
			throw new ConnectException("No connection possible");
		}
	}

	public boolean bookingExists(long booking_id) throws ConnectException {
		if (this.dbConnection != null) {
			String sql = "SELECT * FROM bookings WHERE booking_id = " + booking_id;
			ResultSet result = executeQuery(sql);
			try {
				if (result != null) {
					return result.first();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public int getBookingCountForFlight(int flightId) throws ConnectException {
		if (this.dbConnection != null) {
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

	public int makeABooking(Booking booking, long flightId) throws ConnectException {

		String query = "INSERT INTO mydb.bookings (booking_id, StartAirportId, User, Flight) " + "values(" + booking.getId() + ", " + booking.getFrom().ordinal() + ", '" + booking.getRequester()
				+ "', " + flightId + ")";
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

	@Override
	public ResultSet executeQuery(String sql) throws ConnectException {
		if (this.dbConnection != null) {
			try {
				Statement query = this.dbConnection.createStatement();
				return query.executeQuery(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			throw new ConnectException("Database unreachable");
		}
		return null;
	}

	public ResultSet executeInsert(String sql) throws ConnectException {
		if (this.dbConnection != null) {
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

	public void executeSql(String sqlStatement) throws ConnectException {
		executeQuery(sqlStatement);
	}

	public void close() {
		if (this.dbConnection != null) {
			try {
				this.dbConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public ResultSet executeUpdate(String string) {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getBookingsCount() throws ConnectException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeBooking(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persistTransitionStoreEntry(Transition transition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dePersistTransitionStoreEntry(Transition transition) {
		// TODO Auto-generated method stub
		
	}
}
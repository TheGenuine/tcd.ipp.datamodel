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

public class SqliteDatabaseConnection implements DatabaseConnection {

	private Connection dbConnection = null;


	public SqliteDatabaseConnection(String databaseFileName) throws ConnectException {
		connectToDB(databaseFileName);
	}

	private void connectToDB(String databaseFileName) throws ConnectException {
		try {
			Class.forName("org.sqlite.JDBC");
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:"+databaseFileName);
		} catch (ClassNotFoundException e) {
			throw new ConnectException("JDBC driver not found");
		} catch (SQLException e) {
			throw new ConnectException("No connection possible");
		}
	}

	/* (non-Javadoc)
	 * @see de.reneruck.tcd.ipp.datamodel.DatabaseConnection2#bookingExists(long)
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see de.reneruck.tcd.ipp.datamodel.DatabaseConnection2#getBookingCountForFlight(int)
	 */
	@Override
	public int getBookingCountForFlight(int flightId) throws ConnectException {
		if (this.dbConnection != null) {
			String sql = "SELECT COUNT(ID) FROM bookings WHERE Flight = " + flightId;
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

	/* (non-Javadoc)
	 * @see de.reneruck.tcd.ipp.datamodel.DatabaseConnection2#getFlightForDate(java.util.Date, de.reneruck.tcd.ipp.datamodel.Airport)
	 */
	@Override
	public int getFlightForDate(Date date, Airport from) throws ConnectException {
		long timestamp = date.getTime();
		String sql = "SELECT  idFlight FROM flight where Date = " + timestamp + " " + "OR Date+3600000 >=  " + timestamp + " " + "AND Date - 3600000 <= " + timestamp + " AND DepartureAirport = "
				+ from.ordinal() + " LIMIT 1";
		ResultSet result = executeQuery(sql);
		try {
			if (result != null && result.next()) {
				return result.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/* (non-Javadoc)
	 * @see de.reneruck.tcd.ipp.datamodel.DatabaseConnection2#makeABooking(de.reneruck.tcd.ipp.datamodel.Booking, long)
	 */
	@Override
	public int makeABooking(Booking booking, long flightId) throws ConnectException {
		String query = "INSERT INTO bookings (booking_id, StartAirportId, User, Flight) "
		+ "values(" + booking.getId() + ", " + booking.getFrom().ordinal() + ", '" + booking.getRequester()+ "', " + flightId + ")";
		ResultSet result = executeUpdate(query);
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

	/* (non-Javadoc)
	 * @see de.reneruck.tcd.ipp.datamodel.DatabaseConnection2#executeInsert(java.lang.String)
	 */
	@Override
	public ResultSet executeInsert(String sql) throws ConnectException {
		if (this.dbConnection != null) {
			try {
				PreparedStatement prepareStatement = this.dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
	
	/* (non-Javadoc)
	 * @see de.reneruck.tcd.ipp.datamodel.DatabaseConnection2#executeSql(java.lang.String)
	 */
	@Override
	public void executeSql(String sqlStatement) throws ConnectException {
		if (this.dbConnection != null) {
			try {
				Statement statement = this.dbConnection.createStatement();
				statement.execute(sqlStatement);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			throw new ConnectException("Database unreachable");
		}
	}		

	/* (non-Javadoc)
	 * @see de.reneruck.tcd.ipp.datamodel.DatabaseConnection2#close()
	 */
	@Override
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
	public ResultSet executeUpdate(String sql) throws ConnectException {
		if (this.dbConnection != null) {
			try {
				Statement statement = this.dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int executeUpdate = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
				if(executeUpdate > 0) {
					return statement.getGeneratedKeys();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			throw new ConnectException("Database unreachable");
		}
		return null;
	}
}
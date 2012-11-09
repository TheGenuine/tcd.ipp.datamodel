package de.reneruck.tcd.ipp.datamodel.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;

import de.reneruck.tcd.ipp.datamodel.Statics;

/**
 * Utilities to initialize a database and fill it with default or test data.
 * 
 * @author Rene
 * 
 */
public class DBUtils {

	public static void insertTestData(DatabaseConnection databaseConnection) throws FileNotFoundException, IOException {
		List<String> readLines = IOUtils.readLines(new FileReader(new File("TestData.sql")));
		
		for (String statement : readLines) {
			databaseConnection.executeUpdate(statement);
		}
	}

	public static void setupDBStructure(DatabaseConnection databaseConnection) throws FileNotFoundException, IOException {
		List<String> readLines = IOUtils.readLines(new FileReader(new File(Statics.DB_SCHMEA_FILE)));
		
		StringBuilder queryStringBuilder = new StringBuilder();
		for (String statement : readLines) {
			if(!statement.startsWith("#")) {
				if(statement.startsWith(";")){
					databaseConnection.executeUpdate(queryStringBuilder.toString());
					queryStringBuilder = new StringBuilder();
				} else {
					queryStringBuilder.append(statement);
				}
			}
		}
	}

	public static void setupInitData(DatabaseConnection databaseConnection) throws FileNotFoundException, IOException {
		List<String> readLines = IOUtils.readLines(new FileReader(new File(Statics.DB_INIT_DATA_FILE)));
		
		for (String statement : readLines) {
			databaseConnection.executeUpdate(statement);
		}
	}

}

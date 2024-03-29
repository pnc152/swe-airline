package gmu.swe.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to create a connection to the database.
 * 
 * @author mbsnyder
 * 
 */
public class DbUtils {
	/**
	 * Returns a connection to a HSQLDB database. This class will look up the
	 * directory for the database in the property file under a property name of
	 * "DB_FILE_PATH".
	 * 
	 * @return Connection to a HSQLDB database.
	 * @throws SQLException
	 *             Thrown if the creating of the connection fails.
	 * @throws ClassNotFoundException
	 *             Thrown if the driver for HSQLDB can't be found.
	 */
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("org.hsqldb.jdbcDriver");
		String dbPath = ResourceBundleUtils.getProperty("DB_FILE_PATH");

		String dbConnectionString = "jdbc:hsqldb:file:" + dbPath + "/AIRLINE_DB";

		// connect to the database. This will load the db files and start the
		// database if it is not alread running.
		// db_file_name_prefix is used to open or create files that hold the
		// state
		// of the db.
		// It can contain directory names relative to the
		// current working directory
		Connection connection = DriverManager.getConnection(dbConnectionString, "sa", "");
		connection.setAutoCommit(true);

		return connection;
	}

	/**
	 * Returns a connection to a HSQLDB database. This class will look up the
	 * directory for the database in the provided dbPath.
	 * 
	 * @param dbPath
	 *            Directory location containing the database.
	 * @return Connection to the HSQLDB database
	 * @throws SQLException
	 *             Thrown if the creating of the connection fails.
	 * @throws ClassNotFoundException
	 *             Thrown if the driver for HSQLDB can't be found.
	 */
	public static Connection getConnection(String dbPath) throws SQLException, ClassNotFoundException {
		Class.forName("org.hsqldb.jdbcDriver");

		String dbConnectionString = "jdbc:hsqldb:file:" + dbPath + "/AIRLINE_DB";
		return DriverManager.getConnection(dbConnectionString, "sa", "");
	}
}

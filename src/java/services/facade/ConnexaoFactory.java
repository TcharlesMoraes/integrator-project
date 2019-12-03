package services.facade;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class ConnexaoFactory {

	//private static final String CONNECTION_CONFIGURATION_FILE = "./resources/database-config.properties";
	private static final String CONNECTION_URL = "jdbc:postgresql://localhost:5432/sw4";
	private static final String CONNECTION_USER = "postgres";
	private static final String CONNECTION_PASSWORD = "postgre";

	private static Connection conn;

	public static Connection getConnection() {
		Connection conn= null;
		String url = getConnectionURL();
		Properties props = new Properties();
		props.setProperty("user", getConnectionUser());
		props.setProperty("password", getConnectionPassowrd());
		try {
			conn = DriverManager.getConnection(url, props);
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}

		return conn;

	}

	private static String getConnectionURL() {
		return CONNECTION_URL;
	}

	private static String getConnectionUser() {
		return CONNECTION_USER;
	}

	private static String getConnectionPassowrd() {
		return CONNECTION_PASSWORD;
	}

}

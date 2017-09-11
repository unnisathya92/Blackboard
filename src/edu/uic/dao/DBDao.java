package edu.uic.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBDao {

	public String userName;

	public String getUserName() {
		return userName;
	}

	private String password;
	private String dbmsHost;
	private String databaseSchema;
	private final String jdbcDriver;
	private String url;
	public Connection connection;

	
	public String getDatabaseSchema() {
		return databaseSchema;
	}

	public void setDatabaseSchema(String databaseSchema) {
		this.databaseSchema = databaseSchema;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public DBDao(String userName, String password, String dbmsHost, String databaseSchema, String jdbcDriverForDB) {
		this.userName = userName;
		this.password = password;
		this.dbmsHost = dbmsHost;
		this.databaseSchema = databaseSchema;
		this.jdbcDriver = jdbcDriverForDB;
		this.url = this.dbmsHost + this.databaseSchema;

	}

	public Connection getConnection() {
		return connection;
	}

	public void createConnection() throws ClassNotFoundException, SQLException {

		Class.forName(jdbcDriver);
		connection = DriverManager.getConnection(url, userName, password);

	}

	public void closeConnection() {
		try {
			if (connection != null)
				connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
package ngospodinov.crud.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ngospodinov.crud.model.MyUser;

/* MyUserDAO
 * 
 * This Class provides CRUD database operations for the table in the database
 * 
 * 29-08-2020
 * 
 * @author Nikoly Gospodinov
 */
public class MyUserDAO {
	// Declare my constants for the database
	private String jdbcURL = "jdbc:mysql://localhost:3306/crud?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
	private String jdbcUserName = "root";
	private String jdbcPassword = "sven1234";

	// Declare my constants for the table name and colums in the database + column
	// indexes
	private static final String TABLE_MYUSERS = "myusers";
	private static final String COLUMN_MYUSERS_ID = "id";
	private static final String COLUMN_MYUSERS_NAME = "name";
	private static final String COLUMN_MYUSERS_EMAIL = "email";
	private static final String COLUMN_MYUSERS_COUNTRY = "country";

	private static final int COLUMN_INDEX_ID = 1;
	private static final int COLUMN_INDEX_NAME = 2;
	private static final int COLUMN_INDEX_EMAIL = 3;
	private static final int COLUMN_INDEX_COUNTRY = 4;

	// Declare my constants for SQL Statements
	
	private static final String QUERY_SELECT_USER_BY_ID = "SELECT " + COLUMN_MYUSERS_ID + COLUMN_MYUSERS_NAME
			+ COLUMN_MYUSERS_EMAIL + COLUMN_MYUSERS_COUNTRY + " FROM " + TABLE_MYUSERS + " WHERE " + COLUMN_MYUSERS_ID
			+ " = ?";

	private static final String QUERY_SELECT_ALL_USERS = "SELECT * FROM " + TABLE_MYUSERS;

	private static final String QUERY_DELETE_USER = "DELETE FROM " + TABLE_MYUSERS + " WHERE " + COLUMN_MYUSERS_ID
			+ " = ?";

	private static final String QUERY_UPDATE_USER = "UPDATE " + TABLE_MYUSERS + " SET " + COLUMN_MYUSERS_NAME + " = ?"
			+ "," + COLUMN_MYUSERS_EMAIL + " = ?" + "," + COLUMN_MYUSERS_COUNTRY + " = ?" + " WHERE "
			+ COLUMN_MYUSERS_ID + " = ?";

	private static final String QUERY_INSERT_USER = "INSERT INTO " + TABLE_MYUSERS + "(" + COLUMN_MYUSERS_NAME + ", "
			+ COLUMN_MYUSERS_EMAIL + ", " + COLUMN_MYUSERS_COUNTRY + ") VALUES (?, ?, ?)";

	// Class constructor
	public MyUserDAO() {

	}

	// Method that connects our application to the database
	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPassword);
		} catch (SQLException e) {
			System.out.println("Cannot connect to the database: " + e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Cannot find the class: " + e.getMessage());
			e.printStackTrace();
		}

		return connection;
	}

	// Method that execute Insert User Query
	public void insertUser(MyUser user) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(QUERY_INSERT_USER)) {
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error executing (QUERY_INSERT_USER): " + e.getMessage());
			e.printStackTrace();
		}

	}

	// Method that execute Update User Query
	public boolean updateUser(MyUser user) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(QUERY_UPDATE_USER)) {
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			preparedStatement.setInt(4, user.getId());
			rowUpdated = preparedStatement.executeUpdate() > 0;
		}
		return rowUpdated;

	}

	// Method that execute Select User by Id Query
	public MyUser selectUserById(int id) {
		MyUser user = null;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(QUERY_SELECT_USER_BY_ID)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String name = resultSet.getString(COLUMN_INDEX_NAME);
				String email = resultSet.getString(COLUMN_INDEX_EMAIL);
				String country = resultSet.getString(COLUMN_INDEX_COUNTRY);
				user = new MyUser(id, name, email, country);
			}

		} catch (SQLException e) {
			System.out.println("Error executing (QUERY_SELECT_USER_BY_ID): " + e.getMessage());
			e.printStackTrace();
		}
		return user;
	}

	// Method that execute Select All Users Query
	public List<MyUser> selectAllUsers() {
		List<MyUser> users = new ArrayList<>();
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(QUERY_SELECT_ALL_USERS)) {
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int id = resultSet.getInt(COLUMN_INDEX_ID);
				String name = resultSet.getString(COLUMN_INDEX_NAME);
				String email = resultSet.getString(COLUMN_INDEX_EMAIL);
				String country = resultSet.getString(COLUMN_INDEX_COUNTRY);
				users.add(new MyUser(id, name, email, country));
			}

		} catch (SQLException e) {
			System.out.println("Error executing (QUERY_SELECT_ALL_USERS): " + e.getMessage());
			e.printStackTrace();
		}
		return users;
	}

	// Mehtod that execute Delete User Query
	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(QUERY_DELETE_USER)) {
			preparedStatement.setInt(1, id);
			rowDeleted = preparedStatement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

}

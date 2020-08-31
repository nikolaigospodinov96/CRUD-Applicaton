package ngospodinov.crud.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ngospodinov.crud.dao.MyUserDAO;
import ngospodinov.crud.model.MyUser;

/**
 * Servlet implementation class MyUserServlet
 */
@WebServlet("/")
public class MyUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MyUserDAO userDAO;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyUserServlet() {
		this.userDAO = new MyUserDAO();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getServletPath();

		switch (action) {
		case "/new":
			showNewForm(request, response);
			break;
		case "/insert":
			try {
				insertUser(request, response);
			} catch (SQLException e) {
				System.out.println("Failed to insert New User: " + e.getMessage());
				e.printStackTrace();
			}
			break;
		case "/delete":
			try {
				deleteUser(request, response);
			} catch (SQLException e) {
				System.out.println("Failed to delete the Selected User: " + e.getMessage());
				e.printStackTrace();
			}
			break;
		case "/edit":
			try {
				showEditForm(request, response);
			} catch (SQLException e) {
				System.out.println("Failed to open Edit User Form: " + e.getMessage());
				e.printStackTrace();
			}
			break;
		case "/update":
			try {
				updateUser(request, response);
			} catch (SQLException e) {
				System.out.println("Failed to update the user information: " + e.getMessage());
				e.printStackTrace();
			}
			break;
		default:
			try {
				listUsers(request, response);
			} catch (SQLException e) {
				System.out.println("Failed to list te users from the database: " + e.getMessage());
				e.printStackTrace();
			} 
			break;
		}

	}
	
	// Method that list all users in the datbase
	private void listUsers(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, ServletException, IOException {
		List<MyUser> listUser = userDAO.selectAllUsers();
		request.setAttribute("listUser", listUser);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
		
	}

	// Method that opens newForm for adding user when we press "New" button
	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request, response);

	}

	// Mehtod that opens editForm for changing user info when we press "Edit" button
	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		MyUser existingUser = userDAO.selectUserById(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		request.setAttribute("user", existingUser);
		dispatcher.forward(request, response);

	}

	// Method that insert new User into the database
	private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		MyUser newUser = new MyUser(name, email, country);
		userDAO.insertUser(newUser);
		response.sendRedirect("list");

	}

	// Mathod that update user info in the database
	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("id");
		String email = request.getParameter("email");
		String country = request.getParameter("country");

		MyUser user = new MyUser(id, name, email, country);
		userDAO.updateUser(user);
		response.sendRedirect("list");
	}

	// Method that delete a user from the database when we press "Delete" button
	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		userDAO.deleteUser(id);
		response.sendRedirect("list");

	}
}

package com.cofano.coffeecorner.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import com.cofano.coffeecorner.business.dao.UserDAO;
import com.cofano.coffeecorner.business.model.users.User;
import com.cofano.coffeecorner.exceptions.IllegalUserObjectException;
import com.cofano.coffeecorner.exceptions.IllegalUserStatusCodeException;
import com.cofano.coffeecorner.exceptions.MissingUserEmailException;
import com.cofano.coffeecorner.exceptions.MissingUserNameException;
import com.cofano.coffeecorner.exceptions.UserException;

/**
 * The Servlet handling all {@link User user-related} requests.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 * 
 * @version 1 
 */
@Path("user")
public class UserController implements Controller {
	
	/**
	 * Retrieves all the users from the database, if a user is found in
	 * {@link Broadcaster#getOnlineUser(String)} then retrieve its current status,
	 * otherwise mark the user as offline.
	 * 
	 * @return a complete list of all users registered in the system
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getAllUsers() {
		UserDAO dao = new UserDAO();
		List<User> users = dao.getAll();
		
		for (User u : users) {
			if (Broadcaster.getOnlineUser(u.getId()) == null) {
				//If not contained in Broadcaster#onlineUsers then user is offline
				u.setStatusCode(2);
			} else {
				//If contained in Broadcaster#onlineUsers then get the status code
				u.setStatusCode(Broadcaster.getOnlineUser(u.getId()).getStatusCode());
			}
		}
		
		return users;
	}
	
	/**
	 * Updates a user {@link User status} upon a general @PUT request.
	 * 
	 * @param response
	 */
	@PUT
	public void updateStatus(@QueryParam("id") String id, 
			@QueryParam("status") String statusIn, @Context HttpServletResponse response) {

		try {
			
			if (!isValidAuthorId(id)) { throw new IllegalUserObjectException(); }
			int status = Integer.parseInt(statusIn);
			
			if (validStatusCode(status)) {
				Broadcaster.addUser(id, status);
				Broadcaster.broadcastUsers();
			} else { throw new IllegalUserStatusCodeException(); }
			
		} catch(UserException | NumberFormatException e) {
			try { response.sendError(515); }
			catch (IOException ioException) { ioException.printStackTrace(); }
		}

	}
	
	/**
	 * 
	 * @param statusCode the status code to validate
	 * 
	 * @return true if the specified code represents a valid status
	 */
	private static boolean validStatusCode(int statusCode) {
		return statusCode == 0 || statusCode == 1 || statusCode == 2;
	}
	
	/**
	 * Validates a given {@link User status} object.
	 * 
	 * @param user the status to validate
	 * 
	 * @return the validated status, null if components are missing
	 * 
	 * @throws UserException upon invalid input
	 * @throws  
	 */
	protected static User sanitize(User user) throws UserException {
		//Check status code validity
		if (!validStatusCode(user.getStatusCode())) {
			throw new IllegalUserStatusCodeException();
		}
		
		//Check name validity
		if (user.getName() == null || user.getName().equals("")) {
			throw new MissingUserNameException();
		}
		
		//Check email validity
		if (user.getEmail() == null || user.getEmail().equals("")) {
			throw new MissingUserEmailException();
		}
				
		User s = new User();
		s.setEmail(user.getEmail());
		s.setName(user.getName());
		s.setStatusCode(user.getStatusCode());
		
		return s;
		
	}
}

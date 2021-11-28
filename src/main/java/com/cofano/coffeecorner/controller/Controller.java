package com.cofano.coffeecorner.controller;

import java.util.HashMap;

import com.cofano.coffeecorner.business.dao.UserDAO;
import com.cofano.coffeecorner.business.model.users.User;

/**
 * The Controller interface provides default methods for all controller classes
 * to sanitize and validate user inputs.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 *
 */
public interface Controller {

	/**
	 * Uses {@link #escapeHtml(String)} and {@link #escapeSQL(String)} in sequence
	 * to sanitize a given string {@code input}.
	 * 
	 * @param input the input String to sanitize
	 * 
	 * @return the same string escaped from malicious HTML and SQL code
	 */
	public default String escapeAll(String input) {
		return escapeSQL(escapeHtml(input));
	}
	
	/**
	 * Default method used to prevent XSS on input fed to the Controller classes.
	 * 
	 * @param Html the HTML string to filter
	 * 
	 * @return The escaped HTML string
	 */
	public default String escapeHtml(String inputString) {
		HashMap<Integer, String> charMap = new HashMap<Integer, String>();

		charMap.put(34, "&quot;"); // double quote
		charMap.put(35, "&#35;"); // hash mark (no HTML named entity)
		charMap.put(38, "&amp;"); // ampersand
		charMap.put(39, "&apos;"); // apostrophe, aka single quote
		charMap.put(60, "&lt;"); // less than
		charMap.put(62, "&gt;"); // greater than

		StringBuilder builder = new StringBuilder();
		char[] charArray = inputString.toCharArray();

		for (int i = 0; i < charArray.length; i++) {
			char nextChar = charArray[i];
			String entityName = charMap.get((int) nextChar);

			if (entityName == null) {
				builder.append(nextChar);
			} else {
				builder.append(entityName);
			}
		}
		return builder.toString();
	}

	/**
	 * Escape single quotation marks within SQL queries.
	 * 
	 * @param query SQL query to filter
	 * 
	 * @return the appropriate query to be interpreted by SQL
	 */
	public default String escapeSQL(String query) {
		
		query = query.replaceAll("\"", "\\\"");

		query = query.replaceAll("\r", "\\r");
		
		query = query.replaceAll("\n", "<br />");
		
		query = query.replaceAll("'", "''");
    	
		return query;
	}
	
	/**
	 * Checks if the specified ID is a valid user ID.
	 * 
	 * @param authorId the id to validate
	 * 
	 * @return true if  user with this id is found in the database
	 */
	public default boolean isValidAuthorId(String authorId) {
		User u = new UserDAO().get(authorId);
		return u != null;
	}

}

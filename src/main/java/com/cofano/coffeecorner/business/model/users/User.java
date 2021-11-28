package com.cofano.coffeecorner.business.model.users;

import com.cofano.coffeecorner.business.dao.UserDAO;

/**
 * Defines a User object, created whenever there is a new account logging in, 
 * stored using {@link UserDAO} functionalities.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 * 
 */
public class User {
	
	private String id;
	
	private String email;
	
	private String name;
	
	/**
	 * A statusCode of: <li>0 = ONLINE</li><li>1 = DO NOT DISTURB</li><li>2 = OFFLINE</li>
	 */
	private int statusCode;
	
	private String iconUri;

	public User() { }
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		assert statusCode >=0 && statusCode <= 2;
		this.statusCode = statusCode;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the iconUri
	 */
	public String getIconUri() {
		return iconUri;
	}

	/**
	 * @param iconUri the imageUri to set
	 */
	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	/**
	 * @return the id
	 */
	public String getId() { return id; }

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
}

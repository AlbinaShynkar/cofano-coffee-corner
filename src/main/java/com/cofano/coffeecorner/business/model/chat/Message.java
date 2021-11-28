package com.cofano.coffeecorner.business.model.chat;

import java.util.Date;

import com.cofano.coffeecorner.business.model.users.User;

/**
 * The Message class stores the information about  
 * messages on Chat Page of Cofano Coffee Corner.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 * 
 * @version 2
 *
 * Private int id data type added based on new database.
 * To create new message object in MessageDAO, setters added.
 * 
 * @version 3
 * 
 * Messages hold a {@link User} object to store author data
 */

public class Message {
	
	/** 
	 * Unique message id.
	 */
	private int id;
	
	/** 
	 * The time message was sent to chat.
	 */
	private Date time;
	
	/** 
	 * Text of the message (the body).
	 */
	private String text;
	
	/**
	 * True if this message was starred.
	 */
	private boolean starred;
	
	private User author;
	
	public Message() {};
	
	/**
     * @return the id of message
     */
	public int getId() {
		return id;
	}

	/**
     * @return the time of message
     */
	public Date getTime() {
		return time;
	}

	/**
     * @return the text of message
     */
	public String getText() { return text; }
	
	/**
     * @return the author of message
     */
	public User getAuthor() { return author; }

	/**
     * @param id message id to set
     */
	public void setId (int id) {
		this.id = id;
	}

	/**
     * @param time message time to set
     */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
     * @param text message text to set
     */
	public void setText (String text) {
		this.text = text;
	}

	/**
	 * @return the starred
	 */
	public boolean isStarred() {
		return starred;
	}

	/**
	 * @param starred the starred to set
	 */
	public void setStarred(boolean starred) { this.starred = starred; }

	/**
	 * @param author the author of the message to set
	 */
	public void setAuthor(User author) {
		this.author = author;
	}

}

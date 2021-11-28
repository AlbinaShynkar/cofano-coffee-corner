package com.cofano.coffeecorner.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import com.cofano.coffeecorner.business.dao.MessageDAO;
import com.cofano.coffeecorner.business.dao.UserDAO;
import com.cofano.coffeecorner.business.model.chat.Message;
import com.cofano.coffeecorner.exceptions.MessageException;
import com.cofano.coffeecorner.exceptions.IllegalMessageAuthorIdException;
import com.cofano.coffeecorner.exceptions.InvalidMessageIdException;
import com.cofano.coffeecorner.exceptions.MissingMessageTextException;

/**
 * The Servlet handling all {@link Message message-related} requests.
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
@Path("chat")
public class MessageController implements Controller {
	
	/**
	 * Default amount of messages to retrieve from the DB at once, when none is specified.
	 */
	private static final int DEFAULT_RETRIEVED = 20;
	
	/**
	 * Character input by the user to turn a desired string in a message to bold.
	 */
	private static final char BOLD = '*';
	
	/**
	 * HTML constant to represent a bold string's opening tag.
	 */
	private static final String HTMLBOLDO = "<b>";
	
	/**
	 * HTML constant to represent a bold string's closing tag.
	 */
	private static final String HTMLBOLDC = "</b>";
	
	/**
	 * Character input by the user to turn a desired string in a message to italics.
	 */
	private static final char ITALICS = '_';
	
	/**
	 * HTML constant to represent an italics string's opening tag.
	 */
	private static final String HTMLITALICSO = "<i>";
	
	/**
	 * HTML constant to represent an italics string's closing tag.
	 */
	private static final String HTMLITALICSC = "</i>";
	
	/**
	 * Broadcasts a message to the {@link #Broadcaster}.
	 * 
	 * @param message The message to broadcast
	 * @param httpServletResponse 
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public int addMessage(Message message, @QueryParam("author") String authorId,
			@Context HttpServletResponse httpServletResponse) {

		try {
			if (!isValidAuthorId(authorId) ) { throw new IllegalMessageAuthorIdException(); }

			message.setAuthor((new UserDAO()).get(authorId));

			//Secure message prior to storing
			message = sanitize(message);

			addLinkTags(message);
		
			//Add to database
			MessageDAO dao = new MessageDAO();
			int id = dao.save(message);
			message.setId(id);
			Broadcaster.broadcastMessage(message);
			
			return id;
		} catch (MessageException e) {
			try {
				httpServletResponse.setContentType("application/json");
				httpServletResponse.setCharacterEncoding("UTF-8");
				httpServletResponse.setStatus(515);
				
				PrintWriter out =httpServletResponse.getWriter();
				out.print("{ "
							+ "\"message\" : \"" + e.getMessage() + "\", "
							+ "\"severity\" : " + e.getSeverity()
						+ " }");
				out.flush();
				out.close();
			} catch (IOException | IllegalStateException e1) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	@PUT
	public void markStarred(@QueryParam("id") int mid,  @Context HttpServletResponse httpServletResponse) {
		try {
			MessageDAO dao = new MessageDAO();
			dao.markStarred(mid);
				
			Broadcaster.broadcastStarred(dao.get(mid));
		} catch (NullPointerException e) {
			try {
				throw new InvalidMessageIdException();
			} catch (MessageException e1) {
				try {
					httpServletResponse.setContentType("application/json");
					httpServletResponse.setCharacterEncoding("UTF-8");
					httpServletResponse.setStatus(515);
					
					PrintWriter out =httpServletResponse.getWriter();
					out.print("{ "
								+ "\"message\" : \"" + e1.getMessage() + "\", "
								+ "\"severity\" : " + e1.getSeverity()
							+ " }");
					out.flush();
					out.close();
				} catch (IOException | IllegalStateException e2) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @return all the starred messages
	 */
	@GET
	@Path("starred")
	@Produces ({ MediaType.APPLICATION_JSON })
	public List<Message> getAllStarred() {
		
		MessageDAO dao = new MessageDAO();
		
		List<Message> messages = dao.getAll();
		List<Message> starred = new ArrayList<>();
		
		for (Message m : messages) {
			if (m.isStarred()) {
				starred.add(m);
			}
		}
		
		return starred;
	}
	
	/**
	 * Servlet response to the following URI: /rest/chat/messages. Parameters "min" and "max" specify
	 * the interval of messages to retrieve, they are optional ({@link #DEFAULT_RETRIEVED} will
	 * be used if they are not specified)
	 * 
	 * @param min Optional minimum interval boundary
	 * @param max Optional maximum interval boundary
	 * 
	 * @return List of messages in the specified interval min -> max
	 */
	@Path("messages")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getAllMessages(@QueryParam("min") String min, @QueryParam("max") String max) {
		
		if (min == null && max == null)
			return new MessageDAO().getAmount(0, DEFAULT_RETRIEVED);
		else
			try {
				return new MessageDAO().getAmount(Integer.parseInt(min), Integer.parseInt(max));
			} catch (NumberFormatException e) { System.out.println(e); }

		return new MessageDAO().getAmount(0, DEFAULT_RETRIEVED);

	}
	
	/**
	 * Adds an HTML a tag to make links in a message work.
	 * 
	 * @param message the message to check for links
	 */
	public void addLinkTags(Message message) {
		
		String text = message.getText();
		
		String result = "";
		
		while (containsNextLink(text) != null) {
			
			String prefix = containsNextLink(text);
			
			result += text.substring(0, text.indexOf(prefix));

			text = text.substring(text.indexOf(prefix));
			
			if (text.indexOf(" ") == -1) {
				text += " ";
			}
			
			result += "<a href=\"";
			
			if (!prefix.equals("http://") && !prefix.equals("https://")) {
				result += "http://";
			}
			
			result += text.substring(0, text.indexOf(" ")) + "\" target=\"_blank\">"
			 + text.substring(0, text.indexOf(" ")) + "</a>";
			
			text = text.substring(text.indexOf(" "), text.length());
		}
		
		result += text;
		
		message.setText(result);
	}
	
	/**
	 * Check if a certain text contains any of the given link prefixes, if so return 
	 * the closest one so that it can be handled by {@link #addLinkTags(Message)}.
	 * 
	 * @param text the text to be checked for links
	 * 
	 * @return the link prefix used
	 */
	private String containsNextLink(String text) {
		
		int closestOffset = text.length();
		
		String regex = null;
		
		if (text.indexOf("http://") != -1 && text.indexOf("http://") < closestOffset) {
			regex = "http://";
			closestOffset = text.indexOf(regex);
		} 
		
		if (text.indexOf("https://") != -1 && text.indexOf("https://") < closestOffset) {
			regex = "https://";
			closestOffset = text.indexOf(regex);
		} 
		
		if (text.indexOf("www.") != -1 && text.indexOf("www.") < closestOffset) {
			regex = "www.";
			closestOffset = text.indexOf(regex);
		} 
		
		if (text.indexOf("ww2.") != -1 && text.indexOf("ww2.") < closestOffset) {
			regex = "ww2.";
			closestOffset = text.indexOf(regex);
		}
		
		return regex;
		
	}
	
	/**
	 * Validates a given {@link Message message} object.
	 * 
	 * @param m the message to validate
	 * 
	 * @return the validated message, null if components are missing
	 * 
	 * @throws MessageException upon invalid user input
	 */
	private Message sanitize(Message m) throws MessageException {
		
		
		if (m.getText() == null || m.getText().trim().equals("")) { 
			throw new MissingMessageTextException();
		}
		
		//Easter egg
		if (m.getText().equals("&nbsp;")) {
			m.setText(m.getAuthor().getName() + " found the easter egg!");
		}
		//----------
		
		Message temp = new Message();
		
		temp.setTime(new Date());
		temp.setText(m.getText());
		temp.setAuthor(m.getAuthor());
		
		//Escape XSS and SQL Injection attacks
		temp.setText(this.escapeAll(temp.getText()));	
		//-----------------------------------
		
		temp.setText(formatText(temp.getText()));
		
		return temp;
	}
	
	/**
	 * Adds formatting to a text. Uses this legend:
	 * <li>Text in between two '{@link #BOLD *}' will be marked {@link #HTMLBOLDO}</li>
	 * <li>Text in between two '{@link #ITALICS _}' will be marked {@link #HTMLITALICSO}</li>
	 * 
	 * @param text the text to be formatted
	 */
	public String formatText(String text) {
		if (text.contains(String.valueOf(BOLD))) {
			int asterisks = 0;
			for (int i = 0; i < text.length(); i++ ) {
				if (text.charAt(i) == BOLD) {
					asterisks++;
				}
			}
			
			if (asterisks%2 != 0) { asterisks--; } 
			
			for (int i = 0; i < asterisks; i++) {
				int index = text.indexOf(BOLD);
			
				if (i%2 == 0)
					text = text.substring(0, index) + HTMLBOLDO + text.substring(index+1, text.length());
				else
					text = text.substring(0, index) + HTMLBOLDC + text.substring(index+1, text.length());
			}
		}
		
		if (text.contains(String.valueOf(ITALICS))) {
			int undescrores = 0;
			
			for (int i = 0; i < text.length(); i++ ) {
				if (text.charAt(i) == ITALICS) {
					undescrores++;
				}
			}
			
			if (undescrores%2 != 0) { undescrores--; } 
			
			for (int i = 0; i < undescrores; i++) {
				int index = text.indexOf(ITALICS);
				
				if (i%2 == 0)
					text = text.substring(0, index) + HTMLITALICSO + text.substring(index+1, text.length());
				else
					text = text.substring(0, index) + HTMLITALICSC + text.substring(index+1, text.length());
			}
		}
		
		return text;
	}
}

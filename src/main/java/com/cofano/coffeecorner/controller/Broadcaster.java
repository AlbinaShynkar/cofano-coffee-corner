package com.cofano.coffeecorner.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.inject.Singleton;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;

import com.cofano.coffeecorner.business.dao.UserDAO;
import com.cofano.coffeecorner.business.model.bulletin.Bulletin;
import com.cofano.coffeecorner.business.model.chat.Message;
import com.cofano.coffeecorner.business.model.events.Event;
import com.cofano.coffeecorner.business.model.users.User;
import com.cofano.coffeecorner.business.model.users.UserComparator;
import com.cofano.coffeecorner.exceptions.IllegalUserObjectException;
import com.cofano.coffeecorner.exceptions.UserException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is used to broadcast data from any of the models within the system.
 * 
 * @GET Subscribes a user to this broadcast and adds the client to 
 * {@value #onlineUsers} with the respective {@link EventOutput} object, called upon login.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 *
 */
@Path("broadcaster")
@Singleton
public class Broadcaster {
	
	private static SseBroadcaster broadcaster = new SseBroadcaster();
	private static Map<User, EventOutput> onlineUsers = new HashMap<User, EventOutput>();
	private static volatile boolean firstRun = true;
	
	/**
	 * Subscribes a user to {@link #broadcaster}, and lets other 
	 * users know about the new subscriber.
	 * 
	 * @param response the HttpServletResponse object of this request
	 */
	@GET
	@Produces(SseFeature.SERVER_SENT_EVENTS)
	public EventOutput subscribe(@QueryParam("user") String user, @Context HttpServletResponse response) {
		
		/*
		 * Only start the removal thread on the first run, and if the system is not
		 * on testing mode
		 */
		if (firstRun) {
			
			RemoveClosedConnections removalThread = new RemoveClosedConnections();
			removalThread.setDaemon(true);
			removalThread.start();
			firstRun = false;
			
		}
		
		try {
			
			User u = new ObjectMapper().readValue(user, User.class);
			UserController.sanitize(u);
			EventOutput eventOutput = new EventOutput();
			broadcaster.add(eventOutput);
			onlineUsers.put(u, eventOutput);
			new UserDAO().save(u);
			
			return eventOutput;
			
		} catch (IOException | UserException e) {
			
			try { throw new IllegalUserObjectException(); }
			catch (IllegalUserObjectException e1) {
				
				try {
					
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(515);
					
					PrintWriter out = response.getWriter();
					out.print("{ "
								+ "\"message\" : \"" + e1.getMessage() + "\", "
								+ "\"severity\" : " + e1.getSeverity()
							+ " }");
					out.flush();
					out.close();
					
				} catch (IOException e2) { e2.printStackTrace(); }
				
			}
			
		}
		
		return null;
		
	}
	
	/**
	 * Broadcasts the lists of users after a new user joins/changes status.
	 */
	public static void broadcastUsers() {
		
		List<User> users = new ArrayList<>(onlineUsers.keySet());
		Collections.sort(users, new UserComparator());
		
		OutboundEvent event = new OutboundEvent.Builder()
				.name("users")
				.mediaType(MediaType.APPLICATION_JSON_TYPE)
				.data(users)
				.build();
		
		broadcaster.broadcast(event);
		
	}
	
	/**
	 * Broadcasts a {@link Bulletin bulletin} object to all subscribers.
	 * 
	 * @param b the bulletin to be broadcasted
	 */
	protected static void broadcastBulletin(Bulletin b) {
		
		OutboundEvent event = new OutboundEvent.Builder()
				.name("bulletin")
				.mediaType(MediaType.APPLICATION_JSON_TYPE)
				.data(b)
				.build();
		
		broadcaster.broadcast(event);
		
	}
	
	/**
	 * Broadcasts an {@link Event event} object to all subscribers.
	 * 
	 * @param e the event to be broadcasted
	 */
	protected static void broadcastEvent(Event e) {
		
		OutboundEvent event = new OutboundEvent.Builder()
				.name("events")
				.mediaType(MediaType.APPLICATION_JSON_TYPE)
				.data(e)
				.build();
		
		broadcaster.broadcast(event);
		
	}
	
	/**
	 * Broadcasts a {@link Message message} object to all subscribers.
	 * 
	 * @param m the message to be broadcasted
	 */
	protected static void broadcastMessage(Message m) {
		
		OutboundEvent event = new OutboundEvent.Builder()
				.name("message")
				.mediaType(MediaType.APPLICATION_JSON_TYPE)
				.data(m)
				.build();
		
		broadcaster.broadcast(event);
		
	}
	
	/**
	 * Broadcasts a change made to a {@link Message message} object <i>m</i>.<br>
	 * <i>(Regarding its <b>starred</b> attribute. See {@link Message} for more)</i>
	 * 
	 * @param m the message object affected
	 */
	protected static void broadcastStarred(Message m) {
		
		OutboundEvent event = new OutboundEvent.Builder()
				.name("starred")
				.mediaType(MediaType.APPLICATION_JSON_TYPE)
				.data(m)
				.build();
		
		broadcaster.broadcast(event);
		
	}
	
	
	/**
	 * Checks if a user is already connected and if so, the client's data (status) will be updated
	 * to the desired one, otherwise, he will be added to {@link #onlineUsers}.
	 * 
	 * @param id The updated information regarding this user
	 */
	public static void addUser(String id, int statusCode) {
		
		synchronized (onlineUsers) {
			
			Iterator<User> i = onlineUsers.keySet().iterator();
			Map<User, EventOutput> temp = new HashMap<>();

			while (i.hasNext()) {
				
				User u = i.next();
				if (u.getId().equals(id)) {
					
					u.setStatusCode(statusCode);
					temp.put(u, onlineUsers.get(u));
					i.remove();
					
				}
				
			}
			
			onlineUsers.putAll(temp);
			
		}
		
	}
	
	public static User getOnlineUser(String id) {
		
		for (User u : onlineUsers.keySet()) if (u.getId().equals(id)) return u;
		return null;
		
	}
	
	/**
	 * This class removes offline users from {@link Broadcaster#onlineUsers onlineUsers} every 
	 * {@value RemoveClosedConnections#USERCHECKUP} milliseconds.
	 *
	 */
	private class RemoveClosedConnections extends Thread {

		/**
		 * Specifies the number of milliseconds that pass between each check made against
		 * the users in {@link Broadcaster#onlineUsers} to see if clients are online.
		 */
		private static final int USERCHECKUP = 10000;
		
		@Override
		public void run() {
			
			while (true) {
				
				try { Thread.sleep(USERCHECKUP); }
				catch (InterruptedException e) { e.printStackTrace(); }
				
				synchronized (onlineUsers) {
					
					OutboundEvent sampleEvent = new OutboundEvent.Builder()
							.name("eom")
							.data("EOM")
							.build();
					
					Iterator<User> i = onlineUsers.keySet().iterator();
					
					while (i.hasNext()) {
						
						User u = i.next();
						
						try { onlineUsers.get(u).write(sampleEvent); }
						catch (Exception e) {
							i.remove();
							broadcastUsers();
						}
						
					}
					
				}
			}
		}
	}
}

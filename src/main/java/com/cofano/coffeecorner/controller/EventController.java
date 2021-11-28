package com.cofano.coffeecorner.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import com.cofano.coffeecorner.business.dao.EventDAO;
import com.cofano.coffeecorner.business.dao.UserDAO;
import com.cofano.coffeecorner.business.model.events.Event;
import com.cofano.coffeecorner.business.model.users.User;
import com.cofano.coffeecorner.exceptions.EventException;
import com.cofano.coffeecorner.exceptions.IllegalEventAuthorIdException;
import com.cofano.coffeecorner.exceptions.IllegalEventTimeException;
import com.cofano.coffeecorner.exceptions.InvalidEventIdException;
import com.cofano.coffeecorner.exceptions.InvalidEventTypeException;
import com.cofano.coffeecorner.exceptions.MissingEventTitleException;

/**
 * The Servlet handling all {@link Event event-related} requests.
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

@Path("events")
public class EventController implements Controller {
	
	/**
	 * Default amount of events to retrieve from the DB at once, when none is specified.
	 */
	private static final int DEFAULT_RETRIEVED = 5;
	
	/**
	 * Retrieve all events within the interval specified (min -> max).
	 * 
	 * @param min the amount of most recent events to be skipped
	 * @param max the amount of events to retrieve after </i>min</i>
	 * 
	 * @return a {@link List list} of {@link Event} objects found within the interval
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Event> getEvents(@QueryParam("min") String min, @QueryParam("max") String max) {
		
		EventDAO dao = new EventDAO();
		
		if (min != null && max == null) return dao.getAmount(Integer.parseInt(min), (DEFAULT_RETRIEVED + Integer.parseInt(min)), false);
		
		try { return dao.getAmount(Integer.parseInt(min), Integer.parseInt(max), false); }
		catch (NullPointerException | NumberFormatException e) { e.printStackTrace(); }
		
		return dao.getAmount(0, DEFAULT_RETRIEVED, false);
		
	}
	
	/**
	 * Retrieve all events within the interval specified (min -> max).
	 * 
	 * @return a {@link List list} of {@link Event} objects found within the interval
	 */
	@GET
	@Path("next")
	@Produces({ MediaType.APPLICATION_JSON })
	public Event getNext() {
		
		try { return new EventDAO().getAmount(0, 1, true).get(0); }
		catch (IndexOutOfBoundsException e) { return null; }
		
	}
	
	/**
	 * 
	 * @return all the events of type 'break' stored
	 */
	@GET
	@Path("daily")
	@Produces ({ MediaType.APPLICATION_JSON })
	public List<Event> getDaily() { return new EventDAO().getTodaysEvents(); }
	
	/**
	 * Gets participants of certain event.
	 * 
	 * @param id id of the event
	 * @return list of users who are participants of the event
	 */
	@Path("participants")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getParticipants(@QueryParam("id") String id, @Context HttpServletResponse httpServletResponse) {
		
		try {
			
			int idInt = Integer.parseInt(id);
			List<User> res = (new UserDAO()).getEventParticipants(idInt);
			
			return res;
			
		} catch (NumberFormatException e) {
			try { throw new InvalidEventIdException(); }
			catch (EventException e1) {
				
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
				} catch (IOException | IllegalStateException e2) { e2.printStackTrace(); }
				
			}
			
			return null;
			
		}
		
	} 
	
	/**
	 * Assigning user as participant of the given event. 
	 * 
	 * @param userId id of the user
	 * @param eventId id of given event
	 */
	@Path("subscribe")
	@POST
	public void subscribe(@QueryParam("userId") String userId, 
			@QueryParam("eventId") String eventId, @Context HttpServletResponse httpServletResponse) {
		
		EventDAO dao = new EventDAO();
		
		try {
			
			if (!isValidAuthorId(userId)) throw new IllegalEventAuthorIdException();
		
			int eventIdInt = Integer.parseInt(eventId);
			if (dao.get(eventIdInt) == null) throw new InvalidEventIdException();
			dao.subscribeUser(userId, eventIdInt);
			
		} catch (NumberFormatException | EventException e) {
			
			try { throw new IllegalEventAuthorIdException(); }
			catch (EventException e1) {
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
					
				} catch (IOException | IllegalStateException e2) { e2.printStackTrace(); }
			}
		}
	}
	
	
	/**
	 * Posts an event.
	 * 
	 * @param e the event object to be posted
	 */
	@POST
	@Consumes ({ MediaType.APPLICATION_JSON })
	public int postEvent(Event e, @QueryParam("author") String authorId, @Context HttpServletResponse response) {
		
		try {
			
			if (!isValidAuthorId(authorId) ) { throw new IllegalEventAuthorIdException(); }
		
			e.setAuthor((new UserDAO()).get(authorId));
			
			e = sanitize(e);
			
			EventDAO dao= new EventDAO();
			int id = dao.save(e);
			e.setId(id);
			Broadcaster.broadcastEvent(e);
			
			return id;
			
		} catch(EventException ex) {
			try {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(515);
				
				PrintWriter out = response.getWriter();
				out.print("{ "
							+ "\"message\" : \"" + ex.getMessage() + "\", "
							+ "\"severity\" : " + ex.getSeverity()
						+ " }");
				out.flush();
				out.close();
			} catch (IOException | IllegalStateException e1) {e1.printStackTrace(); }
		}
		return -1;
	}
	
	/**
	 * Deletes an event given its id.
	 * 
	 * @param id the id to delete
	 */
	@DELETE
	public void deleteEvent(@QueryParam("id") String id, @Context HttpServletResponse response) {
		
		try { new EventDAO().delete(Integer.parseInt(id)); }
		catch (NumberFormatException e) { e.printStackTrace(); }
		
	}
	
	/**
	 * Checks if a given type is valid or not. Adds a layer of security to the application
	 * and prevents possible failures/crashes upon certain inputs.
	 * 
	 * @param type the given type to check
	 * 
	 * @return true if the specified type is valid, false otherwise
	 */
	private boolean isValidType(String type) {
		
		ArrayList<String> types = new ArrayList<>();
		types.add("unspecified");
		types.add("break");
		types.add("meeting");
		types.add("stand-up meeting");
		types.add("lunch");
		types.add("corporate event");
		
		for (String t : types) if (type.toLowerCase().equals(t)) return true;
		return false;
		
	}
	
	/**
	 * Checks if the given dates are valid start and end dates for an {@link Event event} object.
	 * 
	 * @param start the start time of the event
	 * @param end the end time of the event
	 * 
	 * @return true if both dates are suitable start and end dates
	 */
	private boolean validStartEndTimes(Date start, Date end) {
		
		if (start == null || end == null) return false;
		if (start.after(end)) return false;
		return true;
		
	}
	
	/**
	 * Validates a given {@link Event event} object.
	 * 
	 * @param event the event to validate
	 * 
	 * @return the validated event, null if components are missing
	 * 
	 * @throws EventException upon invalid user input
	 */
	private Event sanitize(Event event) throws EventException {
		
		boolean nullBody = event.getBody() == null;
		boolean nullImageUri = event.getImageUri() == null;
		
		//Check title validity
		if (event.getTitle() == null || event.getTitle().equals("")) throw new MissingEventTitleException();
		
		//Check start time validity
		if (!validStartEndTimes(event.getStart(), event.getEnd())) throw new IllegalEventTimeException();
		
		//Check event's type validity
		if (event.getType() == null || !isValidType(event.getType())) throw new InvalidEventTypeException();
		
		Event e = new Event();
		
		if (!nullBody) { e.setBody(event.getBody()); }
		if (!nullImageUri) { e.setImageUri(event.getImageUri()); }
		
		e.setTitle(event.getTitle());
		e.setStart(event.getStart());
		e.setEnd(event.getEnd());
		e.setType(event.getType());
		e.setAuthor(event.getAuthor());
		
		
		//Escape XSS and SQL Injection attacks
		e.setTitle(this.escapeAll(e.getTitle()));
		
		if (!nullBody) e.setBody(this.escapeAll(e.getBody()));
		if (!nullImageUri) e.setImageUri(this.escapeAll(e.getImageUri()));
		//------------------------------------
		
		return e;
		
	}
	
}
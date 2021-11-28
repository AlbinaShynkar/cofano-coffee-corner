package com.cofano.coffeecorner.controller;

import java.io.IOException;
import java.io.PrintWriter;
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

import com.cofano.coffeecorner.business.dao.BulletinDAO;
import com.cofano.coffeecorner.business.dao.UserDAO;
import com.cofano.coffeecorner.business.model.bulletin.Bulletin;
import com.cofano.coffeecorner.exceptions.BulletinException;
import com.cofano.coffeecorner.exceptions.IllegalBulletinAuthorIdException;
import com.cofano.coffeecorner.exceptions.MissingBulletinBodyException;
import com.cofano.coffeecorner.exceptions.MissingBulletinTitleException;

/**
 * The Servlet handling all {@link Bulletin bulletin-related} requests.
 * 
 * @POST Will add a Bulletin to the database
 * @GET Will return the specified {@link #DEFAULT_RETRIEVED} amount of bulletins,
 * unless otherwise is stated in @QueryParam.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 *
 */
@Path("bulletins")
public class BulletinController implements Controller {
	
	/**
	 * Default amount of messages to retrieve from the DB at once, when none is specified.
	 */
	private static final int DEFAULT_RETRIEVED = 20;
	
	@GET
	@Produces( MediaType.APPLICATION_JSON )
	public List<Bulletin> getBulletins(@QueryParam("min") String min, @QueryParam("max") String max) {
		
		if (min == null || max == null)
			return new BulletinDAO().getAmount(0, DEFAULT_RETRIEVED);
		else
			try {
				return new BulletinDAO().getAmount(Integer.parseInt(min), Integer.parseInt(max));
			} catch (NumberFormatException e) { System.out.println(e); }

		return null;
	}
	
	/**
	 * Validates a Bulletin object. If valid, it will post a new Bulletin, 
	 * otherwise, it will send a 515 error code.
	 * 
	 * @param b the Bulletin to be posted
	 * @param response servlet response object
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public int newBulletin(Bulletin b, @QueryParam("author") String authorId, @Context HttpServletResponse response) {
		
		try {
			if (!isValidAuthorId(authorId) ) { throw new IllegalBulletinAuthorIdException(); }
			
			b.setAuthor((new UserDAO()).get(authorId));
			
			b = sanitize(b);
			
			BulletinDAO dao = new BulletinDAO();
			
			int id = dao.save(b);
			b.setId(id);
			Broadcaster.broadcastBulletin(b);
			return id;
		} catch (BulletinException e) {
			try {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(515);
				
				PrintWriter out = response.getWriter();
				out.print("{ "
							+ "\"message\" : \"" + e.getMessage() + "\", "
							+ "\"severity\" : " + e.getSeverity()
						+ " }");
				out.flush();
				out.close();
			} catch (IOException | IllegalStateException e1) {
				e1.printStackTrace();
			}
			
			return -1;
		}
	}
	
	/**
	 * Deletes a bulletin given its id.
	 * 
	 * @param id the id to delete
	 */
	@DELETE
	public void deleteBulletin(@QueryParam("id") String id) {
		
		try {
			BulletinDAO dao = new BulletinDAO();
			dao.delete(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Validates a given {@link Bulletin bulletin} object.
	 * 
	 * @param bulletin the bulletin to validate
	 * 
	 * @return the validated bulletin, null if components are missing
	 * 
	 * @throws BulletinException upon invalid user input
	 */
	private Bulletin sanitize(Bulletin bulletin) throws BulletinException {
		
		boolean nullImageUri = bulletin.getImageUri() == null;
		
		//Check title validity
		if (bulletin.getTitle() == null || bulletin.getTitle().equals("")) {
			throw new MissingBulletinTitleException();
		}
		//Check body validity
		if (bulletin.getBody() == null || bulletin.getBody().equals("")) {
			throw new MissingBulletinBodyException();
		}
		
		Bulletin b = new Bulletin();
		
		b.setTitle(bulletin.getTitle());
		b.setBody(bulletin.getBody());
		
		if (!nullImageUri) { b.setImageUri(bulletin.getImageUri()); }
		b.setAuthor(bulletin.getAuthor());
		
		//Escape XSS and SQL Injection attacks
		b.setTitle(this.escapeAll(b.getTitle()));
		b.setBody(this.escapeAll(b.getBody()));
		
		if (!nullImageUri) { b.setImageUri(this.escapeAll(b.getImageUri())); }
		//-----------------------------------
		
		return b;
	}
	
}

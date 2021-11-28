package com.cofano.coffeecorner.business.model.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.cofano.coffeecorner.business.dao.EventDAO;
import com.cofano.coffeecorner.business.dao.UserDAO;
import com.cofano.coffeecorner.business.model.events.Event;
import com.cofano.coffeecorner.business.model.users.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class testUserDAO {
	
	private static final String ID1 = "12345678";
	private static final String ID2 = "123456789";
	
	private static  int EVENT_ID ;

	static UserDAO dao;
	static User u1;
	static User u2;
	
	static EventDAO daoEvent ;
	static Event event ;	

	/**
	 * User name is initialized as "zzz user X", since it is deleted after all, 
	 * it should be the end of the User List which is ordered by name
	 */
	@BeforeAll
	static void setup() {
		
		dao = new UserDAO();
		u1 = new User();
		u2 = new User();
		
		u1.setId(ID1);
		u1.setName("zzz user 1");
		u1.setEmail("user1.gmail");
		u1.setIconUri("testicon1.com");
		
		u2.setId(ID2);
		u2.setName("zzz user 2");
		u2.setEmail("user2.gmail");
		u2.setIconUri("testicon2.com");
		
		daoEvent = new EventDAO(); 
		event = new Event();
		
		Date start = new Date();
		Date end = new Date();
		event.setTitle("user test"); 
		event.setBody(("user test event"));
		event.setType("test"); 
		event.setStart(start); 
		event.setEnd(end);
		event.setImageUri("image_uri"); 
	}
	
	
	@Test
	@Order(1)
	void testSaveUser() {

		dao.save(u1);
		dao.save(u2);
		
		event.setAuthor(u1); 
		EVENT_ID = daoEvent.save(event) ;
	
		
		Assertions.assertNotNull(dao.get(ID1));
		Assertions.assertNotNull(dao.get(ID2));
	}
	
	@Test
	@Order(3)
	void testRetrieveUser() {
		
		//Test the retrieval
		Assertions.assertEquals(u1.getId(), dao.get(ID1).getId());
		Assertions.assertEquals(u1.getName(), dao.get(ID1).getName());
		Assertions.assertEquals(u1.getEmail(), dao.get(ID1).getEmail());
		Assertions.assertEquals(u1.getIconUri(), dao.get(ID1).getIconUri());
		
		Assertions.assertEquals(u2.getId(), dao.get(ID2).getId());
		Assertions.assertEquals(u2.getName(), dao.get(ID2).getName());
		Assertions.assertEquals(u2.getEmail(), dao.get(ID2).getEmail());
		Assertions.assertEquals(u2.getIconUri(), dao.get(ID2).getIconUri());
	}
	
	@Test
	@Order(2)
	void testGetAllUser() {
		
		List<User> out = new ArrayList<>();
		out = dao.getAll();
		
		Assertions.assertEquals(ID1,  out.get( out.size()-2 ).getId());
		Assertions.assertEquals(ID2,  out.get( out.size()-1 ).getId());
		
		Assertions.assertEquals(u1.getName(),  out.get( out.size()-2 ).getName());
		Assertions.assertEquals(u2.getName(),  out.get( out.size()-1 ).getName());
	}
	
	@Test
	@Order(4)
	void testEventParticipants() 
	{
	    daoEvent.subscribeUser(ID1, EVENT_ID);
	    List<User> out = new ArrayList<>(); 
	    out = dao.getEventParticipants(EVENT_ID); 
	  
	    Assertions.assertEquals(ID1, out.get(0).getId());
	}
	
	@Test
    @Order(5) 
    void testDeleteUser() 
	{ 
		//Test the deletion 
		dao.delete(ID1);
		dao.delete(ID2);
		Assertions.assertNull(dao.get(ID1)); 
		Assertions.assertNull(dao.get(ID2)); 
	}
		
	@AfterAll 
	static void cleanDB() 
	{
		daoEvent.delete(event.getId()) ;
	}
	 
}
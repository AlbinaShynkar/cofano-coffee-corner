package com.cofano.coffeecorner.business.model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.cofano.coffeecorner.business.dao.EventDAO;
import com.cofano.coffeecorner.business.dao.UserDAO;
import com.cofano.coffeecorner.business.model.events.Event;
import com.cofano.coffeecorner.business.model.users.User;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class testEventDAO {
	
	private static final String ID1 = "12345";

	static EventDAO dao;
	static Event e1;
	static Event e2;
	static Event e3;

	static UserDAO daoUser;
	static User u1;
	
	static int id1;
	static int id2;
	static int id3;

	@BeforeAll
	static void setup() {

		daoUser = new UserDAO();
		u1 = new User();

		u1.setId(ID1);
		u1.setName("Testing User");
		u1.setEmail("test@gmail.com");
		u1.setIconUri("http://example.com/icon.png");
		daoUser.save(u1);

		dao = new EventDAO();
		e1 = new Event();
		e2 = new Event();
		e3 = new Event();
		
		Date start1 = new Date();
		Date end1 = new Date();
		e1.setTitle("Test Event title 1");
		e1.setStart(start1);
		e1.setEnd(end1);
		e1.setBody("Test Event body 1");
		e1.setType("Test EventType 1");
		e1.setAuthor(u1);
		e1.setImageUri("image1");

		Date start2 = new Date();
		Date end2 = new Date();
		e2.setTitle("Test Event title 2");
		e2.setStart(start2);
		e2.setEnd(end2);
		e2.setBody("Test Event body 2");
		e2.setType("Test EventType 2");
		e2.setAuthor(u1);
		e2.setImageUri("image2");

		Date start3 = new Date();
		Date end3 = new Date();
		e3.setTitle("Test Event title 3");
		e3.setStart(start3);
		e3.setEnd(end3);
		e3.setBody("Test Event body 3");
		e3.setType("Test EventType 3");
		e3.setAuthor(u1);
		e3.setImageUri("image3");;
	}

	@Test
	@Order(1)
	void testSaveGetIdEvent() {

		id1 = dao.save(e1);
		id2 = dao.save(e2);
		id3 = dao.save(e3);
		/* prints the ids which are corect */

		Assertions.assertEquals(id1, id2 - 1);
		Assertions.assertEquals(dao.get(id1).getTitle(), "Test Event title 1");
		Assertions.assertEquals(dao.get(id2).getTitle(), "Test Event title 2");
	}


	@Test
	@Order(3)
	void testGetAmountEvent() {
		
		List<Event> events = new ArrayList<>();

		/* saves 2 other events objects into database */
		for (int i = 4; i < 6; i++) {
			Event e4 = new Event();

			Date start4 = new Date();
			Date end4 = new Date();
			e4.setTitle("Test Event title " + i);
			e4.setStart(start4);
			e4.setEnd(end4);
			e4.setBody("Test Event body " + i);
			e4.setType("Test EventType " + i);
			e4.setAuthor(u1);
			e4.setImageUri("image4");
			dao.save(e4);
		}

		for (int min = 0; min < 5; min++) {

			for (int max = 5; max >= min; max--) {
				/* checks the sizes of retrieved events objects array 
				 * against max-min */
				events = dao.getAmount(min, max, false);
				int count = events.size();
				Assertions.assertEquals(count, max - min);
			}
		}
	}

	
	@Test
	@Order(4) 
    void testDeleteEvent() 
	{ 
		for(int i = 0 ; i<5 ; i++) { 
			dao.delete(id1+i); 
			Assertions.assertNull(dao.get(id1+i)); 
		}
	}
	  
}
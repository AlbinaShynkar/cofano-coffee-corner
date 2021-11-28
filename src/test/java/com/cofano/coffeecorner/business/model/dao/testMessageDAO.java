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

import com.cofano.coffeecorner.business.dao.MessageDAO;
import com.cofano.coffeecorner.business.dao.UserDAO;
import com.cofano.coffeecorner.business.model.chat.Message;
import com.cofano.coffeecorner.business.model.users.User;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class testMessageDAO {

	private static int ID1;
	private static int ID2;
	
	private static final int DEFAULT_MAX = 2;
	private static final int DEFAULT_MIN = 0;
	
	static MessageDAO dao;
	static UserDAO userDAO;
	static User u1;
	
	static Message m1;
	static Message m2;
	
	static int id1;
	static int id2;
	
	static User u;

	@BeforeAll
	static void setup() {
		userDAO = new UserDAO();
		u1 = new User();

		u1.setId("12345");
		u1.setName("Testing User");
		u1.setEmail("test@gmail.com");
		u1.setIconUri("http://example.com/icon.png");
		userDAO.save(u1);
		
		dao = new MessageDAO();
		m1 = new Message();
		m2 = new Message();	
		m1.setText("This is the message from m1");
		m2.setText("This is the message from m2");
		m1.setTime(new Date());
		m2.setTime(new Date());
		m1.setAuthor(u1);
		m2.setAuthor(u1);
	}
	
	@Test
	@Order(1)
	void testSaveMessage() {
		ID1 = dao.save(m1);
		ID2 = dao.save(m2);
		Assertions.assertNotNull(dao.get(ID1));
		Assertions.assertNotNull(dao.get(ID2));
		Assertions.assertEquals(ID1, ID2 - 1);
	}

	@Test
	@Order(2)
	void testGetMessage() {
		
		Assertions.assertEquals(m1.getAuthor().getId(), dao.get(ID1).getAuthor().getId());
		Assertions.assertEquals(m1.getText(), dao.get(ID1).getText());
		
		Assertions.assertEquals(m2.getAuthor().getId(), dao.get(ID2).getAuthor().getId());
		Assertions.assertEquals(m2.getText(), dao.get(ID2).getText());
	}
	
	@Test
	@Order(3)
	void testStarredMessage() {
		
		if (m1.isStarred())
		{
			dao.markStarred(ID1);
			Assertions.assertFalse(dao.get(ID1).isStarred());
		}	
		else
		{
			dao.markStarred(ID1);
			Assertions.assertTrue(dao.get(ID1).isStarred());
		}	
		dao.markStarred(ID1);  
	}
	
	@Test
	@Order(4)
	void testGetAmountMessage() {
		List<Message> out = new ArrayList<>();
		out = dao.getAmount(DEFAULT_MIN, DEFAULT_MAX);
		Assertions.assertEquals(DEFAULT_MAX - DEFAULT_MIN, out.size());
	}
	

}
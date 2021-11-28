package com.cofano.coffeecorner.business.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.cofano.coffeecorner.business.dao.BulletinDAO;
import com.cofano.coffeecorner.business.dao.UserDAO;
import com.cofano.coffeecorner.business.model.bulletin.Bulletin;
import com.cofano.coffeecorner.business.model.users.User;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class testBulletinDAO {
	
	private static final int DEFAULT_MAX = 2;
	private static final int DEFAULT_MIN = 0;
	
	static BulletinDAO dao;
	static Bulletin b1;
	static Bulletin b2;
	
	static UserDAO daoUser;
	static User author1;
	static User author2;
	
	static int id1;
	static int id2;


	@BeforeAll
	static void setup() {
		dao = new BulletinDAO();
		b1 = new Bulletin();
		b2 = new Bulletin();	
		
		daoUser = new UserDAO();
		author1 = new User();
		author2 = new User();
		
		author1.setId("12345678");
		author1.setName("author1");
		author1.setEmail("test.author1.com");
		author1.setIconUri("icon1.com");
		
		author2.setId("123456789");
		author2.setName("author2");
		author2.setEmail("test.author2.com");
		author2.setIconUri("icon2.com");
		
		daoUser.save(author1);
		daoUser.save(author2);
		
		b1.setTitle("Test Title 1");
		b1.setBody("Test body bulletin 1");
		b1.setAuthor(author1);
		
		b2.setTitle("Test Title 2");
		b2.setBody("Test body bulletin 2");
		b2.setAuthor(author2);
	}
	
	@Test
	@Order(1)
	void testSaveBulletin() {
		
		id1 = dao.save(b1);
		id2 = dao.save(b2);
		
		Assertions.assertNotNull(id1);
		Assertions.assertNotNull(id2);
		Assertions.assertEquals(id1, id2-1);
	}
	
	@Test
	@Order(2)
	void testRetrieveBulletin() {
		
		//Test the retrieval
		Assertions.assertEquals(b1.getTitle(), dao.get(id1).getTitle());
		Assertions.assertEquals(b1.getBody(), dao.get(id1).getBody());
		Assertions.assertEquals(b1.getAuthor().getId() , dao.get(id1).getAuthor().getId());
		Assertions.assertEquals(b1.getAuthor().getName() , dao.get(id1).getAuthor().getName());
		
		Assertions.assertEquals(b2.getTitle(), dao.get(id2).getTitle());
		Assertions.assertEquals(b2.getBody(), dao.get(id2).getBody());
		Assertions.assertEquals(b2.getAuthor().getId(), dao.get(id2).getAuthor().getId());
		Assertions.assertEquals(b2.getAuthor().getName() , dao.get(id2).getAuthor().getName());
	}
	
	@Test
	@Order(3)
	void testGetAmountBulletin() {
		
		List<Bulletin> out = new ArrayList<>();
		out = dao.getAmount(DEFAULT_MIN, DEFAULT_MAX);
		Assertions.assertEquals(DEFAULT_MAX - DEFAULT_MIN, out.size());
	}
	
	@Test 
	@Order(4)
	void testDeleteBulletin() { 
		
	  dao.delete(id1);
	  dao.delete(id2);
	  Assertions.assertNull(dao.get(id1)); 
	  Assertions.assertNull(dao.get(id2)); 
	}
	  
	@AfterAll 
	static void cleanDB() 
	{ 
		daoUser.delete(author1.getId()) ;
	    daoUser.delete(author2.getId()) ; 
	}
	 
}
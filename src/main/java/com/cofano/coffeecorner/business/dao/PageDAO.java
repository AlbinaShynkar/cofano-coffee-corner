package com.cofano.coffeecorner.business.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cofano.coffeecorner.business.model.dashboard.Card;
import com.cofano.coffeecorner.business.model.dashboard.Dashboard;
import com.cofano.coffeecorner.business.model.page.Page;
import com.cofano.coffeecorner.data.Database;

/**
 * The class works with database, getting information about different pages from it.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 * 
 * @version 1
 *
 */
public class PageDAO {
	
    public Page fill(ResultSet r1, ResultSet r2) throws SQLException {

        Page page = new Page();
        
        while (r1.next()) page.put(r1.getString("id"), r1.getString(2));
    	while (r2.next()) page.put(r2.getString("attribute"), r2.getString("value"));
    	
        return page;

    }

    /**
     * Retrieves the information from database regarding the bulletin page.
     *
     * @return new {@link Page} object filled with information from database
     */
    public Page getBulletinPage() {

    	Page p = null;
        Database DB = new Database();
    	
        try {
            
            String q1 = "SELECT id, bulletin FROM page;";
            String q2 = "SELECT * FROM bulletin_page;";
            
            ResultSet r1 = DB.execute(q1);
            ResultSet r2 = DB.execute(q2);
            
            p = fill(r1, r2);

        } catch (SQLException ex) { ex.printStackTrace(); }
        finally { DB.close(); }

        return p;

    }

    /**
     * Retrieves the information from database regarding the event page.
     *
     * @return new {@link Page} object filled with information from database
     */
    public Page getEventPage() {

    	Page p = null;
        Database DB = new Database();
    	
        try {

            String q1 = "SELECT id, event FROM page;";
            String q2 = "SELECT * FROM event_page;";
            
            ResultSet r1 = DB.execute(q1);
            ResultSet r2 = DB.execute(q2);
            
            p = fill(r1, r2);
            
        } catch (SQLException ex) { ex.printStackTrace(); }
        finally { DB.close(); }

        return p;
        
    }
    
    /**
     * Retrieves the information from database regarding the chat page.
     *
     * @return new {@link Page} object filled with information from database
     */
    public Page getChatPage() {

    	Page p = null;
        Database DB = new Database();
    	
        try {

            String q1 = "SELECT id, chat FROM page;";
            String q2 = "SELECT * FROM chat_page;";
            
            ResultSet r1 = DB.execute(q1);
            ResultSet r2 = DB.execute(q2);
            
            p = fill(r1, r2);
            
        } catch (SQLException ex) { ex.printStackTrace(); }

        return p;
        
    }
    
    /**
     * Retrieves the information from database regarding the dash board.
     *
     * @return new {@link Page} object filled with information from database
     */
    public Dashboard getDashboard() {

    	Dashboard dashboard = new Dashboard();
        Database DB = new Database();
    	
        try {
        	
            String q1 = "SELECT id, dashboard FROM page;";
            String q2 = "SELECT * FROM dashboard;";
            
            ResultSet r1 = DB.execute(q1);
            ResultSet r2 = DB.execute(q2);
            
            String q3 = "SELECT * FROM card " +
                        "WHERE page_id = 'dashboard' " +
                        "ORDER BY id;";
            
            //Populate the dashboard map
            dashboard = Dashboard.fill(r1, r2);
            
            //Add cards to the dashboard
            ResultSet r3 = DB.execute(q3);
            
			List<Card> cards = new ArrayList<>();
			
			while (r3.next()) {
			 
				Card c = new Card();
				c.setId(r3.getInt("id"));
				c.setTitle(r3.getString("title"));
				c.setImageUri(r3.getString("image_uri"));
				c.setDescription(r3.getString("description"));
				cards.add(c);
				
			}
			
			dashboard.setCards(cards);


        } catch (SQLException ex) { ex.printStackTrace(); }
        finally { DB.close(); }

        return dashboard;
        
    }
    
    /**
     * Retrieves the information from database regarding the login page.
     *
     * @return new {@link Page} object filled with information from database
     */
    public Page getLoginPage() {

    	Page p = null;
        Database DB = new Database();
    	
        try {

            String q1 = "SELECT id, login FROM page;";
            String q2 = "SELECT * FROM login_page;";
            
            ResultSet r1 = DB.execute(q1);
            ResultSet r2 = DB.execute(q2);
            
            p = fill(r1, r2);
            

        } catch (SQLException ex) { ex.printStackTrace(); }
        finally { DB.close(); }

        return p;
        
    }
    
}

package com.cofano.coffeecorner.business.model.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.cofano.coffeecorner.business.model.page.Page;

/**
 * The Dashboard page of the system. This is the initial {@link Page page} 
 * which on top of the {@link Page} attributes holds {@link Card} objects. Its {@link #fill(ResultSet, ResultSet)}
 * method is used by {@link PageDAO} to create an instance of this class.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 *
 */
@SuppressWarnings("serial")
public class Dashboard extends Page {
	
	public Dashboard() {}
	
	public static Dashboard fill(ResultSet r1, ResultSet r2) throws SQLException {

        Dashboard d = new Dashboard();

        while (r1.next()) {
    		d.put(r1.getString("id"), r1.getString(2));
    	}

    	while (r2.next()) {
    		d.put(r2.getString("attribute"), r2.getString("value"));
    	}

        return d;

    }
	
	public void setCards(List<Card> cards) {
		this.put("cards", cards);
	}
	
}

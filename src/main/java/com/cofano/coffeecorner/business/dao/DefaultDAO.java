package com.cofano.coffeecorner.business.dao;

import com.cofano.coffeecorner.business.model.page.Default;
import com.cofano.coffeecorner.business.model.page.Icon;
import com.cofano.coffeecorner.business.model.page.IconExtensionItem;
import com.cofano.coffeecorner.business.model.page.NavButton;
import com.cofano.coffeecorner.data.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Default DAO object class to retrieve information regarding the navigation bar,
 * icons and their respective extension items.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 * 
 */
public class DefaultDAO {
	
	/**
	 * Fills NavButton with information from ResultSet.
	 * 
	 * @param r ResultSet object
	 * 
	 * @return NavButton object with information from ResultSet
	 * 
	 * @throws SQLException upon SQL query failure
	 */
    private NavButton fillNav(ResultSet r) throws SQLException {

        NavButton navButton = new NavButton();
        navButton.setId(r.getInt("id"));
		navButton.setIconUri(r.getString("icon_uri"));
		navButton.setText(r.getString("text"));
		navButton.setTarget(r.getString("target_uri"));

        return navButton;

    }

    /**
	 * Fills Icon with information from ResultSet.
	 * 
	 * @param r ResultSet object
	 * 
	 * @return Icon object with information from ResultSet
	 * 
	 * @throws SQLException upon SQL query failure
	 */
    private Icon fillIcon(ResultSet r) throws SQLException {

        Icon icon = new Icon();
		icon.setId(r.getInt("id"));
		icon.setTitle(r.getString("title"));
		icon.setUri(r.getString("uri"));
		icon.setHeader(r.getString("header"));
		icon.setFooter(r.getString("footer"));

        return icon;

    }
    
    /**
	 * Fills Icon with information from ResultSet.
	 * 
	 * @param r ResultSet object
	 * 
	 * @return Icon object with information from ResultSet
	 * 
	 * @throws SQLException upon SQL query failure
	 */
    private IconExtensionItem fillIconExtensionItem(ResultSet r) throws SQLException {
    	
        IconExtensionItem extensionItem = new IconExtensionItem();
        extensionItem.setId(r.getInt("id"));
        extensionItem.setBody(r.getString("body"));
        extensionItem.setTarget(r.getString("target"));
        extensionItem.setValue(r.getString("value"));
        
        return extensionItem;
        
    }

    /**
	 * Retrieves the information from database regarding Default.
	 * 
	 * @throws SQLException upon SQL query failure
	 */
    public Default get() {
    	
    	Default d = null;
		Database DB = new Database();
    	
    	try {

			String q = "SELECT * FROM nav_button ORDER BY id;";
			ResultSet r = DB.execute(q);
			List<NavButton> buttons = new ArrayList<>();
			while (r.next()) buttons.add(fillNav(r));

			q = "SELECT * FROM icon ORDER BY id;";
			r = DB.execute(q);
			List<Icon> icons = new ArrayList<>();
			
			while (r.next()) {
				
				Icon icon = fillIcon(r);
				icons.add(icon);
				
				List<IconExtensionItem> items = new ArrayList<>();
				q = "SELECT * FROM icon_extension " +
					"WHERE icon_id = " + icon.getId() + " " +
					"ORDER BY id;";
				ResultSet r1 = DB.execute(q);
				
				while (r1.next()) items.add(fillIconExtensionItem(r1));
				icon.setExtensions(items);
				
			}

			d = new Default(icons, buttons);

		} catch (SQLException ex) { ex.printStackTrace(); }
    	finally { DB.close(); }

		return d;

    }
    
}

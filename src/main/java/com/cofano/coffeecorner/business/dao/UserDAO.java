package com.cofano.coffeecorner.business.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cofano.coffeecorner.business.model.users.User;
import com.cofano.coffeecorner.data.Database;

/**
 * The class works with database, retrieving information regarding users.
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
public class UserDAO {

    public static final String TABLENAME = "\"user\"";
    public static final String ASSOCIATIONTABLE = "event_participant";

    public User fill(ResultSet r) throws SQLException {
        
        User user = new User();
        user.setId(r.getString("id"));
        user.setEmail(r.getString("email"));
        user.setName(r.getString("name"));
        user.setIconUri(r.getString("icon_uri"));
        
        return user;
        
    }

    public User get(String id) {
        
        Database DB = new Database();
        User u = null;

        try {
            
            String q = "SELECT * FROM " + TABLENAME  +
                       " WHERE id = ?;";

            PreparedStatement ps = DB.connection.prepareStatement(q);
            ps.setString(1, id);
            ResultSet r = DB.execute(ps);

            while (r.next()) { u = fill(r); }

        } catch (SQLException ex) {ex.printStackTrace(); }
        finally { DB.close(); }

        return u;
        
    }

    /**
     * Retrieves a {@link List list} of participants for the event corresponding
     * to the specified {@code id}.
     *
     * @param id the id of the event from which participants are retrieved
     *
     * @return a list of {@link User} objects
     */
    public List<User> getEventParticipants(int id) {
        
        List<User> participants = new ArrayList<>();
        Database DB = new Database();

        try {

            String q = "SELECT u.* FROM " + TABLENAME + " u, " + ASSOCIATIONTABLE + " ep " +
                       "WHERE ep.event_id = ? " +
                       "AND ep.user_id = u.id " +
                       "ORDER BY u.name;";

            PreparedStatement ps = DB.connection.prepareStatement(q);
            ps.setInt(1, id);
            ResultSet r = DB.execute(ps);
            while (r.next()) { participants.add(fill(r)); }
            
        } catch (SQLException ex) { ex.printStackTrace(); }
        finally { DB.close(); }

        return participants;
        
    }

    public void save(User u) {
        
        // If the user is already contained in DB, check that attributes match
        boolean attributesDiffer = false;
        User retrieved = get(u.getId());
        
        if (retrieved != null) {
            
            if (!retrieved.getEmail().equals(u.getEmail())
                    || !retrieved.getName().equals(u.getName())
                    || !retrieved.getIconUri().equals(u.getIconUri())) {
                
                attributesDiffer = true;
                delete(u.getId());
                
            }
            
        }

        if (attributesDiffer || retrieved == null) {

            Database DB = new Database();

            try {
                
                String q = "INSERT INTO " + TABLENAME + " (id, email, name, icon_uri) VALUES (?, ?, ?, ?);";

                PreparedStatement ps = DB.connection.prepareStatement(q);
                ps.setString(1, u.getId());
                ps.setString(2, u.getEmail());
                ps.setString(3, u.getName());
                ps.setString(4, u.getIconUri());

                DB.execute(ps);
                
            } catch (SQLException e) { e.printStackTrace(); }
            finally { DB.close(); }
            
        }
        
    }

    public List<User> getAll() {
        
        List<User> out = new ArrayList<>();
        Database DB = new Database();

        try {

            String q = "SELECT * FROM " + TABLENAME + " ORDER BY name;";
            ResultSet r = DB.execute(q);
            while (r.next()) { out.add(fill(r)); }

        } catch (SQLException ex) { ex.printStackTrace(); }
        finally { DB.close(); }

        return out;
        
    }

    public void delete(String id) {

        Database DB = new Database();

        try {
            
            String q = "DELETE FROM " + TABLENAME + " WHERE id = ?;";
            PreparedStatement ps = DB.connection.prepareStatement(q);
            ps.setString(1, id);
            DB.execute(ps);
            
        } catch (SQLException e) { e.printStackTrace(); }
        finally { DB.close(); }
        
    }
    
}

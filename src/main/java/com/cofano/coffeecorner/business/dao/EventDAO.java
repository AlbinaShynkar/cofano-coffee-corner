package com.cofano.coffeecorner.business.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.cofano.coffeecorner.business.model.events.Event;
import com.cofano.coffeecorner.business.model.users.User;
import com.cofano.coffeecorner.data.Database;

/**
 * The class works with database, retrieving information regarding events.
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
public class EventDAO {
    
    private static final String TABLENAME = "event";
    private static final String ASSOCIATIONTABLE = "event_participant";
    private static final String USERTABLE = UserDAO.TABLENAME;
    
    public Event fill(ResultSet r, User u) throws SQLException {
        
        Event event = new Event();
        event.setId(r.getInt("id"));
        event.setTitle(r.getString("title"));
        event.setBody(r.getString("body"));
        event.setType(r.getString("type"));
        event.setStart(r.getTimestamp("start_time"));
        event.setEnd(r.getTimestamp("end_time"));
        event.setImageUri(r.getString("image_uri"));
        event.setAuthor(u);
        
        return event;
        
    }
    
    public Event get(int id) {
        
        Event e = null;
        Database DB = new Database();
        
        try {
            String q1 = "SELECT * FROM " + TABLENAME +
                       " WHERE id = ?;";
            
            PreparedStatement ps1 = DB.connection.prepareStatement(q1);
            ps1.setInt(1, id);
            ResultSet r = DB.execute(ps1);
            
            while (r.next()) {
                
                String q2 = "SELECT * FROM "+ USERTABLE + " " +
                            "WHERE id = ?;" ;
                
                PreparedStatement ps2 = DB.connection.prepareStatement(q2);
                ps2.setString(1, r.getString("author_id"));
                ResultSet r2 = DB.execute(ps2);
                
                User u = null;
                if (r2.next()) u = new UserDAO().fill(r2);
                e = fill(r, u);
                
            }
            
        } catch (SQLException ex) { ex.printStackTrace(); }
        finally { DB.close(); }
        
        return e;
        
    }
    
    /**
     * Subscribes certain user to certain event as participant of the event
     * @param userId id of the user
     * @param eventId id of the event
     */
    public void subscribeUser(String userId, int eventId) {
        
        Database DB = new Database();
        
        try {
            
            String q = "SELECT * FROM " + ASSOCIATIONTABLE +
                       " WHERE user_id = ?;";
            PreparedStatement ps = DB.connection.prepareStatement(q);
            ps.setString(1, userId);
            ResultSet r = DB.execute(ps);
            
            boolean participating = false;
            while (r.next()) if (r.getInt("event_id") == eventId) participating = true;
            
            if (!participating) {
                
                q = "INSERT INTO " + ASSOCIATIONTABLE + " (user_id, event_id) VALUES(?, ?);";
                PreparedStatement ps2 = DB.connection.prepareStatement(q);
                ps2.setString(1, userId);
                ps2.setInt(2, eventId);
                DB.execute(ps2);
                
            } else deleteParticipant(userId, eventId);
            
        } catch (SQLException e) { e.printStackTrace(); }
        finally { DB.close(); }
        
    }
    
    /**
     * Deletes user from the list of participants of certain event.
     *
     * @param userId id of the user
     */
    public void deleteParticipant(String userId, int eventId) {
        
        Database DB = new Database();
        
        try{
            
            String q = "DELETE FROM " + ASSOCIATIONTABLE + " WHERE user_id = ? " +
                       "AND event_id = ?;";
            
            PreparedStatement ps = DB.connection.prepareStatement(q);
            ps.setString(1, userId);
            ps.setInt(2, eventId);
            DB.execute(ps);
            
        } catch (SQLException e) { e.printStackTrace(); }
        finally { DB.close(); }
        
    }
    
    /**
     * Save a {@link Event event} object and return its id (SERIALIZED).
     *
     * @param e the event to save
     *
     * @return the id of the event which was just saved
     */
    public int save(Event e) {
        
        Database DB = new Database();
        
        try {
            
            String q = "INSERT INTO " + TABLENAME + " (title, body, type, start_time, end_time, image_uri, author_id) VALUES (? ,? ,? ,? ,? ,? ,? )  " +
                       "RETURNING id;";
            
            PreparedStatement ps = DB.connection.prepareStatement(q, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, e.getTitle());
            ps.setString(2, e.getBody());
            ps.setString(3, e.getType());
            ps.setObject(4, e.getStart().toInstant().atZone(ZoneId.of("GMT+0")).toLocalDateTime());
            ps.setObject(5, e.getEnd().toInstant().atZone(ZoneId.of("GMT+0")).toLocalDateTime());
            ps.setString(6, e.getImageUri());
            ps.setString(7, e.getAuthor().getId());
            
            DB.execute(ps);
            ResultSet keys = ps.getGeneratedKeys();
            
            int idSet = -1;
            if (keys.next()) idSet = keys.getInt(1);
            return idSet;
            
        } catch (SQLException e1) { e1.printStackTrace(); }
        finally { DB.close(); }
        
        return -1;
        
    }
    
    /**
     * Retrieves the list of certain number of events in the future from the database.
     * If <code>now</code> is set to true, events retrieved start from the current
     * time, else they start from today at 00:00.
     *
     * @param min the amount of most recent events to be skipped
     * @param max the amount of events to retrieve after </i>min</i>
     * @param now indicates whether to retrieve events starting right now, or not (i.e. today at 00:00)
     *
     * @return list of future events from database
     */
    public List<Event> getAmount(int min, int max, boolean now) {
        
        List<Event> out = new ArrayList<>();
        Database DB = new Database();
        int limit = max - min;
        
        try {
            
            Date date;
            if (!now) date = todayMidnight();
            else date = new Date();
            
            String q1 = "SELECT * FROM " + TABLENAME + " " +
                        "WHERE start_time >= ? " +
                        "ORDER BY start_time DESC " +
                        "LIMIT ? OFFSET ?";
            
            PreparedStatement ps = DB.connection.prepareStatement(q1);
            ps.setObject(1, date.toInstant().atZone(ZoneId.of("GMT+0")).toLocalDateTime());
            ps.setInt(2, limit);
            ps.setInt(3, min);
            ResultSet r1= DB.execute(ps);
            
            while (r1.next()) {
                
                String q2 = "SELECT * FROM " + USERTABLE + " " +
                            "WHERE id = ?;";
                
                PreparedStatement ps2 = DB.connection.prepareStatement(q2);
                ps2.setString(1, r1.getString("author_id"));
                ResultSet r2 = DB.execute(ps2);
                
                User u = null;
                while (r2.next()) { u = new UserDAO().fill(r2); }
                out.add(fill(r1, u));
                
            }
            
        } catch (SQLException e) { e.printStackTrace(); }
        finally { DB.close(); }
        
        Collections.reverse(out);
        return out;
        
    }
    
    /**
     * Retrieves the list of toda's events from the database. These are events
     * found between {@link #todayMidnight()} and {@link #tomorrowMidnight()}.
     *
     * @return list of toda's events from database
     */
    public List<Event> getTodaysEvents() {
        
        List<Event> out = new ArrayList<>();
        Database DB = new Database();
        
        try {
            
            Date today = todayMidnight();
            Date tomorrow = tomorrowMidnight();
            
            LocalDateTime localDateTimeToday = today.toInstant().atZone(ZoneId.of("GMT+0")).toLocalDateTime();
            LocalDateTime localDateTimeTomorrow = tomorrow.toInstant().atZone(ZoneId.of("GMT+0")).toLocalDateTime();
            
            String q1 = "SELECT * FROM " + TABLENAME + " " +
                        "WHERE start_time >= ? AND end_time < ? " +
                        "ORDER BY start_time DESC;";
            
            PreparedStatement ps = DB.connection.prepareStatement(q1);
            ps.setObject(1, localDateTimeToday);
            ps.setObject(2, localDateTimeTomorrow);
            ResultSet r1= DB.execute(ps);
            
            while (r1.next()) {
                
                String q2 = "SELECT * FROM " + USERTABLE + " " +
                            "WHERE id = ?;" ;
                
                PreparedStatement ps2 = DB.connection.prepareStatement(q2);
                ps2.setString(1, r1.getString("author_id"));
                ResultSet r2 = DB.execute(ps2);
                
                User u = null;
                while (r2.next()) u = new UserDAO().fill(r2);
                out.add(fill(r1, u));
                
            }
            
        } catch (SQLException e) { e.printStackTrace(); } finally { DB.close(); }
        
        Collections.reverse(out);
        return out;
        
    }
    
    /**
     *
     * @return todays's {@link Date date} at 00:00:00:00 (hh:mm:ss:mi)
     */
    private Date todayMidnight() {
        
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
        
    }
    
    /**
     *
     * @return tomorrow's {@link Date date} at 00:00:00:00 (hh:mm:ss:mi)
     */
    private Date tomorrowMidnight() {
        
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
        
    }
    
    public void delete(int id) {
        
        Database DB = new Database();
        
        try {
            
            String q = "DELETE FROM " + TABLENAME + " WHERE id = ?;";
            PreparedStatement ps = DB.connection.prepareStatement(q);
            ps.setInt(1, id);
            DB.execute(ps);
            
        } catch (SQLException e) { e.printStackTrace(); }
        finally { DB.close(); }
        
    }
    
}

package com.cofano.coffeecorner.business.model.events;

import java.util.Date;

import com.cofano.coffeecorner.business.model.users.User;


/**
 * Event class to store user-created events.
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
 * @version 2
 * 
 * Events hold a {@link User} object to store author data
 * 
 */
public class Event {
	
	/** 
	 * Uniue id.
	 */
    private int id;
    
    /** 
	 * Title of event.
	 */
    private String title;
    
    /** 
	 * Date the event will start.
	 */
    private Date start;
    
    /**
     * Date the event will end.
     */
    private Date end;
    
    /** 
	 * Body of event.
	 */
    private String body;
    
    private String imageUri;
    
    private User author;
    
    /**
     * Type of event (E.g. 'event', 'break').
     */
    private String type;
    
    /**
     * Create a new Event object.
     */
    public Event() {}
    
    /**
     * @return the starting date of the event
     */
    public Date getStart() {
        return start;
    }
    
    /**
     * @param time event start date to set
     */
    public void setStart(Date time) {
    	this.start = time;
    }
    
    /**
	 * @return the end date of this event
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * @param end the end date to set
	 */
	public void setEnd(Date end) {
		this.end = end;
	}

	/**
     * @return the id of event
     */
    public int getId() { 
    	return id; 
    }
    
    /**
     * @param id event id to set
     */
    public void setId(int id) { 
    	this.id = id; 
    }

    /**
     * @return the title of event
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * @param title event title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * @return the body of event
     */
    public String getBody() {
        return body;
    }
    
    /**
     * @param body event body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

	/**
	 * @return the type
	 */
	public String getType() { return type; }

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the imageUri
	 */
	public String getImageUri() {
		return imageUri;
	}

	/**
	 * @param imageUri the imageUri to set
	 */
	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	/**
	 * @return the author
	 */
	public User getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(User author) {
		this.author = author;
	}

}

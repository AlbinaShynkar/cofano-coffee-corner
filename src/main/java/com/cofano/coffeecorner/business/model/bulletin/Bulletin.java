package com.cofano.coffeecorner.business.model.bulletin;

import com.cofano.coffeecorner.business.model.users.User;

/**
 * The Bulletin class stores the information regarding  
 * bulletins on the Bulletin Board of Cofano Coffee Corner.
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
 * Bulletins hold a {@link User} object to store author data
 */

public class Bulletin {
	
	/** 
	 * Unique id.
	 */
    private int id;
    
    /**
     *  The title of a bulletin.
     */
    private String title;
    
    /** 
     * The body of a bulletin.
     */
    private String body;
    
    /**
     * URI pointing to this bulletin's image location.
     */
    private String imageUri;
    
	/**
	 * The author of this bulletin.
	 */
    private User author;
    
    public Bulletin() { };
    
    /**
     * @return the id of bulletin
     */
    public int getId() {
        return id;
    }
    
    /**
     * @param id bulletin id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * @return the title of the bulletin
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * @param title bulletin title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * @return body of bulletin
     */
    public String getBody() {
        return body;
    }
    
    /**
     * @param body bulletin body to set
     */
    public void setBody(String body) {
        this.body = body;
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
	 * @return the {@link #author}
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
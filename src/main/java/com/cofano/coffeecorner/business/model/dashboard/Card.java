package com.cofano.coffeecorner.business.model.dashboard;

/**
 * This class describes Cards that are displayed on the 
 * {@link Dashboard} (e.g. Weather, Unread Messages etc.).
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 *
 */
public class Card {
	
	/** 
	 * Unique id.
	 */
	private int id;
	
	/** 
	 * Title of card.
	 */
	private String title;
	
	/** 
	 * imageURI of card.
	 */
	private String imageUri;
	
	/** 
	 * Description of card 
	 */
	private String description;
	
	public Card() {}
	
	/**
     * @return the id of card
     */
	public int getId() {
		return id;
	}
	
	/**
     * @param id card id to set
     */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
     * @return the title of card
     */
	public String getTitle() {
		return title;
	}
	
	/**
     * @param title card title to set
     */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
     * @return the imageUri of card
     */
	public String getImageUri() {
		return imageUri;
	}
	
	/**
     * @param imageUri card imageURI to set
     */
	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}
	
	/**
     * @return the description of card
     */
	public String getDescription() {
		return description;
	}
	
	/**
     * @param description card description to set
     */
	public void setDescription(String description) {
		this.description = description;
	}

}

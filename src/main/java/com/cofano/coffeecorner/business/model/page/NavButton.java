package com.cofano.coffeecorner.business.model.page;
/**
 * The NavButton class stores the information about  
 * buttons on the Bulletin Board of Cofano Coffee Corner.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 * 
 * @version 1
 */
public class NavButton {
	
	/** 
	 * Unique id.
	 */
	private int id;
	
	/** 
	 * URI of this button's icon.
	 */
	private String iconUri;
	
	/** 
	 * Text of button.
	 */
	private String text;
	
	/** 
	 * Target of button.
	 */
	private String target;
	
	public NavButton() { }
	
	/**
     * @return the id of this button
     */
	public int getId() {
		return id;
	}
	
    /**
     * @param id the id to set
     */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
     * @return the URI of this button
     */
	public String getIconUri() {
		return iconUri;
	}
	
	 /**
     * @param iconUri the icon URI to set
     */
	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}
	
	/**
     * @return the text of this button
     */
	public String getText() {
		return text;
	}
	
	 /**
     * @param text the text to set
     */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
     * @return the target of this button
     */
	public String getTarget() {
		return target;
	}
	
	 /**
     * @param target the target to set
     */
	public void setTarget(String target) {
		this.target = target;
	}
	
}

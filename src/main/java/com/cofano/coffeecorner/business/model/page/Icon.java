package com.cofano.coffeecorner.business.model.page;

import java.util.List;

/**
 * Represents an icon.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 * 
 */
public class Icon {
	
	/** 
	 * Unique id.
	 */
	private int id;
	
	 /**
     *  The title of this icon.
     */
	private String title;
	
	/**
     *  The URI of this icon.
     */
	private String uri;
	
	private String header;
	
	private String footer;
	
	private List<IconExtensionItem> extensions;
	
	public Icon() {	}
	
	/**
     * @return the id of this icon
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
     * @return the title of this icon
     */
	public String getTitle() {
		return title;
	}
	/**
     * @param title the title to set
     */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
     * @return the URI of this icon
     */
	public String getUri() {
		return uri;
	}
	/**
     * @param uri the URI to set
     */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the extensions
	 */
	public List<IconExtensionItem> getExtensions() {
		return extensions;
	}

	/**
	 * @param extensions the extensions to set
	 */
	public void setExtensions(List<IconExtensionItem> extensions) {
		this.extensions = extensions;
	}

	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return the footer
	 */
	public String getFooter() {
		return footer;
	}

	/**
	 * @param footer the footer to set
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}
}

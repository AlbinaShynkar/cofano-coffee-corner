package com.cofano.coffeecorner.business.model.page;

import java.util.List;

/**
 * The Default class is used to define buttons and navigation bar
 * for all pages of the Cofano Coffee Corner web-application.
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
public class Default {
	
	/** 
	 * List of icons on default page.
	 */
    private List<Icon> icons;
    
    /** 
	 * List of buttons on default page.
	 */
    private List<NavButton> buttons;

    public Default(List<Icon> icons, List<NavButton> buttons) {
    	this.icons = icons;
    	this.buttons = buttons;
    }
    
    public Default() { }
    
    /**
     * @return list of icons on default page
     */
    public List<Icon> getIcons() {
        return icons;
    }
    
    /**
     * @param icons list of icons to set
     */
    public void setIcons(List<Icon> icons) {
        this.icons = icons;
    }
    
    /**
     * @return list of buttons on default page
     */
    public List<NavButton> getButtons() {
        return buttons;
    }
    
    /**
     * @param buttons list of buttons to set
     */
    public void setButtons(List<NavButton> buttons) {
        this.buttons = buttons;
    }
}

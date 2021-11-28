package com.cofano.coffeecorner.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.cofano.coffeecorner.business.dao.DefaultDAO;
import com.cofano.coffeecorner.business.dao.PageDAO;
import com.cofano.coffeecorner.business.model.dashboard.Dashboard;
import com.cofano.coffeecorner.business.model.page.Default;
import com.cofano.coffeecorner.business.model.page.Page;


/**
 * The class works with the functions of the PageDAO class to retrieve information regarding
 * all the different pages in the Cofano Coffee Corner web application.
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
@Path("page")
public class PageController implements Controller {
	
	/**
	 * Function to retrieve the data regarding the default page.
	 * 
	 * @return {@link Default} object that has  list of icons and list of buttons
	 */
    @Path("default")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Default getDefault() {
        return new DefaultDAO().get();
    }
    
    
    /**
     * Function to retrieve data regarding the dashboard.
     * 
     * @return {@link Dashboard} object
     */ 
    @Path("dashboard")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Dashboard getDashboard() {
        return new PageDAO().getDashboard();
    }

    /**
     * Function to retrieve data regarding the chat page.
     * 
     * @return {@link Page} object
     */ 
    @Path("chat")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Page getChat() { 
    	return new PageDAO().getChatPage(); 
    }
    
    
    /**
     * Function to retrieve data regarding the bulletin page.
     *  
     * @return {@link Page} object
     */ 
    @Path("bulletin")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Page getBulletin() { 
        return new PageDAO().getBulletinPage();
    }
    
    /**
     * Function to retrieve data regarding the event page.
     * 
     * @return {@link Page} object
     */
    @Path("event")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Page getEvents() {
        return new PageDAO().getEventPage();
    }
    
    /**
     * Function to retrieve data regarding the login page.
     * 
     * @return {@link Page} object
     */
    @Path("login")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Page getLogin() {
        return new PageDAO().getLoginPage();
    }

}


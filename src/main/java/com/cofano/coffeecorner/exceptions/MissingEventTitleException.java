package com.cofano.coffeecorner.exceptions;

/**
 * This exception is thrown when an event has a missing title.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 *
 */
@SuppressWarnings("serial")
public class MissingEventTitleException extends EventException {
	
	public MissingEventTitleException() {
		super("The event title is missing.", 0);
	}

}

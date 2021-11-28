package com.cofano.coffeecorner.exceptions;

/**
 * This exception is thrown when an event has an invalid/missing type.
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
public class InvalidEventTypeException extends EventException {
	
	public InvalidEventTypeException() {
		super("The event type is missing or invalid.", 0);
	}

}

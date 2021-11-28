package com.cofano.coffeecorner.exceptions;

/**
 * This exception is thrown when an event has an invalid id.
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
public class InvalidEventIdException extends EventException {

	public InvalidEventIdException() {
		super("This event id is invalid.", 1);
	}
}

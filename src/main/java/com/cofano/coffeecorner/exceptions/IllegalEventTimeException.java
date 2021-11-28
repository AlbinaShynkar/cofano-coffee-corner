package com.cofano.coffeecorner.exceptions;

/**
 * This exception is thrown when an event has invalid/missing event times.
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
public class IllegalEventTimeException extends EventException {
	
	public IllegalEventTimeException() {
		super("The event start/end times are invalid.", 0);
	}

}

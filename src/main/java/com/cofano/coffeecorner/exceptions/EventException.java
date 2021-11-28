package com.cofano.coffeecorner.exceptions;

/**
 * This class is called upon every exception related to an {@link Event} object.
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
public class EventException extends CofanoException {
	private int severity;
	
	public EventException(String message, int severity) {
		super(message);
	}
	
	/**
	 * 
	 * @return {@link #severity}
	 */
	public int getSeverity() {
		return severity;
	}

}

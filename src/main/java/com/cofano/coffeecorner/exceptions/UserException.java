package com.cofano.coffeecorner.exceptions;

/**
 * This class is called upon every exception related to a {@link User} object.
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
public class UserException extends CofanoException {
	private int severity;
	
	public UserException(String message, int severity) {
		super(message);
	}

	public int getSeverity() {
		return severity;
	}

}

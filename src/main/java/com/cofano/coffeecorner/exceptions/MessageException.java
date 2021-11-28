package com.cofano.coffeecorner.exceptions;

/**
 * This class is called upon every exception related to a {@link Message} object.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 * @author Lola Solovyeva
 *
 */
@SuppressWarnings("serial")
public class MessageException extends CofanoException {
	private int severity;
	
	public MessageException(String message, int severity) {
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

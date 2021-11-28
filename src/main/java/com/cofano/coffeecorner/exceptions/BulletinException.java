package com.cofano.coffeecorner.exceptions;

/**
 * This class is called upon every exception related to a {@link Bulletin} object.
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
public class BulletinException extends CofanoException {
	
	private int severity;
	
	public BulletinException(String message, int severity) {
		super(message);
	}

	/**
	 * @return {@link #severity}
	 */
	public int getSeverity() {
		return severity;
	}
}

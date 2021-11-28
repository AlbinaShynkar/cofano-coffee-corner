package com.cofano.coffeecorner.exceptions;

/**
 * This exception is thrown when the status has a missing name.
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
public class MissingUserNameException extends UserException {
	
	public MissingUserNameException() {
		super("The name of the status is missing.", 1);
	}

}

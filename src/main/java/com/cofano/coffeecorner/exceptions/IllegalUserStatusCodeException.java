package com.cofano.coffeecorner.exceptions;

/**
 * This exception is thrown when a status has an invalid/missing status code.
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
public class IllegalUserStatusCodeException extends UserException {
	
	public IllegalUserStatusCodeException() {
		super("The code of the status is missing.", 1);
	}

}

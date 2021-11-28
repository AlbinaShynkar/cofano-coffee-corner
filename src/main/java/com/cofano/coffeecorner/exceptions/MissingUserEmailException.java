package com.cofano.coffeecorner.exceptions;
/**
 * This exception is thrown when the status has a missing email.
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
public class MissingUserEmailException extends UserException {
	
	public MissingUserEmailException() {
		super("The email of the status is missing.", 1);
	}
	
}

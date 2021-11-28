package com.cofano.coffeecorner.exceptions;

/**
 * This is the main exception of the system, parent to all other exceptions.
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
public class CofanoException extends Exception {

	public CofanoException(String message) {
		super(message);
	}
	
}

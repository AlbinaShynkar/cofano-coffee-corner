package com.cofano.coffeecorner.exceptions;

/**
 * This exception is thrown when a message id could not be retrieved.
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
public class InvalidMessageIdException extends MessageException {
	public InvalidMessageIdException() {
		super("The specified message ID is invalid.", 1);
	}
}

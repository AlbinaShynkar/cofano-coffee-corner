package com.cofano.coffeecorner.exceptions;

/**
 * This exception is thrown when a message has a missing text.
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
public class MissingMessageTextException extends MessageException {

	public MissingMessageTextException() {
		super("The text of message is missing.", 0);
	}
	
}

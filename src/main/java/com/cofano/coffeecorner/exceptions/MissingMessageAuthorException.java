package com.cofano.coffeecorner.exceptions;

/**
 * This exception is thrown when a message has a missing author.
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
public class MissingMessageAuthorException extends MessageException {
	
	public MissingMessageAuthorException() {
		super("Author of message is missing.", 1);
	}

}

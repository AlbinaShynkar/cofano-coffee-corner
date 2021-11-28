package com.cofano.coffeecorner.exceptions;

/**
 * This exception is thrown when a message has a missing author's image URI.
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
public class MissingMessageIconUriException extends MessageException {

	public MissingMessageIconUriException() {
		super("The icon URI of author is missing.", 1);
	}
	
}

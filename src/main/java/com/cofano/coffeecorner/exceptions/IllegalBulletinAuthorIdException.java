package com.cofano.coffeecorner.exceptions;

/**
 * This exception is thrown when a bulletin has an invalid author.
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
public class IllegalBulletinAuthorIdException extends BulletinException {
	
	public IllegalBulletinAuthorIdException() {
		super("Author ID of message is invalid.", 1);
	}

}

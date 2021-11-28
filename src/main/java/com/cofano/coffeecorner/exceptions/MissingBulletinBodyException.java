package com.cofano.coffeecorner.exceptions;

/**
 * This exception is thrown when a bulletin has a missing body.
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
public class MissingBulletinBodyException extends BulletinException {
	
	public MissingBulletinBodyException() {
		super("The bulletin body is missing.", 0);
	}
	
}

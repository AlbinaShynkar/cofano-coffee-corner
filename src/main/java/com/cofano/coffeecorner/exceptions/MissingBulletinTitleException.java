package com.cofano.coffeecorner.exceptions;

/**
 * This exception is thrown when a bulletin has a missing title.
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
public class MissingBulletinTitleException extends BulletinException {

	public MissingBulletinTitleException() {
		super("The bulletin title is missing.", 0);
	}
	
}

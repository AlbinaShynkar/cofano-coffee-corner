package com.cofano.coffeecorner.controller;

import com.cofano.coffeecorner.business.model.users.User;
import com.cofano.coffeecorner.data.Database;

/**
 * This class provides data to be used by all integration test classes within
 * this package. The data corresponds to the one found in the testing database
 * (see README for login information), and should be updated accordingly to
 * always be valid.
 * <br><br>
 * Before testing, make sure to turn {@link Database#TESTING} to <code>true</code>.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 * @author Lola Solovyeva
 * 
 */
public class TestingData {
	/**
	 * Used to indicate an invalid ID.
	 */
	private final static int NEGATIVE_ONE = -1; 
	
	public static final String VALID_USER_ID = "12345";
	public static final String INVALID_USER_ID = "invalid";
	
	/**
	 * An encoded URI string defining a valid {@link User} object to
	 * add as query parameter while subscribing to the {@link Broadcaster}.
	 */
	public static final String VALID_USER_OBJECT = "%7B%22id%22%3A%2212345%22%2C%22email%22%3A%22test%40gmail.com%22%2C%22name%22%3A%22Test%20User%22%2C%22iconUri%22%3A%22http%3A%2F%2Fexample.com%2Ficon.jpg%22%2C%22statusCode%22%3A0%7D";
	
	/**
	 * An encoded URI string defining an invalid {@link User} object to
	 * add as query parameter while subscribing to the {@link Broadcaster}.
	 */
	public static final String INVALID_USER_OBJECT = "%7B%22id%22%3A%2212345%22%2C%22email%22%3A%22test%40gmail.com%22%2C%22iconUri%22%3A%22http%3A%2F%2Fexample.com%2Ficon.jpg%22%2C%22statusCode%22%3A0%7D";
	
	public static final int VALID_USER_STATUS = 0;
	public static final int INVALID_USER_STATUS = 3;
	
	public static final int VALID_EVENT_ID = 49;
	public static final int INVALID_EVENT_ID = NEGATIVE_ONE;
	
	public static final String VALID_EVENT_TYPE = "Break";
	public static final String INVALID_EVENT_TYPE = "Invalid";
	
	public static final int VALID_BULLETIN_ID = 9;
	public static final int INVALID_BULLETIN_ID = NEGATIVE_ONE;
	
	public static final int VALID_MESSAGE_ID = 39;
	public static final int INVALID_MESSAGE_ID = NEGATIVE_ONE;
	
	public static final int HTTP_CUSTOM_ERROR_STATUSCODE = 515;
}

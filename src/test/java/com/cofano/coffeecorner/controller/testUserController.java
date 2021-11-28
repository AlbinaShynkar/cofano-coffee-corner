package com.cofano.coffeecorner.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;

/**
 * JUnit test class to test the RESTful end points within {@link UserController}.
 *
 */
class testUserController {
	
	/**
	 * The custom error code of the system.
	 */
	private static final int CUSTOMERROR = TestingData.HTTP_CUSTOM_ERROR_STATUSCODE;
	
	@Test
	public void retrieveAllUsers_then200IsReceived()
	  throws ClientProtocolException, IOException {
	  
	    HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/user" );
	 
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testRetrieveAllUsersReturnsJSONArray() 
			throws ClientProtocolException, IOException {
		
		// Given
		String jsonMimeType = "application/json";
		HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/user" );
		
		// When
	    HttpResponse response = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
	    assertEquals( jsonMimeType, mimeType );
	}
	
	@Test
	public void updateUserStatusInvalidId_then515IsReceived()
	  throws ClientProtocolException, IOException {
	  
	    HttpUriRequest request = new HttpPut( "http://localhost:8080/coffeecorner/rest/user?id=" + TestingData.INVALID_USER_ID + "&status=" + TestingData.VALID_USER_STATUS );
	 
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    assertEquals(CUSTOMERROR, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void updateUserStatusInvalidStatusCode_then515IsReceived()
	  throws ClientProtocolException, IOException {
		
	    HttpUriRequest request = new HttpPut( "http://localhost:8080/coffeecorner/rest/user?id=" + TestingData.VALID_USER_ID +"&status=" + TestingData.INVALID_USER_STATUS);
	 
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    assertEquals(CUSTOMERROR, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void updateUserStatusValidStatusCode_then204IsReceived()
	  throws ClientProtocolException, IOException {
		
	    HttpUriRequest request = new HttpPut( "http://localhost:8080/coffeecorner/rest/user?id=" + TestingData.VALID_USER_ID +"&status=" + TestingData.VALID_USER_STATUS);
	 
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    assertEquals(HttpStatus.SC_NO_CONTENT, httpResponse.getStatusLine().getStatusCode());
	}

}

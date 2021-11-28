package com.cofano.coffeecorner.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;

public class testBroadcaster {

	@Test
	public void testSubscribeInvalidId_then515IsReceived()
	  throws ClientProtocolException, IOException {
	  
	   HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/broadcaster?user=" + TestingData.INVALID_USER_OBJECT );
	   
	   // When
	   HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	   
	   // Then
	   assertEquals(TestingData.HTTP_CUSTOM_ERROR_STATUSCODE, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testSubscribeValidId_then200IsReceived()
	  throws ClientProtocolException, IOException {
	  
	   HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/broadcaster?user=" + TestingData.VALID_USER_OBJECT );
	   
	   // When
	   HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	   
	   // Then
	   assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
	}
	
}

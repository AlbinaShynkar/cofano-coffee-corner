package com.cofano.coffeecorner.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

/**
 * JUnit test class to test the RESTful end points within {@link MessageController}.
 * 
 * @author Jasper van Amerongen
 * @author Nidanur Gunay
 * @author Adamo Mariani
 * @author Albina Shynkar
 * @author Eda Yardim
 *
 */
class testMessageController {
	
	@Test
	public void retrieveAllMessages_then200IsReceived()
	  throws ClientProtocolException, IOException {
	  
	   HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/chat/messages" );
	   
	   // When
	   HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	   
	   // Then
	   assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void getAllMessages_returnsJSONArray() 
			throws ClientProtocolException, IOException {
		// Given
		String jsonMimeType = "application/json";
		HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/chat/messages" );
		
		// When
	    HttpResponse response = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
	    assertEquals( jsonMimeType, mimeType );
	}
	
	@Test
	public void testSendMessageInvalidId_then515IsReceived()
		throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost httpPost = new HttpPost( "http://localhost:8080/coffeecorner/rest/chat" );
		
		String json = "{\"text\":\"This is a sample message\"}";
		StringEntity entity = new StringEntity(json);
		httpPost.setEntity(entity);
	    httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-type", "application/json");
	    
	    CloseableHttpResponse response = client.execute(httpPost);
	    
	    // Then
	    assertEquals(TestingData.HTTP_CUSTOM_ERROR_STATUSCODE, response.getStatusLine().getStatusCode());
	    
	    client.close();
	}
	
	@Test
	public void testSendMessage_then200IsReceived()
		throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost httpPost = new HttpPost( "http://localhost:8080/coffeecorner/rest/chat?author=" + TestingData.VALID_USER_ID);
		
		String json = "{\"text\":\"This is a sample message\"}";
		StringEntity entity = new StringEntity(json);
		httpPost.setEntity(entity);
	    httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-type", "application/json");
	    
	    CloseableHttpResponse response = client.execute(httpPost);
	    
	    // Then
	    assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
	    
	    client.close();
	}
	
	@Test
	public void starMessageInvalidId_then515IsReceived()
	  throws ClientProtocolException, IOException {
	  
	   HttpUriRequest request = new HttpPut( "http://localhost:8080/coffeecorner/rest/chat?id=" + TestingData.INVALID_MESSAGE_ID );
	   
	   // When
	   HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	   
	   // Then
	   assertEquals(TestingData.HTTP_CUSTOM_ERROR_STATUSCODE, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void starMessage_then200IsReceived()
	  throws ClientProtocolException, IOException {
	  
	   HttpUriRequest request = new HttpPut( "http://localhost:8080/coffeecorner/rest/chat?id=" + TestingData.VALID_MESSAGE_ID );
	   
	   // When
	   HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	   
	   // Then
	   assertEquals(HttpStatus.SC_NO_CONTENT, httpResponse.getStatusLine().getStatusCode());
	   
	   //RePlace request so that message STAR attribute remains unchanged
	   HttpUriRequest request2 = new HttpPut( "http://localhost:8080/coffeecorner/rest/chat?id=" + TestingData.VALID_MESSAGE_ID );
	   HttpResponse httpResponse2 = HttpClientBuilder.create().build().execute( request2 );
	   assertEquals(HttpStatus.SC_NO_CONTENT, httpResponse2.getStatusLine().getStatusCode());
	}
	
	@Test
	public void retrieveAllStarred_then200IsReceived()
	  throws ClientProtocolException, IOException {
	  
	   HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/chat/starred" );
	   
	   // When
	   HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	   
	   // Then
	   assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void getAllStarred_returnsJSONArray() 
			throws ClientProtocolException, IOException {
		// Given
		String jsonMimeType = "application/json";
		HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/chat/starred" );
		
		// When
	    HttpResponse response = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
	    assertEquals( jsonMimeType, mimeType );
	}

}

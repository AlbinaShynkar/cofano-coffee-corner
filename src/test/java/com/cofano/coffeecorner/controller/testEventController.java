package com.cofano.coffeecorner.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

public class testEventController {

private static final int CUSTOMERROR = TestingData.HTTP_CUSTOM_ERROR_STATUSCODE;
	
	@Test
	public void retrieveEvents_then200IsReceived()
	  throws ClientProtocolException, IOException {
	  
		HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/events" );
	 
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testRetrieveEventsReturnsJSONArray() 
			throws ClientProtocolException, IOException {
		
		// Given
		String jsonMimeType = "application/json";
		HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/events" );
		
		// When
	    HttpResponse response = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
	    assertEquals( jsonMimeType, mimeType );
	}
	
	@Test
	public void retrieveNextEvent_then200IsReceived()
	  throws ClientProtocolException, IOException {
	  
		HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/events/next" );
	 
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    assertEquals(HttpStatus.SC_NO_CONTENT, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testRetrieveNextEventsReturnsJSONArray() 
			throws ClientProtocolException, IOException {
		
		// Given
		String jsonMimeType = "application/json";
		HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/events" );
		
		// When
	    HttpResponse response = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
	    assertEquals( jsonMimeType, mimeType );
	}
	
	@Test
	public void retrieveDailyEvents_then200IsReceived()
	  throws ClientProtocolException, IOException {
	  
		HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/events/daily" );
	 
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testRetrieveDailyEventsReturnsJSONArray() 
			throws ClientProtocolException, IOException {
		
		// Given
		String jsonMimeType = "application/json";
		HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/events/daily" );
		
		// When
	    HttpResponse response = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
	    assertEquals( jsonMimeType, mimeType );
	}
	
	@Test
	public void retrieveEventsParticipants_then200IsReceived()
	  throws ClientProtocolException, IOException {
	  
		HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/events/participants?id=" + TestingData.VALID_EVENT_ID );
	 
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testEventsParticipantReturnsJSONArray() 
			throws ClientProtocolException, IOException {
		
		// Given
		String jsonMimeType = "application/json";
		HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/events/participants?id=" + TestingData.VALID_EVENT_ID );
		
		// When
	    HttpResponse response = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
	    assertEquals( jsonMimeType, mimeType );
	}
	
	
	@Test
	public void testSubscribesEvent_then200IsReceived()
	  throws ClientProtocolException, IOException {
	  
		HttpUriRequest request = new HttpPost( "http://localhost:8080/coffeecorner/rest/events/subscribe?userId=" + TestingData.VALID_USER_ID +  "&eventId=" + TestingData.VALID_EVENT_ID );
	 
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    assertEquals(HttpStatus.SC_NO_CONTENT, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testSubscribesEventInvalidEventId_then515IsReceived()
	  throws ClientProtocolException, IOException {
	  
		HttpUriRequest request = new HttpPost( "http://localhost:8080/coffeecorner/rest/events/subscribe?userId=" + TestingData.VALID_USER_ID +  "&eventId=" + TestingData.INVALID_EVENT_ID );
	 
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    assertEquals(CUSTOMERROR, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testSubscribesEventInvalidUserId_then515IsReceived()
	  throws ClientProtocolException, IOException {
	  
		HttpUriRequest request = new HttpPost( "http://localhost:8080/coffeecorner/rest/events/subscribe?userId=" + TestingData.INVALID_USER_ID +  "&eventId=" + TestingData.VALID_EVENT_ID  );
	 
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    assertEquals(CUSTOMERROR, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testPostEvent_then200IsReceived()
		throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost httpPost = new HttpPost( "http://localhost:8080/coffeecorner/rest/events?author=" + TestingData.VALID_USER_ID);
		
		String json = "{\"body\":\"This is a sample event body\",\"title\":\"This is a test event title\",\"start\":\"2020-05-24T19:22:51Z\",\"end\":\"2020-05-25T19:22:51Z\",\"type\":\"break\"}";
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
	public void testPostEventInvalidUserId_then515IsReceived()
	  throws ClientProtocolException, IOException {
	  
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost httpPost = new HttpPost( "http://localhost:8080/coffeecorner/rest/events?author=" + TestingData.INVALID_USER_ID);
		
		
		String json = "{\"body\":\"This is a sample event body\",\"title\":\"This is a test event title\",\"start\":1593425400000,\"end\":1593429000000,\"type\":\"break\"}";
		StringEntity entity = new StringEntity(json);
		httpPost.setEntity(entity);
	    httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-type", "application/json");
	    
	    CloseableHttpResponse response = client.execute(httpPost);
	    
	    // Then
	    assertEquals(CUSTOMERROR, response.getStatusLine().getStatusCode());
	    
	    client.close();
	}
	
	
}

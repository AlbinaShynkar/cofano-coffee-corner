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

public class testBulletinController {
	
private static final int CUSTOMERROR = 515;
	
	@Test
	public void retrieveBulletins_then200IsReceived()
	  throws ClientProtocolException, IOException {
	  
		HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/bulletins" );
	 
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testRetrieveBulletinsReturnsJSONArray() 
			throws ClientProtocolException, IOException {
		
		// Given
		String jsonMimeType = "application/json";
		HttpUriRequest request = new HttpGet( "http://localhost:8080/coffeecorner/rest/bulletins" );
		
		// When
	    HttpResponse response = HttpClientBuilder.create().build().execute( request );
	 
	    // Then
	    String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
	    assertEquals( jsonMimeType, mimeType );
	}
	

	
	@Test
	public void testPostBulletin_then200IsReceived()
		throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost httpPost = new HttpPost( "http://localhost:8080/coffeecorner/rest/bulletins?author=" + TestingData.VALID_USER_ID);
		
		String json = "{\"body\":\"This is a sample body\",\"title\":\"This is a test title\"}";
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
	public void testPostBulletinInvalidUserId_then200IsReceived()
	  throws ClientProtocolException, IOException {
	  
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost httpPost = new HttpPost( "http://localhost:8080/coffeecorner/rest/bulletins?author=" + TestingData.INVALID_USER_ID);
		
		String json = "{\"body\":\"This is a sample body\",\"title\":\"This is a test title\"}";
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

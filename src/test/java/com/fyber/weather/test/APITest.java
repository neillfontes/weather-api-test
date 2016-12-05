package com.fyber.weather.test;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fyber.weather.helper.ClientConfig;

public class APITest {
	
	private static ClientConfig clientConfig;
	
	@BeforeClass
	public static void setupClient(){
		clientConfig = new ClientConfig();
	}
	
	@Test
	public void shouldReturnInvalidAPIKeyAndNotReturnWeatherInfoWhenAPIKeyIsInvalidAndRequestParamsAreValid() {
		
		String country = "Germany";
		
		given().
			baseUri(clientConfig.getBaseURL()).
			contentType(clientConfig.getContentType()).
			header(clientConfig.getAuthHeader(), clientConfig.getBadApiKey()).
		when().
			get("/weather?q={country}", country).
		then().
			statusCode(401).
			body("message", containsString("Invalid API key."));
		
	}
	
	@Test
	public void shouldReturnInvalidAPIKeyAndNotReturnWeatherInfoWhenAPIKeyIsValidAndRequestParamsAreValidAndHeaderIsInvalid() {
		
		String country = "Germany";
		
		given().
			baseUri(clientConfig.getBaseURL()).
			contentType(clientConfig.getContentType()).
			header(clientConfig.getBadAuthHeader(), clientConfig.getApiKey()).
		when().
			get("/weather?q={country}", country).
		then().
			statusCode(401).
			body("message", containsString("Invalid API key."));
		
	}
	
	@Test
	public void shouldReturnInvalidAPIKeyAndNotReturnWeatherInfoWhenAPIKeyIsEmptyAndRequestParamsAreValid() {
		
		String country = "Germany";
		
		given().
			baseUri(clientConfig.getBaseURL()).
			contentType(clientConfig.getContentType()).
			header(clientConfig.getAuthHeader(), clientConfig.getEmptyAPIKey()).
		when().
			get("/weather?q={country}", country).
		then().
			statusCode(401).
			body("message", containsString("Invalid API key."));
		
	}
	
	@Test
	public void shouldReturnCityNotFoundWhenAPIKeyIsValidAndInvalidCityName() {
		
		String city = "InvalidCity";
		
		given().
			baseUri(clientConfig.getBaseURL()).
			contentType(clientConfig.getContentType()).
			header(clientConfig.getAuthHeader(), clientConfig.getApiKey()).
		when().
			get("/weather?q={city}", city).
		then().
			statusCode(502).
			body("message", containsString("Not found city"));
		
	}
	
	@Test
	public void shouldAuthenticateAndReturnWeatherForGermanyWhenAPIKeyIsValidAndRequestParamsAreValid() {
		
		String country = "Germany";
		
		given().
			baseUri(clientConfig.getBaseURL()).
			contentType(clientConfig.getContentType()).
			header(clientConfig.getAuthHeader(), clientConfig.getApiKey()).
		when().
			get("/weather?q={country}", country).
		then().
			statusCode(200);
		
	}
	
	@Test
	public void shouldAuthenticateAndReturnWeatherForGermanyWhenAPIKeyIsValidAndRequestParamsAreValidAndContentTypeIsNotApplicationJSON() {
		
		String country = "Germany";
		
		given().
			baseUri(clientConfig.getBaseURL()).
			contentType(clientConfig.getNotJSONContentType()).
			header(clientConfig.getAuthHeader(), clientConfig.getApiKey()).
		when().
			get("/weather?q={country}", country).
		then().
			statusCode(200);
		
	}
	
	
	
}

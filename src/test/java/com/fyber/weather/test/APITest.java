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
	public void shouldAuthenticateAndReturnWeatherForGermanyWhenAPIKeyIsValidAndRequestParamsAreValidAndContentTypeIsNotApplicationJSON() {
		
		String country = "Germany";
		
		given().
			baseUri(clientConfig.getBaseURL()).
			contentType(clientConfig.getNotJSONContentType()).
			header(clientConfig.getAuthHeader(), clientConfig.getApiKey()).
		when().
			get("/weather?q={country}", country).
		then().
			statusCode(200).
			body("cod", equalTo(200));
		
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
			statusCode(200).
			body("cod", equalTo(200));
		
	}
	
	@Test
	public void shouldAuthenticateAndReturnWeatherForBerlinAndGermanyWhenAPIKeyIsValidAndRequestParamsAreValid() {
		
		String city = "Berlin";
		String country = "DE";
		
		given().
			baseUri(clientConfig.getBaseURL()).
			contentType(clientConfig.getContentType()).
			header(clientConfig.getAuthHeader(), clientConfig.getApiKey()).
		when().
			get("/weather?q={city},{country}", city, country).
		then().
			statusCode(200).
			body("cod", equalTo(200));
		
	}
	
	@Test
	public void shouldAuthenticateAndReturnWeatherForBerlinWhenAPIKeyIsValidAndRequestParamsAreValid() {
		
		String city = "Berlin";
		
		given().
			baseUri(clientConfig.getBaseURL()).
			contentType(clientConfig.getContentType()).
			header(clientConfig.getAuthHeader(), clientConfig.getApiKey()).
		when().
			get("/weather?q={city}", city).
		then().
			statusCode(200).
			body("cod", equalTo(200));
		
	}
	
	@Test
	public void shouldAuthenticateAndReturnWeatherForBerlinAsCityIDWhenAPIKeyIsValidAndRequestParamsAreValid() {
		
		String cityID = "2950159";
		
		given().
			baseUri(clientConfig.getBaseURL()).
			contentType(clientConfig.getContentType()).
			header(clientConfig.getAuthHeader(), clientConfig.getApiKey()).
		when().
			get("/weather?id={cityID}", cityID).
		then().
			statusCode(200).
			body("cod", 	equalTo(200), 
				 "name", 	equalTo("Berlin"));
		
	}
		
	@Test
	public void shouldAuthenticateAndReturnWeatherForBerlinAsGeoCoordinatesWhenAPIKeyIsValidAndRequestParamsAreValid() {
		
		String lat = "52.524368";
		String lon = "13.41053";
		
//		float lat = 35;
//		float lon = 139;
		
		given().
			baseUri(clientConfig.getBaseURL()).
			contentType(clientConfig.getContentType()).
			header(clientConfig.getAuthHeader(), clientConfig.getApiKey()).
		when().
			get("/weather?lat={lat}&lon={lon}", lat, lon).
		then().
			statusCode(200).
			body("cod", 	equalTo(200), 
				 "name", 	equalTo("Berlin Mitte"));
		
	}
	
}

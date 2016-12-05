package com.fyber.weather.test;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;

import static org.hamcrest.Matchers.containsString;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fyber.weather.domain.WeatherResponse;
import com.fyber.weather.helper.ClientConfig;
import com.google.gson.Gson;
import com.jayway.restassured.response.Response;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import static org.assertj.core.api.Assertions.*;

import groovyjarjarantlr.collections.List;

public class APITest {
	
	private static ClientConfig clientConfig;
	
	@BeforeClass
	public static void setupClient(){
		clientConfig = new ClientConfig();
	}
	
	/**
	 * Tests if bad API key returns 401 code on invalid API key
	 * @result Expects statusCode = 401 and JSON message containing "Invalid API key"
	 */
	
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
	
	/**
	 * Tests if invalid Authentication header for API key returns 401 code 
	 * @result Expects statusCode = 401 and JSON message containing "Invalid API key"
	 */
	
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
	
	/**
	 * Tests if blank API key returns 401 
	 * @result Expects statusCode = 401 and JSON message containing "Invalid API key"
	 */
	
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
	
	/**
	 * Tests if valid API key but invalid city returns 502
	 * @result Expects statusCode = 502 and JSON message containing "Not found city"
	 */
	
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
	
	/**
	 * Tests if valid API key with valid city returns 200. Content type is set to something else rather than JSON. 
	 * @result Expects statusCode = 200 and JSON message cod: 200
	 */
	
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
	
	/**
	 * Tests if valid API key with valid country returns 200. Country parameter is "Germany" 
	 * @result Expects statusCode = 200 and JSON message cod: 200
	 */
	
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
	
	/**
	 * Tests if valid API key with valid city and country returns 200. City parameter is "Berlin", country parameter is "DE" 
	 * @result Expects statusCode = 200 and JSON message cod: 200
	 */
	
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
	
	/**
	 * Tests if valid API key with valid city returns 200. City parameter is "Berlin", no country. 
	 * @result Expects statusCode = 200 and JSON message cod: 200, name: "Berlin"
	 */
	
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
			body("cod", 	equalTo(200), 
				 "name", 	equalTo("Berlin"));
		
	}
	
	/**
	 * Tests if valid API key with valid city using cityID returns 200. City ID for "Berlin" is 2950159.
	 * Data extracted from: http://bulk.openweathermap.org/sample/city.list.json.gz
	 * @result Expects statusCode = 200 and JSON message cod: 200, name: "Berlin"
	 */
	
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
	
	/**
	 * Tests if valid API key with multiple cities using Berlin, Köln, Munchen, Hamburg using cityIDs returns 200. 
	 * City IDs are:
	 *  Berlin	 				2950159
	 *	Hamburg-Mitte 			2911288
	 *	Regierungsbezirk Köln	2886241
	 *	Garching bei Munchen	2922582
	 * Data extracted from: http://bulk.openweathermap.org/sample/city.list.json.gz
	 * @result Expects statusCode = 200 and JSON message cod: 200, name: "Berlin Mitte"
	 */
	
	@Test
	public void shouldAuthenticateAndReturnWeatherForFourGermanCitiesByCityIDListWhenAPIKeyIsValidAndRequestParamsAreValid() {
		
		String cityIDBerlin 	= "2950159";
		String cityIDKoln 		= "2911288";
		String cityIDMunchen 	= "2886241";
		String cityIDHamburg 	= "2922582";
		
		given().
			baseUri(clientConfig.getBaseURL()).
			contentType(clientConfig.getContentType()).
			header(clientConfig.getAuthHeader(), clientConfig.getApiKey()).
		when().
			get("/group?id={cityIDBerlin},{cityIDKoln},{cityIDMunchen},{cityIDHamburg}", cityIDBerlin, cityIDKoln, cityIDMunchen, cityIDHamburg).
		then().
			statusCode(200).
			body("cnt", 	equalTo(4));
		
	}
	
	/**
	 * Tests if valid API key with Berlin cityID returns a valid temperature in Kelvin scale. City for Berlin is 2950159.
	 * Data extracted from: http://bulk.openweathermap.org/sample/city.list.json.gz
	 * @result Expects statusCode = 200 and temperature between 213K and 333K (lowest and highest temperatures ever recorded on earth)
	 */
	
	@Test
	public void shouldAuthenticateAndReturnWeatherInKelvinForBerlinAsCityIDWhenAPIKeyIsValidAndRequestParamsAreValid() throws UnirestException {
		
		String cityID = "2950159";
		
		//given
		HttpResponse<JsonNode> jsonResponse = Unirest.get(clientConfig.getBaseURL().concat("/weather"))
											  .header(clientConfig.getAuthHeader(), clientConfig.getApiKey())
											  .queryString("id", cityID)
											  .asJson();
		
		Gson gson = new Gson();
		
		//when
		WeatherResponse weatherResponse= gson.fromJson(jsonResponse.getBody().toString(), WeatherResponse.class);
		
		//then
		assertThat(jsonResponse.getStatus()).isEqualTo(200);
		assertThat(weatherResponse.getMain().getTemp()).isNotNull().isBetween(213d, 333d);
		
	}
	
	/**
	 * Tests if valid API key with Berlin cityID returns a valid temperature in Fahrenheit scale. City for Berlin is 2950159.
	 * Data extracted from: http://bulk.openweathermap.org/sample/city.list.json.gz
	 * @result Expects statusCode = 200 and temperature between -76 and 140 (lowest and highest temperatures ever recorded on earth)
	 */
	
	@Test
	public void shouldAuthenticateAndReturnWeatherInFahrenheitForBerlinAsCityIDWhenAPIKeyIsValidAndRequestParamsAreValid() throws UnirestException {
		
		String cityID = "2950159";
		
		Map<String, Object> mapParameters = new HashMap();
		
		mapParameters.put("id", cityID);
		mapParameters.put("units", clientConfig.getImperialUnit());
		
		//given
		HttpResponse<JsonNode> jsonResponse = Unirest.get(clientConfig.getBaseURL().concat("weather"))
											  .header(clientConfig.getAuthHeader(), clientConfig.getApiKey())
											  .queryString(mapParameters)
											  .asJson();
		
		Gson gson = new Gson();
		
		//when
		WeatherResponse weatherResponse= gson.fromJson(jsonResponse.getBody().toString(), WeatherResponse.class);
		
		//then
		assertThat(jsonResponse.getStatus()).isEqualTo(200);
		assertThat(weatherResponse.getMain().getTemp()).isNotNull().isBetween(-76d, 140d);
		
	}
	
	/**
	 * Tests if valid API key with Berlin cityID returns a valid temperature in Celsius scale. City for Berlin is 2950159.
	 * Data extracted from: http://bulk.openweathermap.org/sample/city.list.json.gz
	 * @result Expects statusCode = 200 and temperature between -60 and 60 (lowest and highest temperatures ever recorded on earth)
	 */
	
	@Test
	public void shouldAuthenticateAndReturnWeatherInCelsiusForBerlinAsCityIDWhenAPIKeyIsValidAndRequestParamsAreValid() throws UnirestException {
		
		String cityID = "2950159";
		
		Map<String, Object> mapParameters = new HashMap();
		
		mapParameters.put("id", cityID);
		mapParameters.put("units", clientConfig.getMetricUnit());
		
		//given
		HttpResponse<JsonNode> jsonResponse = Unirest.get(clientConfig.getBaseURL().concat("weather"))
											  .header(clientConfig.getAuthHeader(), clientConfig.getApiKey())
											  .queryString(mapParameters)
											  .asJson();
		
		Gson gson = new Gson();
		
		//when
		WeatherResponse weatherResponse= gson.fromJson(jsonResponse.getBody().toString(), WeatherResponse.class);
		
		//then
		assertThat(jsonResponse.getStatus()).isEqualTo(200);
		assertThat(weatherResponse.getMain().getTemp()).isNotNull().isBetween(-60d, 60d);
		
	}
	
	
	
}

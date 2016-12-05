package com.fyber.weather.helper;

public class ClientConfig {
	
	private String apiKey 				= "5f821613fb757b1abe13ba5f0fb04980";
	private String badAPIKey 			= "5f8a1613ab757a1abe1aba5f0ab04980";
	private String baseURI 				= "http://api.openweathermap.org/data/2.5/";
	private String authHeader 			= "X-API-KEY";
	private String badAuthHeader 		= "X-API-KEY-INVALID";
	private String contentType  		= "application/json";
	private String emptyAPIKey 			= "";
	private String notJSONContentType 	= "application/x-shockwave-flash"; 
	
	public String getBadAuthHeader() {
		return badAuthHeader;
	}

	public void setBadAuthHeader(String badAuthHeader) {
		this.badAuthHeader = badAuthHeader;
	}

	public String getNotJSONContentType() {
		return notJSONContentType;
	}

	public void setNotJSONContentType(String notJSONContentType) {
		this.notJSONContentType = notJSONContentType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getEmptyAPIKey() {
		return emptyAPIKey;
	}

	public void setEmptyAPIKey(String emptyAPIKey) {
		this.emptyAPIKey = emptyAPIKey;
	}

	public String getBadApiKey() {
		return badAPIKey;
	}

	public void setBadApiKey(String badApiKey) {
		this.badAPIKey = badApiKey;
	}

	public String getAuthHeader() {
		return authHeader;
	}

	public void setAuthHeader(String authHeader) {
		this.authHeader = authHeader;
	}

	public String getBaseURL() {
		return baseURI;
	}

	public void setBaseURL(String baseURL) {
		this.baseURI = baseURL;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	
	
}

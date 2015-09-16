package com.main.interconnection.rest.client;

import java.util.Map;

public interface ThirdPartyRestClient {
	
	public <T> T commonRestSearchClient(String url , Class<T> responseType , Map<String, String> params);
}

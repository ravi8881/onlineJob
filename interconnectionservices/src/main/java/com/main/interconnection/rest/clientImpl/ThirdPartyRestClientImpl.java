package com.main.interconnection.rest.clientImpl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import com.main.interconnection.rest.client.ThirdPartyRestClient;

public class ThirdPartyRestClientImpl implements ThirdPartyRestClient {
	
	@Autowired
	protected RestTemplate restTemplate;

	@Override
	public <T> T commonRestSearchClient(String solrUrl, Class<T> responseType, Map<String, String> params) {
		return (T) restTemplate.getForObject(solrUrl, responseType , params);
	}
}

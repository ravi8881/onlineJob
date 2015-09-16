package com.main.interconnection.util;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.main.interconnection.solrUtil.UrlConstant;



public class Yelp {

	  OAuthService service;
	  Token accessToken;
	  
	  public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
		    this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
		    this.accessToken = new Token(token, tokenSecret);
		  }
	
	  public String search(String term, double latitude, double longitude, String limit) {
		    OAuthRequest request = new OAuthRequest(Verb.GET, UrlConstant.YELP_DEALS);
		    request.addQuerystringParameter("term", term);
		    request.addQuerystringParameter("ll", latitude + "," + longitude);
		    request.addQuerystringParameter("limit", limit);		    
		    this.service.signRequest(this.accessToken, request);		    
		    Response response = request.send();
		    return response.getBody();
		  }

}

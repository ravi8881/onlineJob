package com.main.interconnection.clientBo;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;


@XmlRootElement(name = "solr-session")
@JsonWriteNullProperties(false)
public class SolrSession {

	private String userId;
	private String token;
	public String getUserId() {
		return userId;
	}
	public String getToken() {
		return token;
	}
	@JsonProperty("user_id")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@JsonProperty("token")
	public void setToken(String token) {
		this.token = token;
	}	
}

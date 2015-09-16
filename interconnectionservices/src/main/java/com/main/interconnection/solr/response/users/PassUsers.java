package com.main.interconnection.solr.response.users;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

@JsonWriteNullProperties(false)
public class PassUsers {

	public ResponseHeader responseHeader;
	public Response response;
	
	
	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}
	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
}

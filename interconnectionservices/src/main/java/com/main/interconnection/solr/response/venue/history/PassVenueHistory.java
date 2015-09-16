package com.main.interconnection.solr.response.venue.history;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

@XmlRootElement(name = "venue-history")
@JsonWriteNullProperties(false)

public class PassVenueHistory {

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

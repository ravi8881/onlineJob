package com.main.interconnection.solr.response.venue;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;

@XmlRootElement(name = "venue")
@JsonWriteNullProperties(false)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PassVenue {

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

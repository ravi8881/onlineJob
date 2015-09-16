package com.main.interconnection.solr.response.bookmark;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

@XmlRootElement(name = "bookmark")
@JsonWriteNullProperties(false)

public class PassBookmark {

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

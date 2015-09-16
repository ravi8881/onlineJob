package com.main.interconnection.solr.response.venue.history;

public class ResponseHeader {

	public int status;
	public int QTime;
	public Params params;
	
	
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getQTime() {
		return QTime;
	}
	public void setQTime(int qTime) {
		QTime = qTime;
	}
	public Params getParams() {
		return params;
	}
	public void setParams(Params params) {
		this.params = params;
	}
	
	
}

package com.main.interconnection.clientBo;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;

@JsonWriteNullProperties(false)
public class SolrBinaryData {

	public String id;
	public String orignal;
	public String small;
	public String medium;
	public String large;
	public String content_id;
	public String user_id;
	public String type;
	public Date addedDate;
		
	public Date getAddedDate() {
		return addedDate;
	}

	public String getId() {
		return id;
	}
	public String getOrignal() {
		return orignal;
	}
	public String getSmall() {
		return small;
	}
	public String getMedium() {
		return medium;
	}
	public String getLarge() {
		return large;
	}
	public String getContent_id() {
		return content_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public String getType() {
		return type;
	}
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}
	@JsonProperty("orignal")
	public void setOrignal(String orignal) {
		this.orignal = orignal;
	}
	@JsonProperty("small")
	public void setSmall(String small) {
		this.small = small;
	}
	@JsonProperty("medium")
	public void setMedium(String medium) {
		this.medium = medium;
	}
	@JsonProperty("large")
	public void setLarge(String large) {
		this.large = large;
	}
	@JsonProperty("content_id")
	public void setContent_id(String content_id) {
		this.content_id = content_id;
	}
	@JsonProperty("user_id")
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}
	@JsonProperty("added_date")
	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}
}

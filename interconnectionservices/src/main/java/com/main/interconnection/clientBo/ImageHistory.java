package com.main.interconnection.clientBo;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class ImageHistory {
	
	private String id;
	private String imageId;
	private String userId;	
	private Date addedDate;
	private String type;
	
	public String getId() {
		return id;
	}
	public String getImageId() {
		return imageId;
	}
	public String getUserId() {
		return userId;
	}
	public Date getAddedDate() {
		return addedDate;
	}
	public String getType() {
		return type;
	}
	
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}
	@JsonProperty("image_id")
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	@JsonProperty("user_id")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@JsonProperty("added_date")
	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}	
}

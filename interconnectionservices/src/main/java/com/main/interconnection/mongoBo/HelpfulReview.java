package com.main.interconnection.mongoBo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class HelpfulReview {

	@Id
	private String id;
	private String reviewId;
	private String venueId;
	private String userId;
	private String helpfulStatus;
	
	
	public String getId() {
		return id;
	}
	public String getReviewId() {
		return reviewId;
	}
	public String getVenueId() {
		return venueId;
	}
	public String getUserId() {
		return userId;
	}
	public String getHelpfulStatus() {
		return helpfulStatus;
	}

	public void setId(String id) {
		this.id = id;
	}
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setHelpfulStatus(String helpfulStatus) {
		this.helpfulStatus = helpfulStatus;
	}
	
}

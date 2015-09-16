package com.main.interconnection.clientBo;

import java.util.Date;
import org.codehaus.jackson.annotate.JsonProperty;

public class HelpFulReview {
	
	private String id;
	private String reviewId;
	private String venueId;
	private String userId;
	private Date reviewAddedDate;	
	private String reviewHelpFul;	
	private String fromUser;
	
	
	public String getFromUser() {
		return fromUser;
	}
	@JsonProperty("fromUser")
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
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
	public Date getReviewAddedDate() {
		return reviewAddedDate;
	}
	public String getReviewHelpFul() {
		return reviewHelpFul;
	}
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}
	@JsonProperty("review_id")
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	@JsonProperty("venue_id")
	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}
	@JsonProperty("user_id")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@JsonProperty("added_date")
	public void setReviewAddedDate(Date reviewAddedDate) {
		this.reviewAddedDate = reviewAddedDate;
	}
	@JsonProperty("review_helpful")
	public void setReviewHelpFul(String reviewHelpFul) {
		this.reviewHelpFul = reviewHelpFul;
	}	
}

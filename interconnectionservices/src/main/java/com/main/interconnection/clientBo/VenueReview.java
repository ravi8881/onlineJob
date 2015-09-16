package com.main.interconnection.clientBo;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;

import com.main.interconnection.client.InterConnectionClient;
import com.main.interconnection.util.SolrDateUtil;

@XmlRootElement(name = "venue-review")
@JsonWriteNullProperties(false)
public class VenueReview {

	InterConnectionClient interConnectionClient;
	
	private String reviewId;
	private String venueId;
	private String userId;
	private String reviewRating;
	private String tip;
	private String review;
	private String reviewAddedDate;
	private String reviewStatus;
	private String contentId;
	private String updatedSince="undefined";
	private User users;
	
	public String getReviewId() {
		return reviewId;
	}
	public String getVenueId() {
		return venueId;
	}
	
	public String getUserId() {
		return userId;
	}
	public String getReviewRating() {
		return reviewRating;
	}
	public String getTip() {
		return tip;
	}
	public String getReview() {
		return review;
	}
	public String getReviewAddedDate() {
		return reviewAddedDate;
	}
	public String getReviewStatus() {
		return reviewStatus;
	}
	public String getContentId() {
		return contentId;
	}
	@JsonProperty("review_id")
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	@JsonProperty("user_id")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@JsonProperty("venue_id")
	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}
	
	@JsonProperty("review_rate")
	public void setReviewRating(String reviewRating) {
		this.reviewRating = reviewRating;
	}
	@JsonProperty("tip")
	public void setTip(String tip) {
		this.tip = tip;
	}
	@JsonProperty("review")
	public void setReview(String review) {
		this.review = review;
	}
	@JsonProperty("review_date")
	public void setReviewAddedDate(String reviewAddedDate) {
		this.reviewAddedDate = reviewAddedDate;
	}
	@JsonProperty("review_status")
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	@JsonProperty("content_id")
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getUpdatedSince() {
		if(null!=this.reviewAddedDate)
			return this.updatedSince=SolrDateUtil.addDateDifferenceFromSolrToServiceforUpdatedate(this.reviewAddedDate.toString());
		else
			return this.updatedSince;
	}
	@JsonProperty("updatedSince")
	public void setUpdatedSince(String updatedSince) {
		this.updatedSince = updatedSince;
	}
	public User getUsers() {
		return users;
	}
	public void setUsers(User users) {
		this.users = users;
	}
}

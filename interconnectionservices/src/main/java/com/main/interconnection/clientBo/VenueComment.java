package com.main.interconnection.clientBo;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;

import com.main.interconnection.client.InterConnectionClient;
import com.main.interconnection.util.SolrDateUtil;

@XmlRootElement(name = "comment")
@JsonWriteNullProperties(false)
public class VenueComment {

	InterConnectionClient interConnectionClient;
	
	private String commentId;
	private String venueId;
	private String reviewId;
	private String userId;
	private String fromUser;
	private String comment_txt;
	private String commentAddedDate;
	private String updatedSince="undefined";
	private User users;
	
	public String getFromUser() {
		return fromUser;
	}
	@JsonProperty("from_user")
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getCommentId() {
		return commentId;
	}
	public String getVenueId() {
		return venueId;
	}
	public String getReviewId() {
		return reviewId;
	}
	public String getUserId() {
		return userId;
	}
	public String getComment_txt() {
		return comment_txt;
	}
	
	public String getCommentAddedDate() {
		return commentAddedDate;
	}
	
	@JsonProperty("comment_id")
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	@JsonProperty("venue_id")
	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}
	@JsonProperty("review_id")
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	@JsonProperty("user_id")
	public void setUserId(String userId) {
		this.userId = userId;
	//	setUsers();
	}
	@JsonProperty("comment_txt")
	public void setComment_txt(String comment_txt) {
		this.comment_txt = comment_txt;
	}
	@JsonProperty("comment_added_date")
	public void setCommentAddedDate(String commentAddedDate) {
		this.commentAddedDate = commentAddedDate;
	}
	public String getUpdatedSince() {
		if(null!=this.commentAddedDate)
			return this.updatedSince=SolrDateUtil.addDateDifferenceFromSolrToServiceforUpdatedate(this.commentAddedDate.toString());
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

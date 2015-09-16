package com.main.interconnection.clientBo;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import com.main.interconnection.util.SolrDateUtil;

public class VenueCommentReply {
	
	private String replyId;
	private String venueId;
	private String commentId;
	private String reviewId;
	private String userId;
	private String reply_txt;
	private String replyAddedDate;
	private String fromUser;
	private String updatedSince="undefined";
	
	
	public String getFromUser() {
		return fromUser;
	}
	public String getReplyId() {
		return replyId;
	}
	public String getVenueId() {
		return venueId;
	}
	public String getCommentId() {
		return commentId;
	}
	public String getReviewId() {
		return reviewId;
	}
	public String getUserId() {
		return userId;
	}
	public String getReply_txt() {
		return reply_txt;
	}
	public String getReplyAddedDate() {
		return replyAddedDate;
	}
	@JsonProperty("reply_id")
	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}
	@JsonProperty("venue_id")
	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}
	@JsonProperty("comment_id")
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	@JsonProperty("review_id")
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	@JsonProperty("user_id")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@JsonProperty("reply_txt")
	public void setReply_txt(String reply_txt) {
		this.reply_txt = reply_txt;
	}
	@JsonProperty("reply_added_date")
	public void setReplyAddedDate(String replyAddedDate) {
		this.replyAddedDate = replyAddedDate;
	}
	@JsonProperty("from_user")
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	public String getUpdatedSince() {
		if(null!=this.replyAddedDate)
			return this.updatedSince=SolrDateUtil.addDateDifferenceFromSolrToServiceforUpdatedate(this.replyAddedDate.toString());
		else
			return this.updatedSince;
	}
	@JsonProperty("updatedSince")
	public void setUpdatedSince(String updatedSince) {
		this.updatedSince = updatedSince;
	}
	}

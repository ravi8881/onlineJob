package com.main.interconnection.mongoBo;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.main.interconnection.util.SolrDateUtil;


@Document(collection="likeFeeds")
@JsonWriteNullProperties(false)
public class LikeFeeds {

	
	private String likeId;
	@Indexed
	private String userId;
	private String type;
	private String subType;
	
	/**  
	 * These All fields are Related to comments on feeds Section .
	 * 
	 * */	
	private String commentId;
	private String feedUserId;	
	private String commentText;
	private String comment_flag = "0";
	private Date addedDate;
	private String feedId;
	private String updatedSince;
	
	
	public String getUpdatedSince() {
		if(null!=this.addedDate)
			return this.updatedSince=SolrDateUtil.addDateDifferenceFromSolrToServiceforPdate(this.addedDate.toString());
		else
			return this.updatedSince;
	}
	
	public void setUpdatedSince(String updatedSince) {
		this.updatedSince = updatedSince;
	}
	public String getFeedId() {
		return feedId;
	}
	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}
	@JsonSerialize(using =SolrDateUtil.class)
	public Date getAddedDate() {
		return addedDate;
	}
	public void setAddedDate(Date addedDate) {
	    this.addedDate = addedDate;
	}
	public String getLikeId() {
		return likeId;
	}
	public void setLikeId(String likeId) {
		this.likeId = likeId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
	public String getFeedUserId() {
		return feedUserId;
	}
	public void setFeedUserId(String feedUserId) {
		this.feedUserId = feedUserId;
	}
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public String getCommentText() {
		return commentText;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}	
	public String getComment_flag() {
		return comment_flag;
	}
	public void setComment_flag(String comment_flag) {
		this.comment_flag = comment_flag;
	}
}

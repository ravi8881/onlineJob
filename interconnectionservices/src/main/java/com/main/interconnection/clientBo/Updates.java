package com.main.interconnection.clientBo;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;

import com.main.interconnection.client.InterConnectionClient;
import com.main.interconnection.util.SolrDateUtil;

@XmlRootElement(name = "feeds")
@JsonWriteNullProperties(false)
public class Updates {
	
	InterConnectionClient interConnectionClient;
	private String id="undefined";
	private String type="undefined";	
	private String subType="undefined";
	private String property="undefined";
	private String	toUser="undefined";
	private String fromUser="undefined";
	private String reqestId="undefined";
	private String venueId="undefined";
	private String updateId="undefined";
	private String reviewId="undefined";
	private String commentId="undefined";
	private String addedDate;
	private String updatedSince="undefined";
	private String markedSpam = "0";
	private String markedInappropriate = "0";
	private String privacy="undefined";
	private String flag="0";
	private String content_id="undefined";
	private String isLike="undefined";
	private String imageUrl="undefined";
	private String searchName="undefined";
	private Venue venue;
	private String subTypes="undefined";
	private User users;
	private User usersFrom;
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag=flag;
		
	}

	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	

	@JsonProperty("updatedSince")
	public String getUpdatedSince() {
		if(null!=this.addedDate)
			return this.updatedSince=SolrDateUtil.addDateDifferenceFromSolrToServiceforUpdatedate(this.addedDate.toString());
		else
			return this.updatedSince;
	}
	public String getId() {
		return id;
	}
	public String getType() {
		return type;
	}
	public String getSubType() {
		return subType;
	}
	public String getProperty() {
		return property;
	}
	public String getToUser() {
		return toUser;
	}
	public String getFromUser() {
		return fromUser;
	}
	public String getReqestId() {
		return reqestId;
	}	
	public String getVenueId() {
		return venueId;
	}
	public String getUpdateId() {
		return updateId;
	}
	public String getCommentId() {
		return commentId;
	}
	public String getReviewId() {
		return reviewId;
	}
	public String getMarkedSpam() {
		return markedSpam;
	}
	public String getMarkedInappropriate() {
		return markedInappropriate;
	}
	public String getContent_id() {
		return content_id;
	}

	@JsonProperty("addedDate")
	public String getAddedDate() {
		return addedDate;
	}
	
	@JsonProperty("id")
	public void setId(String id) {		
		this.id = id;		
	}
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
		
	}
	@JsonProperty("subType")
	public void setSubType(String subType) {
		this.subType = subType;
	}
	@JsonProperty("property")
	public void setProperty(String property) {
		this.property = property;
	}
	@JsonProperty("toUser")
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	@JsonProperty("fromUser")
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	@JsonProperty("request_id")
	public void setReqestId(String reqestId) {
		this.reqestId = reqestId;
	}
	@JsonProperty("venue_id")
	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}
	@JsonProperty("update_id")
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	@JsonProperty("comment_id")
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	@JsonProperty("addedDate")
	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}
	@JsonProperty("updatedSince")
	public void setUpdatedSince(String updatedSince) {
		this.updatedSince = updatedSince;
	}
	@JsonProperty("review_id")
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	public void setMarkedSpam(String markedSpam) {
		this.markedSpam = markedSpam;
	}
	public void setMarkedInappropriate(String markedInappropriate) {
		this.markedInappropriate = markedInappropriate;
	}
	@JsonProperty("content_id")
	public void setContent_id(String content_id) {
		this.content_id = content_id;
	}

	public String getImageUrl() {
		return imageUrl;
	}
	@JsonProperty("image_url")
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getSearchName() {
		return searchName;
	}
	@JsonProperty("search_name")
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getSubTypes() {
		return subTypes;
	}
	
	public void setSubTypes(String subTypes) {
		this.subTypes = subTypes;
	}

	public User getUsers() {
		return users;
	}

	public void setUsers(User users) {
		this.users = users;
	}

	public User getUsersFrom() {
		return usersFrom;
	}

	public void setUsersFrom(User usersFrom) {
		this.usersFrom = usersFrom;
	}

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}
	
}

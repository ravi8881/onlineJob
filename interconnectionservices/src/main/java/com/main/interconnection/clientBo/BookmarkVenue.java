package com.main.interconnection.clientBo;

import java.util.Date;


import org.codehaus.jackson.annotate.JsonProperty;

public class BookmarkVenue {
	private String id;
	private String userId;
	private String bookId;
	private String fromUser;
	private String bookmarkStatus;
	private String type;
	private Date addedDate;
	private String privacy;
	private String searchName;
	
	public String getPrivacy() {
		return privacy;
	}
	public String getId() {
		return id;
	}
	public String getUserId() {
		return userId;
	}
	public String getBookId() {
		return bookId;
	}
	public String getFromUser() {
		return fromUser;
	}
	public String getBookmarkStatus() {
		return bookmarkStatus;
	}
	public String getType() {
		return type;
	}
	public Date getAddedDate() {
		return addedDate;
	}
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}
	@JsonProperty("userId")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@JsonProperty("bookId")
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	@JsonProperty("fromUser")
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	@JsonProperty("bookmark_status")
	public void setBookmarkStatus(String bookmarkStatus) {
		this.bookmarkStatus = bookmarkStatus;
	}
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}
	@JsonProperty("addedDate")
	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}
	@JsonProperty("privacy")
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	public String getSearchName() {
		return searchName;
	}
	@JsonProperty("searchName")
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

}

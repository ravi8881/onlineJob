package com.main.interconnection.clientBo;

import com.main.interconnection.solr.response.venue.ResponseHeader;

/*
 * @author Name:Gaurav chugh
 * @Created Date:15/11/2014
 * @update Date:15/11/2014
 * @purpose:VenueCommentReplyDetails
 */
public class VenueCommentReplyDetails {
private String isFlag;
public VenueCommentReply venueCommentReply;
public User toUser;
public User fromUser;

public User getToUser() {
	return toUser;
}

public void setToUser(User toUser) {
	this.toUser = toUser;
}

public User getFromUser() {
	return fromUser;
}

public void setFromUser(User fromUser) {
	this.fromUser = fromUser;
}

public String getIsFlag() {
	return isFlag;
}

public void setIsFlag(String isFlag) {
	this.isFlag = isFlag;
}

public VenueCommentReply getVenueCommentReply() {
	return venueCommentReply;
}

public void setVenueCommentReply(VenueCommentReply venueCommentReply) {
	this.venueCommentReply = venueCommentReply;
}

}

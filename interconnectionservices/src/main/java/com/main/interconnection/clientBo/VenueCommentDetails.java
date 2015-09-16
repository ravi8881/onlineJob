package com.main.interconnection.clientBo;

import com.main.interconnection.solr.response.venue.ResponseHeader;

/*
 * @author Name:Gaurav chugh
 * @Created Date:15/11/2014
 * @update Date:15/11/2014
 * @purpose:VenueCommentDetails
 */
public class VenueCommentDetails {
private String isFlag;
public VenueComment venueComment;

public String getIsFlag() {
	return isFlag;
}
public void setIsFlag(String isFlag) {
	this.isFlag = isFlag;
}
public VenueComment getVenueComment() {
	return venueComment;
}
public void setVenueComment(VenueComment venueComment) {
	this.venueComment = venueComment;
}

}

package com.main.interconnection.clientBo;

import com.main.interconnection.solr.response.venue.ResponseHeader;

public class VenueReviewDetails {
	public ResponseHeader responseHeader;
	public int numFound;
	private int commentTotal;
	private int helpfulTotal;
	private int helpFulReviewYes = 0;
	private int  helpFulReviewNo =0;
	private String isFlag;
	private VenueReview venueReview;
	private String isHelpFul;
	private Venue venue;
	
	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}

	public int getNumFound() {
		return numFound;
	}

	public void setNumFound(int numFound) {
		this.numFound = numFound;
	}

	public int getCommentTotal() {
		return commentTotal;
	}

	public void setCommentTotal(int commentTotal) {
		this.commentTotal = commentTotal;
	}

	public int getHelpfulTotal() {
		return helpfulTotal;
	}

	public void setHelpfulTotal(int helpfulTotal) {
		this.helpfulTotal = helpfulTotal;
	}
	public int getHelpFulReviewYes() {
		return helpFulReviewYes;
	}

	public void setHelpFulReviewYes(int helpFulReviewYes) {
		this.helpFulReviewYes = helpFulReviewYes;
	}

	public int getHelpFulReviewNo() {
		return helpFulReviewNo;
	}

	public void setHelpFulReviewNo(int helpFulReviewNo) {
		this.helpFulReviewNo = helpFulReviewNo;
	}

	public String getIsFlag() {
		return isFlag;
	}

	public void setIsFlag(String isFlag) {
		this.isFlag = isFlag;
	}

	public VenueReview getVenueReview() {
		return venueReview;
	}

	public void setVenueReview(VenueReview venueReview) {
		this.venueReview = venueReview;
	}

	public String getIsHelpFul() {
		return isHelpFul;
	}

	public void setIsHelpFul(String isHelpFul) {
		this.isHelpFul = isHelpFul;
	}

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}
}

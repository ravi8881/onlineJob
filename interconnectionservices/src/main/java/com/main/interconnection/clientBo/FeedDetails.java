package com.main.interconnection.clientBo;

import com.main.interconnection.solr.response.venue.ResponseHeader;

public class FeedDetails {

	public ResponseHeader responseHeader;

	public int numFound;
	public Updates updates;
	private String isLikeFlag;
	private String spamFlag;
	private String inappropriate;
	private int comments;
	private long totalLikes;
	private double rating;
	private double averageRating;
	

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

	public Updates getUpdates() {
		return updates;
	}

	public void setUpdates(Updates updates) {
		this.updates = updates;
	}

	public String getIsLikeFlag() {
		return isLikeFlag;
	}

	public void setIsLikeFlag(String isLikeFlag) {
		this.isLikeFlag = isLikeFlag;
	}

	public String getSpamFlag() {
		return spamFlag;
	}

	public void setSpamFlag(String spamFlag) {
		this.spamFlag = spamFlag;
	}

	public String getInappropriate() {
		return inappropriate;
	}

	public void setInappropriate(String inappropriate) {
		this.inappropriate = inappropriate;
	}


	public long getTotalLikes() {
		return totalLikes;
	}

	public void setTotalLikes(long totalLikes) {
		this.totalLikes = totalLikes;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

}

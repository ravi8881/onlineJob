package com.main.interconnection.clientBo;


public class VenueImageDetails {
	public SolrBinaryData solrBinaryData;
	private double averageRating = 0;
	private double rating = 0;
	private int totalImage;
	private int totalReview;
	private int deals;
	private String isflag ="0";
	public SolrBinaryData getSolrBinaryData() {
		return solrBinaryData;
	}
	public void setSolrBinaryData(SolrBinaryData solrBinaryData) {
		this.solrBinaryData = solrBinaryData;
	}
	public double getAverageRating() {
		return averageRating;
	}
	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public int getTotalImage() {
		return totalImage;
	}
	public void setTotalImage(int totalImage) {
		this.totalImage = totalImage;
	}
	public int getTotalReview() {
		return totalReview;
	}
	public void setTotalReview(int totalReview) {
		this.totalReview = totalReview;
	}
	public int getDeals() {
		return deals;
	}
	public void setDeals(int deals) {
		this.deals = deals;
	}
	public String getIsflag() {
		return isflag;
	}
	public void setIsflag(String isflag) {
		this.isflag = isflag;
	}
	
	
}

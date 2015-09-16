package com.main.interconnection.clientBo;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "venue")
@JsonWriteNullProperties(false)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Venue {
	
	private String venueId;
	private String userId;	
	private String venueName;
	private String venueAddedDate;
	private String	address;
	private String venueCityId;
	private String venueStatueId;
	private String venueZipcode;
	
	private String venueLati;
	private String venueLong;
	
	private String venuePhoneNo;
	private String hourOfOperation;
	private String website;
	private String venueCategory;
	private String venueCategoryKey;
	private String venueSubCategory;
	private String venueSubCategoryKey;
	private String venuePhotoLink;
	private String venueAdditionalInfo;
	private int venueStatus;
	private String conformVenue;
	//Flags for admin section approval
	private String phone_no_flag;
	private String address_flag;
	private String website_flag;
	private String photo_flag;
	private String comment_flag;
	private String type;
	
	private String store_loc;

	public String getVenueId() {
		return venueId;
	}
	public String getUserId() {
		return userId;
	}
	public String getVenueName() {
		return venueName;
	}
	public String getVenueAddedDate() {
		return venueAddedDate;
	}
	public String getAddress() {
		return address;
	}
	public String getVenueCityId() {
		return venueCityId;
	}
	public String getVenueStatueId() {
		return venueStatueId;
	}
	public String getVenueZipcode() {
		return venueZipcode;
	}	
	public String getVenueLati() {
		return venueLati;
	}
	public String getVenueLong() {
		return venueLong;
	}	
	public String getVenuePhoneNo() {
		return venuePhoneNo;
	}
	public String getHourOfOperation() {
		return hourOfOperation;
	}
	public String getWebsite() {
		return website;
	}
	public String getVenueCategory() {
		return venueCategory;
	}
	public String getVenuePhotoLink() {
		return venuePhotoLink;
	}
	public String getVenueAdditionalInfo() {
		return venueAdditionalInfo;
	}
	public int getVenueStatus() {
		return venueStatus;
	}
	public String getConformVenue() {
		return conformVenue;
	}
	public String getPhone_no_flag() {
		return phone_no_flag;
	}
	public String getAddress_flag() {
		return address_flag;
	}
	public String getWebsite_flag() {
		return website_flag;
	}
	public String getPhoto_flag() {
		return photo_flag;
	}
	public String getComment_flag() {
		return comment_flag;
	}
	public String getStore_loc() {
		return store_loc;
	}
	@JsonProperty("venue_id")
	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}
	@JsonProperty("user_id")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@JsonProperty("venue_name")
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}
	@JsonProperty("venue_add_date")
	public void setVenueAddedDate(String venueAddedDate) {
		this.venueAddedDate = venueAddedDate;
	}
	@JsonProperty("venue_address")
	public void setAddress(String address) {
		this.address = address;
	}
	@JsonProperty("venue_city_id")
	public void setVenueCityId(String venueCityId) {
		this.venueCityId = venueCityId;
	}
	@JsonProperty("venue_state_id")
	public void setVenueStatueId(String venueStatueId) {
		this.venueStatueId = venueStatueId;
	}
	@JsonProperty("venue_zip_code")
	public void setVenueZipcode(String venueZipcode) {
		this.venueZipcode = venueZipcode;
	}
	@JsonProperty("venue_lati")
	public void setVenueLati(String venueLati) {
		this.venueLati = venueLati;
	}
	@JsonProperty("venue_long")
	public void setVenueLong(String venueLong) {
		this.venueLong = venueLong;
	}	
	@JsonProperty("venue_phone_no")
	public void setVenuePhoneNo(String venuePhoneNo) {
		this.venuePhoneNo = venuePhoneNo;
	}
	@JsonProperty("hour_Of_Operation")
	public void setHourOfOperation(String hourOfOperation) {
		this.hourOfOperation = hourOfOperation;
	}
	@JsonProperty("website")
	public void setWebsite(String website) {
		this.website = website;
	}
	@JsonProperty("venue_category_id")
	public void setVenueCategory(String venueCategory) {
		this.venueCategory = venueCategory;
	}
	@JsonProperty("venue_photo_link")
	public void setVenuePhotoLink(String venuePhotoLink) {
		this.venuePhotoLink = venuePhotoLink;
	}
	@JsonProperty("venue_info")
	public void setVenueAdditionalInfo(String venueAdditionalInfo) {
		this.venueAdditionalInfo = venueAdditionalInfo;
	}
	@JsonProperty("venue_status")
	public void setVenueStatus(int venueStatus) {
		this.venueStatus = venueStatus;
	}
	@JsonProperty("conform_venue")
	public void setConformVenue(String conformVenue) {
		this.conformVenue = conformVenue;
	}
	@JsonProperty("phone_no_flag")
	public void setPhone_no_flag(String phone_no_flag) {
		this.phone_no_flag = phone_no_flag;
	}
	@JsonProperty("address_flag")
	public void setAddress_flag(String address_flag) {
		this.address_flag = address_flag;
	}
	@JsonProperty("website_flag")
	public void setWebsite_flag(String website_flag) {
		this.website_flag = website_flag;
	}
	@JsonProperty("photo_flag")
	public void setPhoto_flag(String photo_flag) {
		this.photo_flag = photo_flag;
	}
	@JsonProperty("comment_flag")
	public void setComment_flag(String comment_flag) {
		this.comment_flag = comment_flag;
	}
	@JsonProperty("store_loc")
	public void setStore_loc(String store_loc) {
		this.store_loc = store_loc;
	}
	public String getVenueSubCategory() {
		return venueSubCategory;
	}
	@JsonProperty("venue_subcategory_id")
	public void setVenueSubCategory(String venueSubCategory) {
		this.venueSubCategory = venueSubCategory;
	}
	public String getType() {
		return type;
	}
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}
	public String getVenueCategoryKey() {
		return venueCategoryKey;
	}
	@JsonProperty("venue_category_key")
	public void setVenueCategoryKey(String venueCategoryKey) {
		this.venueCategoryKey = venueCategoryKey;
	}
	public String getVenueSubCategoryKey() {
		return venueSubCategoryKey;
	}
	@JsonProperty("venue_subcategory_key")
	public void setVenueSubCategoryKey(String venueSubCategoryKey) {
		this.venueSubCategoryKey = venueSubCategoryKey;
	}
	
}
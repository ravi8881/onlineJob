
package com.main.interconnection.clientBo;

import org.codehaus.jackson.annotate.JsonProperty;




public class Deals {

    @JsonProperty("deal_uuid")
    private String dealUuid;
    @JsonProperty("deal_lati")
    private String dealLati;
    @JsonProperty("deal_long")
    private String dealLong;
    @JsonProperty("street")
    private String street;
    @JsonProperty("deal_type")
    private String dealType;
    @JsonProperty("discount")
    private String discount;
    @JsonProperty("discountPercent")
    private String discountPercent;
    @JsonProperty("grouponRating")
    private String grouponRating;
    @JsonProperty("para")
    private String para;
    @JsonProperty("mapImageUrl")
    private String mapImageUrl;
    @JsonProperty("largeImageUrl")
    private String largeImageUrl;
    @JsonProperty("mediumImageUrl")
    private String mediumImageUrl;
    @JsonProperty("shortAnnouncementTitle")
    private String shortAnnouncementTitle;
    @JsonProperty("title")
    private String title;
    @JsonProperty("deal_name")
    private String dealName;
    @JsonProperty("websiteUrl")
    private String websiteUrl;
    @JsonProperty("startAt")
    private String startAt;
    @JsonProperty("endAt")
    private String endAt;
    @JsonProperty("expiresAt")
    private String expiresAt;
    @JsonProperty("expiresAtFormated")
    private String expiresAtFormated;
    @JsonProperty("description")
    private String description;
    @JsonProperty("buyUrl")
    private String buyUrl;
    @JsonProperty("price")
    private String price;
    
    @JsonProperty("deal_name_search")
    private String dealNameSearch;
    
    @JsonProperty("isBookMark")
    private String isBookMark;

    /**
     * 
     * @return
     *     The dealUuid
     */
    @JsonProperty("deal_uuid")
    public String getDealUuid() {
        return dealUuid;
    }

    /**
     * 
     * @param dealUuid
     *     The deal_uuid
     */
    @JsonProperty("deal_uuid")
    public void setDealUuid(String dealUuid) {
        this.dealUuid = dealUuid;
    }

    /**
     * 
     * @return
     *     The dealLati
     */
    @JsonProperty("deal_lati")
    public String getDealLati() {
        return dealLati;
    }

    /**
     * 
     * @param dealLati
     *     The deal_lati
     */
    @JsonProperty("deal_lati")
    public void setDealLati(String dealLati) {
        this.dealLati = dealLati;
    }

    /**
     * 
     * @return
     *     The dealLong
     */
    @JsonProperty("deal_long")
    public String getDealLong() {
        return dealLong;
    }

    /**
     * 
     * @param dealLong
     *     The deal_long
     */
    @JsonProperty("deal_long")
    public void setDealLong(String dealLong) {
        this.dealLong = dealLong;
    }

    /**
     * 
     * @return
     *     The street
     */
    @JsonProperty("street")
    public String getStreet() {
        return street;
    }

    /**
     * 
     * @param street
     *     The street
     */
    @JsonProperty("street")
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * 
     * @return
     *     The dealType
     */
    @JsonProperty("deal_type")
    public String getDealType() {
        return dealType;
    }

    /**
     * 
     * @param dealType
     *     The deal_type
     */
    @JsonProperty("deal_type")
    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    /**
     * 
     * @return
     *     The discount
     */
    @JsonProperty("discount")
    public String getDiscount() {
        return discount;
    }

    /**
     * 
     * @param discount
     *     The discount
     */
    @JsonProperty("discount")
    public void setDiscount(String discount) {
        this.discount = discount;
    }

    /**
     * 
     * @return
     *     The discountPercent
     */
    @JsonProperty("discountPercent")
    public String getDiscountPercent() {
        return discountPercent;
    }

    /**
     * 
     * @param discountPercent
     *     The discountPercent
     */
    @JsonProperty("discountPercent")
    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    /**
     * 
     * @return
     *     The grouponRating
     */
    @JsonProperty("grouponRating")
    public String getGrouponRating() {
        return grouponRating;
    }

    /**
     * 
     * @param grouponRating
     *     The grouponRating
     */
    @JsonProperty("grouponRating")
    public void setGrouponRating(String grouponRating) {
        this.grouponRating = grouponRating;
    }

    /**
     * 
     * @return
     *     The para
     */
    @JsonProperty("para")
    public String getPara() {
        return para;
    }

    /**
     * 
     * @param para
     *     The para
     */
    @JsonProperty("para")
    public void setPara(String para) {
        this.para = para;
    }

    /**
     * 
     * @return
     *     The mapImageUrl
     */
    @JsonProperty("mapImageUrl")
    public String getMapImageUrl() {
        return mapImageUrl;
    }

    /**
     * 
     * @param mapImageUrl
     *     The mapImageUrl
     */
    @JsonProperty("mapImageUrl")
    public void setMapImageUrl(String mapImageUrl) {
        this.mapImageUrl = mapImageUrl;
    }

    /**
     * 
     * @return
     *     The largeImageUrl
     */
    @JsonProperty("largeImageUrl")
    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    /**
     * 
     * @param largeImageUrl
     *     The largeImageUrl
     */
    @JsonProperty("largeImageUrl")
    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    /**
     * 
     * @return
     *     The mediumImageUrl
     */
    @JsonProperty("mediumImageUrl")
    public String getMediumImageUrl() {
        return mediumImageUrl;
    }

    /**
     * 
     * @param mediumImageUrl
     *     The mediumImageUrl
     */
    @JsonProperty("mediumImageUrl")
    public void setMediumImageUrl(String mediumImageUrl) {
        this.mediumImageUrl = mediumImageUrl;
    }

    /**
     * 
     * @return
     *     The shortAnnouncementTitle
     */
    @JsonProperty("shortAnnouncementTitle")
    public String getShortAnnouncementTitle() {
        return shortAnnouncementTitle;
    }

    /**
     * 
     * @param shortAnnouncementTitle
     *     The shortAnnouncementTitle
     */
    @JsonProperty("shortAnnouncementTitle")
    public void setShortAnnouncementTitle(String shortAnnouncementTitle) {
        this.shortAnnouncementTitle = shortAnnouncementTitle;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The dealName
     */
    @JsonProperty("deal_name")
    public String getDealName() {
        return dealName;
    }

    /**
     * 
     * @param dealName
     *     The deal_name
     */
    @JsonProperty("deal_name")
    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    /**
     * 
     * @return
     *     The websiteUrl
     */
    @JsonProperty("websiteUrl")
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    /**
     * 
     * @param websiteUrl
     *     The websiteUrl
     */
    @JsonProperty("websiteUrl")
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    /**
     * 
     * @return
     *     The startAt
     */
    @JsonProperty("startAt")
    public String getStartAt() {
        return startAt;
    }

    /**
     * 
     * @param startAt
     *     The startAt
     */
    @JsonProperty("startAt")
    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    /**
     * 
     * @return
     *     The endAt
     */
    @JsonProperty("endAt")
    public String getEndAt() {
        return endAt;
    }

    /**
     * 
     * @param endAt
     *     The endAt
     */
    @JsonProperty("endAt")
    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    /**
     * 
     * @return
     *     The expiresAt
     */
    @JsonProperty("expiresAt")
    public String getExpiresAt() {
        return expiresAt;
    }

    /**
     * 
     * @param expiresAt
     *     The expiresAt
     */
    @JsonProperty("expiresAt")
    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     * 
     * @return
     *     The expiresAtFormated
     */
    @JsonProperty("expiresAtFormated")
    public String getExpiresAtFormated() {
        return expiresAtFormated;
    }

    /**
     * 
     * @param expiresAtFormated
     *     The expiresAtFormated
     */
    @JsonProperty("expiresAtFormated")
    public void setExpiresAtFormated(String expiresAtFormated) {
        this.expiresAtFormated = expiresAtFormated;
    }

    /**
     * 
     * @return
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The buyUrl
     */
    @JsonProperty("buyUrl")
    public String getBuyUrl() {
        return buyUrl;
    }

    /**
     * 
     * @param buyUrl
     *     The buyUrl
     */
    @JsonProperty("buyUrl")
    public void setBuyUrl(String buyUrl) {
        this.buyUrl = buyUrl;
    }

    /**
     * 
     * @return
     *     The price
     */
    @JsonProperty("price")
    public String getPrice() {
        return price;
    }

    /**
     * 
     * @param price
     *     The price
     */
    @JsonProperty("price")
    public void setPrice(String price) {
        this.price = price;
    }

    @JsonProperty("deal_name_search")
	public String getDealNameSearch() {
		return dealNameSearch;
	}

    @JsonProperty("deal_name_search")
	public void setDealNameSearch(String dealNameSearch) {
		this.dealNameSearch = dealNameSearch;
	}

	public String getIsBookMark() {
		return isBookMark;
	}

	public void setIsBookMark(String isBookMark) {
		this.isBookMark = isBookMark;
	}

    
    

}

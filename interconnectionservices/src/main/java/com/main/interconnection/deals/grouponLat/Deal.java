
package com.main.interconnection.deals.grouponLat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "grouponRating",
    "smallImageUrl",
    "status",
    "salesforceLink",
    "placeholderUrl",
    "isInviteOnly",
    "fulfillmentMethod",
    "startAt",
    "isTipped",
    "options",
    "recommendation",
    "textAd",
    "merchant",
    "limitedQuantityRemaining",
    "highlightsHtml",
    "isTravelBookableDeal",
    "isAutoRefundEnabled",
    "isMerchandisingDeal",
    "channels",
    "dealUrl",
    "dealTypes",
    "displayOptions",
    "tippedAt",
    "locationNote",
    "uuid",
    "vip",
    "tippingPoint",
    "grid6ImageUrl",
    "placementPriority",
    "soldQuantityMessage",
    "mediumImageUrl",
    "tags",
    "id",
    "finePrint",
    "isGliveInventoryDeal",
    "announcementTitle",
    "pitchHtml",
    "accessType",
    "sidebarImageUrl",
    "division",
    "largeImageUrl",
    "endAt",
    "isOptionListComplete",
    "areas",
    "says",
    "type",
    "shortAnnouncementTitle",
    "redemptionLocation",
    "grid4ImageUrl",
    "isNowDeal",
    "title",
    "allowedInCart",
    "soldQuantity",
    "isSoldOut",
    "shippingAddressRequired"
})

@JsonIgnoreProperties(ignoreUnknown=true)
public class Deal {

    @JsonProperty("grouponRating")
    private float grouponRating;
    @JsonProperty("smallImageUrl")
    private String smallImageUrl;
    @JsonProperty("status")
    private String status;
    @JsonProperty("salesforceLink")
    private String salesforceLink;
    @JsonProperty("placeholderUrl")
    private String placeholderUrl;
    @JsonProperty("isInviteOnly")
    private boolean isInviteOnly;
    @JsonProperty("fulfillmentMethod")
    private Object fulfillmentMethod;
    @JsonProperty("startAt")
    private String startAt;
    @JsonProperty("isTipped")
    private boolean isTipped;
    @JsonProperty("options")
    private List<Option> options = new ArrayList<Option>();
    @JsonProperty("recommendation")
    private Recommendation recommendation;
    @JsonProperty("textAd")
    private TextAd textAd;
    @JsonProperty("merchant")
    private Merchant merchant;
    @JsonProperty("limitedQuantityRemaining")
    private Object limitedQuantityRemaining;
    @JsonProperty("highlightsHtml")
    private String highlightsHtml;
    @JsonProperty("isTravelBookableDeal")
    private boolean isTravelBookableDeal;
    @JsonProperty("isAutoRefundEnabled")
    private boolean isAutoRefundEnabled;
    @JsonProperty("isMerchandisingDeal")
    private boolean isMerchandisingDeal;
    @JsonProperty("channels")
    private List<Channel> channels = new ArrayList<Channel>();
    @JsonProperty("dealUrl")
    private String dealUrl;
    @JsonProperty("dealTypes")
    private List<Object> dealTypes = new ArrayList<Object>();
    @JsonProperty("displayOptions")
    private List<DisplayOption> displayOptions = new ArrayList<DisplayOption>();
    @JsonProperty("tippedAt")
    private String tippedAt;
    @JsonProperty("locationNote")
    private String locationNote;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("vip")
    private String vip;
    @JsonProperty("tippingPoint")
    private int tippingPoint;
    @JsonProperty("grid6ImageUrl")
    private String grid6ImageUrl;
    @JsonProperty("placementPriority")
    private String placementPriority;
    @JsonProperty("soldQuantityMessage")
    private String soldQuantityMessage;
    @JsonProperty("mediumImageUrl")
    private String mediumImageUrl;
    @JsonProperty("tags")
    private List<Tag> tags = new ArrayList<Tag>();
    @JsonProperty("id")
    private String id;
    @JsonProperty("finePrint")
    private String finePrint;
    @JsonProperty("isGliveInventoryDeal")
    private boolean isGliveInventoryDeal;
    @JsonProperty("announcementTitle")
    private String announcementTitle;
    @JsonProperty("pitchHtml")
    private String pitchHtml;
    @JsonProperty("accessType")
    private String accessType;
    @JsonProperty("sidebarImageUrl")
    private String sidebarImageUrl;
    @JsonProperty("division")
    private Division division;
    @JsonProperty("largeImageUrl")
    private String largeImageUrl;
    @JsonProperty("endAt")
    private String endAt;
    @JsonProperty("isOptionListComplete")
    private boolean isOptionListComplete;
    @JsonProperty("areas")
    private List<Object> areas = new ArrayList<Object>();
    @JsonProperty("says")
    private Says says;
    @JsonProperty("type")
    private String type;
    @JsonProperty("shortAnnouncementTitle")
    private String shortAnnouncementTitle;
    @JsonProperty("redemptionLocation")
    private String redemptionLocation;
    @JsonProperty("grid4ImageUrl")
    private String grid4ImageUrl;
    @JsonProperty("isNowDeal")
    private boolean isNowDeal;
    @JsonProperty("title")
    private String title;
    @JsonProperty("allowedInCart")
    private boolean allowedInCart;
    @JsonProperty("soldQuantity")
    private int soldQuantity;
    @JsonProperty("isSoldOut")
    private boolean isSoldOut;
    @JsonProperty("shippingAddressRequired")
    private boolean shippingAddressRequired;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    
    @JsonProperty("isBookMark")
    private String isBookMark="0";
    
    @JsonProperty("grouponRating")
    public float getGrouponRating() {
        return grouponRating;
    }

    public String getIsBookMark() {
		return isBookMark;
	}

	public void setIsBookMark(String isBookMark) {
		this.isBookMark = isBookMark;
	}

	@JsonProperty("grouponRating")
    public void setGrouponRating(float grouponRating) {
        this.grouponRating = grouponRating;
    }

    public Deal withGrouponRating(float grouponRating) {
        this.grouponRating = grouponRating;
        return this;
    }

    @JsonProperty("smallImageUrl")
    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    @JsonProperty("smallImageUrl")
    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public Deal withSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
        return this;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    public Deal withStatus(String status) {
        this.status = status;
        return this;
    }

    @JsonProperty("salesforceLink")
    public String getSalesforceLink() {
        return salesforceLink;
    }

    @JsonProperty("salesforceLink")
    public void setSalesforceLink(String salesforceLink) {
        this.salesforceLink = salesforceLink;
    }

    public Deal withSalesforceLink(String salesforceLink) {
        this.salesforceLink = salesforceLink;
        return this;
    }

    @JsonProperty("placeholderUrl")
    public String getPlaceholderUrl() {
        return placeholderUrl;
    }

    @JsonProperty("placeholderUrl")
    public void setPlaceholderUrl(String placeholderUrl) {
        this.placeholderUrl = placeholderUrl;
    }

    public Deal withPlaceholderUrl(String placeholderUrl) {
        this.placeholderUrl = placeholderUrl;
        return this;
    }

    @JsonProperty("isInviteOnly")
    public boolean isIsInviteOnly() {
        return isInviteOnly;
    }

    @JsonProperty("isInviteOnly")
    public void setIsInviteOnly(boolean isInviteOnly) {
        this.isInviteOnly = isInviteOnly;
    }

    public Deal withIsInviteOnly(boolean isInviteOnly) {
        this.isInviteOnly = isInviteOnly;
        return this;
    }

    @JsonProperty("fulfillmentMethod")
    public Object getFulfillmentMethod() {
        return fulfillmentMethod;
    }

    @JsonProperty("fulfillmentMethod")
    public void setFulfillmentMethod(Object fulfillmentMethod) {
        this.fulfillmentMethod = fulfillmentMethod;
    }

    public Deal withFulfillmentMethod(Object fulfillmentMethod) {
        this.fulfillmentMethod = fulfillmentMethod;
        return this;
    }

    @JsonProperty("startAt")
    public String getStartAt() {
        return startAt;
    }

    @JsonProperty("startAt")
    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public Deal withStartAt(String startAt) {
        this.startAt = startAt;
        return this;
    }

    @JsonProperty("isTipped")
    public boolean isIsTipped() {
        return isTipped;
    }

    @JsonProperty("isTipped")
    public void setIsTipped(boolean isTipped) {
        this.isTipped = isTipped;
    }

    public Deal withIsTipped(boolean isTipped) {
        this.isTipped = isTipped;
        return this;
    }

    @JsonProperty("options")
    public List<Option> getOptions() {
        return options;
    }

    @JsonProperty("options")
    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Deal withOptions(List<Option> options) {
        this.options = options;
        return this;
    }

    @JsonProperty("recommendation")
    public Recommendation getRecommendation() {
        return recommendation;
    }

    @JsonProperty("recommendation")
    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public Deal withRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
        return this;
    }

    @JsonProperty("textAd")
    public TextAd getTextAd() {
        return textAd;
    }

    @JsonProperty("textAd")
    public void setTextAd(TextAd textAd) {
        this.textAd = textAd;
    }

    public Deal withTextAd(TextAd textAd) {
        this.textAd = textAd;
        return this;
    }

    @JsonProperty("merchant")
    public Merchant getMerchant() {
        return merchant;
    }

    @JsonProperty("merchant")
    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Deal withMerchant(Merchant merchant) {
        this.merchant = merchant;
        return this;
    }

    @JsonProperty("limitedQuantityRemaining")
    public Object getLimitedQuantityRemaining() {
        return limitedQuantityRemaining;
    }

    @JsonProperty("limitedQuantityRemaining")
    public void setLimitedQuantityRemaining(Object limitedQuantityRemaining) {
        this.limitedQuantityRemaining = limitedQuantityRemaining;
    }

    public Deal withLimitedQuantityRemaining(Object limitedQuantityRemaining) {
        this.limitedQuantityRemaining = limitedQuantityRemaining;
        return this;
    }

    @JsonProperty("highlightsHtml")
    public String getHighlightsHtml() {
        return highlightsHtml;
    }

    @JsonProperty("highlightsHtml")
    public void setHighlightsHtml(String highlightsHtml) {
        this.highlightsHtml = highlightsHtml;
    }

    public Deal withHighlightsHtml(String highlightsHtml) {
        this.highlightsHtml = highlightsHtml;
        return this;
    }

    @JsonProperty("isTravelBookableDeal")
    public boolean isIsTravelBookableDeal() {
        return isTravelBookableDeal;
    }

    @JsonProperty("isTravelBookableDeal")
    public void setIsTravelBookableDeal(boolean isTravelBookableDeal) {
        this.isTravelBookableDeal = isTravelBookableDeal;
    }

    public Deal withIsTravelBookableDeal(boolean isTravelBookableDeal) {
        this.isTravelBookableDeal = isTravelBookableDeal;
        return this;
    }

    @JsonProperty("isAutoRefundEnabled")
    public boolean isIsAutoRefundEnabled() {
        return isAutoRefundEnabled;
    }

    @JsonProperty("isAutoRefundEnabled")
    public void setIsAutoRefundEnabled(boolean isAutoRefundEnabled) {
        this.isAutoRefundEnabled = isAutoRefundEnabled;
    }

    public Deal withIsAutoRefundEnabled(boolean isAutoRefundEnabled) {
        this.isAutoRefundEnabled = isAutoRefundEnabled;
        return this;
    }

    @JsonProperty("isMerchandisingDeal")
    public boolean isIsMerchandisingDeal() {
        return isMerchandisingDeal;
    }

    @JsonProperty("isMerchandisingDeal")
    public void setIsMerchandisingDeal(boolean isMerchandisingDeal) {
        this.isMerchandisingDeal = isMerchandisingDeal;
    }

    public Deal withIsMerchandisingDeal(boolean isMerchandisingDeal) {
        this.isMerchandisingDeal = isMerchandisingDeal;
        return this;
    }

    @JsonProperty("channels")
    public List<Channel> getChannels() {
        return channels;
    }

    @JsonProperty("channels")
    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public Deal withChannels(List<Channel> channels) {
        this.channels = channels;
        return this;
    }

    @JsonProperty("dealUrl")
    public String getDealUrl() {
        return dealUrl;
    }

    @JsonProperty("dealUrl")
    public void setDealUrl(String dealUrl) {
        this.dealUrl = dealUrl;
    }

    public Deal withDealUrl(String dealUrl) {
        this.dealUrl = dealUrl;
        return this;
    }

    @JsonProperty("dealTypes")
    public List<Object> getDealTypes() {
        return dealTypes;
    }

    @JsonProperty("dealTypes")
    public void setDealTypes(List<Object> dealTypes) {
        this.dealTypes = dealTypes;
    }

    public Deal withDealTypes(List<Object> dealTypes) {
        this.dealTypes = dealTypes;
        return this;
    }

    @JsonProperty("displayOptions")
    public List<DisplayOption> getDisplayOptions() {
        return displayOptions;
    }

    @JsonProperty("displayOptions")
    public void setDisplayOptions(List<DisplayOption> displayOptions) {
        this.displayOptions = displayOptions;
    }

    public Deal withDisplayOptions(List<DisplayOption> displayOptions) {
        this.displayOptions = displayOptions;
        return this;
    }

    @JsonProperty("tippedAt")
    public String getTippedAt() {
        return tippedAt;
    }

    @JsonProperty("tippedAt")
    public void setTippedAt(String tippedAt) {
        this.tippedAt = tippedAt;
    }

    public Deal withTippedAt(String tippedAt) {
        this.tippedAt = tippedAt;
        return this;
    }

    @JsonProperty("locationNote")
    public String getLocationNote() {
        return locationNote;
    }

    @JsonProperty("locationNote")
    public void setLocationNote(String locationNote) {
        this.locationNote = locationNote;
    }

    public Deal withLocationNote(String locationNote) {
        this.locationNote = locationNote;
        return this;
    }

    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    @JsonProperty("uuid")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Deal withUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    @JsonProperty("vip")
    public String getVip() {
        return vip;
    }

    @JsonProperty("vip")
    public void setVip(String vip) {
        this.vip = vip;
    }

    public Deal withVip(String vip) {
        this.vip = vip;
        return this;
    }

    @JsonProperty("tippingPoint")
    public int getTippingPoint() {
        return tippingPoint;
    }

    @JsonProperty("tippingPoint")
    public void setTippingPoint(int tippingPoint) {
        this.tippingPoint = tippingPoint;
    }

    public Deal withTippingPoint(int tippingPoint) {
        this.tippingPoint = tippingPoint;
        return this;
    }

    @JsonProperty("grid6ImageUrl")
    public String getGrid6ImageUrl() {
        return grid6ImageUrl;
    }

    @JsonProperty("grid6ImageUrl")
    public void setGrid6ImageUrl(String grid6ImageUrl) {
        this.grid6ImageUrl = grid6ImageUrl;
    }

    public Deal withGrid6ImageUrl(String grid6ImageUrl) {
        this.grid6ImageUrl = grid6ImageUrl;
        return this;
    }

    @JsonProperty("placementPriority")
    public String getPlacementPriority() {
        return placementPriority;
    }

    @JsonProperty("placementPriority")
    public void setPlacementPriority(String placementPriority) {
        this.placementPriority = placementPriority;
    }

    public Deal withPlacementPriority(String placementPriority) {
        this.placementPriority = placementPriority;
        return this;
    }

    @JsonProperty("soldQuantityMessage")
    public String getSoldQuantityMessage() {
        return soldQuantityMessage;
    }

    @JsonProperty("soldQuantityMessage")
    public void setSoldQuantityMessage(String soldQuantityMessage) {
        this.soldQuantityMessage = soldQuantityMessage;
    }

    public Deal withSoldQuantityMessage(String soldQuantityMessage) {
        this.soldQuantityMessage = soldQuantityMessage;
        return this;
    }

    @JsonProperty("mediumImageUrl")
    public String getMediumImageUrl() {
        return mediumImageUrl;
    }

    @JsonProperty("mediumImageUrl")
    public void setMediumImageUrl(String mediumImageUrl) {
        this.mediumImageUrl = mediumImageUrl;
    }

    public Deal withMediumImageUrl(String mediumImageUrl) {
        this.mediumImageUrl = mediumImageUrl;
        return this;
    }

    @JsonProperty("tags")
    public List<Tag> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Deal withTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public Deal withId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("finePrint")
    public String getFinePrint() {
        return finePrint;
    }

    @JsonProperty("finePrint")
    public void setFinePrint(String finePrint) {
        this.finePrint = finePrint;
    }

    public Deal withFinePrint(String finePrint) {
        this.finePrint = finePrint;
        return this;
    }

    @JsonProperty("isGliveInventoryDeal")
    public boolean isIsGliveInventoryDeal() {
        return isGliveInventoryDeal;
    }

    @JsonProperty("isGliveInventoryDeal")
    public void setIsGliveInventoryDeal(boolean isGliveInventoryDeal) {
        this.isGliveInventoryDeal = isGliveInventoryDeal;
    }

    public Deal withIsGliveInventoryDeal(boolean isGliveInventoryDeal) {
        this.isGliveInventoryDeal = isGliveInventoryDeal;
        return this;
    }

    @JsonProperty("announcementTitle")
    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    @JsonProperty("announcementTitle")
    public void setAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }

    public Deal withAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
        return this;
    }

    @JsonProperty("pitchHtml")
    public String getPitchHtml() {
        return pitchHtml;
    }

    @JsonProperty("pitchHtml")
    public void setPitchHtml(String pitchHtml) {
        this.pitchHtml = pitchHtml;
    }

    public Deal withPitchHtml(String pitchHtml) {
        this.pitchHtml = pitchHtml;
        return this;
    }

    @JsonProperty("accessType")
    public String getAccessType() {
        return accessType;
    }

    @JsonProperty("accessType")
    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public Deal withAccessType(String accessType) {
        this.accessType = accessType;
        return this;
    }

    @JsonProperty("sidebarImageUrl")
    public String getSidebarImageUrl() {
        return sidebarImageUrl;
    }

    @JsonProperty("sidebarImageUrl")
    public void setSidebarImageUrl(String sidebarImageUrl) {
        this.sidebarImageUrl = sidebarImageUrl;
    }

    public Deal withSidebarImageUrl(String sidebarImageUrl) {
        this.sidebarImageUrl = sidebarImageUrl;
        return this;
    }

    @JsonProperty("division")
    public Division getDivision() {
        return division;
    }

    @JsonProperty("division")
    public void setDivision(Division division) {
        this.division = division;
    }

    public Deal withDivision(Division division) {
        this.division = division;
        return this;
    }

    @JsonProperty("largeImageUrl")
    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    @JsonProperty("largeImageUrl")
    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public Deal withLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
        return this;
    }

    @JsonProperty("endAt")
    public String getEndAt() {
        return endAt;
    }

    @JsonProperty("endAt")
    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public Deal withEndAt(String endAt) {
        this.endAt = endAt;
        return this;
    }

    @JsonProperty("isOptionListComplete")
    public boolean isIsOptionListComplete() {
        return isOptionListComplete;
    }

    @JsonProperty("isOptionListComplete")
    public void setIsOptionListComplete(boolean isOptionListComplete) {
        this.isOptionListComplete = isOptionListComplete;
    }

    public Deal withIsOptionListComplete(boolean isOptionListComplete) {
        this.isOptionListComplete = isOptionListComplete;
        return this;
    }

    @JsonProperty("areas")
    public List<Object> getAreas() {
        return areas;
    }

    @JsonProperty("areas")
    public void setAreas(List<Object> areas) {
        this.areas = areas;
    }

    public Deal withAreas(List<Object> areas) {
        this.areas = areas;
        return this;
    }

    @JsonProperty("says")
    public Says getSays() {
        return says;
    }

    @JsonProperty("says")
    public void setSays(Says says) {
        this.says = says;
    }

    public Deal withSays(Says says) {
        this.says = says;
        return this;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public Deal withType(String type) {
        this.type = type;
        return this;
    }

    @JsonProperty("shortAnnouncementTitle")
    public String getShortAnnouncementTitle() {
        return shortAnnouncementTitle;
    }

    @JsonProperty("shortAnnouncementTitle")
    public void setShortAnnouncementTitle(String shortAnnouncementTitle) {
        this.shortAnnouncementTitle = shortAnnouncementTitle;
    }

    public Deal withShortAnnouncementTitle(String shortAnnouncementTitle) {
        this.shortAnnouncementTitle = shortAnnouncementTitle;
        return this;
    }

    @JsonProperty("redemptionLocation")
    public String getRedemptionLocation() {
        return redemptionLocation;
    }

    @JsonProperty("redemptionLocation")
    public void setRedemptionLocation(String redemptionLocation) {
        this.redemptionLocation = redemptionLocation;
    }

    public Deal withRedemptionLocation(String redemptionLocation) {
        this.redemptionLocation = redemptionLocation;
        return this;
    }

    @JsonProperty("grid4ImageUrl")
    public String getGrid4ImageUrl() {
        return grid4ImageUrl;
    }

    @JsonProperty("grid4ImageUrl")
    public void setGrid4ImageUrl(String grid4ImageUrl) {
        this.grid4ImageUrl = grid4ImageUrl;
    }

    public Deal withGrid4ImageUrl(String grid4ImageUrl) {
        this.grid4ImageUrl = grid4ImageUrl;
        return this;
    }

    @JsonProperty("isNowDeal")
    public boolean isIsNowDeal() {
        return isNowDeal;
    }

    @JsonProperty("isNowDeal")
    public void setIsNowDeal(boolean isNowDeal) {
        this.isNowDeal = isNowDeal;
    }

    public Deal withIsNowDeal(boolean isNowDeal) {
        this.isNowDeal = isNowDeal;
        return this;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public Deal withTitle(String title) {
        this.title = title;
        return this;
    }

    @JsonProperty("allowedInCart")
    public boolean isAllowedInCart() {
        return allowedInCart;
    }

    @JsonProperty("allowedInCart")
    public void setAllowedInCart(boolean allowedInCart) {
        this.allowedInCart = allowedInCart;
    }

    public Deal withAllowedInCart(boolean allowedInCart) {
        this.allowedInCart = allowedInCart;
        return this;
    }

    @JsonProperty("soldQuantity")
    public int getSoldQuantity() {
        return soldQuantity;
    }

    @JsonProperty("soldQuantity")
    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public Deal withSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
        return this;
    }

    @JsonProperty("isSoldOut")
    public boolean isIsSoldOut() {
        return isSoldOut;
    }

    @JsonProperty("isSoldOut")
    public void setIsSoldOut(boolean isSoldOut) {
        this.isSoldOut = isSoldOut;
    }

    public Deal withIsSoldOut(boolean isSoldOut) {
        this.isSoldOut = isSoldOut;
        return this;
    }

    @JsonProperty("shippingAddressRequired")
    public boolean isShippingAddressRequired() {
        return shippingAddressRequired;
    }

    @JsonProperty("shippingAddressRequired")
    public void setShippingAddressRequired(boolean shippingAddressRequired) {
        this.shippingAddressRequired = shippingAddressRequired;
    }

    public Deal withShippingAddressRequired(boolean shippingAddressRequired) {
        this.shippingAddressRequired = shippingAddressRequired;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

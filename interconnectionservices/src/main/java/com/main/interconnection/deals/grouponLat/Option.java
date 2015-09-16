
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
    "minimumPurchaseQuantity",
    "discount",
    "bookable",
    "initialQuantity",
    "uuid",
    "status",
    "redemptionLocations",
    "price",
    "specificAttributes",
    "value",
    "remainingQuantity",
    "endAt",
    "title",
    "isLimitedQuantity",
    "customFields",
    "traits",
    "soldQuantity",
    "expiresAt",
    "details",
    "isSoldOut",
    "expiresInDays",
    "buyUrl",
    "maximumPurchaseQuantity",
    "discountPercent",
    "id",
    "soldQuantityMessage",
    "externalUrl"
})

@JsonIgnoreProperties(ignoreUnknown=true)
public class Option {

    @JsonProperty("minimumPurchaseQuantity")
    private int minimumPurchaseQuantity;
    @JsonProperty("discount")
    private Discount discount;
    @JsonProperty("bookable")
    private boolean bookable;
    @JsonProperty("initialQuantity")
    private int initialQuantity;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("status")
    private String status;
    @JsonProperty("redemptionLocations")
    private List<Object> redemptionLocations = new ArrayList<Object>();
    @JsonProperty("price")
    private Price price;
    @JsonProperty("specificAttributes")
    private SpecificAttributes specificAttributes;
    @JsonProperty("value")
    private Value value;
    @JsonProperty("remainingQuantity")
    private int remainingQuantity;
    @JsonProperty("endAt")
    private String endAt;
    @JsonProperty("title")
    private String title;
    @JsonProperty("isLimitedQuantity")
    private boolean isLimitedQuantity;
    @JsonProperty("customFields")
    private List<Object> customFields = new ArrayList<Object>();
    @JsonProperty("traits")
    private List<Trait> traits = new ArrayList<Trait>();
    @JsonProperty("soldQuantity")
    private int soldQuantity;
    @JsonProperty("expiresAt")
    private Object expiresAt;
    @JsonProperty("details")
    private List<Detail> details = new ArrayList<Detail>();
    @JsonProperty("isSoldOut")
    private boolean isSoldOut;
    @JsonProperty("expiresInDays")
    private Object expiresInDays;
    @JsonProperty("buyUrl")
    private String buyUrl;
    @JsonProperty("maximumPurchaseQuantity")
    private int maximumPurchaseQuantity;
    @JsonProperty("discountPercent")
    private int discountPercent;
    @JsonProperty("id")
    private int id;
    @JsonProperty("soldQuantityMessage")
    private String soldQuantityMessage;
    @JsonProperty("externalUrl")
    private Object externalUrl;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("minimumPurchaseQuantity")
    public int getMinimumPurchaseQuantity() {
        return minimumPurchaseQuantity;
    }

    @JsonProperty("minimumPurchaseQuantity")
    public void setMinimumPurchaseQuantity(int minimumPurchaseQuantity) {
        this.minimumPurchaseQuantity = minimumPurchaseQuantity;
    }

    public Option withMinimumPurchaseQuantity(int minimumPurchaseQuantity) {
        this.minimumPurchaseQuantity = minimumPurchaseQuantity;
        return this;
    }

    @JsonProperty("discount")
    public Discount getDiscount() {
        return discount;
    }

    @JsonProperty("discount")
    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Option withDiscount(Discount discount) {
        this.discount = discount;
        return this;
    }

    @JsonProperty("bookable")
    public boolean isBookable() {
        return bookable;
    }

    @JsonProperty("bookable")
    public void setBookable(boolean bookable) {
        this.bookable = bookable;
    }

    public Option withBookable(boolean bookable) {
        this.bookable = bookable;
        return this;
    }

    @JsonProperty("initialQuantity")
    public int getInitialQuantity() {
        return initialQuantity;
    }

    @JsonProperty("initialQuantity")
    public void setInitialQuantity(int initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public Option withInitialQuantity(int initialQuantity) {
        this.initialQuantity = initialQuantity;
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

    public Option withUuid(String uuid) {
        this.uuid = uuid;
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

    public Option withStatus(String status) {
        this.status = status;
        return this;
    }

    @JsonProperty("redemptionLocations")
    public List<Object> getRedemptionLocations() {
        return redemptionLocations;
    }

    @JsonProperty("redemptionLocations")
    public void setRedemptionLocations(List<Object> redemptionLocations) {
        this.redemptionLocations = redemptionLocations;
    }

    public Option withRedemptionLocations(List<Object> redemptionLocations) {
        this.redemptionLocations = redemptionLocations;
        return this;
    }

    @JsonProperty("price")
    public Price getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Price price) {
        this.price = price;
    }

    public Option withPrice(Price price) {
        this.price = price;
        return this;
    }

    @JsonProperty("specificAttributes")
    public SpecificAttributes getSpecificAttributes() {
        return specificAttributes;
    }

    @JsonProperty("specificAttributes")
    public void setSpecificAttributes(SpecificAttributes specificAttributes) {
        this.specificAttributes = specificAttributes;
    }

    public Option withSpecificAttributes(SpecificAttributes specificAttributes) {
        this.specificAttributes = specificAttributes;
        return this;
    }

    @JsonProperty("value")
    public Value getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(Value value) {
        this.value = value;
    }

    public Option withValue(Value value) {
        this.value = value;
        return this;
    }

    @JsonProperty("remainingQuantity")
    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    @JsonProperty("remainingQuantity")
    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public Option withRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
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

    public Option withEndAt(String endAt) {
        this.endAt = endAt;
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

    public Option withTitle(String title) {
        this.title = title;
        return this;
    }

    @JsonProperty("isLimitedQuantity")
    public boolean isIsLimitedQuantity() {
        return isLimitedQuantity;
    }

    @JsonProperty("isLimitedQuantity")
    public void setIsLimitedQuantity(boolean isLimitedQuantity) {
        this.isLimitedQuantity = isLimitedQuantity;
    }

    public Option withIsLimitedQuantity(boolean isLimitedQuantity) {
        this.isLimitedQuantity = isLimitedQuantity;
        return this;
    }

    @JsonProperty("customFields")
    public List<Object> getCustomFields() {
        return customFields;
    }

    @JsonProperty("customFields")
    public void setCustomFields(List<Object> customFields) {
        this.customFields = customFields;
    }

    public Option withCustomFields(List<Object> customFields) {
        this.customFields = customFields;
        return this;
    }

    @JsonProperty("traits")
    public List<Trait> getTraits() {
        return traits;
    }

    @JsonProperty("traits")
    public void setTraits(List<Trait> traits) {
        this.traits = traits;
    }

    public Option withTraits(List<Trait> traits) {
        this.traits = traits;
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

    public Option withSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
        return this;
    }

    @JsonProperty("expiresAt")
    public Object getExpiresAt() {
        return expiresAt;
    }

    @JsonProperty("expiresAt")
    public void setExpiresAt(Object expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Option withExpiresAt(Object expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    @JsonProperty("details")
    public List<Detail> getDetails() {
        return details;
    }

    @JsonProperty("details")
    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public Option withDetails(List<Detail> details) {
        this.details = details;
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

    public Option withIsSoldOut(boolean isSoldOut) {
        this.isSoldOut = isSoldOut;
        return this;
    }

    @JsonProperty("expiresInDays")
    public Object getExpiresInDays() {
        return expiresInDays;
    }

    @JsonProperty("expiresInDays")
    public void setExpiresInDays(Object expiresInDays) {
        this.expiresInDays = expiresInDays;
    }

    public Option withExpiresInDays(Object expiresInDays) {
        this.expiresInDays = expiresInDays;
        return this;
    }

    @JsonProperty("buyUrl")
    public String getBuyUrl() {
        return buyUrl;
    }

    @JsonProperty("buyUrl")
    public void setBuyUrl(String buyUrl) {
        this.buyUrl = buyUrl;
    }

    public Option withBuyUrl(String buyUrl) {
        this.buyUrl = buyUrl;
        return this;
    }

    @JsonProperty("maximumPurchaseQuantity")
    public int getMaximumPurchaseQuantity() {
        return maximumPurchaseQuantity;
    }

    @JsonProperty("maximumPurchaseQuantity")
    public void setMaximumPurchaseQuantity(int maximumPurchaseQuantity) {
        this.maximumPurchaseQuantity = maximumPurchaseQuantity;
    }

    public Option withMaximumPurchaseQuantity(int maximumPurchaseQuantity) {
        this.maximumPurchaseQuantity = maximumPurchaseQuantity;
        return this;
    }

    @JsonProperty("discountPercent")
    public int getDiscountPercent() {
        return discountPercent;
    }

    @JsonProperty("discountPercent")
    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Option withDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
        return this;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    public Option withId(int id) {
        this.id = id;
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

    public Option withSoldQuantityMessage(String soldQuantityMessage) {
        this.soldQuantityMessage = soldQuantityMessage;
        return this;
    }

    @JsonProperty("externalUrl")
    public Object getExternalUrl() {
        return externalUrl;
    }

    @JsonProperty("externalUrl")
    public void setExternalUrl(Object externalUrl) {
        this.externalUrl = externalUrl;
    }

    public Option withExternalUrl(Object externalUrl) {
        this.externalUrl = externalUrl;
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
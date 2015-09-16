
package com.main.interconnection.deals.grouponLat;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "linkText",
    "reviewsCount",
    "url",
    "id",
    "rating"
})
public class Rating {

    @JsonProperty("linkText")
    private String linkText;
    @JsonProperty("reviewsCount")
    private int reviewsCount;
    @JsonProperty("url")
    private String url;
    @JsonProperty("id")
    private String id;
    @JsonProperty("rating")
    private float rating;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("linkText")
    public String getLinkText() {
        return linkText;
    }

    @JsonProperty("linkText")
    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public Rating withLinkText(String linkText) {
        this.linkText = linkText;
        return this;
    }

    @JsonProperty("reviewsCount")
    public int getReviewsCount() {
        return reviewsCount;
    }

    @JsonProperty("reviewsCount")
    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public Rating withReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
        return this;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    public Rating withUrl(String url) {
        this.url = url;
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

    public Rating withId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("rating")
    public float getRating() {
        return rating;
    }

    @JsonProperty("rating")
    public void setRating(float rating) {
        this.rating = rating;
    }

    public Rating withRating(float rating) {
        this.rating = rating;
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


package com.main.interconnection.deals.grouponLat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    "websiteUrl",
    "facebookUrl",
    "uuid",
    "id",
    "twitterUrl",
    "name",
    "ratings"
})
public class Merchant {

    @JsonProperty("websiteUrl")
    private String websiteUrl;
    @JsonProperty("facebookUrl")
    private Object facebookUrl;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("id")
    private String id;
    @JsonProperty("twitterUrl")
    private Object twitterUrl;
    @JsonProperty("name")
    private String name;
    @JsonProperty("ratings")
    private List<Rating> ratings = new ArrayList<Rating>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("websiteUrl")
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    @JsonProperty("websiteUrl")
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public Merchant withWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
        return this;
    }

    @JsonProperty("facebookUrl")
    public Object getFacebookUrl() {
        return facebookUrl;
    }

    @JsonProperty("facebookUrl")
    public void setFacebookUrl(Object facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public Merchant withFacebookUrl(Object facebookUrl) {
        this.facebookUrl = facebookUrl;
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

    public Merchant withUuid(String uuid) {
        this.uuid = uuid;
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

    public Merchant withId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("twitterUrl")
    public Object getTwitterUrl() {
        return twitterUrl;
    }

    @JsonProperty("twitterUrl")
    public void setTwitterUrl(Object twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public Merchant withTwitterUrl(Object twitterUrl) {
        this.twitterUrl = twitterUrl;
        return this;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Merchant withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("ratings")
    public List<Rating> getRatings() {
        return ratings;
    }

    @JsonProperty("ratings")
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public Merchant withRatings(List<Rating> ratings) {
        this.ratings = ratings;
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

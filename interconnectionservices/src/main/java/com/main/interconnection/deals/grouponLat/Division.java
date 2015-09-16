
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
    "lat",
    "lng",
    "id",
    "timezoneIdentifier",
    "name",
    "timezoneOffsetInSeconds",
    "timezone"
})
public class Division {

    @JsonProperty("lat")
    private float lat;
    @JsonProperty("lng")
    private float lng;
    @JsonProperty("id")
    private String id;
    @JsonProperty("timezoneIdentifier")
    private String timezoneIdentifier;
    @JsonProperty("name")
    private String name;
    @JsonProperty("timezoneOffsetInSeconds")
    private int timezoneOffsetInSeconds;
    @JsonProperty("timezone")
    private String timezone;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("lat")
    public float getLat() {
        return lat;
    }

    @JsonProperty("lat")
    public void setLat(float lat) {
        this.lat = lat;
    }

    public Division withLat(float lat) {
        this.lat = lat;
        return this;
    }

    @JsonProperty("lng")
    public float getLng() {
        return lng;
    }

    @JsonProperty("lng")
    public void setLng(float lng) {
        this.lng = lng;
    }

    public Division withLng(float lng) {
        this.lng = lng;
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

    public Division withId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("timezoneIdentifier")
    public String getTimezoneIdentifier() {
        return timezoneIdentifier;
    }

    @JsonProperty("timezoneIdentifier")
    public void setTimezoneIdentifier(String timezoneIdentifier) {
        this.timezoneIdentifier = timezoneIdentifier;
    }

    public Division withTimezoneIdentifier(String timezoneIdentifier) {
        this.timezoneIdentifier = timezoneIdentifier;
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

    public Division withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("timezoneOffsetInSeconds")
    public int getTimezoneOffsetInSeconds() {
        return timezoneOffsetInSeconds;
    }

    @JsonProperty("timezoneOffsetInSeconds")
    public void setTimezoneOffsetInSeconds(int timezoneOffsetInSeconds) {
        this.timezoneOffsetInSeconds = timezoneOffsetInSeconds;
    }

    public Division withTimezoneOffsetInSeconds(int timezoneOffsetInSeconds) {
        this.timezoneOffsetInSeconds = timezoneOffsetInSeconds;
        return this;
    }

    @JsonProperty("timezone")
    public String getTimezone() {
        return timezone;
    }

    @JsonProperty("timezone")
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Division withTimezone(String timezone) {
        this.timezone = timezone;
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

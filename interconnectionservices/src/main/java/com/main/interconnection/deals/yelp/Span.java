
package com.main.interconnection.deals.yelp;

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
    "latitude_delta",
    "longitude_delta"
})
public class Span {

    @JsonProperty("latitude_delta")
    private float latitude_delta;
    @JsonProperty("longitude_delta")
    private float longitude_delta;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("latitude_delta")
    public float getLatitude_delta() {
        return latitude_delta;
    }

    @JsonProperty("latitude_delta")
    public void setLatitude_delta(float latitude_delta) {
        this.latitude_delta = latitude_delta;
    }

    public Span withLatitude_delta(float latitude_delta) {
        this.latitude_delta = latitude_delta;
        return this;
    }

    @JsonProperty("longitude_delta")
    public float getLongitude_delta() {
        return longitude_delta;
    }

    @JsonProperty("longitude_delta")
    public void setLongitude_delta(float longitude_delta) {
        this.longitude_delta = longitude_delta;
    }

    public Span withLongitude_delta(float longitude_delta) {
        this.longitude_delta = longitude_delta;
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

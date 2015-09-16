
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
    "latitude",
    "longitude"
})
public class Center {

    @JsonProperty("latitude")
    private float latitude;
    @JsonProperty("longitude")
    private float longitude;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("latitude")
    public float getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public Center withLatitude(float latitude) {
        this.latitude = latitude;
        return this;
    }

    @JsonProperty("longitude")
    public float getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public Center withLongitude(float longitude) {
        this.longitude = longitude;
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

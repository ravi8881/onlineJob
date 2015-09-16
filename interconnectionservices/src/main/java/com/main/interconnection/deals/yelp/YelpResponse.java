
package com.main.interconnection.deals.yelp;

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
    "region",
    "total",
    "businesses"
})
public class YelpResponse {

    @JsonProperty("region")
    private Region region;
    @JsonProperty("total")
    private int total;
    @JsonProperty("businesses")
    private List<Business> businesses = new ArrayList<Business>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("region")
    public Region getRegion() {
        return region;
    }

    @JsonProperty("region")
    public void setRegion(Region region) {
        this.region = region;
    }

    public YelpResponse withRegion(Region region) {
        this.region = region;
        return this;
    }

    @JsonProperty("total")
    public int getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(int total) {
        this.total = total;
    }

    public YelpResponse withTotal(int total) {
        this.total = total;
        return this;
    }

    @JsonProperty("businesses")
    public List<Business> getBusinesses() {
        return businesses;
    }

    @JsonProperty("businesses")
    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    public YelpResponse withBusinesses(List<Business> businesses) {
        this.businesses = businesses;
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

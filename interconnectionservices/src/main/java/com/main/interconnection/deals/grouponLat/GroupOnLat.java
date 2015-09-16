
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
    "deals",
    "facets",
    "pagination"
})

@JsonIgnoreProperties(ignoreUnknown=true)
public class GroupOnLat {

    @JsonProperty("deals")
    private List<Deal> deals = new ArrayList<Deal>();
    @JsonProperty("facets")
    private List<Object> facets = new ArrayList<Object>();
    @JsonProperty("pagination")
    private Pagination pagination;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("deals")
    public List<Deal> getDeals() {
        return deals;
    }

    @JsonProperty("deals")
    public void setDeals(List<Deal> deals) {
        this.deals = deals;
    }

    public GroupOnLat withDeals(List<Deal> deals) {
        this.deals = deals;
        return this;
    }

    @JsonProperty("facets")
    public List<Object> getFacets() {
        return facets;
    }

    @JsonProperty("facets")
    public void setFacets(List<Object> facets) {
        this.facets = facets;
    }

    public GroupOnLat withFacets(List<Object> facets) {
        this.facets = facets;
        return this;
    }

    @JsonProperty("pagination")
    public Pagination getPagination() {
        return pagination;
    }

    @JsonProperty("pagination")
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public GroupOnLat withPagination(Pagination pagination) {
        this.pagination = pagination;
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

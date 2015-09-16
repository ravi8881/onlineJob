
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
    "offset",
    "count"
})
public class Pagination {

    @JsonProperty("offset")
    private int offset;
    @JsonProperty("count")
    private int count;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("offset")
    public int getOffset() {
        return offset;
    }

    @JsonProperty("offset")
    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Pagination withOffset(int offset) {
        this.offset = offset;
        return this;
    }

    @JsonProperty("count")
    public int getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(int count) {
        this.count = count;
    }

    public Pagination withCount(int count) {
        this.count = count;
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

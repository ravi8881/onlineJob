
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
    "description",
    "priceQualifier"
})
public class SpecificAttributes {

    @JsonProperty("description")
    private String description;
    @JsonProperty("priceQualifier")
    private String priceQualifier;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public SpecificAttributes withDescription(String description) {
        this.description = description;
        return this;
    }

    @JsonProperty("priceQualifier")
    public String getPriceQualifier() {
        return priceQualifier;
    }

    @JsonProperty("priceQualifier")
    public void setPriceQualifier(String priceQualifier) {
        this.priceQualifier = priceQualifier;
    }

    public SpecificAttributes withPriceQualifier(String priceQualifier) {
        this.priceQualifier = priceQualifier;
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

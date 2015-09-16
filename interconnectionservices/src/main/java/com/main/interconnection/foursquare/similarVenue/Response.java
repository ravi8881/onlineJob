
package com.main.interconnection.foursquare.similarVenue;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "similarVenues"
})
public class Response {

    @JsonProperty("similarVenues")
    private SimilarVenues similarVenues;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The similarVenues
     */
    @JsonProperty("similarVenues")
    public SimilarVenues getSimilarVenues() {
        return similarVenues;
    }

    /**
     * 
     * @param similarVenues
     *     The similarVenues
     */
    @JsonProperty("similarVenues")
    public void setSimilarVenues(SimilarVenues similarVenues) {
        this.similarVenues = similarVenues;
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

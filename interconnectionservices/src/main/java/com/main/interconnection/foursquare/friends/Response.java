
package com.main.interconnection.foursquare.friends;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "friends",
    "checksum"
})

@JsonIgnoreProperties(ignoreUnknown=true)
public class Response {

    @JsonProperty("friends")
    private Friends friends;
    @JsonProperty("checksum")
    private String checksum;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The friends
     */
    @JsonProperty("friends")
    public Friends getFriends() {
        return friends;
    }

    /**
     * 
     * @param friends
     *     The friends
     */
    @JsonProperty("friends")
    public void setFriends(Friends friends) {
        this.friends = friends;
    }

    /**
     * 
     * @return
     *     The checksum
     */
    @JsonProperty("checksum")
    public String getChecksum() {
        return checksum;
    }

    /**
     * 
     * @param checksum
     *     The checksum
     */
    @JsonProperty("checksum")
    public void setChecksum(String checksum) {
        this.checksum = checksum;
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

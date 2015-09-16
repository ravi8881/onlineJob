
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
    "type",
    "label",
    "anchor",
    "url",
    "mobileUrl"
})
public class Menu {

    @JsonProperty("type")
    private String type;
    @JsonProperty("label")
    private String label;
    @JsonProperty("anchor")
    private String anchor;
    @JsonProperty("url")
    private String url;
    @JsonProperty("mobileUrl")
    private String mobileUrl;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The label
     */
    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    /**
     * 
     * @param label
     *     The label
     */
    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * 
     * @return
     *     The anchor
     */
    @JsonProperty("anchor")
    public String getAnchor() {
        return anchor;
    }

    /**
     * 
     * @param anchor
     *     The anchor
     */
    @JsonProperty("anchor")
    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    /**
     * 
     * @return
     *     The url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The mobileUrl
     */
    @JsonProperty("mobileUrl")
    public String getMobileUrl() {
        return mobileUrl;
    }

    /**
     * 
     * @param mobileUrl
     *     The mobileUrl
     */
    @JsonProperty("mobileUrl")
    public void setMobileUrl(String mobileUrl) {
        this.mobileUrl = mobileUrl;
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

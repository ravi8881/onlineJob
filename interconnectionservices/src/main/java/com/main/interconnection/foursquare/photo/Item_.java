
package com.main.interconnection.foursquare.photo;

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
    "id",
    "createdAt",
    "source",
    "prefix",
    "suffix",
    "width",
    "height",
    "user",
    "visibility"
})
public class Item_ {

    @JsonProperty("id")
    private String id;
    @JsonProperty("createdAt")
    private int createdAt;
    @JsonProperty("source")
    private Source source;
    @JsonProperty("prefix")
    private String prefix;
    @JsonProperty("suffix")
    private String suffix;
    @JsonProperty("width")
    private int width;
    @JsonProperty("height")
    private int height;
    @JsonProperty("user")
    private User user;
    @JsonProperty("visibility")
    private String visibility;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The createdAt
     */
    @JsonProperty("createdAt")
    public int getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     *     The createdAt
     */
    @JsonProperty("createdAt")
    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 
     * @return
     *     The source
     */
    @JsonProperty("source")
    public Source getSource() {
        return source;
    }

    /**
     * 
     * @param source
     *     The source
     */
    @JsonProperty("source")
    public void setSource(Source source) {
        this.source = source;
    }

    /**
     * 
     * @return
     *     The prefix
     */
    @JsonProperty("prefix")
    public String getPrefix() {
        return prefix;
    }

    /**
     * 
     * @param prefix
     *     The prefix
     */
    @JsonProperty("prefix")
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 
     * @return
     *     The suffix
     */
    @JsonProperty("suffix")
    public String getSuffix() {
        return suffix;
    }

    /**
     * 
     * @param suffix
     *     The suffix
     */
    @JsonProperty("suffix")
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * 
     * @return
     *     The width
     */
    @JsonProperty("width")
    public int getWidth() {
        return width;
    }

    /**
     * 
     * @param width
     *     The width
     */
    @JsonProperty("width")
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * 
     * @return
     *     The height
     */
    @JsonProperty("height")
    public int getHeight() {
        return height;
    }

    /**
     * 
     * @param height
     *     The height
     */
    @JsonProperty("height")
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * 
     * @return
     *     The user
     */
    @JsonProperty("user")
    public User getUser() {
        return user;
    }

    /**
     * 
     * @param user
     *     The user
     */
    @JsonProperty("user")
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 
     * @return
     *     The visibility
     */
    @JsonProperty("visibility")
    public String getVisibility() {
        return visibility;
    }

    /**
     * 
     * @param visibility
     *     The visibility
     */
    @JsonProperty("visibility")
    public void setVisibility(String visibility) {
        this.visibility = visibility;
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

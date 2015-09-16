
package com.main.interconnection.foursquare.venue;

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
    "id",
    "name",
    "pluralName",
    "shortName",
    "icon",
    "primary"
})
@JsonIgnoreProperties(ignoreUnknown=true)
public class Category {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("pluralName")
    private String pluralName;
    @JsonProperty("shortName")
    private String shortName;
    @JsonProperty("icon")
    private Icon icon;
    @JsonProperty("primary")
    private Boolean primary;
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
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The pluralName
     */
    @JsonProperty("pluralName")
    public String getPluralName() {
        return pluralName;
    }

    /**
     * 
     * @param pluralName
     *     The pluralName
     */
    @JsonProperty("pluralName")
    public void setPluralName(String pluralName) {
        this.pluralName = pluralName;
    }

    /**
     * 
     * @return
     *     The shortName
     */
    @JsonProperty("shortName")
    public String getShortName() {
        return shortName;
    }

    /**
     * 
     * @param shortName
     *     The shortName
     */
    @JsonProperty("shortName")
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * 
     * @return
     *     The icon
     */
    @JsonProperty("icon")
    public Icon getIcon() {
        return icon;
    }

    /**
     * 
     * @param icon
     *     The icon
     */
    @JsonProperty("icon")
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    /**
     * 
     * @return
     *     The primary
     */
    @JsonProperty("primary")
    public Boolean getPrimary() {
        return primary;
    }

    /**
     * 
     * @param primary
     *     The primary
     */
    @JsonProperty("primary")
    public void setPrimary(Boolean primary) {
        this.primary = primary;
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

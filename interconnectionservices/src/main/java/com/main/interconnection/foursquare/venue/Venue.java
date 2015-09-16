
package com.main.interconnection.foursquare.venue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    "location",
    "categories",
    "verified",
    "stats",
    "specials",
    "hereNow",
    "referralId",
    "hasMenu",
    "venuePage",
    "storeId"
})
@JsonIgnoreProperties(ignoreUnknown=true)
public class Venue {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("location")
    private Location location;
    @JsonProperty("categories")
    private List<Category> categories = new ArrayList<Category>();
    @JsonProperty("verified")
    private Boolean verified;
    @JsonProperty("stats")
    private Stats stats;
    @JsonProperty("specials")
    private Specials specials;
    @JsonProperty("hereNow")
    private HereNow hereNow;
    @JsonProperty("referralId")
    private String referralId;
    @JsonProperty("hasMenu")
    private Boolean hasMenu;
    @JsonProperty("venuePage")
    private VenuePage venuePage;
    @JsonProperty("storeId")
    private String storeId;
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
     *     The location
     */
    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    /**
     * 
     * @param location
     *     The location
     */
    @JsonProperty("location")
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * 
     * @return
     *     The categories
     */
    @JsonProperty("categories")
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * 
     * @param categories
     *     The categories
     */
    @JsonProperty("categories")
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * 
     * @return
     *     The verified
     */
    @JsonProperty("verified")
    public Boolean getVerified() {
        return verified;
    }

    /**
     * 
     * @param verified
     *     The verified
     */
    @JsonProperty("verified")
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    /**
     * 
     * @return
     *     The stats
     */
    @JsonProperty("stats")
    public Stats getStats() {
        return stats;
    }

    /**
     * 
     * @param stats
     *     The stats
     */
    @JsonProperty("stats")
    public void setStats(Stats stats) {
        this.stats = stats;
    }

    /**
     * 
     * @return
     *     The specials
     */
    @JsonProperty("specials")
    public Specials getSpecials() {
        return specials;
    }

    /**
     * 
     * @param specials
     *     The specials
     */
    @JsonProperty("specials")
    public void setSpecials(Specials specials) {
        this.specials = specials;
    }

    /**
     * 
     * @return
     *     The hereNow
     */
    @JsonProperty("hereNow")
    public HereNow getHereNow() {
        return hereNow;
    }

    /**
     * 
     * @param hereNow
     *     The hereNow
     */
    @JsonProperty("hereNow")
    public void setHereNow(HereNow hereNow) {
        this.hereNow = hereNow;
    }

    /**
     * 
     * @return
     *     The referralId
     */
    @JsonProperty("referralId")
    public String getReferralId() {
        return referralId;
    }

    /**
     * 
     * @param referralId
     *     The referralId
     */
    @JsonProperty("referralId")
    public void setReferralId(String referralId) {
        this.referralId = referralId;
    }

    /**
     * 
     * @return
     *     The hasMenu
     */
    @JsonProperty("hasMenu")
    public Boolean getHasMenu() {
        return hasMenu;
    }

    /**
     * 
     * @param hasMenu
     *     The hasMenu
     */
    @JsonProperty("hasMenu")
    public void setHasMenu(Boolean hasMenu) {
        this.hasMenu = hasMenu;
    }

    /**
     * 
     * @return
     *     The venuePage
     */
    @JsonProperty("venuePage")
    public VenuePage getVenuePage() {
        return venuePage;
    }

    /**
     * 
     * @param venuePage
     *     The venuePage
     */
    @JsonProperty("venuePage")
    public void setVenuePage(VenuePage venuePage) {
        this.venuePage = venuePage;
    }

    /**
     * 
     * @return
     *     The storeId
     */
    @JsonProperty("storeId")
    public String getStoreId() {
        return storeId;
    }

    /**
     * 
     * @param storeId
     *     The storeId
     */
    @JsonProperty("storeId")
    public void setStoreId(String storeId) {
        this.storeId = storeId;
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


package com.main.interconnection.foursquare.similarVenue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    "name",
    "contact",
    "location",
    "categories",
    "verified",
    "stats",
    "url",
    "hasMenu",
    "menu",
    "storeId"
})
public class Item_ {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("contact")
    private Contact contact;
    @JsonProperty("location")
    private Location location;
    @JsonProperty("categories")
    private List<Category> categories = new ArrayList<Category>();
    @JsonProperty("verified")
    private boolean verified;
    @JsonProperty("stats")
    private Stats stats;
    @JsonProperty("url")
    private String url;
    @JsonProperty("hasMenu")
    private boolean hasMenu;
    @JsonProperty("menu")
    private Menu menu;
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
     *     The contact
     */
    @JsonProperty("contact")
    public Contact getContact() {
        return contact;
    }

    /**
     * 
     * @param contact
     *     The contact
     */
    @JsonProperty("contact")
    public void setContact(Contact contact) {
        this.contact = contact;
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
    public boolean isVerified() {
        return verified;
    }

    /**
     * 
     * @param verified
     *     The verified
     */
    @JsonProperty("verified")
    public void setVerified(boolean verified) {
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
     *     The hasMenu
     */
    @JsonProperty("hasMenu")
    public boolean isHasMenu() {
        return hasMenu;
    }

    /**
     * 
     * @param hasMenu
     *     The hasMenu
     */
    @JsonProperty("hasMenu")
    public void setHasMenu(boolean hasMenu) {
        this.hasMenu = hasMenu;
    }

    /**
     * 
     * @return
     *     The menu
     */
    @JsonProperty("menu")
    public Menu getMenu() {
        return menu;
    }

    /**
     * 
     * @param menu
     *     The menu
     */
    @JsonProperty("menu")
    public void setMenu(Menu menu) {
        this.menu = menu;
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

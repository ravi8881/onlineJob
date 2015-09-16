
package com.main.interconnection.clientBo;

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
    "kind",
    "etag",
    "title",
    "nextPageToken",
    "totalItems",
    "items"
})
public class GoogleFriends {

    @JsonProperty("kind")
    private String kind;
    @JsonProperty("etag")
    private String etag;
    @JsonProperty("title")
    private String title;
    @JsonProperty("nextPageToken")
    private String nextPageToken;
    @JsonProperty("totalItems")
    private int totalItems;
    @JsonProperty("items")
    private List<Item> items = new ArrayList<Item>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The kind
     */
    @JsonProperty("kind")
    public String getKind() {
        return kind;
    }

    /**
     * 
     * @param kind
     *     The kind
     */
    @JsonProperty("kind")
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * 
     * @return
     *     The etag
     */
    @JsonProperty("etag")
    public String getEtag() {
        return etag;
    }

    /**
     * 
     * @param etag
     *     The etag
     */
    @JsonProperty("etag")
    public void setEtag(String etag) {
        this.etag = etag;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The nextPageToken
     */
    @JsonProperty("nextPageToken")
    public String getNextPageToken() {
        return nextPageToken;
    }

    /**
     * 
     * @param nextPageToken
     *     The nextPageToken
     */
    @JsonProperty("nextPageToken")
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    /**
     * 
     * @return
     *     The totalItems
     */
    @JsonProperty("totalItems")
    public int getTotalItems() {
        return totalItems;
    }

    /**
     * 
     * @param totalItems
     *     The totalItems
     */
    @JsonProperty("totalItems")
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    /**
     * 
     * @return
     *     The items
     */
    @JsonProperty("items")
    public List<Item> getItems() {
        return items;
    }

    /**
     * 
     * @param items
     *     The items
     */
    @JsonProperty("items")
    public void setItems(List<Item> items) {
        this.items = items;
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

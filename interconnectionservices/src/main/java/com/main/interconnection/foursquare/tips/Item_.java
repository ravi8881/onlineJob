
package com.main.interconnection.foursquare.tips;

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
    "createdAt",
    "text",
    "type",
    "canonicalUrl",
    "likes",
    "like",
    "logView",
    "todo",
    "user",
    "photo",
    "photourl",
    "editedAt",
    "startAt",
    "endAt",
    "lang"
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item_ {

    @JsonProperty("id")
    private String id;
    @JsonProperty("createdAt")
    private int createdAt;
    @JsonProperty("text")
    private String text;
    @JsonProperty("type")
    private String type;
    @JsonProperty("canonicalUrl")
    private String canonicalUrl;
    @JsonProperty("likes")
    private Likes likes;
    @JsonProperty("like")
    private boolean like;
    @JsonProperty("logView")
    private boolean logView;
    @JsonProperty("todo")
    private Todo todo;
    @JsonProperty("user")
    private User user;
    @JsonProperty("photo")
    private Photo_ photo;
    @JsonProperty("photourl")
    private String photourl;
    @JsonProperty("editedAt")
    private int editedAt;
    @JsonProperty("startAt")
    private int startAt;
    @JsonProperty("endAt")
    private int endAt;
    @JsonProperty("lang")
    private String lang;
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
     *     The text
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     * 
     * @param text
     *     The text
     */
    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

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
     *     The canonicalUrl
     */
    @JsonProperty("canonicalUrl")
    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    /**
     * 
     * @param canonicalUrl
     *     The canonicalUrl
     */
    @JsonProperty("canonicalUrl")
    public void setCanonicalUrl(String canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }

    /**
     * 
     * @return
     *     The likes
     */
    @JsonProperty("likes")
    public Likes getLikes() {
        return likes;
    }

    /**
     * 
     * @param likes
     *     The likes
     */
    @JsonProperty("likes")
    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    /**
     * 
     * @return
     *     The like
     */
    @JsonProperty("like")
    public boolean isLike() {
        return like;
    }

    /**
     * 
     * @param like
     *     The like
     */
    @JsonProperty("like")
    public void setLike(boolean like) {
        this.like = like;
    }

    /**
     * 
     * @return
     *     The logView
     */
    @JsonProperty("logView")
    public boolean isLogView() {
        return logView;
    }

    /**
     * 
     * @param logView
     *     The logView
     */
    @JsonProperty("logView")
    public void setLogView(boolean logView) {
        this.logView = logView;
    }

    /**
     * 
     * @return
     *     The todo
     */
    @JsonProperty("todo")
    public Todo getTodo() {
        return todo;
    }

    /**
     * 
     * @param todo
     *     The todo
     */
    @JsonProperty("todo")
    public void setTodo(Todo todo) {
        this.todo = todo;
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
     *     The photo
     */
    @JsonProperty("photo")
    public Photo_ getPhoto() {
        return photo;
    }

    /**
     * 
     * @param photo
     *     The photo
     */
    @JsonProperty("photo")
    public void setPhoto(Photo_ photo) {
        this.photo = photo;
    }

    /**
     * 
     * @return
     *     The photourl
     */
    @JsonProperty("photourl")
    public String getPhotourl() {
        return photourl;
    }

    /**
     * 
     * @param photourl
     *     The photourl
     */
    @JsonProperty("photourl")
    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    /**
     * 
     * @return
     *     The editedAt
     */
    @JsonProperty("editedAt")
    public int getEditedAt() {
        return editedAt;
    }

    /**
     * 
     * @param editedAt
     *     The editedAt
     */
    @JsonProperty("editedAt")
    public void setEditedAt(int editedAt) {
        this.editedAt = editedAt;
    }

    /**
     * 
     * @return
     *     The startAt
     */
    @JsonProperty("startAt")
    public int getStartAt() {
        return startAt;
    }

    /**
     * 
     * @param startAt
     *     The startAt
     */
    @JsonProperty("startAt")
    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    /**
     * 
     * @return
     *     The endAt
     */
    @JsonProperty("endAt")
    public int getEndAt() {
        return endAt;
    }

    /**
     * 
     * @param endAt
     *     The endAt
     */
    @JsonProperty("endAt")
    public void setEndAt(int endAt) {
        this.endAt = endAt;
    }

    /**
     * 
     * @return
     *     The lang
     */
    @JsonProperty("lang")
    public String getLang() {
        return lang;
    }

    /**
     * 
     * @param lang
     *     The lang
     */
    @JsonProperty("lang")
    public void setLang(String lang) {
        this.lang = lang;
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

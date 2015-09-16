
package com.main.interconnection.foursquare.friends;

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
    "firstName",
    "lastName",
    "gender",
    "relationship",
    "photo",
    "tips",
    "lists",
    "homeCity",
    "bio",
    "contact"
})
public class Item_ {

    @JsonProperty("id")
    private String id;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("relationship")
    private String relationship;
    @JsonProperty("photo")
    private Photo photo;
    @JsonProperty("tips")
    private Tips tips;
    @JsonProperty("lists")
    private Lists lists;
    @JsonProperty("homeCity")
    private String homeCity;
    @JsonProperty("bio")
    private String bio;
    @JsonProperty("contact")
    private Contact contact;
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
     *     The firstName
     */
    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    /**
     * 
     * @param firstName
     *     The firstName
     */
    @JsonProperty("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * 
     * @return
     *     The lastName
     */
    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    /**
     * 
     * @param lastName
     *     The lastName
     */
    @JsonProperty("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * 
     * @return
     *     The gender
     */
    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    /**
     * 
     * @param gender
     *     The gender
     */
    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 
     * @return
     *     The relationship
     */
    @JsonProperty("relationship")
    public String getRelationship() {
        return relationship;
    }

    /**
     * 
     * @param relationship
     *     The relationship
     */
    @JsonProperty("relationship")
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    /**
     * 
     * @return
     *     The photo
     */
    @JsonProperty("photo")
    public Photo getPhoto() {
        return photo;
    }

    /**
     * 
     * @param photo
     *     The photo
     */
    @JsonProperty("photo")
    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    /**
     * 
     * @return
     *     The tips
     */
    @JsonProperty("tips")
    public Tips getTips() {
        return tips;
    }

    /**
     * 
     * @param tips
     *     The tips
     */
    @JsonProperty("tips")
    public void setTips(Tips tips) {
        this.tips = tips;
    }

    /**
     * 
     * @return
     *     The lists
     */
    @JsonProperty("lists")
    public Lists getLists() {
        return lists;
    }

    /**
     * 
     * @param lists
     *     The lists
     */
    @JsonProperty("lists")
    public void setLists(Lists lists) {
        this.lists = lists;
    }

    /**
     * 
     * @return
     *     The homeCity
     */
    @JsonProperty("homeCity")
    public String getHomeCity() {
        return homeCity;
    }

    /**
     * 
     * @param homeCity
     *     The homeCity
     */
    @JsonProperty("homeCity")
    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    /**
     * 
     * @return
     *     The bio
     */
    @JsonProperty("bio")
    public String getBio() {
        return bio;
    }

    /**
     * 
     * @param bio
     *     The bio
     */
    @JsonProperty("bio")
    public void setBio(String bio) {
        this.bio = bio;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

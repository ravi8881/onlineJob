
package com.main.interconnection.deals.grouponLat;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "postalCode",
    "phoneNumber",
    "neighborhood",
    "streetAddress2",
    "lat",
    "city",
    "streetAddress1",
    "country",
    "state",
    "lng",
    "id",
    "name"
})
public class RedemptionLocation {

    @JsonProperty("postalCode")
    private String postalCode;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("neighborhood")
    private String neighborhood;
    @JsonProperty("streetAddress2")
    private String streetAddress2;
    @JsonProperty("lat")
    private float lat;
    @JsonProperty("city")
    private String city;
    @JsonProperty("streetAddress1")
    private String streetAddress1;
    @JsonProperty("country")
    private String country;
    @JsonProperty("state")
    private String state;
    @JsonProperty("lng")
    private float lng;
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("postalCode")
    public String getPostalCode() {
        return postalCode;
    }

    @JsonProperty("postalCode")
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public RedemptionLocation withPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    @JsonProperty("phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phoneNumber")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public RedemptionLocation withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @JsonProperty("neighborhood")
    public String getNeighborhood() {
        return neighborhood;
    }

    @JsonProperty("neighborhood")
    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public RedemptionLocation withNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
        return this;
    }

    @JsonProperty("streetAddress2")
    public String getStreetAddress2() {
        return streetAddress2;
    }

    @JsonProperty("streetAddress2")
    public void setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }

    public RedemptionLocation withStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
        return this;
    }

    @JsonProperty("lat")
    public float getLat() {
        return lat;
    }

    @JsonProperty("lat")
    public void setLat(float lat) {
        this.lat = lat;
    }

    public RedemptionLocation withLat(float lat) {
        this.lat = lat;
        return this;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    public RedemptionLocation withCity(String city) {
        this.city = city;
        return this;
    }

    @JsonProperty("streetAddress1")
    public String getStreetAddress1() {
        return streetAddress1;
    }

    @JsonProperty("streetAddress1")
    public void setStreetAddress1(String streetAddress1) {
        this.streetAddress1 = streetAddress1;
    }

    public RedemptionLocation withStreetAddress1(String streetAddress1) {
        this.streetAddress1 = streetAddress1;
        return this;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    public RedemptionLocation withCountry(String country) {
        this.country = country;
        return this;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    public RedemptionLocation withState(String state) {
        this.state = state;
        return this;
    }

    @JsonProperty("lng")
    public float getLng() {
        return lng;
    }

    @JsonProperty("lng")
    public void setLng(float lng) {
        this.lng = lng;
    }

    public RedemptionLocation withLng(float lng) {
        this.lng = lng;
        return this;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    public RedemptionLocation withId(int id) {
        this.id = id;
        return this;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public RedemptionLocation withName(String name) {
        this.name = name;
        return this;
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

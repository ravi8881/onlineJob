
package com.main.interconnection.foursquare.similarVenue;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "phone",
    "formattedPhone",
    "twitter",
    "facebook",
    "facebookUsername",
    "facebookName" 
})

public class Contact {

    @JsonProperty("phone")
    private String phone;
    @JsonProperty("formattedPhone")
    private String formattedPhone;
    @JsonProperty("twitter")
    private String twitter;
	@JsonIgnore
    @JsonProperty("facebook")
    private String facebook;
	@JsonIgnore
    @JsonProperty("facebookUsername")
    private String facebookUsername;
	@JsonIgnore
    @JsonProperty("facebookName")
    private String facebookName;

	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getFacebook() {
		return facebook;
	}
    @JsonProperty("facebook")
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getFacebookUsername() {
		return facebookUsername;
	}
    @JsonProperty("facebookUsername")
	public void setFacebookUsername(String facebookUsername) {
		this.facebookUsername = facebookUsername;
	}

	public String getFacebookName() {
		return facebookName;
	}
    @JsonProperty("facebookName")
	public void setFacebookName(String facebookName) {
		this.facebookName = facebookName;
	}

    /**
     * 
     * @return
     *     The phone
     */
    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    /**
     * 
     * @param phone
     *     The phone
     */
    @JsonProperty("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 
     * @return
     *     The formattedPhone
     */
    @JsonProperty("formattedPhone")
    public String getFormattedPhone() {
        return formattedPhone;
    }

    /**
     * 
     * @param formattedPhone
     *     The formattedPhone
     */
    @JsonProperty("formattedPhone")
    public void setFormattedPhone(String formattedPhone) {
        this.formattedPhone = formattedPhone;
    }

    /**
     * 
     * @return
     *     The twitter
     */
    @JsonProperty("twitter")
    public String getTwitter() {
        return twitter;
    }

    /**
     * 
     * @param twitter
     *     The twitter
     */
    @JsonProperty("twitter")
    public void setTwitter(String twitter) {
        this.twitter = twitter;
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

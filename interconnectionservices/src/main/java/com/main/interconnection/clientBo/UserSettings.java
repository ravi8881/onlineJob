package com.main.interconnection.clientBo;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;


@XmlRootElement(name = "userSettings")
@JsonWriteNullProperties(false)
public class UserSettings {

	// Properties for control user search by privacy settings
	
	private Integer profileOnOff;
	private Integer emailOnOff;
	private Integer nameOnOff;
	private Integer phNoOnOff;
	private Integer profilePicOnOff;
	
	// These are not in current sprint but added for saving only
	
	private Integer searchEngOnOff;
	private Integer frndChkInOnOff;
	
	
	public Integer getSearchEngOnOff() {
		return searchEngOnOff;
	}
	
	@JsonProperty("searchEngOnOff")
	public void setSearchEngOnOff(Integer searchEngOnOff) {
		this.searchEngOnOff = searchEngOnOff;
	}

	public Integer getFrndChkInOnOff() {
		return frndChkInOnOff;
	}
	@JsonProperty("frndChkInOnOff")
	public void setFrndChkInOnOff(Integer frndChkInOnOff) {
		this.frndChkInOnOff = frndChkInOnOff;
	}

	public Integer getProfileOnOff() {
		return profileOnOff;
	}
	@JsonProperty("profileOnOff")
	public void setProfileOnOff(Integer profileOnOff) {
		this.profileOnOff = profileOnOff;
	}

	public Integer getEmailOnOff() {
		return emailOnOff;
	}
	@JsonProperty("emailOnOff")
	public void setEmailOnOff(Integer emailOnOff) {
		this.emailOnOff = emailOnOff;
	}

	public Integer getNameOnOff() {
		return nameOnOff;
	}
	@JsonProperty("nameOnOff")
	public void setNameOnOff(Integer nameOnOff) {
		this.nameOnOff = nameOnOff;
	}

	public Integer getPhNoOnOff() {
		return phNoOnOff;
	}
	@JsonProperty("phNoOnOff")
	public void setPhNoOnOff(Integer phNoOnOff) {
		this.phNoOnOff = phNoOnOff;
	}

	public Integer getProfilePicOnOff() {
		return profilePicOnOff;
	}
	@JsonProperty("profilePicOnOff")
	public void setProfilePicOnOff(Integer profilePicOnOff) {
		this.profilePicOnOff = profilePicOnOff;
	}

}

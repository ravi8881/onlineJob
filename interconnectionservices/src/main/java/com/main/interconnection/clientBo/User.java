package com.main.interconnection.clientBo;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.main.interconnection.util.SolrDateUtil;

@XmlRootElement(name = "user")
@JsonWriteNullProperties(false)
public class User {

	private String userId;
	private String name;
	private String searchName;
	private String emailId;
	private String mobileNumber;
	private String password;
	private String ImageUrl;
	private String aboutUs;
	private String city;
	private String state;
	private Integer gendre;
	private String birthday;
	private Integer iagree;
	private Integer detailsOnEmail;
	private Integer emailverifyStatus;
	private Integer mobileverifyStatus;
	private String emailVerifyCode;
	private String mobileVerifyCode;
	private String securityCode;
	private String socialType;
	private String accessToken;
	private String isConnected;
	// Birthday in String
	private String birthDayStr;

	// Properties for Privacy settings
	private Integer profileOnOff;
	private Integer emailOnOff;
	private Integer nameOnOff;
	private Integer phNoOnOff;
	private Integer profilePicOnOff;
	private Integer searchEngOnOff;
	private Integer frndChkInOnOff;
	private Date createDate;
	private String createDateStr;
	private String loginTime;

	// Properties for attempt Login
	private Integer attemptNumber;
	//private Date attemptLoginDate;
	private String updatedSince="undefined";
	
	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = SolrDateUtil.convertSolrDateToSimpleDate(loginTime);
	}

	public String getSocialType() {
		return socialType;
	}

	public String getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public String getPassword() {
		return password;
	}

	public String getImageUrl() {
		return ImageUrl;
	}

	public String getAboutUs() {
		return aboutUs;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public Integer getGendre() {
		return gendre;
	}
	
	public String getBirthday() {
		return birthday;
	}

	public Integer getIagree() {
		return iagree;
	}

	public Integer getDetailsOnEmail() {
		return detailsOnEmail;
	}

	public Integer getEmailverifyStatus() {
		return emailverifyStatus;
	}

	public Integer getMobileverifyStatus() {
		return mobileverifyStatus;
	}

	public String getEmailVerifyCode() {
		return emailVerifyCode;
	}

	public String getMobileVerifyCode() {
		return mobileVerifyCode;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getIsConnected() {
		return isConnected;
	}

	@JsonSerialize(using = SolrDateUtil.class)
	public Date getCreateDate() {
		return createDate;
	}

	public String getSearchName() {
		return searchName;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	@JsonProperty("user_id")
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("emailId")
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@JsonProperty("mobileNo")
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@JsonProperty("password")
	public void setPassword(String password) {
		this.password = password;
	}

	@JsonProperty("imageUrl")
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}

	@JsonProperty("aboutUs")
	public void setAboutUs(String aboutUs) {
		this.aboutUs = aboutUs;
	}

	@JsonProperty("city")
	public void setCity(String city) {
		this.city = city;
	}

	@JsonProperty("state")
	public void setState(String state) {
		this.state = state;
	}

	@JsonProperty("gendre")
	public void setGendre(Integer gendre) {
		this.gendre = gendre;
	}

	@JsonProperty("birthDay")
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	@JsonProperty("iAgree")
	public void setIagree(Integer iagree) {
		this.iagree = iagree;
	}

	@JsonProperty("detailsOnEmail")
	public void setDetailsOnEmail(Integer detailsOnEmail) {
		this.detailsOnEmail = detailsOnEmail;
	}

	@JsonProperty("emailVerifyStatus")
	public void setEmailverifyStatus(Integer emailverifyStatus) {
		this.emailverifyStatus = emailverifyStatus;
	}

	@JsonProperty("mobileverifyStatus")
	public void setMobileverifyStatus(Integer mobileverifyStatus) {
		this.mobileverifyStatus = mobileverifyStatus;
	}

	@JsonProperty("emailVerifyCode")
	public void setEmailVerifyCode(String emailVerifyCode) {
		this.emailVerifyCode = emailVerifyCode;
	}

	@JsonProperty("mobileVerifyCode")
	public void setMobileVerifyCode(String mobileVerifyCode) {
		this.mobileVerifyCode = mobileVerifyCode;
	}

	@JsonProperty("tmpToken")
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	@JsonProperty("social_type")
	public void setSocialType(String socialType) {
		this.socialType = socialType;
	}

	@JsonProperty("access_token")
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@JsonProperty("is_connected")
	public void setIsConnected(String isConnected) {
		this.isConnected = isConnected;
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

	public String getBirthDayStr() {
		return birthDayStr;
	}

	@JsonProperty("birthDayStr")
	public void setBirthDayStr(String birthDayStr) {
		this.birthDayStr = birthDayStr;
	}

	@JsonProperty("createDate")
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
		//this.createDate = SolrDateUtil.convertSolrDateToDate(createDate);
	}

	@JsonProperty("searchName")
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public Integer getAttemptNumber() {
		return attemptNumber;
	}
	@JsonProperty("attemptNumber")
	public void setAttemptNumber(Integer attemptNumber) {
		this.attemptNumber = attemptNumber;
	}


		@JsonProperty("updatedSince")
		public String getUpdatedSince() {
			if(null!=this.createDate)
				return this.updatedSince=SolrDateUtil.userUpdateSincePdate(this.createDate.toString());
			else
				return this.updatedSince;
		}
		public void setUpdatedSince(String updatedSince) {
			this.updatedSince = updatedSince;
		}
	

	/*public Date getAttemptLoginDate() {
		return attemptLoginDate;
	}

	@JsonProperty("attemptLoginDate")
	public void setAttemptLoginDate(Date attemptLoginDate) {
		this.attemptLoginDate = attemptLoginDate;
	}*/
}

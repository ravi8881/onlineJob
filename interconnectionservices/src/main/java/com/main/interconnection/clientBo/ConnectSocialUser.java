package com.main.interconnection.clientBo;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


@Document(collection="connectSocialUser")
@JsonWriteNullProperties(false)
public class ConnectSocialUser {
    
	private String userId;
    private String socialId;
    private String accessToken;
    private String accessSecret;

	private String socialType;
    private Boolean connect;
    
    //Social user Informations
    
    private String userName;
    private String userCity;
    private Integer userGeneder;
    private String userPhoto;
    
    @DateTimeFormat(iso = ISO.DATE_TIME)
	private Date addedDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSocialType() {
		return socialType;
	}

	public void setSocialType(String socialType) {
		this.socialType = socialType;
	}

	public Boolean getConnect() {
		return connect;
	}

	public void setConnect(Boolean connect) {
		this.connect = connect;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserCity() {
		return userCity;
	}

	public void setUserCity(String userCity) {
		this.userCity = userCity;
	}

	public Integer getUserGeneder() {
		return userGeneder;
	}

	public void setUserGeneder(Integer userGeneder) {
		this.userGeneder = userGeneder;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}
    
	public String getAccessSecret() {
		return accessSecret;
	}

	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}
    
}

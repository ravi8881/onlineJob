package com.main.interconnection.clientBo;

import java.util.Set;
import org.codehaus.jackson.annotate.JsonProperty;

public class GoogleUser {

	private String name;
	private String given_name;
	private String family_name;
	private String password;
	private String gender;
	private String country;
	private String accesstoken;
	private String refreshToken;

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	private String email;
	private String link;
	private String birthday;
	private String locale;
	private boolean verified_email;
	private String authenticationtype;
	private Set<String> urls;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGiven_name() {
		return given_name;
	}

	@JsonProperty("firstName")
	public void setGiven_name(String given_name) {
		this.given_name = given_name;
	}

	public String getFamily_name() {
		return family_name;
	}

	@JsonProperty("lastName")
	public void setFamily_name(String family_name) {
		this.family_name = family_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	@JsonProperty("gender")
	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAccesstoken() {
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}

	public String getEmail() {
		return email;
	}

	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public boolean isVerified_email() {
		return verified_email;
	}

	public void setVerified_email(boolean verified_email) {
		this.verified_email = verified_email;
	}

	public String getAuthenticationtype() {
		return authenticationtype;
	}

	public void setAuthenticationtype(String authenticationtype) {
		this.authenticationtype = authenticationtype;
	}

	public Set<String> getUrls() {
		return urls;
	}

	public void setUrls(Set<String> urls) {
		this.urls = urls;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String id;
}
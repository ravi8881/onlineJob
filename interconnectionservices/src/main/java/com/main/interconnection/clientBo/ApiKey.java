package com.main.interconnection.clientBo;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

@XmlRootElement(name = "developer-keys")
@JsonWriteNullProperties(false)
public class ApiKey {

	private String developerKey;
	private String emailId;
	private String apikey;
	private String apiSalt;
	private Date addeddate;
	private Date modifydate;
	private String activationLink;
	
	public String getActivationLink() {
		return activationLink;
	}	
	public String getDeveloperKey() {
		return developerKey;
	}
	public String getEmailId() {
		return emailId;
	}
	public String getApikey() {
		return apikey;
	}
	public String getApiSalt() {
		return apiSalt;
	}
	public Date getAddeddate() {
		return addeddate;
	}
	public Date getModifydate() {
		return modifydate;
	}
	public void setDeveloperKey(String developerKey) {
		this.developerKey = developerKey;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	public void setApiSalt(String apiSalt) {
		this.apiSalt = apiSalt;
	}
	public void setAddeddate(Date addeddate) {
		this.addeddate = addeddate;
	}
	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}
	public void setActivationLink(String activationLink) {
		this.activationLink = activationLink;
	}
	}
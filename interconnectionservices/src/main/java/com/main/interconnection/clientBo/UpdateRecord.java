package com.main.interconnection.clientBo;

import java.util.Date;


import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;

@XmlRootElement(name = "update")
@JsonWriteNullProperties(false)

public class UpdateRecord {
private String feedId;
private Date addedDate;
private  HashMap<String, Object> map;

public String getFeedId() {
	return feedId;
}
@JsonProperty("feedId")
public void setFeedId(String feedId) {
	this.feedId = feedId;
}
public Date getAddedDate() {
	return addedDate;
}
@JsonProperty("addedDate")
public void setAddedDate(Date addedDate) {
	this.addedDate = addedDate;
}
public HashMap<String, Object> getMap() {
	return map;
}
@JsonProperty("map")
public void setMap(HashMap<String, Object> map) {
	this.map = map;
}









}

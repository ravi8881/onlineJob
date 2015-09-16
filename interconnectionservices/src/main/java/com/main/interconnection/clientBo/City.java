package com.main.interconnection.clientBo;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

@XmlRootElement(name = "city")
@JsonWriteNullProperties(false)
public class City {
	
	private int cityId;
	private int stateId;
	private String cityName;
	private int cityStatus;	
	private String latitde;
	private String longitude;
		
	public int getCityId() {
		return cityId;
	}
	public int getStateId() {
		return stateId;
	}
	public String getCityName() {
		return cityName;
	}
	public int getCityStatus() {
		return cityStatus;
	}
	public String getLatitde() {
		return latitde;
	}
	public String getLongitude() {
		return longitude;
	}	
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public void setStateId(int stateId) {
		this.stateId = stateId;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public void setCityStatus(int cityStatus) {
		this.cityStatus = cityStatus;
	}
	public void setLatitde(String latitde) {
		this.latitde = latitde;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}

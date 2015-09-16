package com.main.interconnection.clientBo;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

@XmlRootElement(name = "state")
@JsonWriteNullProperties(false)
public class State {

	private int stateId;
	private String stateName;
	private int stateStatus;
	
	private String latitde;
	private String longitude;
	
	
	
	public int getStateId() {
		return stateId;
	}
	public void setStateId(int stateId) {
		this.stateId = stateId;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public int getStateStatus() {
		return stateStatus;
	}
	public String getLatitde() {
		return latitde;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setStateStatus(int stateStatus) {
		this.stateStatus = stateStatus;
	}	
	public void setLatitde(String latitde) {
		this.latitde = latitde;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}

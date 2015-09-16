package com.main.interconnection.clientBo;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;


@XmlRootElement(name = "markNotification")
@JsonWriteNullProperties(false)
public class MarkNotification {
	private String updateId;
	private String userId;
	private String flag;
	private String id;
	private String addedDate;

	
	public String getUpdateId() {
		return updateId;
	}
	@JsonProperty("feedId")
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	public String getUserId() {
		return userId;
	}
	@JsonProperty("userId")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFlag() {
		return flag;
	}
	@JsonProperty("flag")
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getId() {
		return id;
	}
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}
	public String getAddedDate() {
		return addedDate;
	}
	@JsonProperty("addedDate")
	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}
	
}

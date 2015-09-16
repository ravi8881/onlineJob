package com.main.interconnection.clientBo;

import org.codehaus.jackson.annotate.JsonProperty;

public class Flag {
	private String typeName;
	private String typeValue;
	private String subType;
	private String userId;
	private String isFlagged;
	private String adminApproved;
	private String other;
	private String comment;
	public String getTypeName() {
		return typeName;
	}
	public String getTypeValue() {
		return typeValue;
	}
	public String getSubType() {
		return subType;
	}
	public String getUserId() {
		return userId;
	}
	public String getIsFlagged() {
		return isFlagged;
	}
	public String getAdminApproved() {
		return adminApproved;
	}
	public String getComment() {
		return comment;
	}
	public String getOther() {
		return other;
	}
	@JsonProperty("type_name")
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	@JsonProperty("type_value")
	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}
	@JsonProperty("sub_type")
	public void setSubType(String subType) {
		this.subType = subType;
	}
	@JsonProperty("user_id")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@JsonProperty("is_flagged")
	public void setIsFlagged(String isFlagged) {
		this.isFlagged = isFlagged;
	}
	public void setAdminApproved(String adminApproved) {
		this.adminApproved = adminApproved;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setOther(String other) {
		this.other = other;
	}
	
}

package com.main.interconnection.clientBo;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.main.interconnection.client.InterConnectionClient;
import com.main.interconnection.util.SolrDateUtil;

@XmlRootElement(name = "friend-request")
@JsonWriteNullProperties(false)
public class FriendRequest {

	InterConnectionClient interConnectionClient;
	
	private String requestId;
	private String toUser;
	private String fromUser;
	private String status;
	private String send_date;
	private String updated_date;
	private User users;
	private User usersFrom;
	private String updatedSince="";

	public String getRequestId() {
		return requestId;
	}
	public String getToUser() {
		return toUser;
	}
	public String getFromUser() {
		return fromUser;
	}
	public String getStatus() {
		return status;
	}
	
	public String getSend_date() {
		return send_date;
	}
	
	public String getUpdated_date() {
		return updated_date;
	}
	
	@JsonProperty("request_id")
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	@JsonProperty("to_user")
	public void setToUser(String toUser) {
		this.toUser = toUser;
/*		setUsers();*/
	}
	@JsonProperty("from_user")
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
/*		setUsersFrom();*/
	}
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}
	@JsonProperty("send_date")
	public void setSend_date(String send_date) {
		this.send_date = send_date;
	}
	@JsonProperty("req_updated_date")
	public void setUpdated_date(String updated_date) {
		this.updated_date = updated_date;
	}

	@JsonProperty("updatedSince")
	public String getUpdatedSince() {
		if(null!=this.send_date)
			return this.updatedSince=SolrDateUtil.addDateDifferenceFromSolrToServiceforUpdatedate(this.send_date.toString());
		else
			return this.updatedSince;
	}
	public void setUpdatedSince(String updatedSince) {
		this.updatedSince = updatedSince;
	}
	public User getUsers() {
		return users;
	}
	public void setUsers(User users) {
		this.users = users;
	}
	public User getUsersFrom() {
		return usersFrom;
	}
	public void setUsersFrom(User usersFrom) {
		this.usersFrom = usersFrom;
	}	
}

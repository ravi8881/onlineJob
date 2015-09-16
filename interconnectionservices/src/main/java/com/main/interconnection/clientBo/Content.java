package com.main.interconnection.clientBo;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.main.interconnection.util.SolrDateUtil;

@XmlRootElement(name = "content")
@JsonWriteNullProperties(false)
public class Content {
	private String id;
	private String userId;
	private String type;
	private Date added_date;
	private String contentType;
	private String contentId;
	public String getId() {
		return id;
	}
	public String getUserId() {
		return userId;
	}
	public String getType() {
		return type;
	}
	@JsonSerialize(using = SolrDateUtil.class)
	public Date getAdded_date() {
		return added_date;
	}
	public String getContentType() {
		return contentType;
	}
	public String getContentId() {
		return contentId;
	}
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}
	@JsonProperty("user_id")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}
	@JsonProperty("added_date")
	public void setAdded_date(Date added_date) {
		this.added_date = added_date;
	}
	@JsonProperty("content_type")
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	@JsonProperty("content_id")
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
}

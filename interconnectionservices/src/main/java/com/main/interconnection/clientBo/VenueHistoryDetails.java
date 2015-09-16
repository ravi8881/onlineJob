package com.main.interconnection.clientBo;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;
import com.main.interconnection.client.InterConnectionClient;

@XmlRootElement(name = "venue_history")
@JsonWriteNullProperties(false)
public class VenueHistoryDetails {
	private String id;
	private String venueId;
	private String userId;	
	private String historyAddedDate;
	
	public String getId() {
		return id;
	}
	public String getVenueId() {
		return venueId;
	}
	public String getUserId() {
		return userId;
	}
	public String getHistoryAddedDate() {
		return historyAddedDate;
	}
	
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}
	@JsonProperty("venue_id")
	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}
	@JsonProperty("user_id")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@JsonProperty("added_date")
	public void setHistoryAddedDate(String historyAddedDate) {
		this.historyAddedDate = historyAddedDate;
	}	
}
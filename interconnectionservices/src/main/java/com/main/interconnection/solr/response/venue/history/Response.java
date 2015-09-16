package com.main.interconnection.solr.response.venue.history;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;

import com.main.interconnection.clientBo.VenueHistoryDetails;


@JsonWriteNullProperties(false)
public class Response {

    public int numFound;
    public int start;
    private List<VenueHistoryDetails> docs = null;

    public Response() {
        super();
        // TODO Auto-generated constructor stub
    }

    public int getNumFound() {
        return numFound;
    }

    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

	public List<VenueHistoryDetails> getDocs() {
		return docs;
	}

	@JsonProperty("docs")
	public void setDocs(List<VenueHistoryDetails> docs) {
		this.docs = docs;
	}
}

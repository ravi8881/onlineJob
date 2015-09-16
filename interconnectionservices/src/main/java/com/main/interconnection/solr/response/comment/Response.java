package com.main.interconnection.solr.response.comment;

import java.util.ArrayList;
import java.util.List;

import com.main.interconnection.clientBo.VenueComment;




public class Response {

    public int numFound;
    public int start;
    private List<VenueComment> docs = new ArrayList<VenueComment>();

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

	public List<VenueComment> getDocs() {
		return docs;
	}

	public void setDocs(List<VenueComment> docs) {
		this.docs = docs;
	}
}

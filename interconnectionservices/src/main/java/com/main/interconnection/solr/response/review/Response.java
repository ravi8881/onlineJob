package com.main.interconnection.solr.response.review;

import java.util.ArrayList;
import java.util.List;

import com.main.interconnection.clientBo.VenueReview;



public class Response {

    public int numFound;
    public int start;
    private List<VenueReview> docs = new ArrayList<VenueReview>();

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

	public List<VenueReview> getDocs() {
		return docs;
	}

	public void setDocs(List<VenueReview> docs) {
		this.docs = docs;
	}
}

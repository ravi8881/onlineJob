package com.main.interconnection.solr.response.deals;

import java.util.ArrayList;
import java.util.List;

import com.main.interconnection.clientBo.Deals;

public class Response {

    public int numFound;
    public int start;
    private List<Deals> docs = new ArrayList<Deals>();

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

	public List<Deals> getDocs() {
		return docs;
	}

	public void setDocs(List<Deals> docs) {
		this.docs = docs;
	}	
}

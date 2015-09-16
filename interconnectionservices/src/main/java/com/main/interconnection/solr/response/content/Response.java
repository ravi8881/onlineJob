package com.main.interconnection.solr.response.content;

import java.util.ArrayList;
import java.util.List;

import com.main.interconnection.clientBo.Content;

public class Response {

    public int numFound;
    public int start;
    private List<Content> docs = new ArrayList<Content>();

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

	public List<Content> getDocs() {
		return docs;
	}

	public void setDocs(List<Content> docs) {
		this.docs = docs;
	}
}

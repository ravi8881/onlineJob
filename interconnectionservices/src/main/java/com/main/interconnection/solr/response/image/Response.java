package com.main.interconnection.solr.response.image;

import java.util.ArrayList;
import java.util.List;

import com.main.interconnection.clientBo.ImageHistory;

public class Response {

    public int numFound;
    public int start;
    private List<ImageHistory> docs = new ArrayList<ImageHistory>();

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

	public List<ImageHistory> getDocs() {
		return docs;
	}

	public void setDocs(List<ImageHistory> docs) {
		this.docs = docs;
	}
}

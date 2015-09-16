package com.main.interconnection.solr.response.binary;

import java.util.ArrayList;
import java.util.List;

import com.main.interconnection.clientBo.SolrBinaryData;

public class Response {

    public int numFound;
    public int start;
    private List<SolrBinaryData> docs = new ArrayList<SolrBinaryData>();

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

	public List<SolrBinaryData> getDocs() {
		return docs;
	}

	public void setDocs(List<SolrBinaryData> docs) {
		this.docs = docs;
	}
}

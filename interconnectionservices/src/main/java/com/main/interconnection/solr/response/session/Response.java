package com.main.interconnection.solr.response.session;

import java.util.ArrayList;
import java.util.List;

import com.main.interconnection.clientBo.SolrSession;




public class Response {

    public int numFound;
    public int start;
    private List<SolrSession> docs = new ArrayList<SolrSession>();

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

	public List<SolrSession> getDocs() {
		return docs;
	}

	public void setDocs(List<SolrSession> docs) {
		this.docs = docs;
	}
}

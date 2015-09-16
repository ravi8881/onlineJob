package com.main.interconnection.solr.response.updates;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

import com.main.interconnection.clientBo.Updates;

@JsonWriteNullProperties(false)
public class Response {

    public int numFound;
    public int start;
    private List<Updates> docs = new ArrayList<Updates>();

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

	public List<Updates> getDocs() {
		return docs;
	}

	public void setDocs(List<Updates> docs) {
		this.docs = docs;
	}
}

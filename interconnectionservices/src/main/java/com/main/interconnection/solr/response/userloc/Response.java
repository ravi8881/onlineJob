package com.main.interconnection.solr.response.userloc;

import java.util.List;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

import com.main.interconnection.clientBo.UserLoc;


@JsonWriteNullProperties(false)
public class Response {

    public int numFound;
    public int start;
    private List<UserLoc> docs = null;

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

	public List<UserLoc> getDocs() {
		return docs;
	}

	public void setDocs(List<UserLoc> docs) {
		this.docs = docs;
	}	
}

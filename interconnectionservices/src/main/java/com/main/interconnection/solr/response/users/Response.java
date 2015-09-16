package com.main.interconnection.solr.response.users;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

import com.main.interconnection.clientBo.User;

@JsonWriteNullProperties(false)
public class Response {

    public int numFound;
    public int start;
    private List<User> docs = new ArrayList<User>();

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

	public List<User> getDocs() {
		return docs;
	}

	public void setDocs(List<User> docs) {
		this.docs = docs;
	}
}

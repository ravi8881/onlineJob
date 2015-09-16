package com.main.interconnection.solr.response.friendRequest;

import java.util.ArrayList;
import java.util.List;

import com.main.interconnection.clientBo.FriendRequest;

public class Response {

    public int numFound;
    public int start;
    private List<FriendRequest> docs = new ArrayList<FriendRequest>();

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

	public List<FriendRequest> getDocs() {
		return docs;
	}

	public void setDocs(List<FriendRequest> docs) {
		this.docs = docs;
	}
}

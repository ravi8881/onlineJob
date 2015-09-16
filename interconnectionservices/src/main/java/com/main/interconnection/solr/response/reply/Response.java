package com.main.interconnection.solr.response.reply;

import java.util.ArrayList;
import java.util.List;

import com.main.interconnection.clientBo.VenueCommentReply;




public class Response {

    public int numFound;
    public int start;
    private List<VenueCommentReply> docs = new ArrayList<VenueCommentReply>();

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

	public List<VenueCommentReply> getDocs() {
		return docs;
	}

	public void setDocs(List<VenueCommentReply> docs) {
		this.docs = docs;
	}
}

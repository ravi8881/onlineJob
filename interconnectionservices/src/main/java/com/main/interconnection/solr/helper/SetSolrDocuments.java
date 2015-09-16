package com.main.interconnection.solr.helper;

import org.apache.solr.common.SolrInputDocument;

import com.main.interconnection.clientBo.User;

public class SetSolrDocuments {
	public SolrInputDocument solrDocument = null;

	public SolrInputDocument getUserInputDoc(User user) {
		solrDocument = new SolrInputDocument();
		solrDocument.addField("user_id", user.getUserId());
		solrDocument.addField("name", user.getName());
		solrDocument.addField("searchName", user.getName().trim());
		solrDocument.addField("emailId", user.getEmailId());
		solrDocument.addField("mobileNo", user.getMobileNumber());
		solrDocument.addField("imageUrl", user.getImageUrl());
		solrDocument.addField("aboutUs", user.getAboutUs());
		solrDocument.addField("city", user.getCity());
		solrDocument.addField("state", user.getState());
		solrDocument.addField("createDate", user.getCreateDate());
		solrDocument.addField("gendre", user.getGendre());
		solrDocument.addField("birthDay", user.getBirthday());
		solrDocument.addField("iAgree", user.getIagree());
		solrDocument.addField("detailsOnEmail", user.getDetailsOnEmail());
		return solrDocument;
	}

}

package com.main.interconnection.solrUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

public class SolrUtils {

	public static SolrServer getSolrServer(String solrClientUrl) {
		SolrServer server = null;
		try {
			server = new CommonsHttpSolrServer(solrClientUrl);
		} catch (MalformedURLException e) {

			e.printStackTrace();
		}
		return server;
	}

	public void addSolrDocument(String solrClientUrl,
			SolrInputDocument solrDocument) {
		SolrServer server = getSolrServer(solrClientUrl);
		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		docs.add(solrDocument);

		try {
			server.add(docs);
			server.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void deleteSolrDocument(String solrClientUrl, Object iD) {
		SolrServer server = getSolrServer(solrClientUrl);

		try {
			server.deleteByQuery("ID:" + iD);
			server.commit();
		} catch (SolrServerException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void updateSolrDocument(String solrClientUrl,
			SolrInputDocument solrDocument) {
		SolrServer server = getSolrServer(solrClientUrl);
		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		docs.add(solrDocument);
		try {
			server.add(docs);
			server.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

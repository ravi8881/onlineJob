package com.main.interconnection.solrDao;

import org.apache.solr.common.SolrInputDocument;
public interface SolrCommonClient {

	public boolean addObjectToSolr(String url ,SolrInputDocument solrDocument);
	
	public boolean deleteObject(String serverUrl , String query);
	
	public <T> T commonSolrSearch(String solrUrl , Class<T> responseType , String query , int startElement, int rows ,String filterBy , String sortOrder , String groupField );
		
	public boolean deleteAllObject(String serverUrl);
	
	public <T> T commonSolrSearchWithDistance(String solrUrl , Class<T> responseType , String query , int startElement, int rows ,String filterBy , String sortOrder , String groupField , String fq );
	
}

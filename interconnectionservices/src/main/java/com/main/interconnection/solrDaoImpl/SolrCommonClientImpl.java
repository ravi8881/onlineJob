package com.main.interconnection.solrDaoImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.SolrUtils;


public class SolrCommonClientImpl implements SolrCommonClient {

	@Autowired
	protected RestTemplate restTemplate;
	
	
	Map<String, String> params = new HashMap<String, String>();
	
	private SolrUtils solrUtils ;		
	
	Collection<SolrInputDocument> docs =null;

	public void setSolrUtils(SolrUtils solrUtils) {
		this.solrUtils = solrUtils;
	}

	@Override
	public boolean addObjectToSolr(String url , SolrInputDocument solrDocument) {
		try {
			solrUtils.addSolrDocument(url, solrDocument);
				return true;			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public <T> T commonSolrSearch(String solrUrl, Class<T> responseType,String query, int startElement, int rows, String filterBy,String sortOrder, String groupField) {
		params = new HashMap<String, String>();
			params.put("q", query);
	        params.put("start", "" + startElement);
	        params.put("rows", "" + rows);
	        params.put("group", groupField);
	        params.put("fl", filterBy);
	        
	        if (!sortOrder.equals("")) 
	            params.put("sort", sortOrder);
	         else 
	            params.put("sort", "");
	        return (T) restTemplate.getForObject(solrUrl, responseType, params);
	}

	@Override
	public boolean deleteObject(String serverUrl , String query) {
		SolrServer server = SolrUtils.getSolrServer(serverUrl);		
		docs = new ArrayList<SolrInputDocument>();
	    UpdateResponse response=null;
		try {
			response=server.deleteByQuery(query);
			server.commit();
			return true;
		} catch (SolrServerException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	
	
	public SolrCommonClientImpl(RestTemplate restTemplate, ObjectMapper mapper) {
		this.initEngine(restTemplate ,mapper);		
		params.put("q", "*");
		params.put("version", "2.2");
		params.put("start", "0");
		params.put("rows", "");
		params.put("indent", "on");
		params.put("wt", "json");
		params.put("fl", "");
		params.put("sort", "project_name asc");
		params.put("group", "false");
		params.put("groupFieldValue", "project_map_id");
	}

	private void initEngine(RestTemplate restTemplate ,ObjectMapper mapper) {		
		MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();
		converter.setObjectMapper(mapper);
		List<MediaType> mediaTypes = new ArrayList<MediaType>(2);
		mediaTypes.add(MediaType.APPLICATION_JSON);
		// Solr returns text/plain responses for JSON results
		mediaTypes.add(MediaType.TEXT_PLAIN);
		converter.setSupportedMediaTypes(mediaTypes);
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(1);
		converters.add(converter);
		restTemplate.setMessageConverters(converters);
	}

	@Override
	public boolean deleteAllObject(String serverUrl) {
		SolrServer server = SolrUtils.getSolrServer(serverUrl);
	    try {
	    	String query = "*:*";
	    	server.deleteByQuery(query);
			server.commit();
			return true;
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public <T> T commonSolrSearchWithDistance(String solrUrl, Class<T> responseType,String query, int startElement, int rows, String filterBy,String sortOrder, String groupField, String fq) {
		params = new HashMap<String, String>();
			params.put("q", query);
	        params.put("start", "" + startElement);
	        params.put("rows", "" + rows);
	        params.put("group", groupField);
	        params.put("fl", filterBy);
	        params.put("fq", fq);
	        
	        if (!sortOrder.equals("")) 
	            params.put("sort", sortOrder);
	         else 
	            params.put("sort", "");
	        return (T) restTemplate.getForObject(solrUrl, responseType, params);
	}
}

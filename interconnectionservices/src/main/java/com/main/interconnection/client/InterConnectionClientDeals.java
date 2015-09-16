package com.main.interconnection.client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.main.interconnection.clientBo.Deals;
import com.main.interconnection.clientBo.ErrorCode;
import com.main.interconnection.clientBo.UserLoc;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.deals.grouponLat.GroupOnLat;
import com.main.interconnection.deals.yelp.YelpResponse;
import com.main.interconnection.rest.client.ThirdPartyRestClient;
import com.main.interconnection.solr.response.bookmark.PassBookmark;
import com.main.interconnection.solr.response.deals.PassDeals;
import com.main.interconnection.solr.response.userloc.PassUserLoc;
import com.main.interconnection.solr.response.venue.PassVenue;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
import com.main.interconnection.util.SolrDateUtil;
import com.main.interconnection.util.Yelp;

@Controller
@RequestMapping(value="/deals/*")
public class InterConnectionClientDeals {
	
	private static final Logger logger = LoggerFactory.getLogger(InterConnectionClientDeals.class);
	
	@Autowired
	ThirdPartyRestClient thirdPartyRestClient;
	
	@Autowired
	ApiDao apiDao;
	
	@Autowired
	Yelp yelp;
	
	@Autowired
	SolrInputDocument solrDocument;
	

	@Autowired
	SolrCommonClient solrCommonClient;
	
	Map<String, String> params=null;
	
	@Value("${groupon.token}")
	String groupOnToken;		
	
	@RequestMapping( value="/find-deals-groupon" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView addUserVenue(@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "offset", required = true) String offset,
			@RequestParam(value = "limit", required = true) String limit,
			HttpServletRequest request					
			){
		ModelAndView mav = new ModelAndView();
		params =new HashMap<String, String>();
		try{
			logger.info("Find Deals");
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(id ==null || id.equals("") || offset ==null || offset.equals("") || limit ==null || limit.equals("") ){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(apiDao.validateApiKey(apikey)){
				params.put("token", groupOnToken);
				params.put("id", id);
				params.put("offset", offset);
				params.put("limit", limit);
//				JsonObject grouponDeals=thirdPartyRestClient.commonRestSearchClient(UrlConstant.DEALS, JsonObject.class,params);
				GroupOnLat grouponDeals=thirdPartyRestClient.commonRestSearchClient(UrlConstant.DEALS, GroupOnLat.class,params);
				mav.addObject(grouponDeals);
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	
	@RequestMapping( value="/find-by-location-groupon" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView dealsByLocation(@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "lat", required = true) String lat,
			@RequestParam(value = "lng", required = true) String lng,
			@RequestParam(value = "offset", required = true) String offset,
			@RequestParam(value = "limit", required = true) String limit,
			HttpServletRequest request					
			){
		ModelAndView mav = new ModelAndView();
		params =new HashMap<String, String>();
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(lat ==null || lat.equals("") || lng ==null || lng.equals("") || offset ==null || offset.equals("") || limit ==null || limit.equals("") ){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(apiDao.validateApiKey(apikey)){
				params.put("token", groupOnToken);
				params.put("lat", lat);
				params.put("lng", lng);
				params.put("offset", offset);
				params.put("limit", limit);
				GroupOnLat grouponLatDeals=thirdPartyRestClient.commonRestSearchClient(UrlConstant.DEALS_LAT_LNG, GroupOnLat.class,params);				
				mav.addObject(grouponLatDeals);
				return mav;
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));				
			}			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	
	@RequestMapping( value="/find-by-location-yelp" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView dealsByLocationFromYelp(@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "term", required = false) String term,
			@RequestParam(value = "lat", required = true) String lat,
			@RequestParam(value = "lng", required = true) String lng,
			@RequestParam(value = "offset", required = true) String offset,
			@RequestParam(value = "limit", required = true) String limit,
			HttpServletRequest request					
			){
		ModelAndView mav = new ModelAndView();		
		Gson gson = new Gson();		
		try{
			
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(lat ==null || lat.equals("") || lng ==null || lng.equals("") || offset ==null || offset.equals("") || limit ==null || limit.equals("") ){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(apiDao.validateApiKey(apikey)){
				
				// lat="30.361471";
				// lng="-87.164326";
				
				InputStream is = new ByteArrayInputStream(yelp.search("", 30.361471, -87.164326,"1").getBytes());
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				YelpResponse yelpDeals = gson.fromJson(br, YelpResponse.class);
				mav.addObject(yelpDeals);
				return mav;
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}	

	@RequestMapping( value="/get-deals" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getDealsFromGroupOn(@RequestParam(value = "api_key", required = true) String apikey,			
			HttpServletRequest request					
			){
		ModelAndView mav = new ModelAndView();		
		
		String query;
		PassUserLoc userLoc;
		try{
			
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(apiDao.validateApiKey(apikey)){
				
				query="loc_id: *";
				userLoc = (PassUserLoc) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USER_LOC_URL, PassUserLoc.class, query,0, 1000, "", "","false");
			
				
				for(UserLoc userLocInter:userLoc.getResponse().getDocs()){
					params =new HashMap<String, String>();
					params.put("token", groupOnToken);
					params.put("lat", userLocInter.getLat());
					params.put("lng", userLocInter.getLongitude());
					params.put("offset", "0");
					params.put("limit", "100000");
					
					GroupOnLat grouponLatDeals=thirdPartyRestClient.commonRestSearchClient(UrlConstant.DEALS_LAT_LNG, GroupOnLat.class,params);
					  for(com.main.interconnection.deals.grouponLat.Deal deals:grouponLatDeals.getDeals()){
						  
						  solrDocument = new SolrInputDocument();
						  solrDocument.addField("deal_uuid", deals.getUuid());
						  solrDocument.addField("deal_lati", deals.getDivision().getLat());						
							solrDocument.addField("deal_long", deals.getDivision().getLng());
							solrDocument.addField("street", deals.getDivision().getName());
							
							//not sure
							if(deals.getTags().size()>0)
							solrDocument.addField("deal_type", deals.getTags().get(0).getName());
							else
								solrDocument.addField("deal_type", "Deal");
							
							if(deals.getOptions().size()>0){
							solrDocument.addField("discount", deals.getOptions().get(0).getDiscount().getFormattedAmount());
							solrDocument.addField("discountPercent", deals.getOptions().get(0).getDiscountPercent());
							}
							else{
								solrDocument.addField("discount", "0");
								solrDocument.addField("discountPercent", "0");
							}
							
							solrDocument.addField("grouponRating", deals.getGrouponRating());						
							solrDocument.addField("para", deals.getTitle());
							solrDocument.addField("mapImageUrl", deals.getSmallImageUrl());
							solrDocument.addField("largeImageUrl", deals.getLargeImageUrl());
							solrDocument.addField("mediumImageUrl", deals.getMediumImageUrl());						
							solrDocument.addField("shortAnnouncementTitle", deals.getShortAnnouncementTitle());
							solrDocument.addField("title", deals.getTitle());						
							solrDocument.addField("deal_name", deals.getMerchant().getName());						
							solrDocument.addField("websiteUrl", deals.getMerchant().getWebsiteUrl());						
							solrDocument.addField("startAt", deals.getStartAt());
							solrDocument.addField("endAt", deals.getEndAt());						
							solrDocument.addField("expiresAt", deals.getOptions().get(0).getExpiresAt());						
							solrDocument.addField("expiresAtFormated", deals.getOptions().get(0).getExpiresAt());
							solrDocument.addField("description", deals.getOptions().get(0).getDetails().get(0).getDescription());						
							solrDocument.addField("buyUrl", deals.getOptions().get(0).getBuyUrl());
							solrDocument.addField("price", deals.getOptions().get(0).getPrice().getFormattedAmount());
							
							solrDocument.addField("deal_name_search", deals.getMerchant().getName());
										
							solrCommonClient.addObjectToSolr(UrlConstant.INTERNAL_DEALS_URL, solrDocument);	
							
							 solrDocument = new SolrInputDocument();
							
							 if(deals.getMerchant()!=null){										 
										 solrDocument.addField("venue_id", deals.getMerchant().getUuid());
										 solrDocument.addField("user_id", "Group-on");
										 solrDocument.addField("venue_name", deals.getMerchant().getName());
										 solrDocument.addField("venue_add_date",SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
										 solrDocument.addField("website", deals.getMerchant().getWebsiteUrl());	 
							 }
							 solrCommonClient.addObjectToSolr(UrlConstant.VENUE_FOR_DEALS, solrDocument);
								
					  }
				}
				mav.addObject("Deals Are Avilable for Search");
				return mav;
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}	

	
	@RequestMapping( value="/search-deals" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView searchDeals(@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = true) String filterBy,
			@RequestParam(value = "sort_order", required = true) String sortOrder,
			
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = false) String userId,
		
			HttpServletRequest request					
			){
		ModelAndView mav = new ModelAndView();		
		params =new HashMap<String, String>();
		PassDeals passDeals;
		
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);
		String query;
		
		try{
			
			query="deal_name_search:"+name+"*";
			
			passDeals = (PassDeals) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_INTERNAL_DEALS_URL, PassDeals.class, query,startElementSolr, rowsSolr, filterBy, sortOrder,	"false");
			for(Deals dealLocal:passDeals.getResponse().getDocs()){
				//		is Bookmark flag
			query="bookId: "+dealLocal.getDealUuid() + " AND "+"userId: "+userId;
			PassBookmark passBookMark = (PassBookmark) solrCommonClient.commonSolrSearch(	UrlConstant.SEARCH_BOOKMARK_URL, PassBookmark.class, query,	0, 1, filterBy, "","false");
			 if(passBookMark.getResponse().getDocs().size()>0){						 
				 dealLocal.setIsBookMark("1");
		 }else{
			 dealLocal.setIsBookMark("0");
		 }
	}
			mav.addObject(passDeals);
				
	}catch(Exception e){
		
	e.printStackTrace();
		}
	return mav;
	
	}
	
	@RequestMapping( value="/veue-over-deals" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView venueOverDeals(
			@RequestParam(value = "query", required = true) String query,
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = true) String filterBy,
			@RequestParam(value = "sort_order", required = true) String sortOrder,			
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = false) String userId,
		
			HttpServletRequest request					
			){
		ModelAndView mav = new ModelAndView();		
		PassVenue passVenue;		
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);
		
		try{					
			passVenue = (PassVenue) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_VENUE_FOR_DEALS, PassVenue.class, query,startElementSolr, rowsSolr, filterBy, sortOrder,	"false");		
			mav.addObject(passVenue);				
	}catch(Exception e){		
	e.printStackTrace();
		}
	return mav;	
	}	
	/*
	 * @author Name:Gaurav chugh
	 * @Created Date:27/11/2014
	 * @update Date:27/11/2014
	 * @purpose:deleteAllDeals
	 */
	@RequestMapping( value="/delete-all-deals" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView deleteAllDeals(			
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		boolean dealDelStatus=false;		
		
		dealDelStatus=solrCommonClient.deleteAllObject(UrlConstant.INTERNAL_DEALS_URL);
		if(dealDelStatus)
			mav.addObject("Deals Delete Succesfully");
		else
			mav.addObject("Issue while deleting Deals");		
		
		return mav;
		}	
	/*
	 * @author Name:Gaurav chugh
	 * @Created Date:27/11/2014
	 * @update Date:27/11/2014
	 * @purpose:deleteAllDealsOverVenue
	 */
	@RequestMapping( value="/delete-all-deals-over-venue" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView deleteAllDealsOverVenue(			
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		boolean dealOverVenueDelStatus=false;		
		
		dealOverVenueDelStatus=solrCommonClient.deleteAllObject(UrlConstant.VENUE_FOR_DEALS);
		if(dealOverVenueDelStatus)
			mav.addObject("Deals Over Venue Delete Succesfully");
		else
			mav.addObject("Issue while deleting Deals Over Venue");		
		
		return mav;
		}	

	@RequestMapping( value="/find-by-location-groupon-query" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView dealsByLocationQuery(@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "lat", required = true) String lat,
			@RequestParam(value = "lng", required = true) String lng,
			@RequestParam(value = "offset", required = true) String offset,
			@RequestParam(value = "limit", required = true) String limit,
			@RequestParam(value = "searchName", required = true) String searchName,
			@RequestParam(value = "userId", required = true) String userId,
			HttpServletRequest request					
			){
		ModelAndView mav = new ModelAndView();
		params =new HashMap<String, String>();
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(lat ==null || lat.equals("") || lng ==null || lng.equals("") || offset ==null || offset.equals("") || limit ==null || limit.equals("") ){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(apiDao.validateApiKey(apikey)){
				params.put("token", groupOnToken);
				params.put("lat", lat);
				params.put("lng", lng);
				params.put("offset", offset);
				params.put("limit", limit);
				params.put("id", searchName);
				GroupOnLat grouponLatDeals=thirdPartyRestClient.commonRestSearchClient(UrlConstant.DEALS_QUERY_URL, GroupOnLat.class,params);				
				
				//GroupOnLat grouponLatDeals=thirdPartyRestClient.commonRestSearchClient(UrlConstant.DEALS_LAT_LNG, GroupOnLat.class,params);
				  for(com.main.interconnection.deals.grouponLat.Deal deals:grouponLatDeals.getDeals()){
					  	// Bookmark flag
						// String dealQuery="deal_uuid:"+deals.getUuid();
						 String bookQuery = "bookId: " + deals.getUuid()+ " AND " + "userId: "+ userId;
							PassBookmark passBookmark = (PassBookmark) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_BOOKMARK_URL, PassBookmark.class, bookQuery,0, 1, "", "", "false");
							if (passBookmark.getResponse().getDocs().size() > 0) {
								deals.setIsBookMark(passBookmark.getResponse().getDocs().get(0).getBookmarkStatus());
							} else {
								deals.setIsBookMark("0");
							}
							
					  	solrDocument = new SolrInputDocument();
					  	solrDocument.addField("deal_uuid", deals.getUuid());
					  	solrDocument.addField("deal_lati", deals.getDivision().getLat());						
						solrDocument.addField("deal_long", deals.getDivision().getLng());
						solrDocument.addField("street", deals.getDivision().getName());
						
						//not sure
						if(deals.getTags().size()>0)
						solrDocument.addField("deal_type", deals.getTags().get(0).getName());
						else
							solrDocument.addField("deal_type", "Deal");
						
						if(deals.getOptions().size()>0){
						solrDocument.addField("discount", deals.getOptions().get(0).getDiscount().getFormattedAmount());
						solrDocument.addField("discountPercent", deals.getOptions().get(0).getDiscountPercent());
						}
						else{
							solrDocument.addField("discount", "0");
							solrDocument.addField("discountPercent", "0");
						}
						
						solrDocument.addField("grouponRating", deals.getGrouponRating());						
						solrDocument.addField("para", deals.getTitle());
						solrDocument.addField("mapImageUrl", deals.getSmallImageUrl());
						solrDocument.addField("largeImageUrl", deals.getLargeImageUrl());
						solrDocument.addField("mediumImageUrl", deals.getMediumImageUrl());						
						solrDocument.addField("shortAnnouncementTitle", deals.getShortAnnouncementTitle());
						solrDocument.addField("title", deals.getTitle());						
						solrDocument.addField("deal_name", deals.getMerchant().getName());						
						solrDocument.addField("websiteUrl", deals.getMerchant().getWebsiteUrl());						
						solrDocument.addField("startAt", deals.getStartAt());
						solrDocument.addField("endAt", deals.getEndAt());						
						solrDocument.addField("expiresAt", deals.getOptions().get(0).getExpiresAt());						
						solrDocument.addField("expiresAtFormated", deals.getOptions().get(0).getExpiresAt());
						solrDocument.addField("description", deals.getOptions().get(0).getDetails().get(0).getDescription());						
						solrDocument.addField("buyUrl", deals.getOptions().get(0).getBuyUrl());
						solrDocument.addField("price", deals.getOptions().get(0).getPrice().getFormattedAmount());
						
						solrDocument.addField("deal_name_search", deals.getMerchant().getName());
									
						solrCommonClient.addObjectToSolr(UrlConstant.INTERNAL_DEALS_URL, solrDocument);	
						
						 solrDocument = new SolrInputDocument();
						
						 if(deals.getMerchant()!=null){										 
									 solrDocument.addField("venue_id", deals.getMerchant().getUuid());
									 solrDocument.addField("user_id", "Group-on");
									 solrDocument.addField("venue_name", deals.getMerchant().getName());
									 solrDocument.addField("venue_add_date",SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
									 solrDocument.addField("website", deals.getMerchant().getWebsiteUrl());	 
						 }
						 
						 solrCommonClient.addObjectToSolr(UrlConstant.VENUE_FOR_DEALS, solrDocument);
	
				  }
					mav.addObject("Deals Are Avilable for Search");
					mav.addObject(grouponLatDeals);
					return mav;
		
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));				
			}			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping( value="/serach-deals-by-category" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView serachDealsBycategory(@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "tstoken", required = true) String tstoken,			
			@RequestParam(value = "lat", required = true) String lat,
			@RequestParam(value = "lng", required = true) String lng,
			@RequestParam(value = "cat1", required = true) String cat1,
			@RequestParam(value = "cat2", required = true) String cat2,
			@RequestParam(value = "cat3", required = true) String cat3,
			@RequestParam(value = "offset", required = true) String offset,			
			@RequestParam(value = "limit", required = true) String limit,
			@RequestParam(value = "type", required = true) String type,
			HttpServletRequest request					
			){
		ModelAndView mav = new ModelAndView();
		params =new HashMap<String, String>();
		try{
			logger.info("Find Deals");
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(apiDao.validateApiKey(apikey)){
				
				if(type.equals("Four-Square")){
				
				if(cat1.trim().equals("Event")){
					cat1="Things To Do";
				}else if(cat1.trim().equals("Food")){
					cat1="Food & Drink";
				}else if(cat1.trim().equals("Nightlife Spot")){
					cat1="Things To Do";
				}else if(cat1.trim().equals("Outdoors & Recreation")){
					cat1="Things To Do";
				}else if(cat1.trim().equals("Professional & Other Places")){
					cat1="Local Services";
				}else if(cat1.trim().equals("Residences")){
					cat1="Home Services";
				}else if(cat1.trim().equals("Shop & Service")){
					cat1="Shopping";
				}else if(cat1.trim().equals("Travel & Transport")){
					cat1="Automotive";
				}
				}
				params.put("tstoken", tstoken);
				params.put("lat", lat);
				params.put("lng", lng);
				params.put("cat1", cat1);
				params.put("cat2", cat2);
				params.put("cat3", cat3);
				params.put("offset", offset);
				params.put("limit", limit);				

				GroupOnLat grouponDeals=thirdPartyRestClient.commonRestSearchClient(UrlConstant.DEALS_BY_CATEGORY, GroupOnLat.class,params);
				mav.addObject(grouponDeals);
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	
}
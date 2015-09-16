package com.main.interconnection.client;

import java.util.ArrayList;
import java.util.Date;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.main.interconnection.clientBo.ErrorCode;
import com.main.interconnection.clientBo.Flag;
import com.main.interconnection.clientBo.HelpFulReview;
import com.main.interconnection.clientBo.VenueRating;
import com.main.interconnection.clientBo.VenueReview;
import com.main.interconnection.clientBo.VenueReviewDetails;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.foursquare.tips.FourSquareTips;
import com.main.interconnection.mongoDao.MongoCommonClient;
import com.main.interconnection.solr.response.comment.PassComment;
import com.main.interconnection.solr.response.helpful.PassHelpFulReview;
import com.main.interconnection.solr.response.review.PassReview;
import com.main.interconnection.solr.response.session.PassSession;
import com.main.interconnection.solr.response.users.PassUsers;
import com.main.interconnection.solr.response.venue.PassVenue;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
import com.main.interconnection.util.MagicNumbers;
import com.main.interconnection.util.SolrDateUtil;
import com.main.interconnection.util.UUIDGenrator;
import com.main.interconnection.util.UpdateTypeEnum;

@Controller
@RequestMapping(value="/review/*")
public class InterConnectionClientVenueReview {
	
	
	@Autowired
	ApiDao apiDao;
	
	@Autowired
	SolrCommonClient solrCommonClient;
	
	@Autowired
	MongoCommonClient mongoCommonClient;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	SolrInputDocument solrDocument;	
	
	@Autowired
	protected RestTemplate restTemplate;

	@Value("${foursquare.version}")
	String foursquareVersion;
	
	@Value("${foursquare.outh}")
	String foursquareOuth;

	
	
	@RequestMapping( value="/post-review-on-venue" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView postReviewOnVenue(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "venue_id", required = true) String venueId,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "rating", required = false) double rating,
			@RequestParam(value = "tip", required = false) String tip,
			@RequestParam(value = "review", required = false) String review,
			@RequestParam(value = "content_id", required = false) String contenId,	
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String reviewId=null;		
		boolean reviewAddStatus=false;			
		
		try{			
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(token==null || token.equals("")){
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;					
			}else if(userId ==null || userId.equals("") || venueId ==null || venueId.equals("") || rating ==0){
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().size()==0){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(apiDao.validateApiKey(apikey)){
				reviewId=UUIDGenrator.getUniqueId();
				solrDocument=new SolrInputDocument();
				solrDocument.addField("review_id", reviewId);
				solrDocument.addField("venue_id", venueId);
				solrDocument.addField("user_id", userId);
				solrDocument.addField("review_rate", rating);
				if(review==null || review.equals("")){
					
				}else{
					solrDocument.addField("review",  review);
				}
				if(tip==null || tip.equals("")){
					
				}else{
					solrDocument.addField("tip",  tip);
				}
				solrDocument.addField("review_date",  SolrDateUtil.addDateToSolrWithUtilDate(new Date()));				
				solrDocument.addField("review_status",  MagicNumbers.ACTIVE_YES);				
				if(contenId!=null){
				solrDocument.addField("content_id",  contenId);}		
				reviewAddStatus=solrCommonClient.addObjectToSolr(UrlConstant.REVIEW_URL , solrDocument);
				PassVenue passVenue=(PassVenue)solrCommonClient. commonSolrSearch(UrlConstant.SEARCH_VENUE_URL, PassVenue.class, "venue_id:"+venueId	, 0, 1, "", "", "false");
				String venueName=passVenue.getResponse().getDocs().get(0).getVenueName();
			
				if(reviewAddStatus){
					if(review==null || review.equals("")){
					}else{
						String venueReview=UUIDGenrator.getUniqueId();
	    				solrDocument=new SolrInputDocument();
	    				solrDocument.addField("id", venueReview);
	    				solrDocument.addField("type", UpdateTypeEnum.UpdateType.Feeds.toString());
	    				solrDocument.addField("subType", UpdateTypeEnum.feedsUpdateProperty.VenueReview.toString());	
	    				solrDocument.addField("property", UpdateTypeEnum.feedsUpdateProperty.VenueReview.toString());	
	    				solrDocument.addField("toUser", userId);	
	    				solrDocument.addField("fromUser", userId);	
	    				solrDocument.addField("venue_id", venueId);
	    				solrDocument.addField("review_id", reviewId);	
	    				solrDocument.addField("search_name", venueName.trim());
	    				solrDocument.addField("privacy", MagicNumbers.ACTIVE);
	    				solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
	    				solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
						}
					//Create Feed Card for Review
					
    				//Create Feed Card for Rating
					String reviewRating=UUIDGenrator.getUniqueId();
    				solrDocument=new SolrInputDocument();
    				solrDocument.addField("id", reviewRating);
    				solrDocument.addField("type", UpdateTypeEnum.UpdateType.Feeds.toString());
    				solrDocument.addField("subType", UpdateTypeEnum.feedsUpdateProperty.VenueRating.toString());	
    				solrDocument.addField("property", UpdateTypeEnum.feedsUpdateProperty.VenueRating.toString());	
    				solrDocument.addField("toUser", userId);	
    				solrDocument.addField("fromUser", userId);	
    				solrDocument.addField("venue_id", venueId);
    				solrDocument.addField("review_id", reviewId);	
    				solrDocument.addField("search_name", venueName.toLowerCase().trim());
    				solrDocument.addField("privacy", MagicNumbers.ACTIVE);
    				solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
    				solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
    				//Create Entry in Mongo
    				List<VenueRating> venueRatingList  = new ArrayList<VenueRating>();
    				Query query=new Query();
    				query.addCriteria(Criteria.where("venueId").is(venueId));
    				venueRatingList = (List<VenueRating>) mongoCommonClient.findByQuery(query, VenueRating.class);
    				if(venueRatingList.size() > 0) {
    					VenueRating venueRatingData = venueRatingList.get(0);
    					rating = rating + venueRatingData.getRating();
    					Update update = new Update();
    					update.set("rating", rating);
    					mongoCommonClient.saveOrUpdate(Query.query(Criteria.where("venueId").is(venueId)), VenueRating.class, update);
    					mav.addObject("Message" , "Review added sucessful");
    					mav.addObject("review_id" , reviewId);	
    					return mav;
    				}
    				VenueRating venueRating = new VenueRating();
    				venueRating.setRating(rating);
    				venueRating.setVenueId(venueId);
    				mongoTemplate.insert(venueRating);
					mav.addObject("Message" , "Review added sucessful");
					mav.addObject("review_id" , reviewId);	
					return mav;
				}else{					
					mav.addObject(ErrorCode.getCustomeError(1001));
				}
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));				
			}	
			
		}catch (Exception e) {
			e.printStackTrace();			
			mav.addObject(ErrorCode.getCustomeError(1002));		
		}		
		return mav;		
	}
	
	@RequestMapping( value="/search-review" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView searchReview(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = true) String filterBy,
			@RequestParam(value = "sort_order", required = true) String sortOrder,
			@RequestParam(value = "query", required = false) String query,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		PassReview passReview=new PassReview();
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);		
		passReview=(PassReview) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REVIEW_URL, PassReview.class, query, startElementSolr, rowsSolr, filterBy, sortOrder, "false");
		mav.addObject(passReview);
		mav.addObject("is_helpful", "1");
		mav.addObject("review_flag", "0");
		return mav;
		}

	
	@RequestMapping( value="/delete-all-review" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView deleteAllReview(			
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		boolean reviewDelStatus=false;		
		
		reviewDelStatus=solrCommonClient.deleteAllObject(UrlConstant.REVIEW_URL);
		if(reviewDelStatus)
			mav.addObject("Review Delete Succesfully");
		else
			mav.addObject("Issue while deleting review");		
		return mav;
		}
	
	@RequestMapping( value="/delete-review-by-query" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView deleteReviewById(			
			@RequestParam(value = "query", required = false) String query,
			HttpServletRequest request) {	
		
		ModelAndView mav = new ModelAndView();
		boolean reviewDelStatus=false;		
		
		reviewDelStatus=solrCommonClient.deleteObject(UrlConstant.REVIEW_URL , query);
		if(reviewDelStatus)
			mav.addObject("Review Delete Succesfully");
		else
			mav.addObject("Issue while deleting review");		
		
		return mav;
		}
	
	
	@RequestMapping( value="/add-helpful" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView addHelpfulReview(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "venue_id", required = true) String venueId,
			@RequestParam(value = "review_id", required = true) String reviewId,
			@RequestParam(value = "helpful_status", required = true) String helpfulStatus,
			@RequestParam(value = "token", required = true) String token,		
			@RequestParam(value = "property", required = false) String property,
			@RequestParam(value = "review_userId", required = true) String reviewUserId,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		boolean helpFulReview=false;
		String id=null;
		String newUpdateId=null;
		try{			
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(token==null || token.equals("")){
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;					
			}else if(userId ==null || userId.equals("") || venueId ==null || venueId.equals("") || reviewId ==null || reviewId.equals("")){
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().size()==0){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(apiDao.validateApiKey(apikey)){
				id=UUIDGenrator.getUniqueId();
				solrDocument=new SolrInputDocument();
				solrDocument.addField("id", id);
				solrDocument.addField("review_id", reviewId);
				solrDocument.addField("venue_id", venueId);
				solrDocument.addField("user_id", userId);
				solrDocument.addField("fromUser", reviewUserId);
				solrDocument.addField("added_date", new Date());
				solrDocument.addField("review_helpful",helpfulStatus );		
				helpFulReview=solrCommonClient.addObjectToSolr(UrlConstant.HELPFUL_URL, solrDocument);
				// Create Notification
				if(helpfulStatus.equals("1")){
				solrDocument=new SolrInputDocument();
				newUpdateId=UUIDGenrator.getUniqueId();
				solrDocument.addField("id", newUpdateId);
				solrDocument.addField("type", UpdateTypeEnum.UpdateType.Notifications.toString());
				solrDocument.addField("subType", UpdateTypeEnum.feedsUpdateProperty.HelpfulReview.toString());	
				if(property==null||property.equals("")){
					solrDocument.addField("property", UpdateTypeEnum.feedsUpdateProperty.HelpfulReview);
				}else{
					solrDocument.addField("property",property);
				}
				solrDocument.addField("privacy", MagicNumbers.ACTIVE);
				if(userId.equals(reviewUserId)){
					solrDocument.addField("subTypes",UpdateTypeEnum.UpdateSubTypes.USEROWN);
				}
				solrDocument.addField("toUser",reviewUserId);	
				solrDocument.addField("fromUser", userId );
				solrDocument.addField("venue_id", venueId);	
				solrDocument.addField("review_id", reviewId);
				solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
				solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
				}
				if(helpFulReview){
					mav.addObject("HelpFul" , "Added as helpful review");
					return mav;
				}else{					
					mav.addObject(ErrorCode.getCustomeError(1001));
				}
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));				
			}	
			
		}catch (Exception e) {
			e.printStackTrace();			
			mav.addObject(ErrorCode.getCustomeError(1002));		
		}		
		return mav;		
	}
	
	@RequestMapping( value="/search-helpful-review" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView searchHelpfulReview(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = true) String filterBy,
			@RequestParam(value = "sort_order", required = true) String sortOrder,
			@RequestParam(value = "review_id", required = false) String reviewId,
			@RequestParam(value = "venue_id", required = false) String venueId,
			@RequestParam(value = "user_id", required = false) String userId,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		PassHelpFulReview helpFulReview=new PassHelpFulReview();
		PassReview passReview=new PassReview();
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);		
		String query="review_id:"+reviewId;
		
		int helpful_yes = 0;
		int helpful_no = 0;
		helpFulReview=(PassHelpFulReview) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_HELPFUL_URL, PassHelpFulReview.class, query, startElementSolr, rowsSolr, filterBy, sortOrder, "false");
		for(HelpFulReview help :helpFulReview.getResponse().getDocs() ){
			String helpful = help.getReviewHelpFul();
			if(helpful.equals("1")) {
				helpful_yes ++;
			} else {
				helpful_no ++;
			}
		}
		passReview=(PassReview) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REVIEW_URL, PassReview.class, query, startElementSolr, rowsSolr, filterBy, sortOrder, "false");
		mav.addObject(passReview);
		mav.addObject("helpful_yes" , helpful_yes);
		mav.addObject("helpful_no" , helpful_no);
		return mav;
		}
	
	@RequestMapping( value="/search-helpful-review-by-query" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView searchHelpfulReviewByQuery(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = true) String filterBy,
			@RequestParam(value = "sort_order", required = true) String sortOrder,
			@RequestParam(value = "query", required = true) String query,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		PassHelpFulReview helpFulReview=new PassHelpFulReview();
		PassReview passReview=new PassReview();
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);		
		
		int helpful_yes = 0;
		int helpful_no = 0;
		helpFulReview=(PassHelpFulReview) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_HELPFUL_URL, PassHelpFulReview.class, query, startElementSolr, rowsSolr, filterBy, sortOrder, "false");
		for(HelpFulReview help :helpFulReview.getResponse().getDocs() ){
			String helpful = help.getReviewHelpFul();
			if(helpful.equals("1")) {
				helpful_yes ++;
			} else {
				helpful_no ++;
			}
		}
		passReview=(PassReview) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REVIEW_URL, PassReview.class, query, startElementSolr, rowsSolr, filterBy, sortOrder, "false");
		mav.addObject(passReview);
		mav.addObject("helpful_yes" , helpful_yes);
		mav.addObject("helpful_no" , helpful_no);
		return mav;
		}
	
	@RequestMapping( value="/flag-review" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView flagVenue(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "review_id", required = true) String reviewId,	
			@RequestParam(value = "is_flagged", required = true) String isFlagged,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		if(apikey ==null || apikey.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;			
		}else if(token==null || token.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;					
		}else if(userId ==null || userId.equals("") || reviewId ==null || reviewId.equals("")
				|| isFlagged == null || isFlagged.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;						
		}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().
				getDocs().size()<=0){				
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;	
		}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().
				getDocs().get(0).getToken().equals(token)){				
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;	
		}else if(apiDao.validateApiKey(apikey)){
			List<Flag> flagCommentList  = new ArrayList<Flag>();
			Query query=new Query();
			query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("typeName").is(UpdateTypeEnum.FlagType.review_id.toString()).andOperator(
					Criteria.where("typeValue").is(reviewId))));
			flagCommentList = (List<Flag>) mongoCommonClient.findByQuery(query, Flag.class);
			if(flagCommentList.size() > 0) {
				Update update = new Update();
				update.set("isFlagged", isFlagged);
				mongoCommonClient.saveOrUpdate(Query.query(Criteria.where("userId").is(userId).and("typeName").is(UpdateTypeEnum.FlagType.review_id.toString()).
						and("typeValue").is(reviewId)), Flag.class, update);
				mav.addObject("Review_Flagged", isFlagged);
				mav.addObject("review_id", reviewId);
				return mav;
			} else {
				Flag flagVenue = new Flag();
				flagVenue.setAdminApproved("1");
				flagVenue.setIsFlagged(isFlagged);
				flagVenue.setUserId(userId);
				flagVenue.setTypeName(UpdateTypeEnum.FlagType.review_id.toString());
				flagVenue.setTypeValue(reviewId);
				flagVenue.setSubType("none");
				mongoTemplate.insert(flagVenue);
				mav.addObject("Review_Flagged", isFlagged);
				mav.addObject("review_id", reviewId);
				return mav;
			}
		}
		return mav;
	}

	@RequestMapping( value="/delete-all-helpful" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView deleteAllHelpful(			
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		boolean replyHelpStatus=false;		
		
		replyHelpStatus=solrCommonClient.deleteAllObject(UrlConstant.HELPFUL_URL);
		if(replyHelpStatus)
			mav.addObject("Helpful Delete Succesfully");
		else
			mav.addObject("Issue while deleting Helpful");		
		
		return mav;
		}
	
	@RequestMapping( value="/search-review-details" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView searchReviewDetails(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = false) String filterBy,
			@RequestParam(value = "sort_order", required = false) String sortOrder,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "query", required = false) String query,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		PassReview passReview=new PassReview();
		VenueReviewDetails venueReviewDetails = null;
		List<VenueReviewDetails> veReviewDetailsList=new ArrayList<VenueReviewDetails>();
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);		
		PassUsers passUsers=null;
		String userQuery=null;
		
		passReview=(PassReview) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REVIEW_URL, PassReview.class, query, startElementSolr, rowsSolr, filterBy, sortOrder, "false");
		
		for (VenueReview vReview : passReview.getResponse().getDocs()) {
			venueReviewDetails = new VenueReviewDetails();
			String revId = vReview.getReviewId();
			  if(passReview.getResponse().getDocs().size()>0){
			venueReviewDetails.setVenueReview(vReview);
			  
            //				toUser Object
            userQuery="user_id:"+vReview.getUserId();
            passUsers=(PassUsers)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL, PassUsers.class, userQuery, 0, 10, "", "", "false");
            if(passUsers.getResponse().getDocs().size()>0){
            	vReview.setUsers(passUsers.getResponse().getDocs().get(0));
            }
			//Venue Object getting query
			String venuQuery = "venue_id:"+vReview.getVenueId();//Venue Object
			PassVenue pVenue = solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_VENUE_URL, PassVenue.class, venuQuery, 0, 1, "", "", "false");
			venueReviewDetails.setVenue(pVenue.getResponse().getDocs().get(0));
			//Total comments count
			String revQuery = "review_id:"+" "+revId;
			PassComment passComment = solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_COMMENT_URL, PassComment.class, revQuery, 0, 1, "", "", "false");
			if(passComment.getResponse().numFound>0){
			venueReviewDetails.setCommentTotal(passComment.getResponse().numFound);//comment count
			}
			
			String helpQuery = "review_id:"+" "+revId+" AND user_id:"+userId;
			PassHelpFulReview passHelpFulReview = solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_HELPFUL_URL, PassHelpFulReview.class, helpQuery, 0, 1, "", "", "false");
			if(passHelpFulReview.getResponse().getDocs().size()>0){
				venueReviewDetails.setIsHelpFul(passHelpFulReview.getResponse().getDocs().get(0).getReviewHelpFul());
				}
			//helpfulYes count
			String revQueryYes = "review_id:"+" "+revId+" AND review_helpful:1";
			int helpFulReviewYes = solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_HELPFUL_URL, PassHelpFulReview.class, revQueryYes, 0, 1, "", "", "false").getResponse().getNumFound();
			venueReviewDetails.setHelpFulReviewYes(helpFulReviewYes);

			//helpfulNo count
			String revQueryNo = "review_id:"+" "+revId+" AND review_helpful:0";
			int helpFulReviewNo = solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_HELPFUL_URL, PassHelpFulReview.class, revQueryNo, 0, 1, "", "", "false").getResponse().getNumFound();
			venueReviewDetails.setHelpFulReviewNo(helpFulReviewNo);
			
			//helpful total count
			int totalHelpFul = helpFulReviewYes+helpFulReviewNo;
			venueReviewDetails.setHelpfulTotal(totalHelpFul);
			
			BasicQuery isFlag=new BasicQuery("{'typeValue' : "+"'"+revId +"'"+","+"'userId' : "+"'"+userId+"'"+","+"'typeName' : "+"'"+UpdateTypeEnum.FlagType.review_id.toString()+"'"+"}");
			if(mongoTemplate.count(isFlag, Flag.class)!=0){
				venueReviewDetails.setIsFlag("1");
			}else{
				venueReviewDetails.setIsFlag("0");
			}
			//venueReviewDetails.setVenueReview(vReview);
			veReviewDetailsList.add(venueReviewDetails);
			  }	
		}
		mav.addObject("numFound",passReview.getResponse().numFound);
		mav.addObject("passReview",veReviewDetailsList);
		return mav;
		}
	
	
	
	@RequestMapping( value="/get-foursquare-tip" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getFourSquareTipsByVenueId(			
			
			@RequestParam(value = "venueid", required = true) String venueId,

			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		Map<String, String> params = null;
		
		FourSquareTips fourSquareTips=null;
		
		params = new HashMap<String, String>();
		
		params.put("venueid", venueId);
		params.put("outhToken", foursquareOuth);
		params.put("version", foursquareVersion);
				
		fourSquareTips  =(FourSquareTips)restTemplate.getForObject(UrlConstant.FOUR_SQUARE_TIPS, FourSquareTips.class , params);
		
		System.out.println("----------->"+fourSquareTips.getResponse().getTips().getItems().size());
		
		mav.addObject(fourSquareTips);
		
		return mav;
		}
	
	
	
	@RequestMapping(value="/get-foursquare-tip2" , method={RequestMethod.POST , RequestMethod.GET})
	public ModelAndView getFourSquareTipsByVenueId2(
			@RequestParam(value = "venueid", required = true) String venueId,
			
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		Map<String, String> params = null;
		FourSquareTips fourSquareTips=null;
		
		try{
			params = new HashMap<String, String>();
			
			params.put("venueid", venueId);
			params.put("outhToken", foursquareOuth);
			params.put("version", foursquareVersion);
			
			fourSquareTips  =(FourSquareTips)restTemplate.getForObject(UrlConstant.FOUR_SQUARE_TIPS, FourSquareTips.class , params);
			mav.addObject(fourSquareTips);	
		}catch(Exception  e){
			e.printStackTrace();
			mav.addObject("No-Tips avilable");
		}		
		return mav;
	}
}
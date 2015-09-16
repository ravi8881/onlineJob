package com.main.interconnection.client;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.main.interconnection.clientBo.ErrorCode;
import com.main.interconnection.clientBo.FeedComment;
import com.main.interconnection.clientBo.FeedCommentDetails;
import com.main.interconnection.clientBo.FeedDetails;
import com.main.interconnection.clientBo.Flag;
import com.main.interconnection.clientBo.FriendRequest;
import com.main.interconnection.clientBo.Message;
import com.main.interconnection.clientBo.TagFriends;
import com.main.interconnection.clientBo.Updates;
import com.main.interconnection.clientBo.VenueRating;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.mongo.pagination.repository.LikeFeedRepository;
import com.main.interconnection.mongoBo.LikeFeeds;
import com.main.interconnection.mongoDao.MongoCommonClient;
import com.main.interconnection.solr.response.binary.PassBinary;
import com.main.interconnection.solr.response.comment.feed.PassFeedComment;
import com.main.interconnection.solr.response.content.PassContent;
import com.main.interconnection.solr.response.friendRequest.PassFriendRequest;
import com.main.interconnection.solr.response.markNotification.PassMarkNotification;
import com.main.interconnection.solr.response.review.PassReview;
import com.main.interconnection.solr.response.session.PassSession;
import com.main.interconnection.solr.response.updates.PassUpdates;
import com.main.interconnection.solr.response.users.PassUsers;
import com.main.interconnection.solr.response.venue.PassVenue;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
import com.main.interconnection.util.MagicNumbers;
import com.main.interconnection.util.SolrDateUtil;
import com.main.interconnection.util.UUIDGenrator;
import com.main.interconnection.util.UpdateTypeEnum;

@Controller
@RequestMapping(value="/updates/*")
public class InterConnectionClientUpdates {

	
private static final Logger logger = LoggerFactory.getLogger(InterConnectionClientUpdates.class);
	
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
	LikeFeedRepository likeFeedRep;
	
	public static final int DEFUALT_RESULT_SIZE = 1;

	@RequestMapping( value="/get-user-feeds" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getUserFeeds(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
        PassUpdates passUpdates=null;
        PassFriendRequest friendListIds=null;
        StringBuilder friendList=new StringBuilder();
        StringBuilder flipfriendList=new StringBuilder();
        
        int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);		        
        String query=null;
		try{
            if(apikey ==null || apikey.equals("")){
                mav.addObject(ErrorCode.getCustomeError(104));
                return mav;
            }else if(token==null || token.equals("")){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(userId ==null || userId.equals("") ){
                mav.addObject(ErrorCode.getCustomeError(101));
                return mav;
            }    else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().size()<=0){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(apiDao.validateApiKey(apikey)){
            	query="to_user:"+userId+"*"+" AND "+"status:Accept";
				friendListIds=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, query, startElementSolr, rowsSolr, "", "", "false");
            	if(friendListIds.getResponse().numFound==0){
            		query="toUser:"+userId +" AND type:Feeds" ;                
            	}else{            		
            		for(FriendRequest friend:friendListIds.getResponse().getDocs() ){         			
            			friendList.append(" OR fromUser:"+friend.getFromUser());
            			flipfriendList.append(" OR toUser:"+friend.getFromUser());
            		}         
            		 query="( toUser:"+userId +friendList.toString()+" )  OR  " +"( fromUser:"+userId +flipfriendList.toString()+" )  AND type:Feeds" ;
            	}
            	if(type!= null && !type.equals("")) {
            		query = query + " AND "+"subType:"+type;
            	}
            	query=query+" AND "+"privacy:"+MagicNumbers.ACTIVE;
            	passUpdates=(PassUpdates) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_MANAGE_UPDATES_URL, PassUpdates.class, query, startElementSolr, rowsSolr, "", "addedDate desc", "false");
            	mav.addObject("Feeds",passUpdates);
            	return mav;
            }
		}catch (Exception e) {
			e.printStackTrace();
            mav.addObject(ErrorCode.getCustomeError(1002));
        }
			return mav;
	}
	 
	@RequestMapping( value="/get-user-notifications" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getUserNotifications(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
        PassUpdates passUpdates=null;
        PassFriendRequest friendListIds=null;
        StringBuilder friendList=new StringBuilder();
        int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);		        
        String query=null;
        PassUsers passUsers=null;
        PassMarkNotification passMark=null;
        String userQuery=null;
        PassVenue passVenue=null;
        String userfromQuery=null;
		try{
            if(apikey ==null || apikey.equals("")){
                mav.addObject(ErrorCode.getCustomeError(104));
                return mav;
            }else if(token==null || token.equals("")){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(userId ==null || userId.equals("") ){
                mav.addObject(ErrorCode.getCustomeError(101));
                return mav;
            }    else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().size()<=0){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(apiDao.validateApiKey(apikey)){
            	query="to_user:"+userId+"*"+" AND "+"status:Accept";
				friendListIds=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, query, startElementSolr, rowsSolr, "", "", "false");
            	if(friendListIds.getResponse().numFound==0){
                query="toUser:"+userId +" AND type:Notifications" ;                
            	}else{            		
            		for(FriendRequest friend:friendListIds.getResponse().getDocs() ){         			
            			friendList.append(" OR fromUser:"+friend.getFromUser());
            		}         
            		 query="( toUser:"+userId +friendList.toString()+" ) "  +" AND type:Notifications" ;
            	}
            	query="("+query+")"+ "-" +"("+"subTypes : "+UpdateTypeEnum.UpdateSubTypes.USEROWN+ " AND "+" toUser :"+userId+")";
            	passUpdates=(PassUpdates) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_MANAGE_UPDATES_URL, PassUpdates.class, query, startElementSolr, rowsSolr, "", "addedDate desc", "false");
         
            		if(passUpdates.getResponse().getDocs().size()>0){
            	for(Updates updateLocal:passUpdates.getResponse().getDocs()){
                String flagQuery="userId:"+userId+" AND feedId:"+updateLocal.getId();
                passMark=(PassMarkNotification)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_MANAGE_UPDATES_STATUS_URL, PassMarkNotification.class, flagQuery, startElementSolr, rowsSolr, "", "", "false");
                if(passMark.getResponse().getDocs().size()>0){
                	String flag=passMark.getResponse().getDocs().get(0).getFlag();
                	updateLocal.setFlag(flag);
                }
                //				toUser Object
                userQuery="user_id:"+updateLocal.getToUser();
                passUsers=(PassUsers)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL, PassUsers.class, userQuery, 0, 10, "", "", "false");
                if(passUsers.getResponse().getDocs().size()>0){
                updateLocal.setUsers(passUsers.getResponse().getDocs().get(0));
                }
                //        		fromUser Object
                userfromQuery="user_id:"+updateLocal.getFromUser();
                passUsers=(PassUsers)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL, PassUsers.class, userfromQuery, 0, 10, "", "", "false");
                if(passUsers.getResponse().getDocs().size()>0){
                updateLocal.setUsersFrom(passUsers.getResponse().getDocs().get(0));
                }
                //		Venue Object
                userQuery="venue_id:"+updateLocal.getVenueId() +" OR venue_id:"+updateLocal.getContent_id();
                passVenue=(PassVenue)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_VENUE_URL, PassVenue.class, userQuery, 0, 10, "", "", "false");
                if(passVenue.getResponse().getDocs().size()>0){
                updateLocal.setVenue(passVenue.getResponse().getDocs().get(0));
                }
            	}
               }
            	mav.addObject("Notifications",passUpdates);
                return mav;
            }
			
		}catch (Exception e) {
			e.printStackTrace();
            mav.addObject(ErrorCode.getCustomeError(1002));
        }
			return mav;
	}	
	
	
	@RequestMapping( value="/delete-all-updates" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView deleteAllUpdates(			
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		boolean updatesDelStatus=false;		
		
		updatesDelStatus=solrCommonClient.deleteAllObject(UrlConstant.MANAGE_UPDATES_URL);
		if(updatesDelStatus)
			mav.addObject("Updates Delete Succesfully");
		else
			mav.addObject("Issue while deleting updates");		
		
		return mav;
	}
	

	@RequestMapping( value="/like-feed" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView likeFeeds(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "property", required = false) String property,
			@RequestParam(value = "updateId", required = true) String feedId,
			@RequestParam(value = "feed_userId", required = true) String feedUserId,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();        
        LikeFeeds likeFeeds=null;
        String newUpdateId=null;
        PassUpdates passUpdates=null;
        String solrQuery=null;
        String likeId=null;
        String venueID=null;
		try{
            if(apikey ==null || apikey.equals("")){
                mav.addObject(ErrorCode.getCustomeError(104));
                return mav;
            }else if(token==null || token.equals("")){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(userId ==null || userId.equals("") || feedId ==null || feedId.equals("") ){
                mav.addObject(ErrorCode.getCustomeError(101));
                return mav;
            }    else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().size()<=0){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(apiDao.validateApiKey(apikey)){
            	solrQuery="id: "+feedId;
            	passUpdates=(PassUpdates) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_MANAGE_UPDATES_URL, PassUpdates.class, solrQuery, 0, 1, "", "", "false");           
            	if(passUpdates.getResponse().getNumFound()>0){
            		if (!mongoTemplate.collectionExists(LikeFeeds.class)) {
            			mongoTemplate.createCollection(LikeFeeds.class);
            		}	
            	BasicQuery query=new BasicQuery("{'feedId' : "+"'"+feedId+"'"+","+"'subType' : "+"'"+UpdateTypeEnum.UpdateSubType.like.toString()+"'"+"}");
    			newUpdateId=UUIDGenrator.getUniqueId();
            	likeFeeds=new LikeFeeds();
            	likeId=UUIDGenrator.getUniqueId();
            	likeFeeds.setLikeId(likeId);
            	likeFeeds.setFeedId(feedId);
            	likeFeeds.setUserId(userId);
            	likeFeeds.setType(UpdateTypeEnum.UpdateType.Feeds.toString());
            	likeFeeds.setSubType(UpdateTypeEnum.UpdateSubType.like.toString());
            	likeFeeds.setAddedDate(new Date());
            	mongoTemplate.insert(likeFeeds);
    				// Create Notification
            		for(Updates updates:passUpdates.getResponse().getDocs()){
            			venueID=updates.getVenueId();
            			String cVenueId=updates.getContent_id();
            			solrDocument=new SolrInputDocument();
        				newUpdateId=UUIDGenrator.getUniqueId();
        				solrDocument.addField("id", newUpdateId);
        				solrDocument.addField("type", UpdateTypeEnum.UpdateType.Notifications.toString());
        				solrDocument.addField("subType", UpdateTypeEnum.UpdateSubType.like.toString());	
        				if(property==null||property.equals("")){
        					solrDocument.addField("property", UpdateTypeEnum.feedsUpdateProperty.feedsLike);
        				}else{
        					solrDocument.addField("property",UpdateTypeEnum.feedsUpdateProperty.feedsLike+" for "+property);
        				}
        				if(userId.equals(feedUserId)){
        					solrDocument.addField("subTypes",UpdateTypeEnum.UpdateSubTypes.USEROWN);
        				}
        				solrDocument.addField("privacy", MagicNumbers.ACTIVE);
        				solrDocument.addField("toUser", feedUserId);	
        				solrDocument.addField("fromUser", userId);
        				solrDocument.addField("update_id", feedId);		
        				if(!(venueID==null||venueID.equals("undefined"))){
        					solrDocument.addField("venue_id", venueID);
        				}
        				if(!(cVenueId==null||cVenueId.equals("undefined"))){
        					solrDocument.addField("venue_id", cVenueId);
        				}
        				solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
        				solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
            		
            	}
    				String likesQuery = "subType:like AND update_id:"+feedId;
					PassUpdates commentsLike = (PassUpdates) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_MANAGE_UPDATES_URL,
								PassUpdates.class, likesQuery, 0, 1, "", "", "false");
					mav.addObject("isLikeUserFlag",commentsLike.getResponse().numFound);
                	mav.addObject("likes",mongoTemplate.count(query, LikeFeeds.class));
                	mav.addObject("Feed",passUpdates.getResponse().getDocs());
            		return mav;
            	}else{
            		mav.addObject(ErrorCode.getCustomeError(5001));
            		return mav;
            	}   
            }else{				
				mav.addObject(ErrorCode.getCustomeError(100));	
				return mav;
			}	
			
		}catch (Exception e) {
			e.printStackTrace();
            mav.addObject(ErrorCode.getCustomeError(1002));
        }
			return mav;
	}
	@RequestMapping(value = "/get-like", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView getlikeFeeds(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "feedId", required = true) String feedId,
			@RequestParam(value = "limit", required = false) String limit,
			@RequestParam(value = "pageNumber", required = true) String pageNumber,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassUpdates passUpdates = null;
		String solrQuery=null;
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			}else if (userId == null || userId.equals("") || feedId == null || feedId.equals("") ) {
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;
			}else if (solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1,
							"user_id", "", "false").getResponse().getDocs()
					.size() <= 0) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (!solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1,
							"token", "", "false").getResponse().getDocs()
					.get(0).getToken().equals(token)) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				solrQuery="id: "+feedId;
            	passUpdates=(PassUpdates) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_MANAGE_UPDATES_URL, PassUpdates.class, solrQuery, 0, 1, "", "", "false");           
				if (passUpdates.getResponse().getNumFound() > 0) {
				

					int numOfRecords = DEFUALT_RESULT_SIZE;

					if ((null != limit && !"".equals(limit))
							&& !"0".equals(limit))
						numOfRecords = Integer.parseInt(limit);

					if ((null != pageNumber && !"".equals(pageNumber))
							&& !"0".equals(pageNumber)) {

						Pageable page = new PageRequest(
								(int) Integer.parseInt(pageNumber) - 1,
								numOfRecords);
						Page<LikeFeeds> products = likeFeedRep.findByFeedIdAndSubTypeLikeOrderByAddedDateDesc(
								feedId,UpdateTypeEnum.UpdateSubType.like.toString(), page);
						if (products.hasNext() || products.isFirst()
								|| products.isLast()) {
							if (products.hasContent())
								mav.addObject("likes", products);
						}
						return mav;
					} else
						mav.addObject("1010",
								"Page Number should not be (null,0,blank)");

				}else{
	        		mav.addObject(ErrorCode.getCustomeError(5001));
	        		return mav;
	        	} 
			}  else {
				mav.addObject(ErrorCode.getCustomeError(100));
				return mav;
				}
			
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
		}
		return mav;
	}
	
	@RequestMapping( value="/comment-on-feed" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView commentOnFeeds(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "feed_userId", required = true) String feedUserId,
			@RequestParam(value = "feedId", required = true) String feedId,
			@RequestParam(value = "commetText", required = true) String commentText,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "property", required = false) String property,
			@RequestParam(value = "userIds", required = false) String userIds,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();        
        
        PassUpdates passUpdates=null;
        String solrQuery=null;
        String newUpdateId=null;
        String updateId=null;
		String commentId=null;
		boolean commentAddStatus=false;
		String venueID=null;
		String cVenueId=null;
		try{
            if(apikey ==null || apikey.equals("")){
                mav.addObject(ErrorCode.getCustomeError(104));
                return mav;
            }else if(token==null || token.equals("")){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(userId ==null || userId.equals("") || feedId ==null || feedId.equals("") || commentText ==null || commentText.equals("") || feedUserId ==null || feedUserId.equals("") ){
                mav.addObject(ErrorCode.getCustomeError(101));
                return mav;
            }else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().size()<=0){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(apiDao.validateApiKey(apikey)){
            	solrQuery="id: "+feedId;
            	passUpdates=(PassUpdates) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_MANAGE_UPDATES_URL, PassUpdates.class, solrQuery, 0, 1, "", "", "false");           
            	if(passUpdates.getResponse().getNumFound()>0){
            		venueID=passUpdates.getResponse().getDocs().get(0).getVenueId();
            		cVenueId=passUpdates.getResponse().getDocs().get(0).getContent_id();
            		
            		commentId=UUIDGenrator.getUniqueId();
					solrDocument=new SolrInputDocument();
					solrDocument.addField("comment_id", commentId);
					solrDocument.addField("type", UpdateTypeEnum.UpdateType.Feeds.toString());
					solrDocument.addField("feed_id", feedId);
					solrDocument.addField("user_id", userId);
					solrDocument.addField("from_user", feedUserId);
					solrDocument.addField("comment_txt", commentText);				
					solrDocument.addField("comment_added_date",  SolrDateUtil.addDateToSolrWithUtilDate(new Date()));				
					commentAddStatus=solrCommonClient.addObjectToSolr(UrlConstant.COMMENT_URL , solrDocument);
					if(commentAddStatus){
            		mav.addObject("Message","Comments Added Sucessfully");
    				mav.addObject("feed_id",feedId);
    				mav.addObject("Comment_id",commentId);
    				mav.addObject("user_id",userId);
    				mav.addObject("comment_text",commentText);     
					}
    				//Create Notification
    				newUpdateId=UUIDGenrator.getUniqueId();
					solrDocument=new SolrInputDocument();
					solrDocument.addField("id", newUpdateId);
    				solrDocument.addField("type", UpdateTypeEnum.UpdateType.Notifications.toString());
    				if(property==null||property.equals("")){
    					solrDocument.addField("property", UpdateTypeEnum.feedsUpdateProperty.comments.toString());
    				}else{
    					solrDocument.addField("property", property);
    				}	
    				solrDocument.addField("subType", UpdateTypeEnum.feedsUpdateProperty.comments.toString());
    				if(userId.equals(feedUserId)){
    					solrDocument.addField("subTypes",UpdateTypeEnum.UpdateSubTypes.USEROWN);
    				}
    				solrDocument.addField("toUser", feedUserId);
    				solrDocument.addField("comment_id", commentId);
    				solrDocument.addField("fromUser", userId);
    				solrDocument.addField("privacy", MagicNumbers.ACTIVE);
    				if(!(venueID==null||venueID.equals("undefined"))){
    					solrDocument.addField("venue_id", venueID);
    				}
    				if(!(cVenueId==null||cVenueId.equals("undefined"))){
    					solrDocument.addField("venue_id", cVenueId);
    				}
    				solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
    				solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
    		    	
    				// Code for sending notification when some one like your bio
/*    				
    				for(Updates notifyUpdates:passUpdates.getResponse().getDocs()){    					
    					if(notifyUpdates.getSubType().equals(UpdateTypeEnum.UpdateSubType.BioUpdate.toString()))
        				{
    						 updateId = UUIDGenrator.getUniqueId();
    							solrDocument = new SolrInputDocument();
    							solrDocument.addField("id", updateId);
    							solrDocument.addField("type",UpdateTypeEnum.UpdateType.Notifications.toString());
    							solrDocument.addField("subType",UpdateTypeEnum.UpdateSubType.like.toString());
    							solrDocument.addField("property",UpdateTypeEnum.feedsUpdateProperty.ProfileUpdate.toString());
    							solrDocument.addField("update_id", updateId);
    							if(userId.equals(notifyUpdates.getToUser())){
    								solrDocument.addField("subTypes",UpdateTypeEnum.UpdateSubTypes.USEROWN);
    							}
    							solrDocument.addField("fromUser", userId);
    							solrDocument.addField("toUser", notifyUpdates.getToUser());    							
    							solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL,solrDocument);    						
        				}
    				}*/
    				if(userIds != null && !userIds.equals("")) {
    					//Tag Friends
    					if (!mongoTemplate.collectionExists(TagFriends.class)) {
    	        			mongoTemplate.createCollection(TagFriends.class);
    	        		}
    	        		TagFriends tagFriends=new TagFriends();
    	        		String tagId=UUIDGenrator.getUniqueId();
    	        		tagFriends.setTagId(tagId);
    	        		tagFriends.setUserIds(userIds);
    	        		tagFriends.setCommentId(commentId);
    	        		tagFriends.setFeedId(feedId);
    	        		tagFriends.setType("Feed");
    	            	mongoTemplate.insert(tagFriends);
    	            	//Create Notification
    	            	String id = null;
    	            	String[] userIdArray = userIds.split(",");
    	            	for(String userIdVal : userIdArray) {
    	            		solrDocument=new SolrInputDocument();
    	            		id=UUIDGenrator.getUniqueId();
    	    				solrDocument.addField("id", id);
    	    				solrDocument.addField("type", UpdateTypeEnum.UpdateType.Notifications.toString());
    	    				solrDocument.addField("subType", UpdateTypeEnum.UpdateSubType.Tagged.toString());	
    	    				if(property==null||property.equals("")){
    	    					solrDocument.addField("property", UpdateTypeEnum.UpdateSubType.Tagged.toString());
    	    				}else{
    	    					solrDocument.addField("property", property);
    	    				}	
    	    				solrDocument.addField("privacy", MagicNumbers.ACTIVE);
    	    				solrDocument.addField("toUser", userIdVal);	
    	    				solrDocument.addField("fromUser", userId);
    	    				if(userId.equals(userIdVal)){
    	    					solrDocument.addField("subTypes",UpdateTypeEnum.UpdateSubTypes.USEROWN);
    	    				}
    	    				if(!(venueID==null||venueID.equals("undefined"))){
    	    					solrDocument.addField("venue_id", venueID);
    	    				}
    	    				if(!(cVenueId==null||cVenueId.equals("undefined"))){
    	    					solrDocument.addField("venue_id", cVenueId);
    	    				}
    	    				solrDocument.addField("comment_id", commentId);		
    	    				solrDocument.addField("update_id", feedId);			
    	    				solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
    	    				solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
    	            	}
    				}
	   				return mav;
            	}else{
            		mav.addObject(ErrorCode.getCustomeError(5001));
            		return mav;
            	}   
            }else{				
				mav.addObject(ErrorCode.getCustomeError(100));	
				return mav;
			}	
			
		}catch (Exception e) {
			e.printStackTrace();
            mav.addObject(ErrorCode.getCustomeError(1002));
        }
			return mav;
	}
		
	@RequestMapping(value = "/get-feed-comments", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getFeedComments(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "feedId", required = true) String feedId,
			@RequestParam(value = "limit", required = false) String limit,
			@RequestParam(value = "pageNumber", required = true) String pageNumber,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassUpdates passUpdates = null;
		String solrQuery = null;
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (userId == null || userId.equals("") || feedId == null
					|| feedId.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;
			} else if (solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1,
							"user_id", "", "false").getResponse().getDocs()
					.size() <= 0) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (!solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1,
							"token", "", "false").getResponse().getDocs()
					.get(0).getToken().equals(token)) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				solrQuery = "id: " + feedId;
				passUpdates = (PassUpdates) solrCommonClient.commonSolrSearch(
						UrlConstant.SEARCH_MANAGE_UPDATES_URL,
						PassUpdates.class, solrQuery, 0, 1, "", "", "false");
				if (passUpdates.getResponse().getNumFound() > 0) {

					int numOfRecords = DEFUALT_RESULT_SIZE;

					if ((null != limit && !"".equals(limit))
							&& !"0".equals(limit))
						numOfRecords = Integer.parseInt(limit);

					if ((null != pageNumber && !"".equals(pageNumber))
							&& !"0".equals(pageNumber)) {

						Pageable page = new PageRequest(
								(int) Integer.parseInt(pageNumber) - 1,
								numOfRecords);
						Page<LikeFeeds> products = likeFeedRep
								.findByFeedIdAndSubTypeLikeOrderByAddedDateDesc(
										feedId,UpdateTypeEnum.feedsUpdateProperty.comments.toString(), page);
						if (products.hasNext() || products.isFirst()
								|| products.isLast()) {
							if (products.hasContent()) {
								mav.addObject("likes", products);
							} else {
								mav.addObject("likes", "0");
							}
						}
						return mav;
					} else
						mav.addObject("1010",
								"Page Number should not be (null,0,blank)");

				} else {
					mav.addObject(ErrorCode.getCustomeError(5001));
					return mav;
				}
			} else {
				mav.addObject(ErrorCode.getCustomeError(100));
				return mav;
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
		}
		return mav;
	}
	
	@RequestMapping(value = "/get-feed-Details", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getFeedDetailsById(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "feedId", required = true) String feedId,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassUpdates passUpdates = null;
		String solrQuery = null;
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (userId == null || userId.equals("") || feedId == null
					|| feedId.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;
			} else if (solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1,
							"user_id", "", "false").getResponse().getDocs()
					.size() <= 0) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (!solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1,
							"token", "", "false").getResponse().getDocs()
					.get(0).getToken().equals(token)) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				solrQuery = "id: " + feedId;
				passUpdates = (PassUpdates) solrCommonClient.commonSolrSearch(
						UrlConstant.SEARCH_MANAGE_UPDATES_URL,
						PassUpdates.class, solrQuery, 0, 1, "", "", "false");
				if (passUpdates.getResponse().getNumFound() > 0) {
					String query = "subType:like AND update_id:"+feedId;
					 PassUpdates commentsLike = (PassUpdates) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_MANAGE_UPDATES_URL,
								PassUpdates.class, query, 0, 1, "", "", "false");
					 //				User Like Flag
					 BasicQuery isLikeFlag=new BasicQuery("{'feedId' : "+"'"+feedId+"'"+","+"'userId' : "+"'"+userId+"'"+","+"'subType' : "+"'"+UpdateTypeEnum.UpdateSubType.like.toString()+"'"+"}");
						//			User Spam Flag
					String spam = "user_id:"+userId+" AND content_type:spam AND type:feed AND content_id:"+feedId;
					PassContent spamFlag=(PassContent)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_CONTENT_URL,
								PassContent.class, spam, 0, 1, "", "", "false");
					 //			User inappropriate Flag
					 String inappropriate = "user_id:"+userId+" AND content_type:inappropriate AND type:feed AND content_id:"+feedId;
					PassContent inappropriateFlag=(PassContent)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_CONTENT_URL,
								PassContent.class, spam, 0, 1, "", "", "false");
					 // 		total Comments count
					BasicQuery queryComments=new BasicQuery("{'feedId' : "+"'"+feedId+"'"+","+"'subType' : "+"'"+UpdateTypeEnum.feedsUpdateProperty.comments.toString()+"'"+"}");
                	if(passUpdates.getResponse().numFound>0){
                		String venueID=passUpdates.getResponse().getDocs().get(0).getVenueId();
                		String queryVenue="id:"+venueID+" OR content_id:"+venueID;
        				PassBinary imageIds=(PassBinary)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_PHOTO_URL, PassBinary.class, queryVenue,0,10, "id", "", "false");
        				if(imageIds.getResponse().numFound>0)
        					mav.addObject("Image", imageIds.getResponse().getDocs().get(0).id);
                	}
                	
                	mav.addObject("Feed", passUpdates);
					mav.addObject("spamFlag",spamFlag.getResponse().numFound);
					mav.addObject("inappropriateFlag",inappropriateFlag.getResponse().numFound);
					mav.addObject("comments",mongoTemplate.count(queryComments, LikeFeeds.class));
					mav.addObject("isLike",mongoTemplate.count(isLikeFlag, LikeFeeds.class));
					mav.addObject("likes",commentsLike.getResponse().numFound);
				}else{
					mav.addObject(ErrorCode.getCustomeError(5001));
					return mav;
				}
			} else {
				mav.addObject(ErrorCode.getCustomeError(100));
				return mav;
			}

		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
		}
		return mav;
	}
	@RequestMapping(value = "/delete-user-update-by-id",method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView deleteUserUpdateById(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "updateId", required = true) String updateId,
			@RequestParam(value = "token", required = true) String token,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String query = null;
		boolean isDeleted = false;
		try {
			if (apikey == null || apikey.equals("") || userId == null
					|| userId.equals("") ||updateId == null || updateId.equals("")|| token == null
					|| token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1,
							"user_id", "", "false").getResponse().getDocs()
					.size() <= 0) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (!solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1,
							"token", "", "false").getResponse().getDocs()
					.get(0).getToken().equals(token)) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			}
			else if (apiDao.validateApiKey(apikey)) {
							query = "id:" + updateId;
							isDeleted = solrCommonClient.deleteObject(
									UrlConstant.MANAGE_UPDATES_URL, query);
							if (isDeleted){
								mongoCommonClient.deleteObject(updateId,"feedId",LikeFeeds.class);
								mav.addObject("message",
										Message.getCustomeMessages(8));
							}
							else
								mav.addObject("message",
										"Error Occured to delete ");
							return mav;
						} else {
							mav.addObject(ErrorCode.getCustomeError(3008));
							return mav;
						}
					
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
			
		}
		return mav;
	}

	@RequestMapping(value = "/delete-feed-comment", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView deleteFeedCommentById(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "updateId", required = true) String updateId,
			@RequestParam(value = "id", required = true) String Id,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "token", required = true) String token,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassUpdates passUpdates = null;
		String solrQuery=null;
		boolean commentDelStatus=false;	
		try {
			if (apikey == null || apikey.equals("") || userId == null
					|| userId.equals("") || updateId == null
					|| updateId.equals("") || token == null || token.equals("")
					|| Id == null || Id.equals("") || type == null
					|| type.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1,
							"user_id", "", "false").getResponse().getDocs()
					.size() <= 0) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (!solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1,
							"token", "", "false").getResponse().getDocs()
					.get(0).getToken().equals(token)) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				solrQuery="feed_id:"+updateId+" AND comment_id:"+Id+" AND user_id:"+userId+" AND type:"+type;
				commentDelStatus=solrCommonClient.deleteObject(UrlConstant.COMMENT_URL , solrQuery);
				if(commentDelStatus) {
					mav.addObject("Comment Delete Succesfully");
				} else {
					mav.addObject("Issue while deleting comment");		
				}
				
			} else {
				mav.addObject(ErrorCode.getCustomeError(100));
				return mav;
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
		}
		return mav;
	}
	
	
	@RequestMapping( value="/like-feed-comment" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView likeFeedsComment(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "feedId", required = true) String feedId,
			@RequestParam(value = "commentId", required = true) String commentId,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();        
        LikeFeeds likeFeeds=null;
        PassUpdates passUpdates=null;
        String solrQuery=null;
        String likeId=null;
		try{
            if(apikey ==null || apikey.equals("")){
                mav.addObject(ErrorCode.getCustomeError(104));
                return mav;
            }else if(token==null || token.equals("")){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(userId ==null || userId.equals("") || feedId ==null || feedId.equals("") || commentId==null || commentId.equals("")){
                mav.addObject(ErrorCode.getCustomeError(101));
                return mav;
            }else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().size()<=0){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(apiDao.validateApiKey(apikey)){
            	solrQuery="id: "+feedId+ "AND comment_id:"+commentId;            	
            	passUpdates=(PassUpdates) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_MANAGE_UPDATES_URL, PassUpdates.class, solrQuery, 0, 1, "", "", "false");           
            	if(passUpdates.getResponse().getNumFound()>0){
            		if (!mongoTemplate.collectionExists(LikeFeeds.class)) {
            			mongoTemplate.createCollection(LikeFeeds.class);
            		}
            		likeFeeds=new LikeFeeds();
                	likeId=UUIDGenrator.getUniqueId();
                	likeFeeds.setLikeId(likeId);
                	likeFeeds.setFeedId(feedId);
                	likeFeeds.setCommentId(commentId);
                	likeFeeds.setUserId(userId);
                	likeFeeds.setType(UpdateTypeEnum.UpdateType.Feeds.toString());
                	likeFeeds.setSubType(UpdateTypeEnum.feedsUpdateProperty.commentsLike.toString());
                	likeFeeds.setAddedDate(new Date());
                	mongoTemplate.insert(likeFeeds);
    			//	BasicQuery query=new BasicQuery("{'feedId' : "+"'"+feedId+"'"+","+"'subType' : "+"'"+UpdateTypeEnum.feedsUpdateProperty.commentsLike.toString()+"'"+"}");
    				Query query=new Query();
    			//	query.addCriteria(Criteria.where("feedId").is(feedId).and("comment_id").is(commentId).and("subType").is("commentsLike"));
    				
    				query.addCriteria(
    						Criteria.where("feedId").is(feedId)
    						.andOperator(Criteria.where("commentId").is(commentId)
    						.andOperator(Criteria.where("subType").is("commentsLike"))
    								));
    				
    				mongoTemplate.count(query, LikeFeeds.class);
                	mav.addObject("likes",mongoTemplate.count(query, LikeFeeds.class));
                	mav.addObject("Feed",passUpdates.getResponse().getDocs());
                	
            		return mav;
            	}else{
            		mav.addObject(ErrorCode.getCustomeError(5001));
            		return mav;
            	}   
            }else{				
				mav.addObject(ErrorCode.getCustomeError(100));	
				return mav;
			}	
			
		}catch (Exception e) {
			e.printStackTrace();
            mav.addObject(ErrorCode.getCustomeError(1002));
        }
			return mav;
	}
	@RequestMapping( value="/mark-as-read-not" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView markAsReadNotification(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "feedId", required = true) String id,
			@RequestParam(value = "flag", required = true) String flag,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		String updateId=null;
		try{
            if(apikey ==null || apikey.equals("")){
                mav.addObject(ErrorCode.getCustomeError(104));
                return mav;
            }else if(token==null || token.equals("")){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(userId ==null || userId.equals("") || id ==null || id.equals("") || flag==null || flag.equals("")){
                mav.addObject(ErrorCode.getCustomeError(101));
                return mav;
            }else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().size()<=0){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(apiDao.validateApiKey(apikey)){
            	String[] idArray = id.split(",");
            	for(String idVal : idArray){
            	solrDocument=new SolrInputDocument();
    		    updateId=UUIDGenrator.getUniqueId();
				solrDocument.addField("id", updateId);
            	solrDocument.addField("userId", userId);
				solrDocument.addField("feedId", idVal);
				solrDocument.addField("flag", flag);
				solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
            	solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_STATUS_URL , solrDocument);
            	}
            	mav.addObject("message", "status has been updated");
            	return mav;
            }
		}catch (Exception e) {
			e.printStackTrace();
            mav.addObject(ErrorCode.getCustomeError(1002));
            return mav;
        }
            	return mav; 
	
	}
	
	@RequestMapping( value="/share-feed-card" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView shareFeedCard(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "feed_id", required = true) String feedId,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "userIds", required = false) String userIds,
			@RequestParam(value = "type", required = true) String type,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView(); 
		   if(apikey ==null || apikey.equals("")){
               mav.addObject(ErrorCode.getCustomeError(104));
               return mav;
           }else if(token==null || token.equals("")){
               mav.addObject(ErrorCode.getCustomeError(99));
               return mav;
           }else if(userId ==null || userId.equals("") || feedId ==null || feedId.equals("") ){
               mav.addObject(ErrorCode.getCustomeError(101));
               return mav;
           }else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().size()<=0){
               mav.addObject(ErrorCode.getCustomeError(99));
               return mav;
           }else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){
               mav.addObject(ErrorCode.getCustomeError(99));
               return mav;
           }else if(apiDao.validateApiKey(apikey)){
        	   String  id= null;
        	   if(userIds == null || userIds.equals("")) {
        		    solrDocument=new SolrInputDocument();
        		    id=UUIDGenrator.getUniqueId();
					solrDocument.addField("id", id);
	   				solrDocument.addField("type", UpdateTypeEnum.UpdateType.Feeds.toString());
	   				solrDocument.addField("property", type);
	   				solrDocument.addField("subType", UpdateTypeEnum.UpdateSubType.Shared.toString());
	   				solrDocument.addField("privacy", MagicNumbers.ACTIVE);
	   				solrDocument.addField("toUser", userId);
	   				solrDocument.addField("update_id", id);
	   				solrDocument.addField("fromUser", userId);
	   				solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
	   				solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
	   			 mav.addObject("Shared with User",  userId);
        	   } else {
        		   String[] userIdArray = userIds.split(",");
        		   for(String userIdVal : userIdArray) {
        			   solrDocument=new SolrInputDocument();
            		    id=UUIDGenrator.getUniqueId();
    					solrDocument.addField("id", id);
    	   				solrDocument.addField("type", UpdateTypeEnum.UpdateType.Feeds.toString());
    	   				solrDocument.addField("property", type);
    	   				solrDocument.addField("subType", UpdateTypeEnum.UpdateSubType.Shared.toString());
    	   				solrDocument.addField("privacy", MagicNumbers.ACTIVE);
    	   				solrDocument.addField("toUser", userIdVal);
    	   				solrDocument.addField("update_id", id);
    	   				solrDocument.addField("fromUser", userId);
    	   				solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
    	   				solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
        		   }
        		   mav.addObject("Shared with Users",  userIds);
        	   }
           }
		return mav;
	}

	@RequestMapping( value="/delete-all-markNotification" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView deleteAllMarkNotification(			
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		boolean markNotificationDelStatus=false;		
		
		markNotificationDelStatus=solrCommonClient.deleteAllObject(UrlConstant.MANAGE_UPDATES_STATUS_URL);
		if(markNotificationDelStatus)
			mav.addObject("Mark Notification Delete Succesfully");
		else
			mav.addObject("Issue while deleting Mark Notification");		
		
		return mav;
		}	
	

	@RequestMapping( value="/get-user-feeds-details" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getUserFeedsDetails(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "searchName", required = false) String searchName,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
        PassUpdates passUpdates=null;
        PassFriendRequest friendListIds=null;
        StringBuilder friendList=new StringBuilder();
        StringBuilder flipfriendList=new StringBuilder();
        double averageRating = 0;
		double rating = 0;
        int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);		        
        String query=null;
        FeedDetails feedDetails=null;
		List<FeedDetails> feedde=new ArrayList<FeedDetails>();
        PassUsers passUsers=null;
        PassMarkNotification passMark=null;
        String userQuery=null;
        String userfromQuery=null;
        PassVenue passVenue=null;
		try{
            if(apikey ==null || apikey.equals("")){
                mav.addObject(ErrorCode.getCustomeError(104));
                return mav;
            }else if(token==null || token.equals("")){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(userId ==null || userId.equals("") ){
                mav.addObject(ErrorCode.getCustomeError(101));
                return mav;
            }    else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().size()<=0){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(apiDao.validateApiKey(apikey)){
            	query="to_user:"+userId+"*"+" AND "+"status:Accept";
				friendListIds=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, query, startElementSolr, rowsSolr, "", "", "false");
            	if(friendListIds.getResponse().numFound==0){
            		query="toUser:"+userId +" AND type:Feeds" ;                
            	}else{            		
            		for(FriendRequest friend:friendListIds.getResponse().getDocs() ){         			
            			friendList.append(" OR fromUser:"+friend.getFromUser());
            			flipfriendList.append(" OR toUser:"+friend.getFromUser());
            		}         
            		 query="( toUser:"+userId +friendList.toString()+" )  OR  " +"( fromUser:"+userId +flipfriendList.toString()+" )  AND type:Feeds" ;
            	}
            	if(type!= null && !type.equals("")) {
            		query = query + " AND "+"subType:"+type;
            	}
            	if(searchName!= null && !searchName.equals("")) {
            		query = query + " AND "+"search_name:"+searchName.trim()+"*";
            	}
            	query=query+" AND "+"privacy:"+MagicNumbers.ACTIVE;
            	passUpdates=(PassUpdates) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_MANAGE_UPDATES_URL, PassUpdates.class, query, startElementSolr, rowsSolr, "", "addedDate desc", "false");
            	for(Updates updateLocal: passUpdates.getResponse().getDocs()){	
                    //				toUser Object
                    userQuery="user_id:"+updateLocal.getToUser();
                    passUsers=(PassUsers)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL, PassUsers.class, userQuery, 0, 10, "", "", "false");
                    if(passUsers.getResponse().getDocs().size()>0){
                    updateLocal.setUsers(passUsers.getResponse().getDocs().get(0));
                    }
                    //        		fromUser Object
                    userfromQuery="user_id:"+updateLocal.getFromUser();
                    passUsers=(PassUsers)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL, PassUsers.class, userfromQuery, 0, 10, "", "", "false");
                    if(passUsers.getResponse().getDocs().size()>0){
                    updateLocal.setUsersFrom(passUsers.getResponse().getDocs().get(0));
                    }
                    //		Venue Object
                    userQuery="venue_id:"+updateLocal.getVenueId() +" OR venue_id:"+updateLocal.getContent_id();
                    passVenue=(PassVenue)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_VENUE_URL, PassVenue.class, userQuery, 0, 10, "", "", "false");
                    if(passVenue.getResponse().getDocs().size()>0){
                    updateLocal.setVenue(passVenue.getResponse().getDocs().get(0));
                    }
            		feedDetails=new FeedDetails();
            		feedDetails.setUpdates(updateLocal);
            		String feedId = updateLocal.getId();
            		 //				User Like Flag
      				 BasicQuery isLikeFlag=new BasicQuery("{'feedId' : "+"'"+feedId+"'"+","+"'userId' : "+"'"+userId+"'"+","+"'subType' : "+"'"+UpdateTypeEnum.UpdateSubType.like.toString()+"'"+"}");
      				 if(mongoTemplate.count(isLikeFlag, LikeFeeds.class)>0)
      				{
      					feedDetails.setIsLikeFlag("1");	
      				}else{
      					feedDetails.setIsLikeFlag("0");		
      				}
      				PassReview totalReview = (PassReview) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REVIEW_URL,PassReview.class, "venue_id:"+updateLocal.getVenueId(), 0,	1, "", "", "false");
					Query mongoQuery = new Query();
					mongoQuery.addCriteria(Criteria.where("venueId").is(updateLocal.getVenueId() ));
					List<VenueRating> venueRatingList = (List<VenueRating>) mongoCommonClient
							.findByQuery(mongoQuery, VenueRating.class);
					if (venueRatingList.size() > 0) {
						rating = venueRatingList.get(0).getRating();
						int totalNum = totalReview.getResponse().numFound;
						averageRating = (rating / totalNum);
						//		averageRating
						feedDetails.setAverageRating(averageRating);
						//		rating
						feedDetails.setRating(rating);
					}
      				 //				total Likes 
                 	BasicQuery isTotalLike=new BasicQuery("{'feedId' : "+"'"+feedId+"'"+","+"'subType' : "+"'"+UpdateTypeEnum.UpdateSubType.like.toString()+"'"+"}");
                 	feedDetails.setTotalLikes(mongoTemplate.count(isTotalLike, LikeFeeds.class));
   				 //			User Spam Flag
   				String spam = "user_id:"+userId+" AND content_type:spam AND type:feed AND content_id:"+feedId;
   				PassContent spamFlag=(PassContent)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_CONTENT_URL,
   							PassContent.class, spam, 0, 1, "", "", "false");
   				if(spamFlag.getResponse().numFound>0)
   				{
   					feedDetails.setSpamFlag("1");
   				}else{
   					feedDetails.setSpamFlag("0");
   				}
   				 //			User inappropriate Flag
   				String inappropriate = "user_id:"+userId+" AND content_type:inappropriate AND type:feed AND content_id:"+feedId;
   				PassContent inappropriateFlag=(PassContent)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_CONTENT_URL,
   							PassContent.class, inappropriate, 0, 1, "", "", "false");
   				if(inappropriateFlag.getResponse().numFound>0)
   				{
   					feedDetails.setInappropriate("1");
   				}else{
   					feedDetails.setInappropriate("0");
   				}
   				 // 		total Comments count
   				//BasicQuery queryComments=new BasicQuery("{'feedId' : "+"'"+feedId+"'"+","+"'subType' : "+"'"+UpdateTypeEnum.feedsUpdateProperty.comments.toString()+"'"+"}");
   				String queryComments = "type:Feeds AND feed_id:"+feedId;
   				PassFeedComment passFeedComment=(PassFeedComment)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_COMMENT_URL, PassFeedComment.class, queryComments, 0, 1, "", "", "false");
   				feedDetails.setComments(passFeedComment.getResponse().numFound);
            	
					 feedde.add(feedDetails);
				}
				mav.addObject("numFound",passUpdates.getResponse().numFound);	
				mav.addObject("Feeds",feedde);
            	return mav;
            }
		}catch (Exception e) {
			e.printStackTrace();
            mav.addObject(ErrorCode.getCustomeError(1002));
        }
			return mav;
	}
	/*
	 * @author Name:Gaurav chugh
	 * @Created Date:28/11/2014
	 * @update Date:28/11/2014
	 * @purpose:searchFeedCommentDetails
	 */
	@RequestMapping( value="/get-feed-comments-details" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView searchFeedCommentDetails(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "feedId", required = true) String feedId,		
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = false) String filterBy,
			@RequestParam(value = "sort_order", required = false) String sortOrder,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		PassFeedComment passFeedComment=null;
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);		
		FeedCommentDetails feedCommentDetails=null;
		List<FeedCommentDetails> feedCommentDetailsList=new ArrayList<FeedCommentDetails>();
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (userId == null || userId.equals("") || feedId == null
					|| feedId.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;
			} else if (solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1,
							"user_id", "", "false").getResponse().getDocs()
					.size() <= 0) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (!solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1,
							"token", "", "false").getResponse().getDocs()
					.get(0).getToken().equals(token)) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
		passFeedComment=(PassFeedComment) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_COMMENT_URL, PassFeedComment.class, "feed_id:"+feedId, startElementSolr, rowsSolr, filterBy, sortOrder, "false");		
		for(FeedComment feedCommentLocal:passFeedComment.getResponse().getDocs()){
			feedCommentDetails=new FeedCommentDetails();
			feedCommentDetails.setFeedComment(feedCommentLocal);
			 //			  User Flag on venue  
			BasicQuery isFlag=new BasicQuery("{'typeValue' : "+"'"+feedCommentLocal.getCommentId() +"'"+","+"'userId' : "+"'"+userId+"'"+","+"'typeName' : "+"'"+UpdateTypeEnum.FlagType.comment_id.toString()+"'"+"}");
			if(mongoTemplate.count(isFlag, Flag.class)!=0){
				feedCommentDetails.setIsFlag("1");
			}else{
				feedCommentDetails.setIsFlag("0");
			}
			
			feedCommentDetailsList.add(feedCommentDetails);
		}
		mav.addObject("numFound",passFeedComment.getResponse().numFound);
		mav.addObject("comments",feedCommentDetailsList);
		
		return mav;
	} else {
			mav.addObject(ErrorCode.getCustomeError(100));
			return mav;
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return mav;
}

	@RequestMapping( value="/like-feed-new" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView likeFeedsNew(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "property", required = false) String property,
			@RequestParam(value = "updateId", required = true) String feedId,
			@RequestParam(value = "feed_userId", required = true) String feedUserId,
			@RequestParam(value = "status", required = true) String status,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();        
        LikeFeeds likeFeeds=null;
        String newUpdateId=null;
        PassUpdates passUpdates=null;
        String solrQuery=null;
        String likeId=null;
        String venueID=null;

		try{
            if(apikey ==null || apikey.equals("")){
                mav.addObject(ErrorCode.getCustomeError(104));
                return mav;
            }else if(token==null || token.equals("")){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(userId ==null || userId.equals("") || feedId ==null || feedId.equals("") ){
                mav.addObject(ErrorCode.getCustomeError(101));
                return mav;
            }    else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().size()<=0){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){
                mav.addObject(ErrorCode.getCustomeError(99));
                return mav;
            }else if(apiDao.validateApiKey(apikey)){
            	solrQuery="id: "+feedId;
            	passUpdates=(PassUpdates) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_MANAGE_UPDATES_URL, PassUpdates.class, solrQuery, 0, 1, "", "", "false");           
            	if(passUpdates.getResponse().getNumFound()>0){
            		if (!mongoTemplate.collectionExists(LikeFeeds.class)) {
            			mongoTemplate.createCollection(LikeFeeds.class);
            		}
            		BasicQuery query=new BasicQuery("{'feedId' : "+"'"+feedId+"'"+","+"'subType' : "+"'"+UpdateTypeEnum.UpdateSubType.like.toString()+"'"+"}");
            		BasicQuery query1=new BasicQuery("{'feedId' : "+"'"+feedId+"'"+","+"userId : "+"'"+userId+"'"+","+"'subType' : "+"'"+UpdateTypeEnum.UpdateSubType.like.toString()+"'"+"}");            		
            		if (status.equals("0") || status.length() == 0) {
    					mongoTemplate.remove(query1, LikeFeeds.class);
    					solrQuery="update_id: "+feedId+" AND type:Notifications AND subType:like AND fromUser:"+userId;
    					solrCommonClient.deleteObject(UrlConstant.MANAGE_UPDATES_URL,solrQuery);
    					mav.addObject("Message :", "unLike successfully");
    				} else{
    		
    			newUpdateId=UUIDGenrator.getUniqueId();
            	likeFeeds=new LikeFeeds();
            	likeId=UUIDGenrator.getUniqueId();
            	likeFeeds.setLikeId(likeId);
            	likeFeeds.setFeedId(feedId);
            	likeFeeds.setUserId(userId);
            	likeFeeds.setType(UpdateTypeEnum.UpdateType.Feeds.toString());
            	likeFeeds.setSubType(UpdateTypeEnum.UpdateSubType.like.toString());
            	likeFeeds.setAddedDate(new Date());
            	mongoTemplate.insert(likeFeeds);
            	mav.addObject("Message :", "Like successfully");
    				// Create Notification
            		for(Updates updates:passUpdates.getResponse().getDocs()){
            			venueID=updates.getVenueId();
            			String cVenueId=updates.getContent_id();
            			solrDocument=new SolrInputDocument();
        				newUpdateId=UUIDGenrator.getUniqueId();
        				solrDocument.addField("id", newUpdateId);
        				solrDocument.addField("type", UpdateTypeEnum.UpdateType.Notifications.toString());
        				solrDocument.addField("subType", UpdateTypeEnum.UpdateSubType.like.toString());	
        				if(property==null||property.equals("")){
        					solrDocument.addField("property", UpdateTypeEnum.feedsUpdateProperty.feedsLike);
        				}else{
        					solrDocument.addField("property",UpdateTypeEnum.feedsUpdateProperty.feedsLike+" for "+property);
        				}
        				if(userId.equals(feedUserId)){
        					solrDocument.addField("subTypes",UpdateTypeEnum.UpdateSubTypes.USEROWN);
        				}
        				solrDocument.addField("privacy", MagicNumbers.ACTIVE);
        				solrDocument.addField("toUser", feedUserId);	
        				solrDocument.addField("fromUser", userId);
        				solrDocument.addField("update_id", feedId);		
        				if(!(venueID==null||venueID.equals("undefined"))){
        					solrDocument.addField("venue_id", venueID);
        				}
        				if(!(cVenueId==null||cVenueId.equals("undefined"))){
        					solrDocument.addField("venue_id", cVenueId);
        				}
        				solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
        				solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
            		}
    				}
					PassUpdates commentsLike = (PassUpdates) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_MANAGE_UPDATES_URL,
								PassUpdates.class, solrQuery, 0, 1, "", "", "false");
    				mav.addObject("isLikeUserFlag",commentsLike.getResponse().numFound);
            		mav.addObject("likes",mongoTemplate.count(query, LikeFeeds.class));
                	mav.addObject("Feed",passUpdates.getResponse().getDocs());
            		return mav;
    				
            	}else{
            		mav.addObject(ErrorCode.getCustomeError(5001));
            		return mav;
            	}   
            }else{				
				mav.addObject(ErrorCode.getCustomeError(100));	
				return mav;
			}	
			
		}catch (Exception e) {
			e.printStackTrace();
            mav.addObject(ErrorCode.getCustomeError(1002));
        }
			return mav;
	}
	
}

package com.main.interconnection.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.main.interconnection.clientBo.ErrorCode;
import com.main.interconnection.clientBo.Flag;
import com.main.interconnection.clientBo.VenueComment;
import com.main.interconnection.clientBo.VenueCommentDetails;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.mongoDao.MongoCommonClient;
import com.main.interconnection.solr.response.comment.PassComment;
import com.main.interconnection.solr.response.session.PassSession;
import com.main.interconnection.solr.response.users.PassUsers;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
import com.main.interconnection.util.MagicNumbers;
import com.main.interconnection.util.SolrDateUtil;
import com.main.interconnection.util.UUIDGenrator;
import com.main.interconnection.util.UpdateTypeEnum;


@Controller
@RequestMapping(value="/comment/*")
public class InterConnectionClientComment {
	
	
	@Autowired
	ApiDao apiDao;
	
	@Autowired
	SolrCommonClient solrCommonClient;
	
	@Autowired
	SolrInputDocument solrDocument;
	
	@Autowired
	MongoCommonClient mongoCommonClient;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@RequestMapping( value="/post-comment-on-review" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView postCommentOnReview(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "venue_id", required = true) String venueId,			
			@RequestParam(value = "review_id", required = true) String reviewId,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "coment_txt", required = true) String commentTxt,
			@RequestParam(value = "review_userId", required = true) String reviewUserId,
			@RequestParam(value = "userIds", required = false) String userIds,		
			HttpServletRequest request) {
			ModelAndView mav = new ModelAndView();		
			String commentId=null;
			String newUpdateId=null;
			boolean commentAddStatus=false;
			try{
				if(apikey ==null || apikey.equals("")){				
					mav.addObject(ErrorCode.getCustomeError(104));
					return mav;			
				}else if(token==null || token.equals("")){
					mav.addObject(ErrorCode.getCustomeError(99));
					return mav;					
				}else if(userId ==null || userId.equals("") || venueId ==null || venueId.equals("") || reviewId==null || reviewId.equals("")){
					mav.addObject(ErrorCode.getCustomeError(101));
					return mav;						
				}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().size()==0){				
					mav.addObject(ErrorCode.getCustomeError(99));
					return mav;	
				}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){
					mav.addObject(ErrorCode.getCustomeError(99));
					return mav;	
				}else if(apiDao.validateApiKey(apikey)){
					commentId=UUIDGenrator.getUniqueId();
					solrDocument=new SolrInputDocument();
					solrDocument.addField("comment_id", commentId);
					solrDocument.addField("venue_id", venueId);
					solrDocument.addField("review_id", reviewId);
					solrDocument.addField("user_id", userId);
					solrDocument.addField("from_user", reviewUserId);
					solrDocument.addField("comment_txt", commentTxt);				
					solrDocument.addField("comment_added_date",  SolrDateUtil.addDateToSolrWithUtilDate(new Date()));				
					commentAddStatus=solrCommonClient.addObjectToSolr(UrlConstant.COMMENT_URL , solrDocument);
					if(commentAddStatus){
						mav.addObject("comment" , "Comment added sucessful");
						mav.addObject("comment_id" , commentId);	
	    				//Create Notification
	    				newUpdateId=UUIDGenrator.getUniqueId();
						solrDocument=new SolrInputDocument();
						solrDocument.addField("id", newUpdateId);
	    				solrDocument.addField("type", UpdateTypeEnum.UpdateType.Notifications.toString());
	    				solrDocument.addField("property", UpdateTypeEnum.feedsUpdateProperty.VenueReview.toString());
	    				solrDocument.addField("subType", UpdateTypeEnum.feedsUpdateProperty.comments.toString());
	    				solrDocument.addField("toUser", reviewUserId);
	    				solrDocument.addField("venue_id", venueId);
						solrDocument.addField("review_id", reviewId);
	    				if(userId.equals(reviewUserId)){
	    					solrDocument.addField("subTypes",UpdateTypeEnum.UpdateSubTypes.USEROWN);
	    				}
						solrDocument.addField("comment_id", commentId);		
	    				solrDocument.addField("fromUser",userId );
	    				solrDocument.addField("privacy", MagicNumbers.ACTIVE);
	    				solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
	    				solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
	    				
	    				//
						/*if(userIds == null || userIds.equals("")) {
							//Create Notification
							solrDocument=new SolrInputDocument();
		            		id=UUIDGenrator.getUniqueId();
		    				solrDocument.addField("id", id);
		    				solrDocument.addField("type", UpdateTypeEnum.UpdateType.Notifications.toString());
		    				solrDocument.addField("subType", UpdateTypeEnum.UpdateSubType.Tagged.toString());	
		    				solrDocument.addField("property", UpdateTypeEnum.UpdateSubType.Tagged.toString());
		    				solrDocument.addField("privacy", MagicNumbers.ACTIVE);
		    				solrDocument.addField("toUser", userId);	
		    				solrDocument.addField("fromUser", userId);
		    				solrDocument.addField("comment_id", commentId);		
		    				solrDocument.addField("review_id", reviewId);			
		    				solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
		    				solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
						} else {
							//Tag Friends
							if (!mongoTemplate.collectionExists(TagFriends.class)) {
			        			mongoTemplate.createCollection(TagFriends.class);
			        		}
			        		TagFriends tagFriends=new TagFriends();
			        		String tagId=UUIDGenrator.getUniqueId();
			        		tagFriends.setTagId(tagId);
			        		tagFriends.setUserIds(userIds);
			        		tagFriends.setCommentId(commentId);
			        		tagFriends.setReviewId(reviewId);
			        		tagFriends.setType("Review");
			            	mongoTemplate.insert(tagFriends);
			            	//Create Notification
			            	String[] userIdArray = userIds.split(",");
			            	for(String userIdVal : userIdArray) {
			            		solrDocument=new SolrInputDocument();
			            		id=UUIDGenrator.getUniqueId();
			    				solrDocument.addField("id", id);
			    				solrDocument.addField("type", UpdateTypeEnum.UpdateType.Notifications.toString());
			    				solrDocument.addField("subType", UpdateTypeEnum.UpdateSubType.Tagged.toString());	
			    				solrDocument.addField("property", UpdateTypeEnum.UpdateSubType.Tagged.toString());
			    				solrDocument.addField("privacy", MagicNumbers.ACTIVE);
			    				solrDocument.addField("toUser", userIdVal);	
			    				solrDocument.addField("fromUser", userId);
			    				solrDocument.addField("comment_id", commentId);		
			    				solrDocument.addField("review_id", reviewId);			
			    				solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
			    				solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
			            	}
						}*/
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
	
	
	@RequestMapping( value="/search-comment" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView searchComment(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = false) String filterBy,
			@RequestParam(value = "sort_order", required = true) String sortOrder,
			@RequestParam(value = "query", required = false) String query,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		PassComment passComment=new PassComment();
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);		
		passComment=(PassComment) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_COMMENT_URL, PassComment.class, query, startElementSolr, rowsSolr, filterBy, sortOrder, "false");		
		mav.addObject(passComment);
		mav.addObject("comment_flag", "0");
		return mav;
		}
	
	@RequestMapping( value="/delete-all-comment" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView deleteAllComment(			
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		boolean commentDelStatus=false;		
		
		commentDelStatus=solrCommonClient.deleteAllObject(UrlConstant.COMMENT_URL);
		if(commentDelStatus)
			mav.addObject("Comment Delete Succesfully");
		else
			mav.addObject("Issue while deleting comment");		
		
		return mav;
		}
	
	@RequestMapping( value="/delete-comment-by-query" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView deleteCommentByQuery(@RequestParam(value = "api_key", required = false) String apiKey,		
			@RequestParam(value = "query", required = false) String query,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		if(apiKey ==null || apiKey.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;			
		} else if(query == null || query.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;
		} else if(apiDao.validateApiKey(apiKey)){
			boolean commentDelStatus=false;		
			commentDelStatus=solrCommonClient.deleteObject(UrlConstant.COMMENT_URL , query);
			if(commentDelStatus) {
				mav.addObject("Comment Delete Succesfully");
			} else {
				mav.addObject("Issue while deleting comment");		
			}
		}
		return mav;
	}
	
	@RequestMapping( value="/flag-comment" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView flagComment(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "comment_id", required = true) String commentId,	
			@RequestParam(value = "is_flagged", required = true) String isFlagged,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		if(apikey ==null || apikey.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;			
		}else if(token==null || token.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;					
		}else if(userId ==null || userId.equals("") || commentId ==null || commentId.equals("")
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
			query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("typeName").is(UpdateTypeEnum.FlagType.comment_id).andOperator(
					Criteria.where("typeValue").is(commentId))));
			flagCommentList = (List<Flag>) mongoCommonClient.findByQuery(query, Flag.class);
			if(flagCommentList.size() > 0) {
				Update update = new Update();
				update.set("isFlagged", isFlagged);
				mongoCommonClient.saveOrUpdate(Query.query(Criteria.where("userId").is(userId).and("typeName").is(UpdateTypeEnum.FlagType.comment_id.toString()).
						and("typeValue").is(commentId)), Flag.class, update);
				mav.addObject("Comment_Flagged", isFlagged);
				mav.addObject("comment_id", commentId);
				return mav;
			} else {
				Flag flagVenue = new Flag();
				flagVenue.setAdminApproved("1");
				flagVenue.setIsFlagged(isFlagged);
				flagVenue.setUserId(userId);
				flagVenue.setTypeName(UpdateTypeEnum.FlagType.comment_id.toString());
				flagVenue.setTypeValue(commentId);
				flagVenue.setSubType("none");
				mongoTemplate.insert(flagVenue);
				mav.addObject("Comment_Flagged", isFlagged);
				mav.addObject("comment_id", commentId);
				return mav;
			}
		}
		return mav;
	}
	/*
	 * @author Name:Gaurav chugh
	 * @Created Date:15/11/2014
	 * @update Date:15/11/2014
	 * @purpose:searchCommentDetails
	 */
	
	@RequestMapping( value="/search-comment-details" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView searchCommentDetails(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = false) String filterBy,
			@RequestParam(value = "sort_order", required = true) String sortOrder,
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "userId", required = true) String userId,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		PassComment passComment=new PassComment();
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);		
		VenueCommentDetails venueCommentDetails=null;
		List<VenueCommentDetails> venueCommentDetailsList=new ArrayList<VenueCommentDetails>();
		String userQuery=null;
		PassUsers passUsers=null;
		passComment=(PassComment) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_COMMENT_URL, PassComment.class, query, startElementSolr, rowsSolr, filterBy, sortOrder, "false");		
		
		if(passComment.getResponse().getDocs().size()>0){
		for(VenueComment venueCommentLocal:passComment.getResponse().getDocs()){
			venueCommentDetails=new VenueCommentDetails();
			venueCommentDetails.setVenueComment(venueCommentLocal);
            //				toUser Object
            userQuery="user_id:"+venueCommentLocal.getUserId();
            passUsers=(PassUsers)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL, PassUsers.class, userQuery, 0, 10, "", "", "false");
            if(passUsers.getResponse().getDocs().size()>0){
            	venueCommentLocal.setUsers(passUsers.getResponse().getDocs().get(0));
            }
			 //			  User Flag on venue  
			BasicQuery isFlag=new BasicQuery("{'typeValue' : "+"'"+venueCommentLocal.getCommentId() +"'"+","+"'userId' : "+"'"+userId+"'"+","+"'typeName' : "+"'"+UpdateTypeEnum.FlagType.comment_id.toString()+"'"+"}");
			if(mongoTemplate.count(isFlag, Flag.class)!=0){
				venueCommentDetails.setIsFlag("1");
			}else{
				venueCommentDetails.setIsFlag("0");
			}
			
			venueCommentDetailsList.add(venueCommentDetails);
		}
	}
		mav.addObject("numFound",passComment.getResponse().numFound);
		mav.addObject("venueCommentDetails",venueCommentDetailsList);
		
		return mav;
		}
}

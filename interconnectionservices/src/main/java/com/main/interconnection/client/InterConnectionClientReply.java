package com.main.interconnection.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.main.interconnection.clientBo.ErrorCode;
import com.main.interconnection.clientBo.Flag;
import com.main.interconnection.clientBo.User;
import com.main.interconnection.clientBo.VenueCommentReply;
import com.main.interconnection.clientBo.VenueCommentReplyDetails;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.solr.response.reply.PassReply;
import com.main.interconnection.solr.response.session.PassSession;
import com.main.interconnection.solr.response.users.PassUsers;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
import com.main.interconnection.util.MagicNumbers;
import com.main.interconnection.util.SolrDateUtil;
import com.main.interconnection.util.UUIDGenrator;
import com.main.interconnection.util.UpdateTypeEnum;


@Controller
@RequestMapping(value="/reply/*")
public class InterConnectionClientReply {
	
	@Autowired
	ApiDao apiDao;
	
	@Autowired
	SolrCommonClient solrCommonClient;
	
	SolrInputDocument solrDocument;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@RequestMapping( value="/post-reply-on-comment" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView postReplyOnComment(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "venue_id", required = true) String venueId,			
			@RequestParam(value = "review_id", required = true) String reviewId,
			@RequestParam(value = "comment_id", required = true) String commentId,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "comment_userId", required = true) String commentUserId,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "reply_txt", required = true) String replyTxt,				
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();		
		String replyId=null;
		boolean commentAddStatus=false;
			String newUpdateId=null;
		try{
			
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(token==null || token.equals("")){
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;					
			}else if(userId ==null || userId.equals("") || venueId ==null || venueId.equals("") || reviewId==null || reviewId.equals("")  || commentId==null || commentId.equals("")){
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().size()==0){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(apiDao.validateApiKey(apikey)){
				replyId=UUIDGenrator.getUniqueId();				
				solrDocument=new SolrInputDocument();
				solrDocument.addField("reply_id", replyId);
				solrDocument.addField("venue_id", venueId);				
				solrDocument.addField("comment_id", commentId);				
				solrDocument.addField("review_id", reviewId);				
				solrDocument.addField("user_id", userId);
				solrDocument.addField("from_user", commentUserId);	
				solrDocument.addField("reply_txt", replyTxt);				
				solrDocument.addField("reply_added_date",  SolrDateUtil.addDateToSolrWithUtilDate(new Date()));				
				commentAddStatus=solrCommonClient.addObjectToSolr(UrlConstant.REPLY_URL , solrDocument);
/*				//Create Notification
				newUpdateId=UUIDGenrator.getUniqueId();
				solrDocument=new SolrInputDocument();
				solrDocument.addField("id", newUpdateId);
				solrDocument.addField("type", UpdateTypeEnum.UpdateType.Notifications.toString());
				solrDocument.addField("property", UpdateTypeEnum.feedsUpdateProperty.reply.toString());
				solrDocument.addField("subType", UpdateTypeEnum.feedsUpdateProperty.VenueReview.toString());
				solrDocument.addField("toUser", commentUserId);
				solrDocument.addField("fromUser", userId);
				solrDocument.addField("venue_id", venueId);
				if(userId.equals(commentUserId)){
					solrDocument.addField("subTypes",UpdateTypeEnum.UpdateSubTypes.USEROWN);
				}
				solrDocument.addField("review_id", reviewId);
				solrDocument.addField("comment_id", commentId);	
				solrDocument.addField("reply_id", replyId);
				solrDocument.addField("privacy", MagicNumbers.ACTIVE);
				solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
				solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
				*/
				if(commentAddStatus){
					mav.addObject("reply" , "Reply added sucessful");
					mav.addObject("reply_id" , replyId);	
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
	
	
	@RequestMapping( value="/search-reply" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView searchReply(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = false) String filterBy,
			@RequestParam(value = "sort_order", required = true) String sortOrder,
			@RequestParam(value = "query", required = false) String query,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		PassReply passReply=new PassReply();
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);				
		passReply=(PassReply) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REPLY_URL, PassReply.class, query, startElementSolr, rowsSolr, filterBy, sortOrder, "false");		
		mav.addObject(passReply);
		return mav;
		}
	
	@RequestMapping( value="/delete-all-reply" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView deleteAllReply(			
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		boolean replyDelStatus=false;		
		
		replyDelStatus=solrCommonClient.deleteAllObject(UrlConstant.REPLY_URL);
		if(replyDelStatus)
			mav.addObject("Reply Delete Succesfully");
		else
			mav.addObject("Issue while deleting Reply");		
		
		return mav;
		}
	
	@RequestMapping( value="/delete-reply-by-query" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView deleteReplyById(			
			@RequestParam(value = "query", required = false) String query,
			HttpServletRequest request) {	
		
		ModelAndView mav = new ModelAndView();
		boolean replyDelStatus=false;		
		
		replyDelStatus=solrCommonClient.deleteObject(UrlConstant.REPLY_URL , query);
		if(replyDelStatus)
			mav.addObject("Reply Delete Succesfully");
		else
			mav.addObject("Issue while deleting reply");		
		
		return mav;
		}
	/*
	 * @author Name:Gaurav chugh
	 * @Created Date:15/11/2014
	 * @update Date:15/11/2014
	 * @purpose:searchReplyDetails
	 */
	@RequestMapping( value="/search-reply-details" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView searchReplyDetails(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = false) String filterBy,
			@RequestParam(value = "sort_order", required = true) String sortOrder,
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "userId", required = true) String userId,
			HttpServletRequest request) {	
		VenueCommentReplyDetails venueCommentReplyDetails=null;	
		List<VenueCommentReplyDetails> replyList=new ArrayList<VenueCommentReplyDetails>();
		ModelAndView mav = new ModelAndView();
		PassReply passReply=new PassReply();
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);				
		passReply=(PassReply) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REPLY_URL, PassReply.class, query, startElementSolr, rowsSolr, filterBy, sortOrder, "false");		
		for(VenueCommentReply commentReplyLocal:passReply.getResponse().getDocs()){
			venueCommentReplyDetails=new VenueCommentReplyDetails();
			venueCommentReplyDetails.setVenueCommentReply(commentReplyLocal);
			//		from User Object
			query = "user_id:" + commentReplyLocal.getFromUser();
			PassUsers userDetails = (PassUsers) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL,	PassUsers.class, query,	0, 1, "","", "false");
			for (User userLocal:userDetails.getResponse().getDocs() ){
				venueCommentReplyDetails.setFromUser(userLocal);
			}
			//		to User Object
			query = "user_id:" + commentReplyLocal.getUserId();
			PassUsers userFromDetails = (PassUsers) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL,	PassUsers.class, query,	0, 1, "","", "false");
			for (User userLocal:userFromDetails.getResponse().getDocs() ){
				venueCommentReplyDetails.setToUser(userLocal);
			}
			//		isFlag
			BasicQuery isFlag=new BasicQuery("{'typeValue' : "+"'"+commentReplyLocal.getReplyId() +"'"+","+"'userId' : "+"'"+userId+"'"+","+"'typeName' : "+"'"+UpdateTypeEnum.FlagType.comment_id.toString()+"'"+"}");
			if(mongoTemplate.count(isFlag, Flag.class)!=0){
				venueCommentReplyDetails.setIsFlag("1");
			}else{
				venueCommentReplyDetails.setIsFlag("0");
			}
			
			replyList.add(venueCommentReplyDetails);
		}
		mav.addObject("numFound",passReply.getResponse().numFound);
		mav.addObject("VenueCommentReply",replyList);
		return mav;
		}
	

}

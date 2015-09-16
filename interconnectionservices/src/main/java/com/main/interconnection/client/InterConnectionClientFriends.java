package com.main.interconnection.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.main.interconnection.clientBo.ErrorCode;
import com.main.interconnection.clientBo.FriendRequest;
import com.main.interconnection.clientBo.Message;
import com.main.interconnection.clientBo.User;
import com.main.interconnection.clientBo.UserDetails;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.solr.response.friendRequest.PassFriendRequest;
import com.main.interconnection.solr.response.session.PassSession;
import com.main.interconnection.solr.response.users.PassUsers;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
import com.main.interconnection.util.GenericComparator;
import com.main.interconnection.util.MagicNumbers;
import com.main.interconnection.util.QueryBuilder;
import com.main.interconnection.util.SolrDateUtil;
import com.main.interconnection.util.UUIDGenrator;
import com.main.interconnection.util.UpdateTypeEnum;


@Controller
@RequestMapping(value="/friend/*")
public class InterConnectionClientFriends {
	
	@Autowired
	ApiDao apiDao;
	
	@Autowired
	SolrCommonClient solrCommonClient;
	
	SolrInputDocument solrDocument;
	
	private static final Logger logger = LoggerFactory
			.getLogger(InterConnectionClientFriends.class);
	
	@RequestMapping( value="/search-friends" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView searchFriends(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "userid", required = true) String userId,
			@RequestParam(value = "params", required = true) String params,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		String query=null;
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);	
		PassUsers users=null;
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(token==null || token.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;					
			}else if(params ==null || params.equals("")  ){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().get(0).getUserId()==null){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(apiDao.validateApiKey(apikey)){
				//Added to access records on the bases of user privacy settings
				String emailOff = "(emailOnOff : 1)";//Email Privacy Parameter Off. 
				String nameOff = "(nameOnOff : 1)";//Name Privacy Parameter Off. 
				String phNoOff = "(phNoOnOff : 1)";//Phone No Privacy Parameter Off. 
				String profilePicOff = "(profilePicOnOff : 1)";//Picture Privacy Parameter Off. 
				String searchEngOff = "(searchEngOnOff : 1)";//Search Privacy Parameter Off. 
				if(params.contains("@")) {
					query="emailId:"+params+"*";
					query = "(" +(query) +")" + "-"+(emailOff);
					//check the user emailOnOff
				} else if(params.matches(".*\\d+.*")) {
					query="mobileNo:"+params+"*";
					query = "(" +(query) +")" + "-"+(phNoOff);
				} else {
					params = params;
					query="searchName:"+params+"*";
					query = "(" +(query) +")" + "-"+(nameOff)+"-"+(profilePicOff)+"-"+(searchEngOff);
				}
			
				//query = "(" +(query) +")" + "-"+(emailOff)+"-"+(nameOff)+"-"+(phNoOff)+"-"+(profilePicOff)+"-"+(searchEngOff);
				users=(PassUsers)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL, PassUsers.class, query, startElementSolr, rowsSolr, "", "", "false");				
				mav.addObject("users_details", users);		
				return mav;
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));				
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}		
	return mav;	
	}	
	
	
	@RequestMapping( value="/send-friend-request" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView sendFriendRequest(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "to-userid", required = true) String toUserId,
			@RequestParam(value = "from-userid", required = true) String fromUserId,			
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		String requestId=null;
		String updateId=null;
		boolean requestAddStatus=false;
		String requestId2=null;
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(token==null || token.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;					
			}else if(toUserId ==null || toUserId.equals("") || fromUserId ==null ||  fromUserId.equals("") ){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+fromUserId, 0, 1, "user_id", "", "false").getResponse().getDocs().get(0).getUserId()==null){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+fromUserId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(apiDao.validateApiKey(apikey)){				
				if(toUserId.equals(fromUserId)){
					mav.addObject("Error", "You cannot send friend request to yourself");
					return mav;
				}
				String query="to_user:"+fromUserId+" AND from_user:"+ toUserId+" AND status:Sent";
				PassFriendRequest friendRequest=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, query, 0, 1, "token", "", "false");
				if(friendRequest.getResponse().getDocs().size()==0){
				requestId=UUIDGenrator.getUniqueId();				
				solrDocument=new SolrInputDocument();
				solrDocument.addField("request_id", requestId);
				solrDocument.addField("to_user", fromUserId);
				solrDocument.addField("from_user", toUserId);				
				solrDocument.addField("status", MagicNumbers.FRIEND_REQUEST_SENT);				
				solrDocument.addField("send_date", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
				solrDocument.addField("req_updated_date", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
				requestAddStatus=solrCommonClient.addObjectToSolr(UrlConstant.REQUEST_URL , solrDocument);
				//		Create flip Entry
				requestId2=UUIDGenrator.getUniqueId();	
				solrDocument=new SolrInputDocument();
				solrDocument.addField("request_id", requestId2);
				solrDocument.addField("to_user",toUserId );
				solrDocument.addField("from_user", fromUserId);				
				solrDocument.addField("status", MagicNumbers.FRIEND_REQUEST_RECEIVED);				
				solrDocument.addField("send_date", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
				solrDocument.addField("req_updated_date", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
				requestAddStatus=solrCommonClient.addObjectToSolr(UrlConstant.REQUEST_URL , solrDocument);
				if(requestAddStatus){
					updateId=UUIDGenrator.getUniqueId();
					solrDocument=new SolrInputDocument();
					solrDocument.addField("id", updateId);
					solrDocument.addField("type", UpdateTypeEnum.UpdateType.Notifications.toString());
					solrDocument.addField("subType", UpdateTypeEnum.UpdateSubType.FriendRequest.toString());
					solrDocument.addField("property", UpdateTypeEnum.FriendRequestUpdateProperty.ReceivedRequest.toString());
					if(toUserId.equals(fromUserId)){
						solrDocument.addField("subTypes",UpdateTypeEnum.UpdateSubTypes.USEROWN);
					}
					solrDocument.addField("toUser", toUserId);
					solrDocument.addField("fromUser", fromUserId);
					solrDocument.addField("request_id", requestId2);		
					solrDocument.addField("privacy", MagicNumbers.ACTIVE);
					solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
					solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
					mav.addObject("Message" , "Your request has been Sent Sucessfully");
					mav.addObject("request_id" , requestId2);	
					return mav;
				}else{					
					mav.addObject(ErrorCode.getCustomeError(1001));
				}	
				}else{
					mav.addObject("message","Already friend request sent");
				}
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));				
			}	
			
		}catch (Exception e) {
			e.printStackTrace();
		}		
	return mav;	
	}

	
	@RequestMapping( value="/manage-friend-request" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView manageFriendRequest(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "to-userid", required = true) String toUserId,
			@RequestParam(value = "from-userid", required = true) String fromUserId,
			@RequestParam(value = "request-id", required = true) String requestId,
			@RequestParam(value = "status", required = true) String status,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		String query=null;
		String updateId=null;
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(token==null || token.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;					
			}else if(toUserId ==null || toUserId.equals("") || fromUserId ==null ||  fromUserId.equals("") || requestId==null || requestId.equals("") || status==null || status.equals("") ){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+fromUserId, 0, 1, "user_id", "", "false").getResponse().getDocs().get(0).getUserId()==null){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+fromUserId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, "request_id:"+requestId, 0, 1, "request_id", "", "false").getResponse().numFound<0){				
				mav.addObject(ErrorCode.getCustomeError(5000));
				return mav;
			}else if(apiDao.validateApiKey(apikey)){						
					if(status.equals("Accept")){
						solrDocument=new SolrInputDocument();
						solrDocument.addField("request_id", requestId);
						solrDocument.addField("to_user", toUserId);
						solrDocument.addField("from_user", fromUserId);				
						solrDocument.addField("status", status);				
						solrDocument.addField("req_updated_date", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
						solrCommonClient.addObjectToSolr(UrlConstant.REQUEST_URL , solrDocument);
						// 		Create Flip Entry for Request
						updateId=UUIDGenrator.getUniqueId();
						solrDocument=new SolrInputDocument();
						solrDocument.addField("request_id", updateId);
						solrDocument.addField("to_user", fromUserId);
						solrDocument.addField("from_user", toUserId);				
						solrDocument.addField("status", status);				
						solrDocument.addField("req_updated_date", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
						solrCommonClient.addObjectToSolr(UrlConstant.REQUEST_URL , solrDocument);
						// remove Sent Request Entry
						 query="to_user:"+fromUserId+" AND from_user:"+toUserId+" AND status:Sent";
						solrCommonClient.deleteObject(UrlConstant.REQUEST_URL, query);
						// remove notification when request Accept
						query="toUser:"+toUserId+" AND fromUser:"+fromUserId+" AND type:Notifications  AND property:ReceivedRequest";
						solrCommonClient.deleteObject(UrlConstant.MANAGE_UPDATES_URL, query);
						
								//		Create Notification;
						updateId=UUIDGenrator.getUniqueId();
						solrDocument=new SolrInputDocument();
						solrDocument.addField("id", updateId);
						solrDocument.addField("type", UpdateTypeEnum.UpdateType.Notifications.toString());
						solrDocument.addField("subType", UpdateTypeEnum.UpdateSubType.FriendRequest.toString());
						solrDocument.addField("property", UpdateTypeEnum.FriendRequestUpdateProperty.AcceptsRequest.toString());					
						solrDocument.addField("toUser", fromUserId);
						solrDocument.addField("request_id", requestId);
						solrDocument.addField("fromUser", toUserId);
						if(toUserId.equals(fromUserId)){
							solrDocument.addField("subTypes",UpdateTypeEnum.UpdateSubTypes.USEROWN);
						}
						solrDocument.addField("privacy", MagicNumbers.ACTIVE);
						solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
						solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);	
					}else if(status.equals("Reject")){
						updateId=UUIDGenrator.getUniqueId();
						solrDocument=new SolrInputDocument();
						solrDocument.addField("id", updateId);
						solrDocument.addField("type", UpdateTypeEnum.UpdateType.Notifications.toString());
						solrDocument.addField("subType", UpdateTypeEnum.UpdateSubType.FriendRequest.toString());
						solrDocument.addField("property", UpdateTypeEnum.FriendRequestUpdateProperty.RejectRequest.toString());					
						solrDocument.addField("toUser", fromUserId);
						solrDocument.addField("fromUser",toUserId);
						if(toUserId.equals(fromUserId)){
							solrDocument.addField("subTypes",UpdateTypeEnum.UpdateSubTypes.USEROWN);
						}
						solrDocument.addField("request_id", requestId);
						solrDocument.addField("privacy", MagicNumbers.ACTIVE);
						solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
						solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);	
						//		Delete both Request in case of Reject
						 query="to_user:"+toUserId+" AND from_user:"+fromUserId+" AND status:Received";
						solrCommonClient.deleteObject(UrlConstant.REQUEST_URL, query);
						
						 query="to_user:"+fromUserId+" AND from_user:"+toUserId+" AND status:Sent";
						solrCommonClient.deleteObject(UrlConstant.REQUEST_URL, query);
					}else{					
					mav.addObject(ErrorCode.getCustomeError(1001));
				}	
					mav.addObject("Message" , "Your request has been "+status);						
				return mav;					
				}else{				
				mav.addObject(ErrorCode.getCustomeError(100));				
					}	
			}catch (Exception e) {
			e.printStackTrace();
		}		
	return mav;	
	}
	
	
	@RequestMapping( value="/get-all-friends-list" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getAllFriendsList(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "status", required = true) String status,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		String query=null;
		
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);	
		PassFriendRequest friendListIds=null;
		List<String> friendsList=new ArrayList<String>();
		PassUsers passUsers=null;
		String userQuery=null;
		String userfromQuery=null;
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(token==null || token.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;					
			}else if(status ==null || status.equals("")  ){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().get(0).getUserId()==null){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(apiDao.validateApiKey(apikey)){
				/*if(status.equals("Accept")) {
					query="(to_user:"+userId+"*"+" OR "+"from_user:"+userId+") AND "+"status:"+status+"*";
				} else {
					query="to_user:"+userId+"*"+" AND "+"status:"+status+"*";
				}*/
				query="to_user:"+userId+"*"+" AND "+"status:"+status+"*";
				friendListIds=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, query, startElementSolr, rowsSolr, "", "", "false");
				for(FriendRequest friend:friendListIds.getResponse().getDocs()){
	                //				toUser Object
	                userQuery="user_id:"+friend.getToUser();
	                passUsers=(PassUsers)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL, PassUsers.class, userQuery, 0, 10, "", "", "false");
	                if(passUsers.getResponse().getDocs().size()>0){
	                	friend.setUsers(passUsers.getResponse().getDocs().get(0));
	                }
	                //        		fromUser Object
	                userfromQuery="user_id:"+friend.getFromUser();
	                passUsers=(PassUsers)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL, PassUsers.class, userfromQuery, 0, 10, "", "", "false");
	                if(passUsers.getResponse().getDocs().size()>0){
	                	friend.setUsersFrom(passUsers.getResponse().getDocs().get(0));
	                }
					
				}
				/*for(FriendRequest friend:friendListIds.getResponse().getDocs()){
					if(friend.getToUser().equals(userId)){
						friendsList.add(friend.getFromUser());
					}else{
						friendsList.add(friend.getToUser());
					}
				}	*/			
				mav.addObject("friends_list_ids", friendListIds);
				mav.addObject("friendsList", friendsList);
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));				
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}		
		return mav;	
	}
	
	@RequestMapping( value="/get-other-all-friends-list" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getOtherUserAllFriendsList(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "other_user_id", required = true) String otherUserId,
			@RequestParam(value = "status", required = true) String status,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		String query=null;
		
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);	
		PassFriendRequest friendListIds=null;
		List<String> friendsList=new ArrayList<String>();
		
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(token==null || token.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;					
			}else if(status ==null || status.equals("")||otherUserId==null||"".equals(otherUserId)  ){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().get(0).getUserId()==null){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(apiDao.validateApiKey(apikey)){
				if(status.equals("Accept")) {
					query="(to_user:"+otherUserId+"*"+" OR "+"from_user:"+otherUserId+") AND "+"status:"+status+"*";
				} else {
					query="to_user:"+otherUserId+"*"+" AND "+"status:"+status+"*";
				}
				friendListIds=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, query, startElementSolr, rowsSolr, "", "", "false");
				
				for(FriendRequest friend:friendListIds.getResponse().getDocs()){
					if(friend.getToUser().equals(otherUserId)){
						friendsList.add(friend.getFromUser());
					}else{
						friendsList.add(friend.getToUser());
					}
				}				
				mav.addObject("friends_list_ids", friendListIds);
				mav.addObject("friendsList", friendsList);
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));				
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}		
		return mav;	
	}
	
	@RequestMapping( value="/get-friends-detailed-list" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getAllFriendsListWithDetails(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "status", required = true) String status,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			HttpServletRequest request){	
		ModelAndView mav = new ModelAndView();
		String query=null;
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);	
		PassFriendRequest friendListIds=null;
		PassUsers passUsers= null;
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(token==null || token.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;					
			}else if(status ==null || status.equals("") ||startElement ==null || startElement.equals("")||rows ==null || rows.equals("") ){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().size()==0){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(apiDao.validateApiKey(apikey)){
				query="to_user:"+userId+"*"+" AND "+"status:"+status+"*";
				logger.info("Finding friend requests..");
				friendListIds=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, query, startElementSolr, rowsSolr, "", "", "false");				
				if(null!=friendListIds&&null!=friendListIds.getResponse().getDocs()&&friendListIds.getResponse().getDocs().size()>0)
				{
					//mav.addObject("friend_list_ids",friendListIds.getResponse().getDocs());
					logger.info("Friend requests Found..");
					StringBuffer buffer=QueryBuilder.getSolrFrndInQueryForUserId(friendListIds.getResponse().getDocs());
					logger.info("Query for get user details..",buffer.toString());
					passUsers = (PassUsers) solrCommonClient
							.commonSolrSearch(UrlConstant.SEARCH_USERS_URL,
									PassUsers.class, buffer.toString(),
									startElementSolr,rowsSolr, "", "",
									"false");
					List<User> users=passUsers.getResponse().getDocs();
					if(null!=passUsers&&null!=users&&passUsers.getResponse().getDocs().size()>0)
					{
						users=(List<User>) GenericComparator.getSortedListBySortedField("name", passUsers.getResponse().getDocs(), true);
						passUsers.getResponse().setDocs(users);
							
					}
					mav.addObject("friends_list_Details",passUsers);
				}else{
					logger.info("No friend requests to show");
					mav.addObject(ErrorCode.getCustomeError(6002));
				}
				}else{				
					mav.addObject(ErrorCode.getCustomeError(100));				
				}			
		}catch (Exception e){
			logger.info("Error Occur..."+e.getMessage());
			mav.addObject(ErrorCode.getCustomeError(1001));	
		}		
		return mav;	
	}
	@RequestMapping(value = "/remove-friend", method = {RequestMethod.POST,
			RequestMethod.GET})
	public ModelAndView removeFriend(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "to_userId", required = true) String toUserId,
			@RequestParam(value = "from_userId", required = true) String fromUserId,
			@RequestParam(value = "request_id", required = true) String requestId,
			HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		String query=null;
		boolean isDeleted=false;
		boolean isDeletedFlip=false;
		String status="Accept";
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			}else if(requestId ==null || requestId.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}
			else if (solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + toUserId, 0, 1,
							"user_id", "", "false").getResponse().getDocs()
					.size() == 0) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (!solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + toUserId, 0, 1,
							"token", "", "false").getResponse().getDocs()
					.get(0).getToken().equals(token)) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (solrCommonClient.commonSolrSearch(
					UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class,
					"request_id:" + requestId, 0, 1, "request_id", "", "false")
					.getResponse().numFound == 0) {
				mav.addObject(ErrorCode.getCustomeError(5000));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				query = "to_user:"+toUserId+" AND "+"from_user:"+ fromUserId + " AND "+"status:"+status;;
				//query = "request_id:"+requestId+ " AND "+"status:"+status;
				isDeleted = solrCommonClient.deleteObject(
						UrlConstant.REQUEST_URL, query);
				//Delete Flip Entry
				query = "to_user:"+fromUserId+" AND "+"from_user:"+ toUserId + " AND "+"status:"+status;;
				//query = "request_id:"+requestId+ " AND "+"status:"+status;
				isDeletedFlip = solrCommonClient.deleteObject(
						UrlConstant.REQUEST_URL, query);
				if (isDeleted && isDeletedFlip){
					query = "request_id:"+requestId+" AND "+"subType:"+UpdateTypeEnum.UpdateSubType.FriendRequest.toString();
					isDeleted = solrCommonClient.deleteObject(
							UrlConstant.MANAGE_UPDATES_URL, query);
					mav.addObject("message",
							Message.getCustomeMessages(10));
				}
				if(!isDeleted || !isDeletedFlip)
					mav.addObject("message",
							"Error Occured to delete request");
				return mav;
			} else {
				logger.info("Invalid Api Key");
				mav.addObject(ErrorCode.getCustomeError(100));
			}

		}catch (Exception e){
			logger.info("Error Occur..."+e.getMessage());
			mav.addObject(ErrorCode.getCustomeError(1001));	
		}
		return mav;
	}
	
	
	@RequestMapping( value="/suggested-friends" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView suggestedFriends(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "userid", required = true) String userId,					
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		String query=null;
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);	
		PassFriendRequest friendListIds=null; 
		PassFriendRequest friendListId1st=null;
		PassFriendRequest friendListId2nd=null;
		
		Set<String> suggestFriends=new HashSet<String>();

		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(token==null || token.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;					
			}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().get(0).getUserId()==null){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(apiDao.validateApiKey(apikey)){
				
				query="to_user:"+userId+" AND "+"status:Accept";
				
				friendListIds=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, query, startElementSolr, rowsSolr, "", "", "false");				
				
				for(FriendRequest friend: friendListIds.getResponse().getDocs()){
					
					query="to_user:"+friend.getFromUser()+" AND "+"status:Accept";
					
					if(friendListId1st==null){
					friendListId1st=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, query, startElementSolr, rowsSolr, "", "", "false");
					}
					
					if(friendListId2nd==null){
					friendListId2nd=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, query, startElementSolr, rowsSolr, "", "", "false");
					}
					
					if(!friendListId1st.getResponse().getDocs().isEmpty() && !friendListId2nd.getResponse().getDocs().isEmpty()){
						
						for(FriendRequest friend1stids: friendListId1st.getResponse().getDocs()){
							System.out.println("1st->"+friend1stids.getToUser());
							for(FriendRequest friend2ndids: friendListId2nd.getResponse().getDocs()){							
								System.out.println("2nd->"+friend2ndids.getToUser());
								if(friend1stids.getFromUser().equals(friend2ndids.getFromUser())){
									suggestFriends.add(friend1stids.getFromUser());									
								}							
							}						
						}
						friendListId1st=null;
						friendListId2nd=null;
					}					
				}				
				mav.addObject("suggestFriends", suggestFriends);		
				return mav;
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));				
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}		
	return mav;	
	}	

	
	

	@RequestMapping( value="/suggested-friends-new" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView suggestedFriends2(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "userid", required = true) String userId,					
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		String query=null;
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);	
		PassFriendRequest friendListIds=null;
		PassFriendRequest friendListId1st=null;	
		PassFriendRequest friendListId2nd=null;
		Set<String> suggestFriends=new HashSet<String>();
		
		List<User> suggestedUser=new ArrayList<User>();

		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(token==null || token.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;					
			}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().get(0).getUserId()==null){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(apiDao.validateApiKey(apikey)){
				
				//query="to_user:"+userId+" AND "+"status:Accept" +" AND "+" status:Received ";
				query="to_user:"+userId+" AND "+"status:Accept";
				friendListIds=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, query, startElementSolr, rowsSolr, "", "", "false");				
				
				for(FriendRequest friend: friendListIds.getResponse().getDocs()){
					
					//query="to_user:"+friend.getFromUser()+" AND "+"status:Accept" +" AND "+" status:Received ";										
					query="to_user:"+friend.getFromUser()+" AND "+"status:Accept";
					friendListId1st=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, query, startElementSolr, rowsSolr, "", "", "false");
					
					for(FriendRequest friend1stids: friendListId1st.getResponse().getDocs()){					
						suggestFriends.add(friend1stids.getFromUser());						
					}
				}			
				int row1=rowsSolr*rowsSolr;
				query="to_user:"+userId+" AND "+"( status:Accept "  +" OR "+" status:Received "  +" OR "+" status:Sent )";
				friendListId2nd=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, query, startElementSolr, row1, "", "", "false");				
				for(FriendRequest friend: friendListId2nd.getResponse().getDocs()){
					suggestFriends.remove(friend.getFromUser());
				}
				
				suggestFriends.remove(userId);
				
				for(String suggest : suggestFriends){
					query = "user_id:" + suggest;
					PassUsers userDetails = (PassUsers) solrCommonClient.commonSolrSearch(	UrlConstant.SEARCH_USERS_URL,PassUsers.class,	query,	0,	1,"","", "false");
					suggestedUser.add(userDetails.getResponse().getDocs().get(0));							
		        }			
				mav.addObject(suggestedUser);
				return mav;
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));				
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}		
	return mav;	
	}	

	@RequestMapping( value="/delete-all-request" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView deleteAllRequest(			
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		boolean requestDelStatus=false;		
		
		requestDelStatus=solrCommonClient.deleteAllObject(UrlConstant.REQUEST_URL);
		if(requestDelStatus)
			mav.addObject("Request Delete Succesfully");
		else
			mav.addObject("Issue while deleting Request");		
		
		return mav;
		}	
	/*
	 * @author Name:Gaurav chugh
	 * @Created Date:15/11/2014
	 * @update Date:27/11/2014
	 * @purpose:searchFriendsDetails
	 */
	@RequestMapping( value="/search-friends-details" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView searchFriendsDetails(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "userid", required = true) String userId,
			@RequestParam(value = "params", required = true) String params,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		String query=null;
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);	
		PassUsers users=null;
		List<UserDetails> userde=new ArrayList<UserDetails>();
		UserDetails userDetailsLocal=null;
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(token==null || token.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;					
			}else if(params ==null || params.equals("")  ){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().get(0).getUserId()==null){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(apiDao.validateApiKey(apikey)){
				//Added to access records on the bases of user privacy settings
				String emailOff = "(emailOnOff : 1)";//Email Privacy Parameter Off. 
				String nameOff = "(nameOnOff : 1)";//Name Privacy Parameter Off. 
				String phNoOff = "(phNoOnOff : 1)";//Phone No Privacy Parameter Off. 
				String profilePicOff = "(profilePicOnOff : 1)";//Picture Privacy Parameter Off. 
				String searchEngOff = "(searchEngOnOff : 1)";//Search Privacy Parameter Off. 
				if(params.contains("@")) {
					query="emailId:"+params+"*";
					query = "(" +(query) +")" + "-"+(emailOff);
					//check the user emailOnOff
				} else if(params.matches(".*\\d+.*")) {
					query="mobileNo:"+params+"*";
					query = "(" +(query) +")" + "-"+(phNoOff);
				} else {
					params = params;
					query="searchName:"+params+"*";
					query = "(" +(query) +")" + "-"+(nameOff)+"-"+(profilePicOff)+"-"+(searchEngOff);
				}
				logger.info("Query search-friends ::"+query);
				//query = "(" +(query) +")" + "-"+(emailOff)+"-"+(nameOff)+"-"+(phNoOff)+"-"+(profilePicOff)+"-"+(searchEngOff);
				users=(PassUsers)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL, PassUsers.class, query, startElementSolr, rowsSolr, "", "", "false");				

				for (User userLocal:users.getResponse().getDocs() ){
					userDetailsLocal=new UserDetails();
					 userDetailsLocal.setUsers(userLocal);
					 query="to_user:"+userId +" AND from_user: "+ userLocal.getUserId();		
					 PassFriendRequest friendListIds=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, query, 0, 1, "", "", "false");
				if(friendListIds.getResponse().getDocs().size()>0){
					userDetailsLocal.setIsFriend(friendListIds.getResponse().getDocs().get(0).getStatus());
					userDetailsLocal.setRequestId(friendListIds.getResponse().getDocs().get(0).getRequestId());
				}
				 userde.add(userDetailsLocal);
				}
				mav.addObject("numFound",users.getResponse().getNumFound());
				mav.addObject(userde);		
				return mav;
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));				
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}		
	return mav;	
	}	
	

}

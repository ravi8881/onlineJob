package com.main.interconnection.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.main.interconnection.client.social.helpers.AddSocialUser;
import com.main.interconnection.client.social.helpers.FourSquareAPI;
import com.main.interconnection.client.social.helpers.SocialTypesEnum;
import com.main.interconnection.clientBo.ConnectSocialUser;
import com.main.interconnection.clientBo.ErrorCode;
import com.main.interconnection.clientBo.User;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.dao.UserDao;
import com.main.interconnection.foursquare.friends.FourSquareFriend;
import com.main.interconnection.foursquare.friends.Item_;
import com.main.interconnection.foursquare.similarVenue.FourSquareSimilarVenue;
import com.main.interconnection.mongoDao.MongoCommonClient;
import com.main.interconnection.rest.client.ThirdPartyRestClient;
import com.main.interconnection.solr.response.friendRequest.PassFriendRequest;
import com.main.interconnection.solr.response.session.PassSession;
import com.main.interconnection.solr.response.users.PassUsers;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
import com.main.interconnection.util.CommonEmail;

import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompleteUser;
import fi.foyt.foursquare.api.entities.UserGroup;

@Controller
@RequestMapping(value = "/foursquare/*")
public class InterconnectionFourSquareClient {

	private static final Logger logger = LoggerFactory
			.getLogger(InterconnectionFourSquareClient.class);

	@Autowired
	ApiDao apiDao;

	@Autowired
	CommonEmail commonEmail;

	@Autowired
	UserDao userDao;

	@Autowired
	SolrCommonClient solrCommonClient;
	
	@Autowired
	MongoCommonClient mongoCommonClient;
	
	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	FourSquareAPI fourSquareCall;
	
	@Autowired
	ThirdPartyRestClient thirdPartyRestClient;
	
	@Value("${foursquare.version}")
	String foursquareVersion;
	
	@Value("${foursquare.outh}")
	String foursquareOuth;
	
	@Value("${foursquare.outh}")
	String foursquareAuthToken;

	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/save-access-token",method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView saveAccessToken(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "access_token", required = true) String accessToken,
			@RequestParam(value = "connect", required = true) String connect,
			@RequestParam(value = "socialId", required = true) String social,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		Result<CompleteUser> result = null;
		CompleteUser completeUser = null;
		HashMap<String, String> params = new HashMap<String, String>();
		String templatePath = null;
		User user = null;
		ConnectSocialUser connectSocialUser = null;
		List<ConnectSocialUser> connectSocialUsers=null;
		boolean mongoUpdateFlag=false;
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (userId == null || userId.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;
			}
			else if (accessToken == null || accessToken.equals("")||connect == null || connect.equals("")||(!connect.equals("true") && !connect.equals("false"))) {
				mav.addObject(ErrorCode.getCustomeError(105));
				return mav;
			}else if (solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1,
							"user_id", "", "false").getResponse().getDocs().get(0)
					.getUserId() == null) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (!solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1, "token",
							"", "false").getResponse().getDocs().get(0).getToken()
					.equals(token)) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			}else if (apiDao.validateApiKey(apikey)) {
				
				Query query=new Query();
				query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.FourSquar.toString())));
				connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(query, ConnectSocialUser.class);
				
				if (connect != null && !connect.equals("") // connect value send false
						&& connect.equals("false"))  {
					if (null != connectSocialUsers
							&& connectSocialUsers.size() > 0) {
						mongoCommonClient.deleteObject(social, "socialId",
								ConnectSocialUser.class);
					
					}
					mav.addObject("User_Connected", false);
					mav.addObject("userId", userId);

				} 

				if (connect != null && !connect.equals("")// connect value send false
						&& connect.equals("true")) {

					if (null != connectSocialUsers
							&& connectSocialUsers.size() > 0) {// connect user social id exist
						mongoCommonClient.deleteObject(social, "socialId",
								ConnectSocialUser.class);
						result = fourSquareCall.getFourSquareUserDetails(
								social, accessToken);
						if (null != result) {
							if (!mongoTemplate
									.collectionExists(ConnectSocialUser.class)) {
								mongoTemplate
										.createCollection(ConnectSocialUser.class);
							}
							completeUser = result.getResult();
							AddSocialUser addSocialUser = new AddSocialUser();
							connectSocialUser = addSocialUser
									.setFourSquareUser(completeUser);
							connectSocialUser.setAccessToken(accessToken);
							connectSocialUser.setConnect(true);
							connectSocialUser.setUserId(userId);
							mongoTemplate.insert(connectSocialUser);
						}
						mav.addObject("User_Connected", true);
						mav.addObject("userId", userId);
					} else {// connect user social id with first time
						result = fourSquareCall.getFourSquareUserDetails(
								social, accessToken);

						if (null != result) {
							if (!mongoTemplate
									.collectionExists(ConnectSocialUser.class)) {
								mongoTemplate
										.createCollection(ConnectSocialUser.class);
							}
							completeUser = result.getResult();
							AddSocialUser addSocialUser = new AddSocialUser();
							connectSocialUser = addSocialUser
									.setFourSquareUser(completeUser);
							connectSocialUser.setAccessToken(accessToken);
							connectSocialUser.setConnect(true);
							connectSocialUser.setUserId(userId);
							mongoTemplate.insert(connectSocialUser);
							mongoUpdateFlag = true;
							user = userDao.getRegisterUserDetails(userId);
							params.put("_NAME_", user.getName());
							params.put("_SOCIAL_",
									"With your Foursquare Account.");
							templatePath = request
									.getSession()
									.getServletContext()
									.getRealPath(
											"/WEB-INF/mailTemplate/welcomeSocial.template");
							commonEmail.sendEmail(templatePath, params,
									"Waspit welcome email", user.getEmailId());
							mav.addObject("User_Connected", true);
							mav.addObject("userId",userId);

						}

					}
				}

			}
		} catch (Exception e) {
			// Added to do rollback if exception occur for transaction control
			if (mongoUpdateFlag == false)
				mongoCommonClient.deleteObject(userId, "userId",
						ConnectSocialUser.class);
			logger.info(" Exception occur == LogTime ===" + new Date(), e);
			mav.addObject(ErrorCode.getCustomeError(1002));
		}
		return mav;
	}
	
	//Commented because method not needs this time
	/*@RequestMapping(value = "/get-user-details", method = RequestMethod.GET)
	public ModelAndView getUserDetails(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		Result<CompleteUser> result = null;
		if (apikey == null || apikey.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;
		} else if (token == null || token.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (userId == null || userId.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;
		} else if (solrCommonClient
				.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
						PassSession.class, "user_id:" + userId, 0, 1,
						"user_id", "", "false").getResponse().getDocs().get(0)
				.getUserId() == null) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (!solrCommonClient
				.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
						PassSession.class, "user_id:" + userId, 0, 1, "token",
						"", "false").getResponse().getDocs().get(0).getToken()
				.equals(token)) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (apiDao.validateApiKey(apikey)) {
			if (mongoTemplate.collectionExists(ConnectSocialUser.class)) {
				ConnectSocialUser user = (ConnectSocialUser) mongoCommonClient
						.findById(userId, "userId", ConnectSocialUser.class);
				String accessToken=null;
				if(null!=user)
					accessToken = user.getAccessToken();
				result = fourSquareCall.getFourSquareUserDetails(userId,
						accessToken);
				if (null != result)
					mav.addObject("foursquare_user_details", result.getResult());
				else
					mav.addObject(ErrorCode.getCustomeError(5002));
			} else
				mav.addObject(ErrorCode.getCustomeError(100));
		}
		return mav;
	}
	@RequestMapping(value = "/get-user-friends", method = RequestMethod.GET)
	public ModelAndView getUserFriends(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		Result<UserGroup> result = null;
		if (apikey == null || apikey.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;
		} else if (token == null || token.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (userId == null || userId.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;
		}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().get(0).getUserId()==null){				
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;	
		}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){				
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;	
		}
		User waspitUser = userDao.getRegisterUserDetails(userId);
		String accessToken = waspitUser.getAccessToken();
		result = fourSquareCall.getFourSquareFriends(userId, accessToken);
		if (null != result)
			mav.addObject("foursquare_user_friends", result.getResult());
		else{
			logger.info("No friends found for user on foursquare");
			mav.addObject(ErrorCode.getCustomeError(5002));
		}
		return mav;
	}*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/get-waspit-friends",method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getUserWaspitFriends(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		Result<UserGroup> result = null;
		PassUsers passUsers = null;
		Integer startElements = Integer.parseInt(startElement);
		Integer rows1 = Integer.parseInt(rows);
		List<ConnectSocialUser> connectSocialUsers=null;
		try{
		if (apikey == null || apikey.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;
		} else if (token == null || token.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (userId == null || userId.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;
		} else if (startElement == null || rows.equals("")||rows == null || startElement.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(105));
			return mav;
		}else if (solrCommonClient
				.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
						PassSession.class, "user_id:" + userId, 0, 1,
						"user_id", "", "false").getResponse().getDocs().get(0)
				.getUserId() == null) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (!solrCommonClient
				.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
						PassSession.class, "user_id:" + userId, 0, 1, "token",
						"", "false").getResponse().getDocs().get(0).getToken()
				.equals(token)) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (apiDao.validateApiKey(apikey)) {
			if (mongoTemplate.collectionExists(ConnectSocialUser.class)) {
				connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient
						.findById(userId, "userId", ConnectSocialUser.class);
				if (null!=connectSocialUsers&&connectSocialUsers.size()>0) {
					ConnectSocialUser user=connectSocialUsers.get(0);
					if(user.getConnect()==false)
					{
						return mav.addObject(ErrorCode.getCustomeError(5004));
					}
					String accessToken=user.getAccessToken();
					String socialId=user.getSocialId();
					result = fourSquareCall.getFourSquareFriendsWithPaging(
							socialId, accessToken,rows1,startElements);
					if (null != result) {
						AddSocialUser addSocialUser = new AddSocialUser();
						addSocialUser.setFourSquareFrndDetails(result,
								accessToken);
						List<CompleteUser> list = addSocialUser
								.getfourSquareFriendsDeatils();
						StringBuffer buffer = AddSocialUser
								.getSolrFrndInQuery(list);
						passUsers = (PassUsers) solrCommonClient
								.commonSolrSearch(UrlConstant.SEARCH_USERS_URL,
										PassUsers.class, buffer.toString(),
										startElements,rows1, "", "",
										"false");
						mav.addObject("foursquare_waspit_friends", passUsers);
					} else {
						logger.info("No friends are on waspit");
						mav.addObject(ErrorCode.getCustomeError(5003));
					}
				} else{
					mav.addObject(ErrorCode.getCustomeError(5002));
				}
			}
		}else{
			mav.addObject(ErrorCode.getCustomeError(100));
		}
		}catch(Exception e){
			logger.info(" Exception occur == LogTime ===" + new Date(), e);
			mav.addObject(ErrorCode.getCustomeError(1002));
		}
		return mav;
	}
	
	@RequestMapping( value="/get-foursquare-friends" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getFourSquareFriends(
			
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		
		List<ConnectSocialUser> connectSocialUsers=new ArrayList<ConnectSocialUser>();
		PassFriendRequest	friendListIds=null;
		PassUsers passUsers=null;
		Map<String, String> params = null;
		
		Query query=null;
		
		String solrQuery;
		
		PassUsers users=null;
		
		FourSquareFriend fourSquareFriend=null;
		List<User> waspitUser = new ArrayList<User>();
		try{
			params = new HashMap<String, String>();	
			
			if(userId!=null && !userId.equals("")){
				query=new Query();
				query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.FourSquar.toString())));
				
				
				connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(query, ConnectSocialUser.class);
				
				if(connectSocialUsers.size()>0){
					params.put("userid", connectSocialUsers.get(0).getSocialId());	
				}				
			}
			
			params.put("outhToken", foursquareOuth);
			params.put("version", foursquareVersion);
			
			fourSquareFriend=(FourSquareFriend)thirdPartyRestClient.commonRestSearchClient(UrlConstant.FOUR_SQUARE_FRIENDS, FourSquareFriend.class,params);
			
			for(Item_ fourSquareFriendlocal:fourSquareFriend.getResponse().getFriends().getItems()){
				
				 query=new Query();
				
				query.addCriteria(Criteria.where("socialId").is(fourSquareFriendlocal.getId()).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.FourSquar.toString())));
				
				connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(query, ConnectSocialUser.class);
				
				if (null != connectSocialUsers && connectSocialUsers.size() > 0) {
					
					solrQuery="to_user:"+connectSocialUsers.get(0).getUserId() +" OR from_user: "+ connectSocialUsers.get(0).getUserId();	
					//find the request for user...
					friendListIds=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, solrQuery, 0, 1, "", "", "false");
					if(friendListIds.getResponse().getDocs().size()==0){
						
							//Getting User Detail from Solr
							String userQuery="user_id:"+connectSocialUsers.get(0).getUserId();
							passUsers=(PassUsers)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL, PassUsers.class, userQuery, 0, 1, "", "", "false");
							
							if(passUsers.getResponse().numFound!=0){
								for(User usr:passUsers.getResponse().getDocs())
								{
									waspitUser.add(usr);
								}
							}
					}	
				}
			}
			mav.addObject("Waspit_friends", waspitUser);
			mav.addObject("fourSquare_friends", fourSquareFriend);

		}catch(Exception e){
			e.printStackTrace();
		}		
		return mav;
		}
	
	
	
	@RequestMapping(value="/get-foursquare-similar-venue-by-id" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getFourSquareImageById(@RequestParam(value = "venue-id", required = true) String venueId,			
					 HttpServletRequest request) {	
			ModelAndView mav = new ModelAndView();
			Map<String, String> params=new HashMap<String, String>();
			
			params.put("venueid", venueId);
			params.put("authToken", foursquareAuthToken);
			params.put("version", foursquareVersion);
			try{
				
				FourSquareSimilarVenue similarVenue=thirdPartyRestClient.commonRestSearchClient(UrlConstant.FOUR_SQUARE_VENUE_SIMILAR, FourSquareSimilarVenue.class,params);
				mav.addObject(similarVenue);
				
				}catch(Exception e){
					e.printStackTrace();
				}
			return mav;
	}
	

}

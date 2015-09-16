package com.main.interconnection.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.common.SolrInputDocument;
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
import com.main.interconnection.client.social.helpers.SocialTypesEnum;
import com.main.interconnection.clientBo.ConnectSocialUser;
import com.main.interconnection.clientBo.ErrorCode;
import com.main.interconnection.clientBo.SocialFriends;
import com.main.interconnection.clientBo.Updates;
import com.main.interconnection.clientBo.User;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.dao.UserDao;
import com.main.interconnection.mongoDao.MongoCommonClient;
import com.main.interconnection.solr.response.friendRequest.PassFriendRequest;
import com.main.interconnection.solr.response.session.PassSession;
import com.main.interconnection.solr.response.users.PassUsers;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
import com.main.interconnection.util.CommonEmail;
import com.main.interconnection.util.FacebookData;
import com.main.interconnection.util.MagicNumbers;
import com.main.interconnection.util.SolrDateUtil;
import com.main.interconnection.util.UUIDGenrator;
import com.main.interconnection.util.UpdateTypeEnum;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookException;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.FacebookType;


@Controller
@RequestMapping(value = "/facebook/*")
public class InterconnectionFacebookClient {

	private static final Logger logger = LoggerFactory.getLogger(InterconnectionFacebookClient.class);
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	CommonEmail commonEmail;
	
	@Autowired
	SolrCommonClient solrCommonClient;

	SolrInputDocument solrDocument;
	
	@Autowired
	MongoCommonClient mongoCommonClient;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	ApiDao apiDao;
	
	@Value("${facebook.appiId}")
	String facebookAppId;
	
	

	@RequestMapping(value = "/save-access-token", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView saveAccessToken(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "access_token", required = true) String accessToken,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();	
		//Flag to signify registedUser
		boolean is_registered = false;
		//Flag to signify verifiedUser
		boolean is_verified = false;
		//Write code to validate Api user
		if(apikey ==null || apikey.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;			
		} else if(accessToken ==null || accessToken.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;						
		}
		//Get Response from Facebook API
		ModelAndView facebookUserObj = getUserDetails(apikey, accessToken, request);
		//Get FaceBook User
		com.restfb.types.User user = (com.restfb.types.User) facebookUserObj.getModel().get("fb_user_details");
		//Check if user exists with EmailId
		String userId = userDao.getUserIdByEmail(user.getEmail());
		//Generate Unique userId
		ConnectSocialUser connectSocialUser = null;
		//Register New Waspit User in case email does not exists
		if(userId == null) {
			User registeredUser = userDao.getRegisterUserDetails(user.getId());
			if(registeredUser != null) {
				mav.addObject(user);
				mav.addObject("is_registered", is_registered);
				mav.addObject("is_verified", is_verified);
				return mav;
			}
			com.main.interconnection.clientBo.User waspitUser = new User();
			waspitUser.setUserId(user.getId());
			boolean status = userDao.registerSocialLoginUser(waspitUser);
			if(!status) {
				return mav.addObject(ErrorCode.getCustomeError(1001));
			}
			AddSocialUser addSocialUser = new AddSocialUser();
			connectSocialUser = addSocialUser.setFacebookUser(user);
			connectSocialUser.setAccessToken(accessToken);
			connectSocialUser.setConnect(true);
			connectSocialUser.setUserId(user.getId());
			mongoTemplate.insert(connectSocialUser);
			mav.addObject(user);
			mav.addObject("is_registered", is_registered);
			mav.addObject("is_verified", is_verified);
			return mav;
		}
		//Get Registered User in case email exists
		is_registered = true;
		User registeredUser = userDao.getRegisterUserDetails(userId);
		if(registeredUser.getMobileverifyStatus() == 1 && registeredUser.getEmailverifyStatus() == 1) {
			is_verified = true;
		}
		Query query=new Query();
		query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.FaceBook.toString())));
		List<ConnectSocialUser> connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(query, ConnectSocialUser.class);
		if(connectSocialUsers != null && connectSocialUsers.size() > 0) {
			mongoCommonClient.deleteObject(connectSocialUsers.get(0).getSocialId(), "socialId", ConnectSocialUser.class);
		}
		AddSocialUser addSocialUser = new AddSocialUser();
		connectSocialUser = addSocialUser.setFacebookUser(user);
		connectSocialUser.setAccessToken(accessToken);
		connectSocialUser.setConnect(true);
		connectSocialUser.setUserId(userId);
		mongoTemplate.insert(connectSocialUser);PassUsers userSolrDetails = (PassUsers)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL,PassUsers.class,	"user_id:" + registeredUser.getUserId(),	0,	1,"","", "false");
		mav.addObject("user",userSolrDetails.getResponse().getDocs().get(0));
		//mav.addObject(registeredUser);
		mav.addObject("is_registered", is_registered);
		mav.addObject("is_verified", is_verified);
		//Create session in solr
		solrDocument = new SolrInputDocument();
		solrDocument.addField("user_id", userId);
		solrDocument.addField("token", "1");
		solrCommonClient.addObjectToSolr(
				UrlConstant.SESSION_URL, solrDocument);
		mav.addObject("token", solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:" + userId,
				0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken());
		return mav;
		
	}
	
	
	@RequestMapping(value = "/get-user-details", method = RequestMethod.GET)
	public ModelAndView getUserDetails(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "access_token", required = true) String accessToken,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();	
		if(apikey ==null || apikey.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;			
		}else if(accessToken ==null || accessToken.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;						
		}
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
		com.restfb.types.User user = facebookClient.fetchObject("me", com.restfb.types.User.class);	
		logger.info("Facebook User with Id :"+user.getId());
		mav.addObject("fb_user_details", user);
		return mav;
	}


	@RequestMapping(value = "/get-user-friend-list", method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getUserFriendList(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/");	
		//Validate User
		if(apikey ==null || apikey.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;			
		}else if(token==null || token.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;					
		}else if(userId ==null || userId.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;						
		}
		User user = userDao.getRegisterUserDetails(userId);
		if(user == null) {
			mav.addObject(ErrorCode.INVALID_PARAMETER_105);
			return mav;
		}
		String isConnected = null;
		Query query=new Query();
		String solrQuery = null;
		PassFriendRequest friendListIds = null;
		PassUsers passUsers = null;
		List<User> waspitUserList = new ArrayList<User>();
		query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.FaceBook.toString())));
		List<ConnectSocialUser> connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(query, ConnectSocialUser.class);
		String accessToken = null;
		if (null!=connectSocialUsers && connectSocialUsers.size()>0) {
			for(ConnectSocialUser socialUser : connectSocialUsers) {
				if(socialUser.getSocialType().equals(SocialTypesEnum.SocialTypes.FaceBook.toString())) {
					isConnected = socialUser.getConnect().toString();
					accessToken = socialUser.getAccessToken();
				}
			}
		} else {
			mav.addObject(ErrorCode.NOT_CONNECTED_TO_FB);
			return mav;
		}
		if(isConnected == null || isConnected.equals("false")) {
			mav.addObject(ErrorCode.NOT_CONNECTED_TO_FB);
			return mav;
		}
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
		Connection<com.restfb.types.User> friends = null;
		try{
			friends = facebookClient.fetchConnection(connectSocialUsers.get(0).getSocialId()+"/friends", com.restfb.types.User.class );
		}
		catch (FacebookOAuthException oe) {
			mav.addObject(ErrorCode.INVALID_ACCESS_TOKEN_7001);
		}
	    List<com.restfb.types.User> friendsList = friends.getData();
	    FacebookData fac = new FacebookData();
	    List<User> fbWaspitFriends = new ArrayList<User>();
	    List<FacebookData> facebookDatas = new ArrayList<FacebookData>();
	    for(com.restfb.types.User friend : friendsList) {
	    	//getting facebook friends & save in arryList
	    	try{
	    		fac = new FacebookData();
	    		fac.setFacebookId(friend.getId());
	    		fac.setFrdName(friend.getName());
	    		facebookDatas.add(fac);
	    	User waspitUser = userDao.getRegisterUserDetails(friend.getId());
	    	if(waspitUser != null) {
	    		fbWaspitFriends.add(waspitUser);
	    	}
	    	}catch(Exception e)
	    	{
	    		logger.info("Not Exist in waspit"+e);
	    	}
	    }
	    
	    //facebook friend not as waspit user List....
		for (FacebookData faceBookGetField : facebookDatas) {
			Query socialQuery = new Query();
			socialQuery.addCriteria(Criteria.where("socialId").is(faceBookGetField.getFacebookId()).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.Google.toString())));
			connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(socialQuery, ConnectSocialUser.class);
			if (null != connectSocialUsers && connectSocialUsers.size() > 0) {
				solrQuery = "to_user:" + connectSocialUsers.get(0).getUserId()+ " OR from_user: "+ connectSocialUsers.get(0).getUserId();
				// find the request for user...
				friendListIds = (PassFriendRequest) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL,PassFriendRequest.class, solrQuery, 0, 1, "",
								"", "false");
				if (friendListIds.getResponse().getDocs().size() == 0) {
					// Getting User Detail from Solr
					String userQuery = "user_id:"+ connectSocialUsers.get(0).getUserId();
					passUsers = (PassUsers) solrCommonClient.commonSolrSearch(
							UrlConstant.SEARCH_USERS_URL, PassUsers.class,
							userQuery, 0, 1, "", "", "false");

					if (passUsers.getResponse().numFound != 0) {
						for (User usr : passUsers.getResponse().getDocs()) {
							//  Create notification
			           			solrDocument=new SolrInputDocument();
		        			String newUpdateId=UUIDGenrator.getUniqueId();
		        				solrDocument.addField("id", newUpdateId);
		        				solrDocument.addField("type", UpdateTypeEnum.UpdateType.Notifications.toString());
		        				solrDocument.addField("subType", UpdateTypeEnum.UpdateSubType.Facebook.toString());	
		        				solrDocument.addField("property", UpdateTypeEnum.UpdateSubType.Facebook.toString());
		        				solrDocument.addField("privacy", MagicNumbers.ACTIVE);
		        				solrDocument.addField("toUser", connectSocialUsers.get(0).getUserId());	
		        				solrDocument.addField("fromUser", userId);
		        				solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
		        				solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
							waspitUserList.add(usr);
						}
					}
				}
			}
		}
	    
	    
	    
		logger.info("Friends:-"+friends);
		SocialFriends socialFriends = new SocialFriends();
		socialFriends.setCreatedDate(new Date());
		socialFriends.setUser(fbWaspitFriends);
		mongoTemplate.insert(socialFriends);
		mav.addObject("friends_list", facebookDatas);
		mav.addObject("waspitUser",waspitUserList);
		mav.addObject("nextPageUrl", friends.getNextPageUrl());
		return mav;
	}
	    
	
	
	@RequestMapping(value = "/post-message", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView postMessage(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "app_secret", required = true) String appSecret,
			@RequestParam(value = "fb_message", required = true) String fBMessage,
			@RequestParam(value = "picture", required = false) String picture,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();	
		if (apikey == null || apikey.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;
		} else if (token == null || token.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (userId ==null || userId.equals("") || appSecret ==null || appSecret.equals("") 
				|| fBMessage == null || fBMessage.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;
		} else if (solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,PassSession.class, "user_id:" + userId, 0, 1,"user_id", "", 
				"false").getResponse().getDocs().get(0).getUserId() == null) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:" + userId, 0, 1, "token",
						"", "false").getResponse().getDocs().get(0).getToken().equals(token)) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		}else if (apiDao.validateApiKey(apikey)) {
			//Validate User
			User user = userDao.getRegisterUserDetails(userId);
			if(user == null) {
				mav.addObject(ErrorCode.INVALID_PARAMETER_105);
				return mav;
			}
			Query query=new Query();
			query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.FaceBook.toString())));
			List<ConnectSocialUser> connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(query, ConnectSocialUser.class);
			String accessToken = null;
			if (null!=connectSocialUsers && connectSocialUsers.size()>0) {
				if(!connectSocialUsers.get(0).getConnect()) {
					mav.addObject(ErrorCode.NOT_CONNECTED_TO_FB);
					return mav;
				}
				accessToken = connectSocialUsers.get(0).getAccessToken();
			} else {
				mav.addObject(ErrorCode.NOT_CONNECTED_TO_FB);
				return mav;
			}
			FacebookClient facebookClient = new DefaultFacebookClient(accessToken, appSecret);
			FacebookType publishMessageResponse = null;
			try{
				publishMessageResponse= facebookClient.publish(connectSocialUsers.get(0).getSocialId()+"/feed", FacebookType.class, Parameter.with("message", fBMessage),Parameter.with("picture", picture));
			} catch(FacebookException fae){
				fae.printStackTrace();
				mav.addObject("Error", fae.getMessage());
				return mav;
			}
			String fbMessageID = publishMessageResponse.getId();
			logger.info("Message:- "+fbMessageID);
			mav.addObject("message_id", fbMessageID);
			mav.addObject("message", fBMessage);
		}
		return mav;
	}
	@RequestMapping(value = "/linkFbAccount", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView linkFbAccount(
			@RequestParam(value = "api_key", required = true) String apikey,	
			@RequestParam(value = "token", required = true) String token,	
			@RequestParam(value = "access_token", required = true) String accessToken,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "connect", required = true) String connect,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();	
		if (apikey == null || apikey.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;
		} else if (token == null || token.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (userId ==null || userId.equals("") || connect ==null || connect.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;
		} else if (solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,PassSession.class, "user_id:" + userId, 0, 1,"user_id", "", 
				"false").getResponse().getDocs().get(0).getUserId() == null) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:" + userId, 0, 1, "token",
						"", "false").getResponse().getDocs().get(0).getToken().equals(token)) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		}else if (apiDao.validateApiKey(apikey)) {
			User user = userDao.getRegisterUserAllDetails(userId);
			if(user == null) {
				mav.addObject(ErrorCode.getCustomeError(5002));
				return mav;
			}
			if(!connect.equals("true") && !connect.equals("false")) {
				mav.addObject(ErrorCode.getCustomeError(105));
				return mav;
			}
			//Get Response from Facebook API
			ModelAndView userDetailMap = getUserDetails(apikey, accessToken, request);
			//Get FaceBook User
			com.restfb.types.User fbUser = (com.restfb.types.User) userDetailMap.getModel().get("fb_user_details");
			Query query=new Query();
			query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.FaceBook.toString())));
			List<ConnectSocialUser> connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(query, ConnectSocialUser.class);
			if (null!=connectSocialUsers && connectSocialUsers.size()>0) {
				ConnectSocialUser socialUser = connectSocialUsers.get(0);
				//if mongo entry with connect is false then delete mongo entry
				if(connect.equals("false")) {
					mongoCommonClient.deleteObject(socialUser.getSocialId(), "socialId", ConnectSocialUser.class);
					mav.addObject("User_Connected", connect);
					mav.addObject("userId",userId);
					return mav;
				}
			} else {
				//connect is True then save in Mongo entry with true
				ConnectSocialUser connectSocialUser = null;
				AddSocialUser addSocialUser = new AddSocialUser();
				connectSocialUser = addSocialUser.setFacebookUser(fbUser);
				connectSocialUser.setAccessToken(accessToken);
				if(connect.equals("true")) {
					connectSocialUser.setConnect(true);
				} else {
					connectSocialUser.setConnect(false);
				}
				//Get Registered User Id
				connectSocialUser.setUserId(userId);
				mongoTemplate.insert(connectSocialUser);
			}
			mav.addObject("userId", userId);
			mav.addObject("User_Connected", connect);
		}
		return mav;
	}
	
	@RequestMapping(value = "/get-facebook-recomendation", method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getFacebookRecom(
			@RequestParam(value = "user_id", required = false) String userId,
			@RequestParam(value = "access_token", required = true) String accessToken,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();	
		//Validate User
		
		String query = "SELECT uid,first_name,last_name,middle_name,pic_square,is_app_user,email FROM"+ 
						" user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1 = me()) AND" +
						" is_app_user = 1";
		Connection<com.restfb.types.User> friends = null;
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
		List<com.restfb.types.User> list = facebookClient.executeFqlQuery(query, com.restfb.types.User.class);
	    friends = facebookClient.fetchConnection("me/friends", com.restfb.types.User.class );
	    List<com.restfb.types.User> friendsList = friends.getData();
	    List<User> fbWaspitFriends = new ArrayList<User>();
	    
		logger.info("Friends:-"+friends);
		mav.addObject("friends_list", fbWaspitFriends);
		mav.addObject("nextPageUrl", friends.getNextPageUrl());
		return mav;
	}
}

package com.main.interconnection.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.social.ApiException;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.main.interconnection.client.social.helpers.AddSocialUser;
import com.main.interconnection.client.social.helpers.SocialTypesEnum;
import com.main.interconnection.clientBo.ConnectSocialUser;
import com.main.interconnection.clientBo.ErrorCode;
import com.main.interconnection.clientBo.FriendRequest;
import com.main.interconnection.clientBo.SocialFriends;
import com.main.interconnection.clientBo.User;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.dao.UserDao;
import com.main.interconnection.mongoDao.MongoCommonClient;
import com.main.interconnection.solr.response.friendRequest.PassFriendRequest;
import com.main.interconnection.solr.response.session.PassSession;
import com.main.interconnection.solr.response.users.PassUsers;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
import com.main.interconnection.twitterImpl.TwitterPostServiceImpl;
import com.main.interconnection.twitterImpl.TwitterUserServiceImpl;
import com.main.interconnection.util.CommonEmail;

@Controller
@RequestMapping(value = "/twitter/*")
public class InterconnectionTwitterClient {
	
	private static final Logger logger = LoggerFactory.getLogger(InterconnectionTwitterClient.class);
	
	@Autowired
	TwitterUserServiceImpl twitterUserService;
	
	@Autowired
	TwitterPostServiceImpl twitterPostService;
	
	@Autowired
	SolrCommonClient solrCommonClient;
	
	@Autowired
	MongoCommonClient mongoCommonClient;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	ApiDao apiDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	CommonEmail commonEmail;
	
	/*@Value("${twitter.consumerKey}")
	String consumerKey;
	
	@Value("${twitter.consumer.secret}")
	String consumerSecret;*/
	
	@RequestMapping(value = "/save-access-token",method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView saveAccessToken(@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "access_token", required = true) String accessToken,
			@RequestParam(value = "access_secret", required = true) String accessSecret,
			@RequestParam(value = "consumer_key", required = true) String consumerKey,
			@RequestParam(value = "consumer_secret", required = true) String consumerSecret,
			@RequestParam(value = "connect", required = true) String connect,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		ConnectSocialUser connectSocialUser = null;
		List<ConnectSocialUser> connectSocialUsers=new ArrayList<ConnectSocialUser>();
		HashMap<String, String> params = new HashMap<String, String>();
		User user = null;
		String templatePath = null;
		if (apikey == null || apikey.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;
		} else if (token == null || token.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (userId == null || userId.equals("") ||
				connect == null || connect.equals("") ||
				accessToken == null || accessToken.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;
		}
		else if ((!connect.equals("true") && !connect.equals("false"))) {
			mav.addObject(ErrorCode.getCustomeError(105));
			return mav;
		}else if (solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,PassSession.class, "user_id:" + userId, 0, 1,"user_id", "", 
				"false").getResponse().getDocs().get(0).getUserId() == null) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:" + userId, 0, 1, "token",
						"", "false").getResponse().getDocs().get(0).getToken().equals(token)) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		}else if (apiDao.validateApiKey(apikey)) {
			Query query=new Query();
			query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.Twitter.toString())));
			connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(query, ConnectSocialUser.class);
			if(connect.equals("false")) {
				if (null != connectSocialUsers && connectSocialUsers.size() > 0) {
					mongoCommonClient.deleteObject(connectSocialUsers.get(0).getSocialId(), "socialId", ConnectSocialUser.class);
					mav.addObject("User_Connected", connect);
					mav.addObject("userId",userId);
					return mav;
				}
			}
			TwitterProfile profile = null;
			try{
				profile = twitterUserService.getUserInfo(accessToken, accessSecret, consumerKey, consumerSecret);
			} catch(ApiException e) {
				mav.addObject(e.getMessage());
				return mav;
			}
			if (null != connectSocialUsers && connectSocialUsers.size()>0) {
				mongoCommonClient.deleteObject(connectSocialUsers.get(0).getSocialId(), "socialId", ConnectSocialUser.class);
			} 
			// if social user account is not connected then register social user
			try{
				profile = twitterUserService.getUserInfo(accessToken, 
						accessSecret, consumerKey, consumerSecret);
			} catch(ApiException e) {
				mav.addObject(e.getMessage());
				return mav;
			}
			if (null != profile) {
				if (!mongoTemplate.collectionExists(ConnectSocialUser.class)) {
					mongoTemplate.createCollection(ConnectSocialUser.class);
				}
				AddSocialUser addSocialUser = new AddSocialUser();
				connectSocialUser = addSocialUser.setTwitterUserUser(profile);
				connectSocialUser.setAccessToken(accessToken);
				connectSocialUser.setAccessSecret(accessSecret);
				if(connect.equals("true")) {
					connectSocialUser.setConnect(true);
				} else {
					connectSocialUser.setConnect(false);
				}
				connectSocialUser.setUserId(userId);
				mongoTemplate.insert(connectSocialUser);
				user=userDao.getRegisterUserDetails(userId);
				if(user.getName() != null && !user.getName().equals("")) {
					params.put("_NAME_", user.getName());
				}
				params.put("_SOCIAL_", "With your Twitter Account.");
				templatePath = request.getSession().getServletContext().getRealPath(
							"/WEB-INF/mailTemplate/welcomeSocial.template");
				commonEmail.sendEmail(templatePath, params, "Waspit welcome email", user.getEmailId());
				mav.addObject("User_Connected", connect);
				mav.addObject("userId",userId);
				return mav;
			}else{
				logger.info(ErrorCode.getCustomeError(5002).toString());
				mav.addObject(ErrorCode.getCustomeError(5002).toString()+"Twitter");
			}
		} else {
				mav.addObject(ErrorCode.getCustomeError(100));
		}
		return mav;
	}
	
	@RequestMapping(value = "/update-status",method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView updateStatus(@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "consumer_key", required = true) String consumerKey,
			@RequestParam(value = "consumer_secret", required = true) String consumerSecret,
			@RequestParam(value = "picture", required = false) String picture,
			@RequestParam(value = "status", required = false) String status) {
		ModelAndView mav = new ModelAndView();
		
		String result = null;
		if (apikey == null || apikey.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;
		} else if (token == null || token.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (userId == null || userId.equals("") ||
				status == null || status.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;
		} else if (solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:" + userId, 0, 1,
				"user_id", "", "false").getResponse().getDocs().get(0).getUserId() == null) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:" + userId, 0, 1, "token",
				"", "false").getResponse().getDocs().get(0).getToken().equals(token)) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		}else if (apiDao.validateApiKey(apikey)) {
			List<ConnectSocialUser> connectSocialUsers=null;
			Query query=new Query();
			query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.Twitter.toString())));
			connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(query, ConnectSocialUser.class);
			if (null!=connectSocialUsers && connectSocialUsers.size()>0) {
				ConnectSocialUser user=connectSocialUsers.get(0);
				if(user.getConnect()==false) {
					return mav.addObject(ErrorCode.getCustomeError(5004));
				}
				String accessToken=user.getAccessToken();
				String accessSecret=user.getAccessSecret();
				/*//Publish Message
				try {
					result = twitterPostService.updateStatus(accessToken, accessSecret, status, consumerKey, consumerSecret);
				}catch(ApiException e) {
					mav.addObject("Error ", e.getMessage());
					return mav;
				}*/
				//Publish Picture
				try {
					result = twitterPostService.postImage(accessToken, accessSecret, picture, status, consumerKey, consumerSecret);
				}catch(ApiException e) {
					mav.addObject("Error ", e.getMessage());
					return mav;
				}
				if(result == null) {
					mav.addObject("Error ", ErrorCode.STATUS_NOT_POSTED_7003);
					return mav;
				}
				mav.addObject("Message: ","Status Posted");
				mav.addObject("MessageId: ",result);
			}
		}
		return mav;
	}
	
	@RequestMapping(value = "/get-followers",method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getFollowers(@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "consumer_key", required = true) String consumerKey,
			@RequestParam(value = "consumer_secret", required = true) String consumerSecret,
			@RequestParam(value = "cursor", required = true) long cursor) {
		ModelAndView mav = new ModelAndView();
		String accessToken = null;
		String accessSecret = null;
		String solrQuery=null;
		PassUsers passUsers=null;
		PassFriendRequest friendListIds=null;
		List<User> waspitUser = new ArrayList<User>();
		CursoredList<TwitterProfile> followers = null;
		if (apikey == null || apikey.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;
		} else if (token == null || token.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (userId == null || userId.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;
		} else if (solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:" + userId, 0, 1,
				"user_id", "", "false").getResponse().getDocs().get(0).getUserId() == null) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:" + userId, 0, 1, "token",
				"", "false").getResponse().getDocs().get(0).getToken().equals(token)) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		}else if (apiDao.validateApiKey(apikey)) {
			Query query=new Query();
			query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.Twitter.toString())));
			List<ConnectSocialUser> connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(query, ConnectSocialUser.class);
			if (null!=connectSocialUsers && connectSocialUsers.size()>0) {
				ConnectSocialUser socialUser = connectSocialUsers.get(0);
				accessToken=socialUser.getAccessToken();
				accessSecret=socialUser.getAccessSecret(); 
				if(socialUser.getConnect() == false) {
					return mav.addObject(ErrorCode.getCustomeError(5004));
				}	
				try {
					followers = twitterUserService.getUserFollowers(userId, accessToken, accessSecret, consumerKey, consumerSecret, cursor);
				}catch(ApiException e) {
					mav.addObject("Error ", e.getMessage());
					return mav;
				}
				//followers are not blank
				if (null != followers) {
					ArrayList<String> twitterId = new ArrayList<String>();
					for(TwitterProfile follower : followers) {
						twitterId.add(String.valueOf(follower.getId()));
					}
					
					for(String follower : twitterId) {
						//Twitter user Exist in Mongo
						Query socialQuery=new Query();
						socialQuery.addCriteria(Criteria.where("socialId").is(follower).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.Twitter.toString())));
						connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(socialQuery, ConnectSocialUser.class);
						if (null != connectSocialUsers && connectSocialUsers.size() > 0) {
							solrQuery="to_user:"+connectSocialUsers.get(0).getUserId() +" OR from_user: "+ connectSocialUsers.get(0).getUserId();	
							//find the request for user...
							friendListIds=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, solrQuery, 0, 1, "", "", "false");
							if(friendListIds.getResponse().getDocs().size()==0){
								
									//Getting User Detail from Solr
									String userQuery="user_id:"+connectSocialUsers.get(0).getUserId();
									passUsers=(PassUsers)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL, PassUsers.class, userQuery, 0, 1, "", "", "false");
									
									if(passUsers.getResponse().numFound!=0)
									{
										for(User usr:passUsers.getResponse().getDocs())
										{
											waspitUser.add(usr);
										}
									}
								
							}	
								}
					}
				
					//get waspi
					
					mav.addObject("Waspit_friends", waspitUser);
					mav.addObject("twitter_friends", followers);
				}
				
				//followers are blank		
				else {
					logger.info("No followers are on twitter and Waspit");
					mav.addObject("Waspit_friends", 0);
					mav.addObject("twitter_friends",0);
				}
			}
		}
		return mav;
	}
	
	//Get All Followers and save in mongo for sending recommendation
	@RequestMapping(value = "/get-all-followers",method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getAllFollowers(@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "consumer_key", required = true) String consumerKey,
			@RequestParam(value = "consumer_secret", required = true) String consumerSecret) {
		ModelAndView mav = new ModelAndView();
		String accessToken = null;
		String accessSecret = null;
		CursoredList<TwitterProfile> followers = null;
		if (apikey == null || apikey.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;
		} else if (token == null || token.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (userId == null || userId.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;
		} else if (solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:" + userId, 0, 1,
				"user_id", "", "false").getResponse().getDocs().get(0).getUserId() == null) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:" + userId, 0, 1, "token",
				"", "false").getResponse().getDocs().get(0).getToken().equals(token)) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		}else if (apiDao.validateApiKey(apikey)) {
			Query query=new Query();
			query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.Twitter.toString())));
			List<ConnectSocialUser> connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(query, ConnectSocialUser.class);
			if (null!=connectSocialUsers && connectSocialUsers.size()>0) {
				ConnectSocialUser socialUser = connectSocialUsers.get(0);
				accessToken=socialUser.getAccessToken();
				accessSecret=socialUser.getAccessSecret(); 
				if(socialUser.getConnect() == false) {
					return mav.addObject(ErrorCode.getCustomeError(5004));
				}	
				try {
					followers = twitterUserService.getUsersAllFollowers(userId, accessToken, accessSecret, consumerKey, consumerSecret);
				}catch(ApiException e) {
					mav.addObject("Error ", e.getMessage());
					return mav;
				}
				if (null != followers) {
					//Check if follower is connection with twitter on waspit
					//TODO Move code to AddSocialUser
					List<User> twitterFollowers = new ArrayList<User>();
					for(TwitterProfile follower : followers) {
						List<ConnectSocialUser> twitterFollowerList = (List<ConnectSocialUser>) mongoCommonClient.
								findById(String.valueOf(follower.getId()), "socialId", ConnectSocialUser.class);
						if(twitterFollowerList != null && twitterFollowerList.size() > 0) {
							SocialFriends socialFriends = new SocialFriends();
							socialFriends.setCreatedDate(new Date());
							socialFriends.setUser(twitterFollowerList);
							mongoTemplate.insert(socialFriends);
						}
					}
					mav.addObject("twitter_waspit_friends", twitterFollowers);
				} else {
					logger.info("No friends are on waspit");
					mav.addObject(ErrorCode.getCustomeError(5003));
				}
			}
		}
		return mav;
	}
}

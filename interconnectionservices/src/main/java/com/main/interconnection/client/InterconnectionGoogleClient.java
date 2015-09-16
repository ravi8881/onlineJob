package com.main.interconnection.client;

import java.util.ArrayList;
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
import com.main.interconnection.clientBo.GoogleFriends;
import com.main.interconnection.clientBo.GoogleUser;
import com.main.interconnection.clientBo.Item;
import com.main.interconnection.clientBo.User;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.dao.UserDao;
import com.main.interconnection.google.GoogleUserService;
import com.main.interconnection.mongoDao.MongoCommonClient;
import com.main.interconnection.solr.response.friendRequest.PassFriendRequest;
import com.main.interconnection.solr.response.session.PassSession;
import com.main.interconnection.solr.response.users.PassUsers;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
  
@Controller
@RequestMapping(value = "/google/*")
public class InterconnectionGoogleClient {

	@Value("${google.apiId}")
	String googleApiId;

	@Value("${google.apiKey}")
	String googleApiKey;

	@Value("${google.appName}")
	String googleAppName;

	@Value("${google.dissconnectUrl}")
	String disconnectUrl;

	@Value("${google.userinfoUrl}")
	String userInfoUrl;

	@Autowired
	UserDao userDao;

	@Autowired
	SolrCommonClient solrCommonClient;

	SolrInputDocument solrDocument;

	@Autowired
	GoogleUserService googleUserService;

	@Autowired
	MongoCommonClient mongoCommonClient;

	@Autowired
	ApiDao apiDao;
	
	@Autowired
	MongoTemplate mongoTemplate;

	private static final Logger logger = LoggerFactory.getLogger(InterconnectionGoogleClient.class);

	@RequestMapping(value = "/google-save-access-token", method = {RequestMethod.POST, RequestMethod.GET })
	
	public ModelAndView saveAccessToken(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "access_token", required = true) String accessToken,HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		//Flag to signify registedUser
		boolean is_registered = false;
		//Flag to signify verifiedUser
		boolean is_verified = false;
		//Write code to validate Api user
		if (apikey == null || apikey.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;
		} else if (accessToken == null || accessToken.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;
		}
		//Get Response from Google API
		GoogleUser user = googleUserService.getUserInfoWithAccessToken(accessToken, userInfoUrl);
		//Check if user exists with EmailId
		String userId = userDao.getUserIdByEmail(user.getEmail());
		ConnectSocialUser connectSocialUser = null;
		// Register New Waspit User in case email does not exists
		if(userId == null) {
			User registeredUser = userDao.getRegisterUserDetails(user.getId());
			if(registeredUser != null) {
				mav.addObject("user",user);
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
			connectSocialUser = addSocialUser.setGoogleUser(user);
			connectSocialUser.setAccessToken(accessToken);
			connectSocialUser.setConnect(true);
			connectSocialUser.setUserId(user.getId());
			mongoTemplate.insert(connectSocialUser);
			mav.addObject("user",user);
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
		query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.Google.toString())));
		List<ConnectSocialUser> connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(query, ConnectSocialUser.class);
		if(connectSocialUsers != null && connectSocialUsers.size() > 0) {
			mongoCommonClient.deleteObject(connectSocialUsers.get(0).getSocialId(), "socialId", ConnectSocialUser.class);
		}
		AddSocialUser addSocialUser = new AddSocialUser();
		connectSocialUser = addSocialUser.setGoogleUser(user);
		connectSocialUser.setAccessToken(accessToken);
		connectSocialUser.setConnect(true);
		connectSocialUser.setUserId(userId);
		mongoTemplate.insert(connectSocialUser);
		PassUsers userSolrDetails = (PassUsers)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL,PassUsers.class,	"user_id:" + registeredUser.getUserId(),	0,	1,"","", "false");
		mav.addObject("user",userSolrDetails.getResponse().getDocs().get(0));

	//	mav.addObject("user",registeredUser);
		mav.addObject("is_registered", is_registered);
		mav.addObject("is_verified", is_verified);
		//Create session in solr
		solrDocument = new SolrInputDocument();
		solrDocument.addField("user_id", userId);
		solrDocument.addField("token", "1");
		solrCommonClient.addObjectToSolr(UrlConstant.SESSION_URL, solrDocument);
		mav.addObject("token", solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:" + userId,
				0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken());
		return mav;
	}

	@RequestMapping(value = "/linkGoogleAccount", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView linkGoogleAccount(
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
			//Get Google User
			GoogleUser googleUser = googleUserService.getUserInfoWithAccessToken(accessToken, userInfoUrl);
			Query query=new Query();
			query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.Google.toString())));
			List<ConnectSocialUser> connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(query, ConnectSocialUser.class);
			logger.info("********connectSocialUsers********"+connectSocialUsers);
			if (null!=connectSocialUsers && connectSocialUsers.size()>0) {
				ConnectSocialUser socialUser = connectSocialUsers.get(0);
				logger.info("********socialUser********"+socialUser+"*****connect*********"+connect);
				if(connect.equals("false")) {
					mongoCommonClient.deleteObject(socialUser.getSocialId(), "socialId", ConnectSocialUser.class);
					mav.addObject("User_Connected", connect);
					mav.addObject("userId",userId);
					return mav;
				}
			} else {
				logger.info("********else*******"+connect);
				ConnectSocialUser connectSocialUser = null;
				AddSocialUser addSocialUser = new AddSocialUser();
				connectSocialUser = addSocialUser.setGoogleUser(googleUser);
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
	

	@RequestMapping(value = "/google-plus-user", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView googlePlusUser(
			
			@RequestParam(value = "access_token", required = false) String accessToken,
			@RequestParam(value = "user_id", required = false) String userId,
			@RequestParam(value = "connect", required = false) String connect,
			HttpServletRequest request) {
		    ModelAndView mav = new ModelAndView();	
			String accessToken1 = null;
			String solrQuery=null;
			PassUsers passUsers=null;
			PassFriendRequest friendListIds=null;
			List<User> waspitUser = new ArrayList<User>();

			   Query query=new Query();
				query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.Google.toString())));
				List<ConnectSocialUser> connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(query, ConnectSocialUser.class);
				if (null!=connectSocialUsers && connectSocialUsers.size()>0) {
					ConnectSocialUser socialUser = connectSocialUsers.get(0);
					accessToken1=socialUser.getAccessToken();
					
					if(socialUser.getConnect() == false) {
						return mav.addObject(ErrorCode.getCustomeError(5004));
					}
				// Google Friends	
					try{
		    GoogleFriends googleFriends= googleUserService.getUserFriends(accessToken, UrlConstant.GOOGLE_PLUS_FRIENDS);
		    
		    ArrayList<String> googleId = new ArrayList<String>();
			for(Item id : googleFriends.getItems()) {
				googleId.add(String.valueOf(id.getId()));
				}
			for(String id : googleId) {
		    Query socialQuery=new Query();
			socialQuery.addCriteria(Criteria.where("socialId").is(id).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.Google.toString())));
			connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(socialQuery, ConnectSocialUser.class);
			if (null != connectSocialUsers && connectSocialUsers.size() > 0) {
				solrQuery="to_user:"+connectSocialUsers.get(0).getUserId() +" OR from_user: "+ connectSocialUsers.get(0).getUserId();	
				//  find the request for user...
				friendListIds=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, solrQuery, 0, 1, "", "", "false");
				if(friendListIds.getResponse().getDocs().size()==0){
				//  Getting User Detail from Solr
					String userQuery="user_id:"+connectSocialUsers.get(0).getUserId();
					passUsers=(PassUsers)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL, PassUsers.class, userQuery, 0, 1, "", "", "false");
					
					if(passUsers.getResponse().numFound!=0){
						for(User usr:passUsers.getResponse().getDocs()){
							waspitUser.add(usr);
							}
						}
					}
				}
			}
			mav.addObject("Waspit_friends", waspitUser);
			mav.addObject("Google_friends",googleFriends);
		
		}catch(Exception e){
			mav.addObject("Error :"," Invaild Access Token ");
			e.printStackTrace();
		}
						
	}
	return mav;	
	}
	
	
}



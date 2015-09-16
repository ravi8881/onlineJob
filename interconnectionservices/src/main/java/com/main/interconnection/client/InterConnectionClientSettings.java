package com.main.interconnection.client;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.main.interconnection.clientBo.ErrorCode;
import com.main.interconnection.clientBo.UserSettings;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.mongoDao.MongoCommonClient;
import com.main.interconnection.solr.helper.SetSolrDocuments;
import com.main.interconnection.solr.response.session.PassSession;
import com.main.interconnection.solr.response.users.PassUsers;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
import com.main.interconnection.util.ParseUtils;

@Controller
@RequestMapping(value = "/settings/*")
public class InterConnectionClientSettings {

	@Autowired
	ApiDao apiDao;
	@Autowired
	SolrCommonClient solrCommonClient;
	@Autowired
	MongoCommonClient mongoCommonClient;
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	SetSolrDocuments setSolrDocuments;

	private static final Logger logger = LoggerFactory.getLogger(InterConnectionClientSettings.class);

	@RequestMapping(value = "/privacy-setting", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView userPrivacySettings(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "profileOnOff", required = true) String profileOnOff,
			@RequestParam(value = "emailOnOff", required = true) String emailOnOff,
			@RequestParam(value = "nameOnOff", required = true) String nameOnOff,
			@RequestParam(value = "phoneOnOff", required = true) String phoneOnOff,
			@RequestParam(value = "profilePicOnOff", required = true) String profilePicOnOff,
			@RequestParam(value = "searchEngOnOff", required = true) String searchEngOnOff,
			@RequestParam(value = "frndChkInOnOff", required = true) String frndChkInOnOff,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String query = null;
		boolean usersAddStatus=false;
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
				query="user_id:"+userId;
				PassUsers passUsers=(PassUsers)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL, PassUsers.class, query, 0, 1, "", "", "false");	
				SolrInputDocument solrInputDocument=setSolrDocuments.getUserInputDoc(passUsers.getResponse().getDocs().get(0)); 
				solrInputDocument.addField("profileOnOff", profileOnOff);
				solrInputDocument.addField("emailOnOff", emailOnOff);
				solrInputDocument.addField("nameOnOff", nameOnOff);
				solrInputDocument.addField("phNoOnOff", phoneOnOff);
				solrInputDocument.addField("searchEngOnOff", searchEngOnOff);
				solrInputDocument.addField("profilePicOnOff", profilePicOnOff);
				solrInputDocument.addField("frndChkInOnOff", frndChkInOnOff);
				usersAddStatus = solrCommonClient.addObjectToSolr(
						UrlConstant.USERS_URL, solrInputDocument);
				if(usersAddStatus){
					mav.addObject("userId",userId);
					UserSettings userSettings = new UserSettings();
					userSettings.setProfileOnOff(ParseUtils.stringToInt(profileOnOff));
					userSettings.setEmailOnOff(ParseUtils.stringToInt(emailOnOff));
					userSettings.setNameOnOff(ParseUtils.stringToInt(nameOnOff));
					userSettings.setPhNoOnOff(ParseUtils.stringToInt(phoneOnOff));
					userSettings.setSearchEngOnOff(ParseUtils.stringToInt(searchEngOnOff));
					userSettings.setProfilePicOnOff(ParseUtils.stringToInt(profilePicOnOff));
					userSettings.setFrndChkInOnOff(ParseUtils.stringToInt(frndChkInOnOff));
					mav.addObject("userPrivacySetting", userSettings);
				}else{
					logger.info("Problem occurred to save Privacy Settings");
					mav.addObject(ErrorCode.getCustomeError(1001));
				}
				
			} else {
				return mav.addObject(ErrorCode.getCustomeError(100));
			}

		} catch (Exception e) {
			logger.info(
					"Controller /settings/----- Inside User Privacy Settings ---- ",
					e);
			logger.info("--LogTime----" + Calendar.getInstance().getTime());
			return mav.addObject(ErrorCode.getCustomeError(1001));
		}
		return mav;
	}
	@RequestMapping(value = "/get-privacy-setting", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getPrivacySettings(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String query=null;
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
					query = "user_id:" + userId;
					PassUsers userDetails = (PassUsers) solrCommonClient
							.commonSolrSearch(
									UrlConstant.SEARCH_USERS_URL,
									PassUsers.class,
									query,
									0,
									1,
									"user_id,profileOnOff,emailOnOff,nameOnOff,phNoOnOff,searchEngOnOff,frndChkInOnOff,profilePicOnOff",
									"", "false");
					mav.addObject(userDetails);
			} else {
				return mav.addObject(ErrorCode.getCustomeError(100));
			}

		} catch (Exception e) {
			logger.info(
					"Controller /settings/----- Inside get user Privacy Settings ---- ",
					e);
			logger.info("--LogTime----" + Calendar.getInstance().getTime());
			return mav.addObject(ErrorCode.getCustomeError(1001));
		}
		return mav;
	}
}

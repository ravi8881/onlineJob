package com.main.interconnection.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.main.interconnection.client.social.helpers.SocialTypesEnum;
import com.main.interconnection.clientBo.ApiKey;
import com.main.interconnection.clientBo.City;
import com.main.interconnection.clientBo.ConnectSocialUser;
import com.main.interconnection.clientBo.ErrorCode;
import com.main.interconnection.clientBo.HelpFulReview;
import com.main.interconnection.clientBo.Message;
import com.main.interconnection.clientBo.State;
import com.main.interconnection.clientBo.TagFriends;
import com.main.interconnection.clientBo.UpdateRecord;
import com.main.interconnection.clientBo.User;
import com.main.interconnection.clientBo.UserDetails;
import com.main.interconnection.clientBo.Venue;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.dao.CityDao;
import com.main.interconnection.dao.StateDao;
import com.main.interconnection.dao.UserDao;
import com.main.interconnection.mongoDao.MongoCommonClient;
import com.main.interconnection.solr.response.friendRequest.PassFriendRequest;
import com.main.interconnection.solr.response.image.PassImage;
import com.main.interconnection.solr.response.session.PassSession;
import com.main.interconnection.solr.response.users.PassUsers;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
import com.main.interconnection.util.CommonEmail;
import com.main.interconnection.util.MagicNumbers;
import com.main.interconnection.util.SolrDateUtil;
import com.main.interconnection.util.UUIDGenrator;
import com.main.interconnection.util.UpdateTypeEnum;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Controller
@RequestMapping(value = "/client/*")
public class InterConnectionClient {

	private static final Logger logger = LoggerFactory.getLogger(InterConnectionClient.class);

	@Autowired
	CommonEmail commonEmail;

	@Autowired
	ApiDao apiDao;
	
	@Autowired
	MongoCommonClient mongoCommonClient;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	UserDao userDao;
	
	@Autowired
	CityDao cityDao;
	
	@Autowired
	StateDao stateDao;

	@Autowired
	SolrCommonClient solrCommonClient;
	@Autowired
	SolrInputDocument solrDocument;
	
	public static final String DEFUALT_PRIVACY_SETTINGS="0";	

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/client", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! the client locale is " + locale.toString());

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);

		try {
			BufferedImage originalImage = ImageIO
					.read(new File("c:\\Koala.jpg"));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "jpg", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			String encodedImage = Base64.encode(baos.toByteArray());
			model.addAttribute("Image", encodedImage);
			baos.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		model.addAttribute("serverTime", formattedDate);
		return "home";
	}

	@RequestMapping(value = "/register-api-key", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView rgisterApiKey(
			@RequestParam(value = "email_id", required = false) String emailId,
			HttpServletRequest request) {

		ModelAndView mav = new ModelAndView();

		ApiKey apiKey = new ApiKey();
		boolean apiFlag = false;
		ApiKey userApiKey = null;
		if (emailId == null || emailId.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(1001));
			return mav;
		}
		apiKey.setDeveloperKey(UUIDGenrator.getUniqueId());
		apiKey.setEmailId(emailId);
		apiKey.setApikey(UUIDGenrator.getUniqueId());
		apiKey.setApiSalt(UUIDGenrator.getUniqueId());
		apiKey.setAddeddate(new Date());
		apiKey.setModifydate(new Date());
		boolean userEmailExist = apiDao.userEmailExist(emailId);

		if (!userEmailExist) {
			apiFlag = apiDao.registerApiKey(apiKey);
			if (apiFlag) {
				userApiKey = apiDao.findRegisterEmail(emailId);
				mav.addObject(userApiKey);
			} else {
				mav.addObject(ErrorCode.getCustomeError(3001));
				return mav;
			}
		} else {
			mav.addObject(ErrorCode.getCustomeError(3001));
			return mav;
		}
		return mav;
	}

	@RequestMapping(value = "/get-api-key", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView getApiKey(
			@RequestParam(value = "email_id", required = false) String emailId,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		ApiKey userApiKey = null;

		if (emailId == null || emailId.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(1001));
			return mav;
		} else {
			if (apiDao.userEmailExist(emailId)) {
				userApiKey = apiDao.findRegisterEmail(emailId);
				mav.addObject(userApiKey);
			} else {
				mav.addObject(ErrorCode.getCustomeError(103));
			}
		}
		return mav;
	}

	@RequestMapping(value = "/email-exists", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView userEmailExist(
			@RequestParam(value = "email_id", required = false) String emailId,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();

		if (emailId == null || emailId.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(1001));
			return mav;
		}
		boolean exists = apiDao.userEmailExist(emailId);
		mav.addObject("email_exists", exists);
		return mav;
	}

	@RequestMapping(value = "/validate-api-key", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView ValidateApi(
			@RequestParam(value = "api_key", required = false) String apikey,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();

		if (apikey == null || apikey.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;
		}

		boolean apiKeyStatus = apiDao.validateApiKey(apikey);
		if (apiKeyStatus) {
			mav.addObject("Api Status", apiKeyStatus);
			mav.addObject("Api Message", "ApiKey exists");
		} else {
			mav.addObject("Api Status", apiKeyStatus);
			mav.addObject("Api Message", "ApiKey does not exists");
			return mav;
		}
		return mav;
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "/register-user", method = { RequestMethod.POST,RequestMethod.GET })
	public ModelAndView registerUser(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_name", required = true) String userName,
			@RequestParam(value = "email_id", required = true) String emailId,
			@RequestParam(value = "mobile_number", required = true) String mobileNumber,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "image_id", required = false) String imageUrl,
			@RequestParam(value = "about_us", required = false) String aboutUs,
			@RequestParam(value = "city", required = true) String city,
			@RequestParam(value = "state", required = true) String state,
			@RequestParam(value = "gendre", required = true) String gendre,
			@RequestParam(value = "birthday", required = false) String birthday,
			@RequestParam(value = "iAgree", required = true) String iAgree,
			@RequestParam(value = "detailsOnEmail", required = true) String detailsOnEmail,
			@RequestParam(value = "lat", required = false) String lat,
			@RequestParam(value = "long", required = false) String longitude,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		User user = new User();
		boolean userInsert = false;
		Integer gendreInt = Integer.valueOf(gendre);
		Integer iAgreeInt = Integer.valueOf(iAgree);
		Integer detailsOnEmailInt = Integer.valueOf(detailsOnEmail);
		boolean usersAddStatus = false;
		HashMap<String, String> params = new HashMap<String, String>();
		String templatePath=null;
		Date date= null;
		String formattedDate = null;
		if(birthday != null && !birthday.equals("")) {
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		}
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (userName == null || userName.equals("")
					|| emailId == null || emailId.equals("")
					|| password == null || password.equals("")
					|| mobileNumber == null) {
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;
			} else if (iAgreeInt != MagicNumbers.IAGREE) {
				mav.addObject(ErrorCode.getCustomeError(3004));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {	
				user.setUserId(UUIDGenrator.getUniqueId());
				user.setName(userName);
				user.setEmailId(emailId);
				user.setMobileNumber(mobileNumber);
				user.setPassword(password);
				user.setImageUrl(imageUrl);
				user.setAboutUs(aboutUs);
				City cityObject = cityDao.getCityDetailsById(Integer.parseInt(city));
				String cityVal = city + ":" + cityObject.getCityName();
				if (cityObject == null) {
					mav.addObject(ErrorCode.getCustomeError(108));
					return mav;
				} else {
					user.setCity(cityVal);
				}
				State stateObject = stateDao.getStateDetailsById(Integer.parseInt(state));
				String stateVal = state + ":" + stateObject.getStateName();
				if (stateObject == null) {
					mav.addObject(ErrorCode.getCustomeError(107));
					return mav;
				} else {
					user.setState(stateVal);
				}
				user.setGendre(gendreInt);
				user.setBirthday(formattedDate);
				user.setBirthDayStr(birthday);
				user.setIagree(iAgreeInt);
				user.setCreateDate(new Date());
				if (detailsOnEmail == null)
					user.setDetailsOnEmail(MagicNumbers.EMAIL_DETAILS_YES);
				else
					user.setDetailsOnEmail(detailsOnEmailInt);
				user.setEmailVerifyCode(UUIDGenrator.generateCaptchaCode());
				user.setMobileVerifyCode(UUIDGenrator.generateCaptchaCode());
				if (userDao.waspitUserEmailExist(user.getEmailId())==0) {
					userInsert = userDao.registerUser(user);
					if (userInsert) {
						solrDocument = new SolrInputDocument();
						solrDocument.addField("user_id", user.getUserId());
						solrDocument.addField("name", user.getName());
						solrDocument.addField("searchName", user.getName());
						solrDocument.addField("emailId", user.getEmailId());
						solrDocument.addField("mobileNo",user.getMobileNumber());
						solrDocument.addField("imageUrl", user.getImageUrl());
						solrDocument.addField("aboutUs", user.getAboutUs());
						solrDocument.addField("city", user.getCity());
						solrDocument.addField("state", user.getState());
						solrDocument.addField("gendre", gendre);
						solrDocument.addField("birthDay", SolrDateUtil.addDateToSolr(birthday));
						solrDocument.addField("iAgree", iAgree);
						solrDocument.addField("createDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
						solrDocument.addField("profileOnOff",DEFUALT_PRIVACY_SETTINGS);
						solrDocument.addField("emailOnOff",DEFUALT_PRIVACY_SETTINGS);
						solrDocument.addField("nameOnOff", DEFUALT_PRIVACY_SETTINGS);
						solrDocument.addField("phNoOnOff",DEFUALT_PRIVACY_SETTINGS);
						solrDocument.addField("searchEngOnOff",DEFUALT_PRIVACY_SETTINGS);
						solrDocument.addField("profilePicOnOff",DEFUALT_PRIVACY_SETTINGS);
						solrDocument.addField("frndChkInOnOff",DEFUALT_PRIVACY_SETTINGS);
						if (detailsOnEmail == null)
							solrDocument.addField("detailsOnEmail", "1");
						else
							solrDocument.addField("detailsOnEmail", "0");
						usersAddStatus = solrCommonClient.addObjectToSolr(UrlConstant.USERS_URL, solrDocument);
						if(usersAddStatus){
							params.put("_NAME_", user.getName());
							templatePath=request.getSession().getServletContext().getRealPath("/WEB-INF/mailTemplate/welcomeMail.template");
							commonEmail.sendEmail(templatePath, params , "Waspit welcome email", user.getEmailId());
						}
					}
					
					if(lat!=null && lat!="" && longitude!=null && longitude!=""){					
						solrDocument = new SolrInputDocument();
						solrDocument.addField("loc_id", UUIDGenrator.getUniqueId());
						solrDocument.addField("lat", lat);						
						solrDocument.addField("longitude", longitude);
						solrCommonClient.addObjectToSolr(UrlConstant.USER_LOC_URL, solrDocument);
						
					}				
					mav.addObject(user);
				} else {
					mav.addObject(ErrorCode.getCustomeError(3003));
					return mav;
				}
				/*if (userInsert) {
					user = userDao.getRegisterUserDetails(user.getUserId());
					mav.addObject(user);
				} else {
					mav.addObject(ErrorCode.getCustomeError(3002));
					return mav;
				}*/
			} else {
				mav.addObject(ErrorCode.getCustomeError(100));
				return mav;
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
			return mav;
		}
		return mav;
	}

	@RequestMapping(value = "/user-email-exists", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView waspitUserEmailExist(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "email_id", required = true) String emailId,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();

		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (emailId == null || emailId.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				mav.addObject("flag", userDao.waspitUserEmailExist(emailId));
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

	@RequestMapping(value = "/verify-activation-code", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView verifyActivationCode(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "activation_code", required = true) String activationCode,
			@RequestParam(value = "type", required = true) String activationType,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		int activationSize = 0;
		String templatePath = null;
		User user=null;
		HashMap<String, String> params = new HashMap<String, String>();
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			}else if (activationCode == null || activationCode.equals("")
					|| activationType == null || activationType.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;
			}else if (apiDao.validateApiKey(apikey)) {
				User registeredUser = userDao.getRegisterUserDetails(userId);
				if (activationType.trim().equals("mobile")) {
					if(!activationCode.trim().equals(registeredUser.getMobileVerifyCode())) {
						mav.addObject(ErrorCode.INVALID_PARAMETER_105);
						return mav;
					}
					activationSize = userDao.validateMobileCode(userId,
							activationCode);
				} else if (activationType.trim().equals("email")) {
					if(!activationCode.trim().equals(registeredUser.getEmailVerifyCode())) {
						mav.addObject(ErrorCode.INVALID_PARAMETER_105);
						return mav;
					}
					activationSize = userDao.validateEmailCode(userId,
							activationCode);
				}else {
					mav.addObject(ErrorCode.getCustomeError(105));
					return mav;
				}
				if (activationSize > 0){
					mav.addObject("userId", userId);
					mav.addObject("activationCode", activationCode);
					mav.addObject("activationType", activationType);
					mav.addObject("message", "Activated sucessfully");
					user=userDao.verifyActivationStatus(userId);
					if(user.getEmailverifyStatus()==1 && user.getMobileverifyStatus()==1){						
						params.put("_NAME_", user.getName());
						templatePath = request.getSession().getServletContext().getRealPath("/WEB-INF/mailTemplate/welcomeVerifyEmail.template");
						commonEmail.sendEmail(templatePath, params,"Waspit welcome email", user.getEmailId());
					}					
					return mav;
				}else{
					mav.addObject(ErrorCode.getCustomeError(105));
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

	
	/*
	 * @author Name:Rajiv Kumar
	 * @Created Date:15/10/2014
	 * @update Date:15/11/2014
	 * @purpose:Login attempt check in authenticate-user service
	 *  
	 */
	@RequestMapping(value = "/authanticate-user", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView doLogin(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_email", required = true) String userEmail,
			@RequestParam(value = "password", required = true) String password,
			HttpServletRequest request) {
			ModelAndView mav = new ModelAndView();
			User user = null;
			try {
				if (apikey == null || apikey.equals("")) {
					mav.addObject(ErrorCode.getCustomeError(104));
					return mav;
				} else if (userEmail == null || userEmail.equals("")
						|| password == null || password.equals("")) {
					mav.addObject(ErrorCode.getCustomeError(101));
					return mav;
				} else if (apiDao.validateApiKey(apikey)) {
					Integer maxLoginHit = apiDao.findMaxLoginHit(apikey);
					User beUser = userDao.getUserByEmailId(userEmail);//getUserByEmailId
					if(beUser==null)
					{
						return mav.addObject(ErrorCode.getCustomeError(3005));
					}else{

					Boolean loginAttemptFlag = loginAttemptCheck(beUser,
							password, maxLoginHit);

					if (loginAttemptFlag == true) {

						mav.addObject(ErrorCode.getCustomeError(113));
						return mav;
					}
					}
					
					user = userDao.validateUser(userEmail, password);
					
					if(user==null)
					{
						return mav.addObject(ErrorCode.getCustomeError(3005));
					}
					if (user != null) {
					
						if (user.getEmailverifyStatus() != MagicNumbers.ACTIVE_YES	|| user.getMobileverifyStatus() != MagicNumbers.ACTIVE_YES) {
							mav.addObject(ErrorCode.getCustomeError(3006));
							return mav;
						} else {
							// session.setAttribute(user.getUserId(),
							// UUIDGenrator.getUniqueId() );
							
							user.setAttemptNumber(new Integer(0)) ;userDao.updateUserAttemptLogin(user);
							solrDocument = new SolrInputDocument();
							solrDocument.addField("user_id", user.getUserId());
							solrDocument.addField("token", "1");
							solrCommonClient.addObjectToSolr(UrlConstant.SESSION_URL, solrDocument);
							mav.addObject("token",solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,PassSession.class,"user_id:" + user.getUserId(),0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken());
							PassUsers userDetails = (PassUsers) solrCommonClient.commonSolrSearch(	UrlConstant.SEARCH_USERS_URL,PassUsers.class,	"user_id:" + user.getUserId(),	0,	1,"","", "false");
							mav.addObject("loginTime", userDetails.getResponse().getDocs().get(0).getLoginTime());
							mav.addObject("user",userDetails);
							return mav;
						}
					} else {
						mav.addObject(ErrorCode.getCustomeError(3005));
						return mav;
					}
				} else {
					mav.addObject(ErrorCode.getCustomeError(100));
					return mav;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return mav;
	}

	@RequestMapping(value = "/forget-password-code", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView sendsecurityCode(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_email", required = true) String userEmail,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String securityCode = null;
		String templatePath = null;
		HashMap<String, String> params = new HashMap<String, String>();
		try {

			if (apikey == null || apikey.equals("") || userEmail == null
					|| userEmail.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				if (userDao.findUserByEmail(userEmail)) {
					securityCode = UUIDGenrator.generateCaptchaCode();
					if (userDao.updateSecurityCode(userEmail, securityCode)) {
						params.put("_SECURITYCODE_", securityCode);
						templatePath = request.getSession().getServletContext().getRealPath("/WEB-INF/mailTemplate/forgetPassword.template");
						commonEmail.sendEmail(templatePath, params,"Forget-password links", userEmail);
						mav.addObject("securityCode", securityCode);
						return mav;
					} else {
						mav.addObject(ErrorCode.getCustomeError(1001));
						return mav;
					}
				} else {
					mav.addObject(ErrorCode.getCustomeError(103));
					return mav;
				}
			} else {
				mav.addObject(ErrorCode.getCustomeError(100));
				return mav;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	@RequestMapping(value = "/verify-password-code", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView verifysecurityCode(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_email", required = true) String userEmail,
			@RequestParam(value = "secure_code", required = true) String secureCode,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String tmpToken = null;
		try {
			if (apikey == null || apikey.equals("") || userEmail == null
					|| userEmail.equals("") || secureCode == null
					|| secureCode.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				if (userDao.findUserByEmail(userEmail)) {
					if (userDao.verifySecureCode(userEmail, secureCode) > 0) {
						tmpToken = UUIDGenrator.getUniqueId();
						if (userDao.updateTmpToken(userEmail, tmpToken) > 0) {
							mav.addObject("tempToken", tmpToken);
							mav.addObject("message",
									Message.getCustomeMessages(3));
							return mav;
						} else {
							mav.addObject(ErrorCode.getCustomeError(405));
							return mav;

						}
					} else {
						mav.addObject("message", Message.getCustomeMessages(4));
						return mav;
					}
				} else {
					mav.addObject(ErrorCode.getCustomeError(103));
					return mav;
				}
			} else {
				mav.addObject(ErrorCode.getCustomeError(100));
				return mav;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/reset-password", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView resetPassword(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_email", required = true) String userEmail,
			@RequestParam(value = "tmp_token", required = true) String tmpToken,
			@RequestParam(value = "new_password", required = true) String newPassword,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		try {
			if (apikey == null || apikey.equals("") || userEmail == null
					|| userEmail.equals("") || tmpToken == null
					|| tmpToken.equals("") || newPassword == null
					|| newPassword.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				if (userDao.verifyEmailIdandTmpToken(userEmail, tmpToken) > 0) {
					if (userDao.resetPassword(userEmail, newPassword)) {
						userDao.resetTempToken(userEmail, "securityCode");
						mav.addObject("message", Message.getCustomeMessages(5));
						return mav;
					} else {
						mav.addObject(ErrorCode.getCustomeError(3011));
						return mav;
					}
				} else {
					mav.addObject(ErrorCode.getCustomeError(3010));
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

	@RequestMapping(value = "/log-out", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView logoutUser(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "user_token", required = true) String userToken,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();

		PassSession passSession = null;
		String query = null;
		try {

			if (apikey == null || apikey.equals("") || userId == null
					|| userId.equals("") || userToken == null
					|| userToken.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				query = "user_id:" + userId + " AND token:" + userToken;
				passSession = (PassSession) solrCommonClient.commonSolrSearch(
						UrlConstant.SEARCH_SESSION_URL, PassSession.class,
						query, 0, 1, "", "", "false");
				if (passSession.getResponse().getDocs().size() > 0) {
					boolean deleteStatus = solrCommonClient.deleteObject(UrlConstant.SESSION_URL,
							query);
					if(deleteStatus) {
						User user = userDao.getRegisterUserAllDetails(userId);
						PassUsers userSolrDetails = (PassUsers) solrCommonClient.commonSolrSearch(	UrlConstant.SEARCH_USERS_URL,PassUsers.class,	"user_id:" + user.getUserId(),	0,	1,"","", "false");
						String mb =userDao.getMobileNo(user);
						//Add Last Login field in user core
						solrDocument = new SolrInputDocument();
						solrDocument.addField("user_id", user.getUserId());
						solrDocument.addField("name", user.getName());
						solrDocument.addField("createDate", user.getCreateDate());
						solrDocument.addField("searchName", user.getName());
						solrDocument.addField("emailId", user.getEmailId());
						solrDocument.addField("mobileNo",mb);
						solrDocument.addField("imageUrl", user.getImageUrl());
						solrDocument.addField("aboutUs", user.getAboutUs());
						solrDocument.addField("city", user.getCity());
						solrDocument.addField("state", user.getState());
						solrDocument.addField("gendre", user.getGendre());
						solrDocument.addField("birthDay",SolrDateUtil.addDateToSolrWithSQLDate(user.getBirthday()));
						solrDocument.addField("iAgree", user.getIagree());
						solrDocument.addField("loginTime",SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
						solrDocument.addField("detailsOnEmail", user.getDetailsOnEmail());
						solrDocument.addField("profileOnOff",userSolrDetails.getResponse().getDocs().get(0).getProfileOnOff());
						solrDocument.addField("emailOnOff",userSolrDetails.getResponse().getDocs().get(0).getEmailOnOff());
						solrDocument.addField("nameOnOff", userSolrDetails.getResponse().getDocs().get(0).getNameOnOff());
						solrDocument.addField("phNoOnOff",userSolrDetails.getResponse().getDocs().get(0).getPhNoOnOff());
						solrDocument.addField("searchEngOnOff",userSolrDetails.getResponse().getDocs().get(0).getSearchEngOnOff());
						solrDocument.addField("profilePicOnOff",userSolrDetails.getResponse().getDocs().get(0).getProfilePicOnOff());
						solrDocument.addField("frndChkInOnOff",userSolrDetails.getResponse().getDocs().get(0).getFrndChkInOnOff());
						solrCommonClient.addObjectToSolr(UrlConstant.USERS_URL, solrDocument);
					}
					mav.addObject("message", Message.getCustomeMessages(6));
					return mav;
				} else {
					mav.addObject(ErrorCode.getCustomeError(3008));
					return mav;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
			return mav;
		}
		return mav;
	}

	@RequestMapping(value = "/user-details-by-id", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getUserDetailsById(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "user_token", required = true) String userToken,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassSession passSession = null;
		String query = null;
		try {
			if (apikey == null || apikey.equals("") || userId == null
					|| userId.equals("") || userToken == null
					|| userToken.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				query = "user_id:" + userId + " AND token:" + userToken;
				passSession = (PassSession) solrCommonClient.commonSolrSearch(
						UrlConstant.SEARCH_SESSION_URL, PassSession.class,
						query, 0, 1, "", "", "false");
				if (passSession.getResponse().getDocs().size() > 0) {
					query = "user_id:" + userId;
					PassUsers userDetails = (PassUsers) solrCommonClient.commonSolrSearch(	UrlConstant.SEARCH_USERS_URL,PassUsers.class,	query,	0,	1,"user_id,name,"+ "emailId,"
							+ "mobileNo,imageUrl,aboutUs,city,state,gendre,birthDay,iAgree,detailsOnEmail,emailVerifyCode,emailVerifyStatus,mobileVerifyCode,mobileverifyStatus,loginTime,createDate","", "false");
					mav.addObject(userDetails);
					return mav;
				} else {
					mav.addObject(ErrorCode.getCustomeError(3008));
					return mav;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
			return mav;
		}
		return mav;
	}
	@RequestMapping(value = "/user-other-details-by-id", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getOtherUserDetailsById(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "other_user_id", required = true) String otherUserId,
			@RequestParam(value = "user_token", required = true) String userToken,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassSession passSession = null;
		String query = null;
		try {
			if (apikey == null || apikey.equals("") || userId == null
					|| userId.equals("") || userToken == null
					|| userToken.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				query = "user_id:" + userId + " AND token:" + userToken;
				passSession = (PassSession) solrCommonClient.commonSolrSearch(
						UrlConstant.SEARCH_SESSION_URL, PassSession.class,
						query, 0, 1, "", "", "false");
				if (passSession.getResponse().getDocs().size() > 0) {
					query = "user_id:" + otherUserId;
					PassUsers userDetails = (PassUsers) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL,	PassUsers.class, query,	0, 1, "user_id,name,emailId,mobileNo,imageUrl,aboutUs,city,state,gendre,birthDay,iAgree,detailsOnEmail,emailVerifyCode,emailVerifyStatus,mobileVerifyCode,mobileverifyStatus,createDate,loginTime","", "false");
					mav.addObject(userDetails);
					return mav;
				} else {
					mav.addObject(ErrorCode.getCustomeError(3008));
					return mav;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
			return mav;
		}
		return mav;
	}
	

	@RequestMapping(value = "/delete-all-users", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView deleteAllUsers(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		boolean userDelStatus = false;

		userDelStatus = solrCommonClient.deleteAllObject(UrlConstant.USERS_URL);
		if (userDelStatus)
			mav.addObject("Users Delete Succesfully");
		else
			mav.addObject("Issue while deleting Users");

		return mav;
	}

	@RequestMapping(value = "/delete-all", method = {RequestMethod.POST,RequestMethod.GET })
	public ModelAndView deleteAll(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		solrCommonClient.deleteAllObject(UrlConstant.VENUE_URL);
		solrCommonClient.deleteAllObject(UrlConstant.REVIEW_URL);
		solrCommonClient.deleteAllObject(UrlConstant.PHOTO_URL);
		solrCommonClient.deleteAllObject(UrlConstant.SESSION_URL);

		solrCommonClient.deleteAllObject(UrlConstant.USERS_URL);
		solrCommonClient.deleteAllObject(UrlConstant.REQUEST_URL);
		solrCommonClient.deleteAllObject(UrlConstant.COMMENT_URL);
		solrCommonClient.deleteAllObject(UrlConstant.REPLY_URL);
		solrCommonClient.deleteAllObject(UrlConstant.HELPFUL_URL);
		solrCommonClient.deleteAllObject(UrlConstant.MANAGE_UPDATES_URL);
		solrCommonClient.deleteAllObject(UrlConstant.FEED_COMMENT_URL);
		solrCommonClient.deleteAllObject(UrlConstant.VENUE_HISTORY_URL);
		solrCommonClient.deleteAllObject(UrlConstant.CONTENT_URL);
		solrCommonClient.deleteAllObject(UrlConstant.IMAGE_URL);
		solrCommonClient.deleteAllObject(UrlConstant.MANAGE_UPDATES_STATUS_URL);
		solrCommonClient.deleteAllObject(UrlConstant.BOOKMARK_URL);
		solrCommonClient.deleteAllObject(UrlConstant.PHOTO_URL);
		solrCommonClient.deleteAllObject(UrlConstant.INTERNAL_DEALS_URL);
		solrCommonClient.deleteAllObject(UrlConstant.USER_LOC_URL);
		solrCommonClient.deleteAllObject(UrlConstant.VENUE_FOR_DEALS);

		mav.addObject("All data deleted");

		return mav;
	}

	@RequestMapping(value = "/delete-user-by-id", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView deleteUserById(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "user_token", required = true) String userToken,
			@RequestParam(value = "user_email", required = false) String userEmail,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassSession passSession = null;
		String query = null;
		boolean isDeleted = false;
		try {
			if (apikey == null || apikey.equals("") || userId == null
					|| userId.equals("") || userToken == null
					|| userToken.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				//commented currently becuase team need to delete record either from solr or mysql
				if (userDao.findUserByEmail(userEmail)) {
				   if (userDao.deleteUser(userId)) {
						userDao.deleteUser(userId);
						query = "user_id:" + userId + " AND token:" + userToken;
						passSession = (PassSession) solrCommonClient
								.commonSolrSearch(
										UrlConstant.SEARCH_SESSION_URL,
										PassSession.class, query, 0, 1, "", "",
										"false");
						if (passSession.getResponse().getDocs().size() > 0) {
							query = "user_id:" + userId;
							isDeleted = solrCommonClient.deleteObject(
									UrlConstant.USERS_URL, query);
							if (isDeleted)
								mav.addObject("message",
										Message.getCustomeMessages(7));
							else
								mav.addObject("message",
										"Error Occured to delete user.");
							return mav;
						} else {
							mav.addObject(ErrorCode.getCustomeError(3008));
							return mav;
						}
					} else {
						mav.addObject(ErrorCode.getCustomeError(103));
						return mav;
					}
				} else {
					mav.addObject(ErrorCode.getCustomeError(103));
					return mav;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
			return mav;
		}
		return mav;
	}
	//Added to update user Profile(For BIO update)
	@SuppressWarnings("unused")
	@RequestMapping(value = "/user-profile-update", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView userProfileUpdate(
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String userToken,
			@RequestParam(value = "user_name", required = true) String userName,
			@RequestParam(value = "email_id", required = true) String emailId,
			@RequestParam(value = "mobile_number", required = true) String mobileNumber,
			@RequestParam(value = "image_id", required = false) String imageUrl,
			@RequestParam(value = "about_us", required = false) String aboutUs,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "gender", required = true) String gender,
			@RequestParam(value = "birthday", required = false) String birthday,
			@RequestParam(value = "iAgree", required = true) String iAgree,
			@RequestParam(value = "detailsOnEmail", required = true) String detailsOnEmail,
			@RequestParam(value = "updatedVals", required = true) String updatedVals,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		Date date= null;
		String updateRecordId=null;
		String updateId=null;
		UpdateRecord updateRecord=null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		User user = new User();
		int userUpdated = 0;
		boolean usersAddStatus = false;
		PassSession passSession = null;
		String query = null;
		try {
			Integer gendreInt = Integer.valueOf(gender);
			Integer iAgreeInt = Integer.valueOf(iAgree);
			Integer detailsOnEmailInt = Integer.valueOf(detailsOnEmail);
			if (apikey == null || apikey.equals("") || userId == null
					|| userId.equals("") || userToken == null
					|| userToken.equals("") || updatedVals == null
					|| updatedVals.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				query = "user_id:" + userId + " AND token:" + userToken;
				passSession = (PassSession) solrCommonClient.commonSolrSearch(
						UrlConstant.SEARCH_SESSION_URL, PassSession.class,
						query, 0, 1, "", "", "false");
				if (passSession.getResponse().getDocs().size() > 0) {
					user=userDao.getRegisterUserDetails(userId);
					user.setName(userName);
					//user.setEmailId(emailId);
					user.setMobileNumber(mobileNumber);
					user.setImageUrl(imageUrl);
					user.setAboutUs(aboutUs);
					City cityObject = cityDao.getCityDetailsById(Integer.parseInt(city));
					String cityVal = city + ":" + cityObject.getCityName();
					if (cityObject == null) {
						mav.addObject(ErrorCode.getCustomeError(108));
						return mav;
					} else {
						user.setCity(cityVal);
					}
					user.setGendre(gendreInt);
					
					user.setBirthday(formattedDate);
					user.setBirthDayStr(birthday);
					user.setCreateDate(user.getCreateDate());
					user.setIagree(iAgreeInt);
					if (detailsOnEmail == null)
						user.setDetailsOnEmail(MagicNumbers.EMAIL_DETAILS_YES);
					else
						user.setDetailsOnEmail(detailsOnEmailInt);
					userUpdated = userDao.updateUser(user);
					if (userUpdated == 1) {
						solrDocument = new SolrInputDocument();
						solrDocument.addField("user_id", user.getUserId());
						solrDocument.addField("name", user.getName());
						solrDocument.addField("searchName", user.getName());
						solrDocument.addField("emailId", user.getEmailId());
						solrDocument.addField("mobileNo",user.getMobileNumber());
						solrDocument.addField("imageUrl", user.getImageUrl());
						solrDocument.addField("aboutUs", user.getAboutUs());
						solrDocument.addField("city", user.getCity());
						solrDocument.addField("state", user.getState());
						solrDocument.addField("gendre", gender);
						solrDocument.addField("birthDay",SolrDateUtil.addDateToSolrWithSQLDate(user.getBirthday()));
						solrDocument.addField("iAgree", iAgree);
						solrDocument.addField("createDate", SolrDateUtil.addDateToSolrWithSQLDate(user.getCreateDate().toString()));
						solrDocument.addField("loginTime", user.getLoginTime());
						solrDocument.addField("profileOnOff",DEFUALT_PRIVACY_SETTINGS);
						solrDocument.addField("emailOnOff",DEFUALT_PRIVACY_SETTINGS);
						solrDocument.addField("nameOnOff", DEFUALT_PRIVACY_SETTINGS);
						solrDocument.addField("phNoOnOff",DEFUALT_PRIVACY_SETTINGS);
						solrDocument.addField("searchEngOnOff",DEFUALT_PRIVACY_SETTINGS);
						solrDocument.addField("profilePicOnOff",DEFUALT_PRIVACY_SETTINGS);
						solrDocument.addField("frndChkInOnOff",DEFUALT_PRIVACY_SETTINGS);
						if (detailsOnEmail == null)
							solrDocument.addField("detailsOnEmail", "1");
						else
							solrDocument.addField("detailsOnEmail", "0");
						usersAddStatus = solrCommonClient.addObjectToSolr(
								UrlConstant.USERS_URL, solrDocument);
						if (usersAddStatus) {
							updateId =updateFeed(updatedVals,user);
						//	updateNotification(updatedVals, user);
						}
						user=userDao.getRegisterUserDetails(userId);
					}
					// Register User Of MultipleRecords Users			
					if (!mongoTemplate.collectionExists(UpdateRecord.class)) {
		    			mongoTemplate.createCollection(UpdateRecord.class);
		    			
		    		}
					PassUsers passUsers = solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL, PassUsers.class, "user_id:"+userId, 0, 10,  "", "", "false");
					HashMap<String,Object> map = new HashMap<String,Object>() ;
					  updateRecord=new UpdateRecord();            		
					  updateRecordId=UUIDGenrator.getUniqueId();
					  map.put("User", passUsers);
					  updateRecord.setAddedDate(new Date());
					  updateRecord.setFeedId(updateId);
					  updateRecord.setMap(map);
		
					  mongoTemplate.insert(updateRecord);  
				}else{
					mav.addObject(ErrorCode.getCustomeError(99));
			 }
			}
		}catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
		}
		return mav.addObject("UserUpdated",user);
	}

	public String updateFeed(String updatedVals,User user) {
		String[] elements = updatedVals.split("::");
		String updateId=null;
		for (String elememnt : elements) {
			 updateId = UUIDGenrator.getUniqueId();
			solrDocument = new SolrInputDocument();
			solrDocument.addField("id", updateId);
			solrDocument.addField("type",
					UpdateTypeEnum.UpdateType.Feeds.toString());
			solrDocument.addField("subType",
					UpdateTypeEnum.UpdateSubType.BioUpdate.toString());
			solrDocument.addField("property",elememnt);
			solrDocument.addField("update_id", updateId);
			solrDocument.addField("fromUser", user.getUserId());
			solrDocument.addField("toUser", user.getUserId());
			solrDocument.addField("search_name", user.getName().trim());
			solrDocument.addField("privacy", MagicNumbers.ACTIVE);
			solrDocument.addField("addedDate",
					SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
			solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL,
					solrDocument);
			
		}
		return updateId;
	}

	public void updateNotification(String updatedVals, User user) {
		String[] elements = updatedVals.split(",");
		for (String elememnt : elements) {
			String updateId = UUIDGenrator.getUniqueId();
			solrDocument = new SolrInputDocument();
			solrDocument.addField("id", updateId);
			solrDocument.addField("type",
					UpdateTypeEnum.UpdateType.Notifications.toString());
			solrDocument.addField("subType",
					UpdateTypeEnum.UpdateSubType.BioUpdate.toString() + " For "
							+ elememnt);
			solrDocument	.addField("property",
							UpdateTypeEnum.feedsUpdateProperty.ProfileUpdate
									.toString());
			solrDocument.addField("update_id", updateId);
			solrDocument.addField("fromUser", user.getUserId());
			solrDocument.addField("toUser", user.getUserId());
			solrDocument.addField("privacy", MagicNumbers.ACTIVE);
			solrDocument.addField("addedDate",
					SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
			solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL,
					solrDocument);
		}
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/register-fb-user", method = { RequestMethod.POST,
			RequestMethod.GET })
			public ModelAndView registerFacebookUser(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "user_name", required = true) String userName,
			@RequestParam(value = "email_id", required = true) String emailId,
			@RequestParam(value = "mobile_number", required = true) String mobileNumber,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "image_id", required = true) String imageUrl,
			@RequestParam(value = "about_us", required = false) String aboutUs,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "gender", required = true) String gender,
			@RequestParam(value = "birthday", required = false) String birthday,
			@RequestParam(value = "iAgree", required = true) String iAgree,
			@RequestParam(value = "detailsOnEmail", required = true) String detailsOnEmail,
			@RequestParam(value = "lat", required = false) String lat,
			@RequestParam(value = "long", required = false) String longitude,
			
			HttpServletRequest request) {
			ModelAndView mav = new ModelAndView();
			User user = new User();
			int registerUser = 0;

			Integer gendreInt = Integer.valueOf(gender);
			Integer iAgreeInt = Integer.valueOf(iAgree);
			Integer detailsOnEmailInt = Integer.valueOf(detailsOnEmail);

			boolean usersAddStatus = false;
			HashMap<String, String> params = new HashMap<String, String>();
			String templatePath=null;

			try {
			if (apikey == null || apikey.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;
			} else if (userName == null || userName.equals("")
			|| emailId == null || emailId.equals("")
			|| password == null || password.equals("")
			|| userId == null || userId.equals("")
			|| mobileNumber == null) {
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;
			} else if (iAgreeInt != MagicNumbers.IAGREE) {
			mav.addObject(ErrorCode.getCustomeError(3004));
			return mav;
			} else if (apiDao.validateApiKey(apikey)) {
			user.setUserId(userId);
			user.setName(userName);
			user.setEmailId(emailId);
			user.setMobileNumber(mobileNumber);
			user.setPassword(password);
			user.setImageUrl(imageUrl);
			user.setAboutUs(aboutUs);
			City cityObject = cityDao.getCityDetailsById(Integer.parseInt(city));
			String cityVal = city + ":" + cityObject.getCityName();
			if (cityObject == null) {
				mav.addObject(ErrorCode.getCustomeError(108));
				return mav;
			} else {
				user.setCity(cityVal);
			}
			State stateObject = stateDao.getStateDetailsById(Integer.parseInt(state));
			String stateVal = state + ":" + stateObject.getStateName();
			if (stateObject == null) {
				mav.addObject(ErrorCode.getCustomeError(107));
				return mav;
			} else {
				user.setState(stateVal);
			}
			user.setGendre(gendreInt);
			Date date= null;
			String formattedDate = null;
			if(birthday != null && !birthday.equals("")) {
				try {
					date = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
			}
			user.setBirthday(formattedDate);
			user.setCreateDate(new Date());
			user.setIagree(iAgreeInt);
			if (detailsOnEmail == null)
			user.setDetailsOnEmail(MagicNumbers.EMAIL_DETAILS_YES);
			else {
			user.setDetailsOnEmail(detailsOnEmailInt);
			user.setEmailVerifyCode(UUIDGenrator.generateCaptchaCode());
			user.setMobileVerifyCode(UUIDGenrator.generateCaptchaCode());
			}
			if (!userDao.findUserByEmail(user.getEmailId())) {
			registerUser = userDao.updateExistingUser(user);
			if (registerUser == 1) {
			solrDocument = new SolrInputDocument();
			solrDocument.addField("user_id", user.getUserId());
			solrDocument.addField("name", user.getName());
			solrDocument.addField("searchName", user.getName());
			solrDocument.addField("emailId", user.getEmailId());
			solrDocument.addField("mobileNo",
			user.getMobileNumber());
			solrDocument.addField("imageUrl", user.getImageUrl());
			solrDocument.addField("aboutUs", user.getAboutUs());
			solrDocument.addField("city", user.getCity());
			solrDocument.addField("state", user.getState());
			solrDocument.addField("gendre", gender);
			solrDocument.addField("birthDay", new Date());
			solrDocument.addField("iAgree", iAgree);
			solrDocument.addField("createDate", new Date());
			solrDocument.addField("profileOnOff",DEFUALT_PRIVACY_SETTINGS);
			solrDocument.addField("emailOnOff",DEFUALT_PRIVACY_SETTINGS);
			solrDocument.addField("nameOnOff", DEFUALT_PRIVACY_SETTINGS);
			solrDocument.addField("phNoOnOff",DEFUALT_PRIVACY_SETTINGS);
			solrDocument.addField("searchEngOnOff",DEFUALT_PRIVACY_SETTINGS);
			solrDocument.addField("profilePicOnOff",DEFUALT_PRIVACY_SETTINGS);
			solrDocument.addField("frndChkInOnOff",DEFUALT_PRIVACY_SETTINGS);
			if (detailsOnEmail == null)
			solrDocument.addField("detailsOnEmail", "1");
			else
			solrDocument.addField("detailsOnEmail", "0");
			usersAddStatus = solrCommonClient.addObjectToSolr(UrlConstant.USERS_URL, solrDocument);
			if(usersAddStatus){
			params.put("_NAME_", user.getName());
			templatePath=request.getSession().getServletContext().getRealPath("/WEB-INF/mailTemplate/welcomeMail.template");
			commonEmail.sendEmail(templatePath, params , "Waspit welcome email", user.getEmailId());
			}
			if(lat!=null && lat!="" && longitude!=null && longitude!=""){					
				solrDocument = new SolrInputDocument();
				solrDocument.addField("loc_id", UUIDGenrator.getUniqueId());
				solrDocument.addField("lat", lat);						
				solrDocument.addField("longitude", longitude);
				solrCommonClient.addObjectToSolr(UrlConstant.USER_LOC_URL, solrDocument);
				
			}
			
			}

			} else {
			registerUser = userDao.updateExistingUser(user);
			mav.addObject(user);
			return mav;
			}
			if (registerUser == 1) {
			user = userDao.getRegisterUserDetails(user.getUserId());
			mav.addObject(user);
			} else {
			mav.addObject(ErrorCode.getCustomeError(3002));
			return mav;
			}
			} else {
			mav.addObject(ErrorCode.getCustomeError(100));
			return mav;
			}
			} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
			return mav;
			}
			return mav;
			}
	
	
	@RequestMapping(value = "/change_password", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView changePassword(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "old_password", required = true) String oldPassword,
			@RequestParam(value = "new_password", required = true) String newPassword,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		User user = null;
		boolean oldPassWrdCheck = false;
		HashMap<String, String> params = new HashMap<String, String>();
		String templatePath = null;
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			}
			if (userId == null || userId.equals("") || newPassword == null
					|| newPassword.equals("") || oldPassword == null
					|| oldPassword.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(105));
				return mav;
			} else if (solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1,
							"user_id", "", "false").getResponse().getDocs().size() == 0) {
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
				logger.info("validating user...");
				user = userDao.validateUser(userId);
				String mbNo = userDao.getMobileNo(user);
				user.setMobileNumber(mbNo);
				if(null==user){
					logger.info("Invalid user or not found in database.");
					mav.addObject(Message.getCustomeMessages(3007));
				}
				String userOldPassword = user.getPassword();
				logger.info(":::Mb No:"+user.getMobileNumber());
				if (null != userOldPassword) {
					oldPassWrdCheck = userOldPassword.equals(oldPassword);
					if (oldPassWrdCheck) {
						user.setPassword(newPassword);
						logger.info("updating user...");
						int updated = userDao.updateUser(user);
						if (updated == 1) {
							logger.info("user updated...");
							params.put("_NAME_", user.getName());
							templatePath = request
									.getSession()
									.getServletContext()
									.getRealPath(
											"/WEB-INF/mailTemplate/changePassword.template");
							commonEmail.sendEmail(templatePath, params,
									"Waspit change password email",
									user.getEmailId());
							mav.addObject(Message.getCustomeMessages(9));
						}
					} else {
						logger.info("Error occurred in password change",ErrorCode.getCustomeError(6001));
						mav.addObject(ErrorCode.getCustomeError(6001));
					}
				}
			}else{
				mav.addObject(ErrorCode.getCustomeError(100));
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
		}
		return mav;
	}
	
	
	@RequestMapping(value = "/add-image-history", method = {RequestMethod.POST, RequestMethod.GET })
	public ModelAndView addImageHistory(
			@RequestParam(value = "user_id", required = false) String userId,
			@RequestParam(value = "api_key", required = false) String apikey,
			@RequestParam(value = "token", required = false) String userToken,
			@RequestParam(value = "image_id", required = false) String imageId,
			@RequestParam(value = "type", required = false) String type,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		try {
			if (apikey == null || apikey.equals("") || userId == null	|| userId.equals("") || userToken == null || userToken.equals("") || imageId == null || imageId.equals("") || type == null || type.equals("") ) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().get(0).getUserId()==null){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(userToken)){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if (apiDao.validateApiKey(apikey)) {
				
						solrDocument = new SolrInputDocument();
						solrDocument.addField("id", imageId);
						solrDocument.addField("image_id", imageId);
						solrDocument.addField("user_id", userId);
						solrDocument.addField("added_date", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
						solrDocument.addField("type",	type);
						 solrCommonClient.addObjectToSolr(UrlConstant.IMAGE_URL, solrDocument);
						mav.addObject("Message" , "Image Saved to history");
						mav.addObject("image_id" , imageId);	
						return mav;
				}else{
					mav.addObject(ErrorCode.getCustomeError(99));
				}
		}catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
		}
		return mav;
	}
	
	
	@RequestMapping( value="/image-history-by-query" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView imageHistoryByQuery(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "query", required = true) String query,			
			@RequestParam(value = "user_id", required = false) String userId,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = true) String filterBy,
			@RequestParam(value = "sort_order", required = true) String sortOrder,			
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		int startElementSolr = 0;
		int rowsSolr = 0;
		PassImage passImages=null;
		if(startElement!= null && !startElement.equals("")) {
			startElementSolr = Integer.parseInt(startElement);
		}
		if(rows!= null && !rows.equals("")) {
			rowsSolr = Integer.parseInt(rows);
		}
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(apiDao.validateApiKey(apikey)){
				passImages=(PassImage) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_IMAGE_URL, PassImage.class, query, startElementSolr, rowsSolr, filterBy, sortOrder, "false");				
				mav.addObject(passImages);				
				return mav;
			}else{
				mav.addObject(ErrorCode.getCustomeError(100));
				return mav;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return mav;
		}	
/*
	public  User getUserDetails(String userId){		
			User userdetails=new User();
			String query = "user_id:" + userId;	
			String url = UrlConstant.SOLR_HTTP_URL+"/solr/users/select/?q="+query+"&version=2.2&start=0&rows=10&indent=on&wt=json";
			HttpClient client = new DefaultHttpClient();
			HttpGet request1 = new HttpGet(url);
	
			// add request header
			HttpResponse response = null; 
			String json=null;
			try {
			response = client.execute(request1);
			json = EntityUtils.toString(response.getEntity());
			} catch (ClientProtocolException e1) {
			e1.printStackTrace();
			} catch (IOException e1) {
			e1.printStackTrace();
			}
	
			JSONObject myObject=null;
			try {
				myObject= new JSONObject(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				if(myObject.getJSONObject("response").getJSONArray("docs").length()==0){
				return userdetails;
				}
			JSONObject js=(JSONObject) myObject.getJSONObject("response").getJSONArray("docs").get(0);		
			if(js.has("user_id")) {
				userdetails.setUserId(js.get("user_id").toString());
			} else {
				userdetails.setUserId("null");
			}
			if(js.has("name")) {
				userdetails.setName(js.get("name").toString());
			} else {
				userdetails.setName("null");
			}
			if(js.has("imageUrl")) {
				userdetails.setImageUrl(js.get("imageUrl").toString());
			} else {
				userdetails.setImageUrl("null");
			}
			if(js.has("gendre")) {
				Integer i=Integer.parseInt(js.get("gendre").toString());
				userdetails.setGendre(i);
			} else {
				userdetails.setGendre(0);
			}
			if(js.has("aboutUs")) {
				userdetails.setAboutUs(js.get("aboutUs").toString());
			} else {
				userdetails.setAboutUs("null");
			}
			if(js.has("mobileNo")) {
				userdetails.setMobileNumber(js.get("mobileNo").toString());
			} else {
				userdetails.setMobileNumber("null");
			}
			if(js.has("birthDay")) {
				userdetails.setBirthday(js.get("birthDay").toString());
			} else {
				userdetails.setBirthday("null");
			}
			if(js.has("city")) {
				userdetails.setCity(js.get("city").toString());
			} else {
				userdetails.setCity("null");
			}
			if(js.has("emailId")) {
				userdetails.setEmailId(js.get("emailId").toString());
			} else {
				userdetails.setEmailId("null");
			}
			if(js.has("loginTime")) {
				userdetails.setLoginTime(js.get("loginTime").toString());
			} else {
				userdetails.setLoginTime(SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
			}
			if(js.has("createDate")) {
				userdetails.setCreateDateStr(js.get("createDate").toString());
			} else {
				userdetails.setCreateDate(new Date());
			}
			
			
			if(js.has("state")) {
				userdetails.setState(js.get("state").toString());
			} else {
				userdetails.setState(null);
			}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return userdetails;
		}*/
	@RequestMapping(value = "/get-social-flags",method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView saveAccessToken(@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		ConnectSocialUser connectSocialUser = null;
		List<ConnectSocialUser> connectSocialUsers=new ArrayList<ConnectSocialUser>();
		boolean twitterFlag=false;
		boolean facebookFlag=false;
		boolean foursquareFlag=false;
		boolean googleFlag=false;
		String twitterUser = null;
		String facebookUser=null;
		String foursquareUser=null;
		String googleUser=null;
		List<Map<String,Object>> socialMap=new ArrayList<Map<String, Object>>();
		Map<String,Object> twitterMap=new HashMap<String, Object>();
		Map<String,Object> facebookMap=new HashMap<String, Object>();
		Map<String,Object> fourSquareMap=new HashMap<String, Object>();
		Map<String,Object> googleMap=new HashMap<String, Object>();
		
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			}
			if (userId == null || userId.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(105));
				return mav;
			} else if (solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1,
							"user_id", "", "false").getResponse().getDocs().size() == 0) {
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
				Query queryTwitter=new Query();
				queryTwitter.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.Twitter.toString())));
				connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(queryTwitter, ConnectSocialUser.class);
				if (null != connectSocialUsers && connectSocialUsers.size() > 0) {
					connectSocialUser = connectSocialUsers.get(0);
					twitterFlag=connectSocialUser.getConnect();
					twitterUser = connectSocialUser.getUserName();
				}
				twitterMap.put("connectTwitter", twitterFlag);
				twitterMap.put("twitterUser", twitterUser);
				Query queryFacebook=new Query();
				queryFacebook.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.FaceBook.toString())));
				connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(queryFacebook, ConnectSocialUser.class);
				if (null != connectSocialUsers && connectSocialUsers.size() > 0) {
					connectSocialUser = connectSocialUsers.get(0);
					facebookFlag=connectSocialUser.getConnect();
					facebookUser = connectSocialUser.getUserName();
				}
				facebookMap.put("connectFacebook", facebookFlag);
				facebookMap.put("facebookUser", facebookUser);
				Query queryFoursquare=new Query();
				queryFoursquare.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.FourSquar.toString())));
				connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(queryFoursquare, ConnectSocialUser.class);
				if (null != connectSocialUsers && connectSocialUsers.size() > 0) {
					connectSocialUser = connectSocialUsers.get(0);
					foursquareFlag=connectSocialUser.getConnect();
					foursquareUser = connectSocialUser.getUserName();
				}
				fourSquareMap.put("connectFourSquare", foursquareFlag);
				fourSquareMap.put("foursquareUser", foursquareUser);
				Query queryGoogle=new Query();
				queryGoogle.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("socialType").is(SocialTypesEnum.SocialTypes.Google.toString())));
				connectSocialUsers = (List<ConnectSocialUser>) mongoCommonClient.findByQuery(queryGoogle, ConnectSocialUser.class);
				if (null != connectSocialUsers && connectSocialUsers.size() > 0) {
					connectSocialUser = connectSocialUsers.get(0);
					googleFlag=connectSocialUser.getConnect();
					googleUser = connectSocialUser.getUserName();
				}
				googleMap.put("connectGoogle", googleFlag);
				googleMap.put("googleUser", googleUser);
				// Added all social Maps
				socialMap.add(twitterMap);
				socialMap.add(facebookMap);
				socialMap.add(fourSquareMap);
				socialMap.add(googleMap);
			}
			}catch (Exception e) {
				e.printStackTrace();
				mav.addObject(ErrorCode.getCustomeError(1002));
			}
		mav.addObject("SocialDetails",socialMap);
		return mav;
	}	
	
	/*public  String getNotificationStatus(String userId,String feedId){		
		String status = null;
		String query ="feedId:" +feedId;	
		String url = UrlConstant.SOLR_HTTP_URL+"/solr/manage-fed-not-status/select/?q="+query+"&version=2.2&start=0&rows=10&indent=on&wt=json";
		HttpClient client = new DefaultHttpClient();
		HttpGet request1 = new HttpGet(url);

		// add request header
		HttpResponse response = null; 
		String json=null;
		try {
		response = client.execute(request1);
		json = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e1) {
		e1.printStackTrace();
		} catch (IOException e1) {
		e1.printStackTrace();
		}

		JSONObject myObject=null;
		try {
			myObject= new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			if(myObject.getJSONObject("response").getJSONArray("docs").length()==0){
				return "0";
				}
		JSONObject js=(JSONObject) myObject.getJSONObject("response").getJSONArray("docs").get(0);
		if(js.has("flag")) {
			status=js.get("flag").toString();
			return status;
		} else {
			status="0";
			return status;
		}
				} catch (JSONException e) {
			e.printStackTrace();
		}
		return status;
	}
	*/
	
	@RequestMapping(value = "/user-details-by-query", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getUserDetailsByQuery(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "query", required = true) String query,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassSession passSession = null;
		try {
			String sessionQuery = null;
			if (apikey == null || apikey.equals("") || userId == null
					|| userId.equals("") || token == null
					|| token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				sessionQuery = "user_id:" + userId + " AND token:" + token;
				passSession = (PassSession) solrCommonClient.commonSolrSearch(
						UrlConstant.SEARCH_SESSION_URL, PassSession.class,
						sessionQuery, 0, 1, "", "", "false");
				query = query+"*";
				if (passSession.getResponse().getDocs().size() > 0) {
					PassUsers userDetails = (PassUsers) solrCommonClient.commonSolrSearch(	UrlConstant.SEARCH_USERS_URL,PassUsers.class,	query,	0,	10,"name,user_id,imageUrl","", "false");
					mav.addObject(userDetails);
					return mav;
				} else {
					mav.addObject(ErrorCode.getCustomeError(3008));
					return mav;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
			return mav;
		}
		return mav;
	}
	
	@RequestMapping(value = "/tag-friends", method = {RequestMethod.POST, RequestMethod.GET })
	public ModelAndView tagFriends(@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "userIdList", required = true) String userIdList,
			@RequestParam(value = "feed_id", required = true) String feedId,
			@RequestParam(value = "type", required = true) String type,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		try {
			if (apikey == null || apikey.equals("") || userId == null || userId.equals("") || token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
			
				if (!mongoTemplate.collectionExists(TagFriends.class)) {
        			mongoTemplate.createCollection(TagFriends.class);
        		}
        		TagFriends tagFriends=new TagFriends();
        		String tagId=UUIDGenrator.getUniqueId();
        		tagFriends.setTagId(tagId);
        		tagFriends.setUserIds(userIdList);
        		tagFriends.setFeedId(feedId);
        		tagFriends.setType(type);
            	mongoTemplate.insert(tagFriends);
            	//Create Notification
            	String[] userIdArray = userIdList.split(",");
            	String id = null;
            	for(String userIdVal : userIdArray) {
            		solrDocument=new SolrInputDocument();
            		id=UUIDGenrator.getUniqueId();
    				solrDocument.addField("id", id);
    				solrDocument.addField("type", UpdateTypeEnum.UpdateType.Notifications.toString());
    				solrDocument.addField("subType", UpdateTypeEnum.UpdateSubType.Tagged.toString());	
    				solrDocument.addField("property", UpdateTypeEnum.UpdateSubType.Tagged.toString());
    				solrDocument.addField("privacy", MagicNumbers.ACTIVE);
    				if(userId.equals(userIdVal)){
    					solrDocument.addField("subTypes",UpdateTypeEnum.UpdateSubTypes.USEROWN);
    				}
    				solrDocument.addField("toUser", userIdVal);	
    				solrDocument.addField("fromUser", userId);
    				solrDocument.addField("update_id", feedId);				
    				solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
    				solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
            	}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
			return mav;
		}
		mav.addObject("Tagged Users ", userIdList);
		return mav;
	}
	
	@RequestMapping( value="/getupdateRecord-users" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getupdateRecordUser(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "feed_Id", required = true) String feedId,		
			@RequestParam(value = "user_Id", required = true) String userId,
		HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();		 
		if(apikey ==null||apikey.equals("")){	
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;			
		}else if(token==null||token.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;					
	
		}else if(userId ==null||userId.equals("")){
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
		}else if(feedId ==null||feedId.equals("")){
			mav.addObject("FeedId"," Feed_ Id Not Found");
			return mav;
		
       } else if (apiDao.validateApiKey(apikey)) {
			List<UpdateRecord> updateRecord=null;
			Query query=new Query();
			query.addCriteria(Criteria.where("feedId").is(feedId));
			 updateRecord = (List<UpdateRecord>) mongoCommonClient.findByQuery(query, UpdateRecord.class);
			if (null!=updateRecord && updateRecord.size()>0) {
				UpdateRecord update=updateRecord.get(0);
					return mav.addObject("Records", update);
					
		   }else{
		    mav.addObject("Message", "No Records Found");

  }
		}
			return mav;
	}
	/*
	 * @author Name:Rajiv Kumar
	 * @Created Date:15/10/2014
	 * @update Date:15/10/2014
	 * @purpose:Login attempt check
	 *  
	 */
	
	public Boolean loginAttemptCheck(User beUser , String password ,Integer maxLoginHit ) {
		
		Boolean flag = false;
		
		if (beUser.getAttemptNumber() == maxLoginHit) {
			flag = true;
			return flag;
		} else {
			if(!beUser.getPassword().equals(password))
			{
				Integer attemptLogin = userDao.getAttemptLogin(beUser);
				beUser.setAttemptNumber(attemptLogin+1);
				userDao.updateUserAttemptLogin(beUser);
			logger.info(beUser.getAttemptNumber()+":::loginAttemptCheck(User beUser , String password):::"+InterConnectionClient.class);
			}

			return flag;
		}
	}
	/*
	 * @author Name:Gaurav chugh
	 * @Created Date:17/10/2014
	 * @update Date:17/10/2014
	 * @purpose:getVenueDetails
	 *  
	 */
	/*public  Venue getVenueDetails(String venueId){		
		Venue venueDetails=new Venue();
		String query = "venue_id:" + venueId;	
		String url = UrlConstant.SOLR_HTTP_URL+"/solr/venue/select/?q="+query+"&version=2.2&start=0&rows=10&indent=on&wt=json";
		HttpClient client = new DefaultHttpClient();
		HttpGet request1 = new HttpGet(url);
		// add request header
		HttpResponse response = null; 
		String json=null;
		try {
		response = client.execute(request1);
		json = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e1) {
		e1.printStackTrace();
		} catch (IOException e1) {
		e1.printStackTrace();
		}

		JSONObject myObject=null;
		try {
			myObject= new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			if(myObject.getJSONObject("response").getJSONArray("docs").length()==0){
			return venueDetails;
			}
		JSONObject js=(JSONObject) myObject.getJSONObject("response").getJSONArray("docs").get(0);
		if(js.has("user_id")) {
			venueDetails.setUserId(js.get("user_id").toString());
		} else {
			venueDetails.setUserId("null");
		}
			if(js.has("venue_phone_no")) {
			venueDetails.setVenuePhoneNo(js.get("venue_phone_no").toString());
		} else {
			venueDetails.setVenuePhoneNo("null");
		}
		if(js.has("website")) {
			venueDetails.setWebsite(js.get("website").toString());
		} else {
			venueDetails.setWebsite("null");
		}
		if(js.has("hour_Of_Operation")) {
			venueDetails.setHourOfOperation(js.get("hour_Of_Operation").toString());
		} else {
			venueDetails.setHourOfOperation("null");
		}
		if(js.has("venue_address")) {
			venueDetails.setAddress(js.get("venue_address").toString());
		} else {
			venueDetails.setAddress("null");
		}
		if(js.has("venue_city_id")) {
			venueDetails.setVenueCityId(js.get("venue_city_id").toString());
		} else {
			venueDetails.setVenueCityId("null");
		}
		if(js.has("venue_name")) {
			venueDetails.setVenueName(js.get("venue_name").toString());
		} else {
			venueDetails.setVenueName("null");
		}
		if(js.has("venue_zip_code")) {
			venueDetails.setVenueZipcode(js.get("venue_zip_code").toString());
		} else {
			venueDetails.setVenueZipcode("null");
		}
		if(js.has("venue_state_id")) {
			venueDetails.setVenueStatueId(js.get("venue_state_id").toString());
		} else {
			venueDetails.setVenueStatueId("null");
		}
		if(js.has("venue_photo_link")) {
			venueDetails.setVenuePhotoLink(js.get("venue_photo_link").toString());
		} else {
			venueDetails.setVenuePhotoLink("null");
		}
		if(js.has("venue_id")) {
			venueDetails.setVenueId(js.get("venue_id").toString());
		} else {
			venueDetails.setVenueId("null");
		}
		if(js.has("venue_add_date")) {
			venueDetails.setVenueAddedDate(js.get("venue_add_date").toString());
		} else {
			venueDetails.setVenueAddedDate("null");
		}
		if(js.has("venue_category_id")) {
			venueDetails.setVenueCategory(js.get("venue_category_id").toString());
		} else {
			venueDetails.setVenueCategory("null");
		}
		if(js.has("venue_subcategory_id")) {
			venueDetails.setVenueSubCategory(js.get("venue_subcategory_id").toString());
		} else {
			venueDetails.setVenueSubCategory("null");
		}
		if(js.has("type")) {
			venueDetails.setType(js.get("type").toString());
		} else {
			venueDetails.setType("null");
		}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return venueDetails;
	}*/
	/*
	 * @author Name:Gaurav chugh
	 * @Created Date:27/10/2014
	 * @update Date:27/10/2014
	 * @purpose:getHelpfulReviewDetails
	 *  
	 */
	/*public  List<HelpFulReview> getHelpfulReviewDetails(String reviewId){		
		ArrayList<HelpFulReview> al = new ArrayList<HelpFulReview>();
		String query = "review_id:" + reviewId;	
		String url = UrlConstant.SOLR_HTTP_URL+"/solr/helpful/select/?q="+query+"&version=2.2&start=0&rows=10&indent=on&wt=json";
		HttpClient client = new DefaultHttpClient();
		HttpGet request1 = new HttpGet(url);
		// add request header
		HttpResponse response = null; 
		String json=null;
		try {
		response = client.execute(request1);
		json = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e1) {
		e1.printStackTrace();
		} catch (IOException e1) {
		e1.printStackTrace();
		}

		JSONObject myObject=null;
		JSONArray object = null;
		try {
			myObject= new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			object = myObject.getJSONObject("response").getJSONArray("docs");
			if(object.length()==0){
			return al;
			}
			for(int i=0;i<object.length();i++){
				HelpFulReview helpFulReview=new HelpFulReview();
				JSONObject js = (JSONObject) myObject.getJSONObject("response")
						.getJSONArray("docs").get(i);
				
				if (js.has("review_helpful")) {
					helpFulReview.setReviewHelpFul(js.get("review_helpful")
							.toString());
				} else {
					helpFulReview.setReviewHelpFul("null");
				}
				if (js.has("id")) {
					helpFulReview.setId(js.get("id").toString());
				} else {
					helpFulReview.setId("null");
				}
				if (js.has("review_id")) {
					helpFulReview.setReviewId(js.get("review_id").toString());
				} else {
					helpFulReview.setReviewId("null");
				}
				if (js.has("venue_id")) {
					helpFulReview.setVenueId(js.get("venue_id").toString());
				} else {
					helpFulReview.setVenueId("null");
				}
				if (js.has("user_id")) {
					helpFulReview.setUserId(js.get("user_id").toString());
				} else {
					helpFulReview.setUserId("null");
				}
				al.add(helpFulReview);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return al;
	}*/
	/*
	 * @author Name:Gaurav chugh
	 * @Created Date:11/11/2014
	 * @update Date:27/11/2014
	 * @purpose:getOtherUserDetailsByIdDetails
	 */
	@RequestMapping(value = "/user-other-details-by-id-details", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getOtherUserDetailsByIdDetails(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "other_user_id", required = true) String otherUserId,
			@RequestParam(value = "user_token", required = true) String userToken,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassSession passSession = null;
		String query = null;		
		UserDetails userDetailsLocal=null;
		PassFriendRequest friendListIds=null;
		List<UserDetails> userde= new ArrayList<UserDetails>();
		
		try {
			if (apikey == null || apikey.equals("") || userId == null
					|| userId.equals("") || userToken == null
					|| userToken.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				query = "user_id:" + userId + " AND token:" + userToken;
				passSession = (PassSession) solrCommonClient.commonSolrSearch(
						UrlConstant.SEARCH_SESSION_URL, PassSession.class,
						query, 0, 1, "", "", "false");
				if (passSession.getResponse().getDocs().size() > 0) {
					query = "user_id:" + otherUserId;
					PassUsers userDetails = (PassUsers) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_USERS_URL,	PassUsers.class, query,	0, 1, "","", "false");
				
					for (User userLocal:userDetails.getResponse().getDocs() ){
						 userDetailsLocal=new UserDetails();
						 userDetailsLocal.setUsers(userLocal);
						 query="to_user:"+userId +" AND from_user: "+ otherUserId;		
						 friendListIds=(PassFriendRequest)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REQUEST_URL, PassFriendRequest.class, query, 0, 1, "", "", "false");
					if(friendListIds.getResponse().getDocs().size()>0){
						userDetailsLocal.setIsFriend(friendListIds.getResponse().getDocs().get(0).getStatus());
						userDetailsLocal.setRequestId(friendListIds.getResponse().getDocs().get(0).getRequestId());
					}
						 userde.add(userDetailsLocal);
					}
					mav.addObject("numFound",userDetails.getResponse().getNumFound());
					mav.addObject(userde);
					return mav;
				} else {
					mav.addObject(ErrorCode.getCustomeError(3008));
					return mav;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
			return mav;
		}
		return mav;
	}
	/*
	 * @author Name:Gaurav chugh
	 * @Created Date:27/11/2014
	 * @update Date:27/11/2014
	 * @purpose:deleteAllUserLoc
	 */	
	@RequestMapping( value="/delete-all-userLoc" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView deleteAllUserLoc(			
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		boolean userLocDelStatus=false;		
		
		userLocDelStatus=	solrCommonClient.deleteAllObject(UrlConstant.USER_LOC_URL);
		if(userLocDelStatus)
			mav.addObject("UserLoc Delete Succesfully");
		else
			mav.addObject("Issue while deleting UserLoc");		
		
		return mav;
		}	
	
}

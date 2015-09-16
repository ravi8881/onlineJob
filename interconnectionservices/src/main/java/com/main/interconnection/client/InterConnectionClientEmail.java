package com.main.interconnection.client;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.main.interconnection.clientBo.ErrorCode;
import com.main.interconnection.clientBo.Message;
import com.main.interconnection.clientBo.User;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.dao.UserDao;
import com.main.interconnection.solr.response.session.PassSession;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
import com.main.interconnection.util.CommonEmail;


@Controller
@RequestMapping(value="/email/*")
public class InterConnectionClientEmail {
	
	private static final Logger logger = LoggerFactory.getLogger(InterConnectionClientEmail.class);
	
	@Autowired
	CommonEmail commonEmail;
	
	@Autowired
	UserDao userDao;
		
	@Autowired
	ApiDao apiDao;
	
	@Autowired
	SolrCommonClient solrCommonClient;
	
	@RequestMapping(value="/send-activation-link" , method={RequestMethod.POST,RequestMethod.GET} )
	public ModelAndView sendActivationLink(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			 HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HashMap<String, String> params = new HashMap<String, String>();
		String activationUrlMobile=null;
		String activationUrl=null;
		String templatePath=null;
		User user=null;
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(userId ==null || userId.equals("") ){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(apiDao.validateApiKey(apikey)){
				user=userDao.getRegisterUserDetails(userId);
				if(user!=null){
				activationUrl=apiDao.getValidationUrl(apikey);	
				if(!activationUrl.isEmpty()){
					
					logger.info("Mobile Activation code "+ user.getMobileVerifyCode());
					logger.info("Email Activation code "+ user.getEmailVerifyCode());
					
					activationUrlMobile=activationUrl.replace("{userId}", user.getUserId());
					activationUrlMobile=activationUrlMobile.replace("{phone_code}", user.getMobileVerifyCode());
					activationUrlMobile=activationUrlMobile.replace("{email_code}",user.getEmailVerifyCode());		
					}else{
						mav.addObject(ErrorCode.getCustomeError(4001));
						return mav;	
					}
					params.put("_NAME_", user.getName());
					params.put("_URLACTIVATIONLINK_", activationUrlMobile);
					params.put("_MobileCODE_", user.getMobileVerifyCode());	
					params.put("_EmailCODE_", user.getEmailVerifyCode());	
					templatePath=request.getSession().getServletContext().getRealPath("/WEB-INF/mailTemplate/activationMail.template");
					commonEmail.sendEmail(templatePath, params , "Waspit Activation links", user.getEmailId());
					mav.addObject("emailCode",user.getEmailVerifyCode());
					mav.addObject("mobileCode",user.getMobileVerifyCode());
					mav.addObject(Message.getCustomeMessages(2));
				}else{
					mav.addObject(ErrorCode.getCustomeError(3008));
					return mav;	
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}		
	return mav;
	
	}
	
	@RequestMapping(value = "/share-venue-by-email", method = {	RequestMethod.POST, RequestMethod.GET })
	public ModelAndView shareVenueByEmail(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "venue_id", required = true) String venueId,
			@RequestParam(value = "email_id", required = true) String emailId,
			@RequestParam(value = "user_token", required = true) String userToken,
			@RequestParam(value = "description", required = false) String description,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassSession passSession = null;
		String query = null;
		String templatePath=null;
		HashMap<String, String> params = new HashMap<String, String>();
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (userId == null || userId.equals("") || venueId == null || venueId.equals("") || emailId == null || emailId.equals("") ) {
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				query = "user_id:" + userId + " AND token:" + userToken;
				passSession = (PassSession) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class,query, 0, 1, "", "", "false");
				if (passSession.getResponse().getDocs().size() > 0) {
					params.put("_NAME_", emailId);
					params.put("_VENUEID_", "http://dev.waspit.miracleglobal.com/php/index.php/AddVenue#"+venueId);
					params.put("_DESCRIPTION_", description);
					templatePath=request.getSession().getServletContext().getRealPath("/WEB-INF/mailTemplate/shareVenue.template");
					commonEmail.sendEmail(templatePath, params , "Share Venue", emailId);					
					mav.addObject(Message.getCustomeMessages(2));					
					return mav;
				} else {
					mav.addObject(ErrorCode.getCustomeError(3008));
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

}

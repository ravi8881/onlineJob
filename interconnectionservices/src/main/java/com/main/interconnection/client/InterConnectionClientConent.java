package com.main.interconnection.client;

import java.util.Date;

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
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.solr.response.session.PassSession;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
import com.main.interconnection.util.SolrDateUtil;


@Controller
@RequestMapping(value="/content/*")
public class InterConnectionClientConent {
	
	private static final Logger logger = LoggerFactory.getLogger(InterConnectionClientConent.class);
	
	@Autowired
	ApiDao apiDao;
	
	@Autowired
	SolrCommonClient solrCommonClient;
	
	@Autowired
	SolrInputDocument solrDocument;
	
	
	@RequestMapping( value="/mark-spam" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView markContentSpam(
			@RequestParam(value = "api_key", required = true) String apikey,		
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "content_id", required = true) String contentId,			
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "type", required = true) String type,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();		
		try{		
		
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(token==null || token.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;					
			}else if(userId ==null || userId.equals("") || contentId ==null || contentId.equals("") || type ==null || type.equals("") ){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().size()<=0){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(apiDao.validateApiKey(apikey)){
				logger.info("Mark-spam");			
				solrDocument=new SolrInputDocument();
				solrDocument.addField("id", contentId+userId);
				solrDocument.addField("content_id", contentId);				
				solrDocument.addField("user_id", userId);
				solrDocument.addField("type", type);
				solrDocument.addField("added_date", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
				solrDocument.addField("content_type", "spam");
				solrCommonClient.addObjectToSolr(UrlConstant.CONTENT_URL , solrDocument);
				mav.addObject("Content Mark as spam");
				mav.addObject("Content id:",contentId+userId);				
				return mav;
			}
		}catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(3009));				
		}	
		return mav;
		}
	
	@RequestMapping( value="/mark-inappropriate" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView markContentInappropirate(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "content_id", required = true) String contentId,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "type", required = true) String type,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		
		
		try{		
		
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(token==null || token.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;					
			}else if(userId ==null || userId.equals("") || contentId ==null || contentId.equals("") || type ==null || type.equals("") ){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;						
			}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().getDocs().size()<=0){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){				
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;	
			}else if(apiDao.validateApiKey(apikey)){
				logger.info("Mark-inappropriate");			
				solrDocument=new SolrInputDocument();
				solrDocument.addField("id", userId+contentId);
				solrDocument.addField("content_id", contentId);				
				solrDocument.addField("user_id", userId);
				solrDocument.addField("type", type);
				solrDocument.addField("added_date", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
				solrDocument.addField("content_type", "inappropriate");
				solrCommonClient.addObjectToSolr(UrlConstant.CONTENT_URL , solrDocument);
				mav.addObject("Content Mark as inappropriate");
				mav.addObject("Content id:",userId+contentId);				
				return mav;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(3009));
		}	
		return mav;
		}

	@RequestMapping( value="/delete-all-content" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView deleteAllContent(			
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		boolean contentDelStatus=false;		
		
		contentDelStatus=solrCommonClient.deleteAllObject(UrlConstant.CONTENT_URL);
		if(contentDelStatus)
			mav.addObject("Content Delete Succesfully");
		else
			mav.addObject("Issue while deleting Content");		
		
		return mav;
		}	
}

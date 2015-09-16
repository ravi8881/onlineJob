package com.main.interconnection.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.main.interconnection.clientBo.Category;
import com.main.interconnection.clientBo.City;
import com.main.interconnection.clientBo.ErrorCode;
import com.main.interconnection.clientBo.Flag;
import com.main.interconnection.clientBo.Message;
import com.main.interconnection.clientBo.State;
import com.main.interconnection.clientBo.SubCategory;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.dao.CityDao;
import com.main.interconnection.dao.CommonDao;
import com.main.interconnection.dao.StateDao;
import com.main.interconnection.foursquare.photo.FourSquarePhoto;
import com.main.interconnection.mongoDao.MongoCommonClient;
import com.main.interconnection.rest.client.ThirdPartyRestClient;
import com.main.interconnection.solr.response.binary.PassBinary;
import com.main.interconnection.solr.response.session.PassSession;
import com.main.interconnection.solr.response.venue.PassVenue;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
import com.main.interconnection.util.ImageUtils;
import com.main.interconnection.util.MagicNumbers;
import com.main.interconnection.util.SolrDateUtil;
import com.main.interconnection.util.UUIDGenrator;
import com.main.interconnection.util.UpdateTypeEnum;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Controller
@RequestMapping(value="/util/*")
public class InterConnectionClientUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(InterConnectionClientUtil.class);
	
	@Autowired
	ApiDao apiDao;
	
	@Autowired
	StateDao stateDao;
	
	@Autowired
	CityDao cityDao;
	
	@Autowired
	CommonDao commonDao;
	
	@Autowired
	SolrCommonClient solrCommonClient;
	
	@Autowired
	MongoCommonClient mongoCommonClient;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	ThirdPartyRestClient thirdPartyRestClient;
	
	@Value("${foursquare.clientid}")
	String foursquareClientId;
	
	@Value("${foursquare.clientsecret}")
	String foursquareClientSecret;
	
	@Value("${foursquare.version}")
	String foursquareVersion;
	
	@Value("${foursquare.m}")
	String foursquareM;
	
	@Value("${foursquare.outh}")
	String foursquareAuthToken;
	
	Map<String, String> params=null;
	
	@RequestMapping( value="/get-all-state" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getAllState(
			@RequestParam(value = "api_key", required = true) String apikey,			
			HttpServletRequest request) {
	
		ModelAndView mav = new ModelAndView();		
		List<State> stateList=new ArrayList<State>();
		logger.info("Get  states Api");
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(apiDao.validateApiKey(apikey)){
				stateList=stateDao.getAllState();
				if(stateList.size()>0){
					mav.addObject("state",stateList);					
				}else{
					mav.addObject(ErrorCode.getCustomeError(107));
					return mav;	
				}			
			}else{
				mav.addObject(ErrorCode.getCustomeError(100));
				return mav;	
			}
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return mav;
 }
	
	
	@RequestMapping( value="/get-all-cities" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getAllCities(
			@RequestParam(value = "api_key", required = true) String apikey,				
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();		
		List<City> cityList=new ArrayList<City>();
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(apiDao.validateApiKey(apikey)){
				cityList=cityDao.getAllCity();
				if(cityList.size()>0){
					mav.addObject("cities",cityList);					
				}else{					
					mav.addObject(ErrorCode.getCustomeError(108));
					return mav;	
				}			
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));
				return mav;	
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	return mav;
	}
	
	@RequestMapping( value="/cities-basedon-stateid" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getAllCitiesBsedOnStateId(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "state_id", required = true) Integer stateId,	
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();		
		List<City> cityList=new ArrayList<City>();
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(stateId ==null){				
				mav.addObject(ErrorCode.getCustomeError(1001));
				return mav;	
			}else if(apiDao.validateApiKey(apikey)){
				cityList=cityDao.getCityBasedOnStateId(stateId);
				if(cityList.size()>0){
					mav.addObject("cities",cityList);					
				}else{					
					mav.addObject(ErrorCode.getCustomeError(108));
					return mav;	
				}			
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));
				return mav;	
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return mav;
	}
	
	
	@RequestMapping( value="/get-all-category" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getAllCategory(
			@RequestParam(value = "api_key", required = true) String apikey,				
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();		
		List<Category> categoryList=new ArrayList<Category>();
		try{
			
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(apiDao.validateApiKey(apikey)){
				categoryList=commonDao.getAllCategory();
				if(categoryList.size()>0){
					mav.addObject("category",categoryList);					
				}else{					
					mav.addObject(ErrorCode.getCustomeError(109));
					return mav;	
				}			
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));
				return mav;	
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	return mav;
	}
	
	
	@RequestMapping( value="/get-user-token" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getUserToken(
			@RequestParam(value = "api_key", required = true) String apikey,	
			@RequestParam(value = "user_id", required = true) String userId,				
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();		
		HttpSession session = request.getSession();
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(userId ==null || userId.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;	
				
			}else if(apiDao.validateApiKey(apikey)){
				if(session.getAttribute(userId)!=null)	{			
				mav.addObject("token",session.getAttribute(userId));
				return mav;
				}
				else{					
					mav.addObject(ErrorCode.getCustomeError(3007));
					return mav;
				}				
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));
				return mav;	
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}		
	return mav;
	}
	
	@RequestMapping( value="/get-all-error-code" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getAllErrorMessage(
			@RequestParam(value = "api_key", required = true) String apikey,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();		
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(apiDao.validateApiKey(apikey)){				
				mav.addObject("99",ErrorCode.getCustomeError(99));
				mav.addObject("100",ErrorCode.getCustomeError(100));
				mav.addObject("101",ErrorCode.getCustomeError(101));
				mav.addObject("102",ErrorCode.getCustomeError(102));
				mav.addObject("103",ErrorCode.getCustomeError(103));
				mav.addObject("104",ErrorCode.getCustomeError(104));
				mav.addObject("105",ErrorCode.getCustomeError(105));
				mav.addObject("106",ErrorCode.getCustomeError(106));
				mav.addObject("107",ErrorCode.getCustomeError(107));
				mav.addObject("108",ErrorCode.getCustomeError(108));
				mav.addObject("109",ErrorCode.getCustomeError(109));
				mav.addObject("110",ErrorCode.getCustomeError(110));
				mav.addObject("400",ErrorCode.getCustomeError(400));
				mav.addObject("404",ErrorCode.getCustomeError(404));
				mav.addObject("405",ErrorCode.getCustomeError(405));				
				mav.addObject("1001",ErrorCode.getCustomeError(1001));
				mav.addObject("1002",ErrorCode.getCustomeError(1002));				
				mav.addObject("3001",ErrorCode.getCustomeError(3001));
				mav.addObject("3002",ErrorCode.getCustomeError(3002));
				mav.addObject("3003",ErrorCode.getCustomeError(3003));
				mav.addObject("3004",ErrorCode.getCustomeError(3004));
				mav.addObject("3005",ErrorCode.getCustomeError(3005));
				mav.addObject("3006",ErrorCode.getCustomeError(3006));
				mav.addObject("3007",ErrorCode.getCustomeError(3007));
				mav.addObject("3008",ErrorCode.getCustomeError(3008));
				mav.addObject("3009",ErrorCode.getCustomeError(3009));
				mav.addObject("4001",ErrorCode.getCustomeError(4001));	
				return mav;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return mav;
	}
	
	@RequestMapping( value="/get-all-message-code" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getAllMessage(@RequestParam(value = "api_key", required = true) String apikey,		
			HttpServletRequest request) {		
		ModelAndView mav = new ModelAndView();		
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(apiDao.validateApiKey(apikey)){				
				mav.addObject("0",Message.getCustomeMessages(0));	
				mav.addObject("1",Message.getCustomeMessages(1));				
				return mav;
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
		return mav;
	}
	
	@RequestMapping(value="/getIm" , method=RequestMethod.GET )
	public ResponseEntity<byte[]> getIM(@RequestParam(value = "image_id", required = false) String imageId,
			 HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedImage originalImage=null;		
		try{
			 if(imageId.equals("1"))
			originalImage = ImageIO.read(new File("c:\\Koala2.jpg"));
			 else
				 originalImage = ImageIO.read(new File("c:\\Koala.jpg"));			
			ImageIO.write( originalImage, "jpg", baos );
			baos.flush();
			byte[] imageInByte = baos.toByteArray();			 
			mav.addObject(Base64.encode(baos.toByteArray()));			
			baos.close();		 
			}catch(IOException e){
				System.out.println(e.getMessage());
			}	
		return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
 }	
	
	@RequestMapping(value="/getImage" , method=RequestMethod.GET )
	public ResponseEntity<byte[]> getI(@RequestParam(value = "query", required = true) String query,
			@RequestParam(value = "fl", required = false) String fl,
			 HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);	
		
		byte[] imageInByte=null;
		if(fl==null||fl.equals("")){
			fl="medium";
		}
		try{
			PassBinary sol=(PassBinary)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_PHOTO_URL, PassBinary.class, query, 0, 1, fl, "", "false");
			if(fl.equals("small"))
			imageInByte=Base64.decode(sol.getResponse().getDocs().get(0).getSmall());
			else if(fl.equals("medium"))
			imageInByte=Base64.decode(sol.getResponse().getDocs().get(0).getMedium());
			else if(fl.equals("orignal"))
			imageInByte=Base64.decode(sol.getResponse().getDocs().get(0).getOrignal());
			
			
			}catch(Exception e){
				e.printStackTrace();
				
			}
		return new ResponseEntity<byte[]>(imageInByte, headers, HttpStatus.CREATED);
 }
	
	
	@RequestMapping(value="/getImageById" , method=RequestMethod.GET )
	public ResponseEntity<byte[]> getIMageById(@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "fl", required = false) String fl,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			
			 HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);		
		String query=null;		
		byte[] imageInByte=null;
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);		
		try{
			query="id:"+id+" OR content_id:"+id;
			PassBinary imageBinary=(PassBinary)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_PHOTO_URL, PassBinary.class, query, startElementSolr, rowsSolr, fl, "", "false");
			
			if(fl.equals("small"))
				imageInByte=Base64.decode(imageBinary.getResponse().getDocs().get(0).getSmall());
			else if(fl.equals("medium"))
				imageInByte=Base64.decode(imageBinary.getResponse().getDocs().get(0).getMedium());
			else if(fl.equals("orignal"))
				imageInByte=Base64.decode(imageBinary.getResponse().getDocs().get(0).getOrignal());
			else if(fl.equals("large"))
				imageInByte=Base64.decode(imageBinary.getResponse().getDocs().get(0).getLarge());
			}catch(Exception e){
				e.printStackTrace();
			}
		return new ResponseEntity<byte[]>(imageInByte, headers, HttpStatus.CREATED);
 }	
	
	
	@RequestMapping(value="/upload-image" ,  method={RequestMethod.POST,RequestMethod.GET} )
	public ModelAndView uploadImage(@RequestParam(value = "base-image", required = true) String baseImage,
			@RequestParam(value = "content_id", required = false) String contentId,
			@RequestParam(value = "user_id", required = false) String userId,
			@RequestParam(value = "type", required = false) String type,
			 HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		ByteArrayOutputStream baosOrg = new ByteArrayOutputStream();
		ByteArrayOutputStream baosSmall = new ByteArrayOutputStream();
		ByteArrayOutputStream baosMed = new ByteArrayOutputStream();
		byte[] imageInByte=null;
		if(contentId ==null || contentId.equals("")){
			contentId="none";
		}
		/* if(type ==null || type.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;		
		 }*/
	//	PassBinary sol=(PassBinary)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_PHOTO_URL, PassBinary.class, "id:0470CC41-82AC-4C39-B065-33EC7685D8C2", 0, 1, "", "", "false");
		
	//	String name=sol.getResponse().getDocs().get(0).getSmall();
			try{
				imageInByte=Base64.decode(baseImage);	
				InputStream in = new ByteArrayInputStream(imageInByte);
				BufferedImage bufferedGalleryImage = ImageIO.read(in);
				ImageIO.write( bufferedGalleryImage, "jpg", baosOrg );
				byte[] imageOriginalByte = baosOrg.toByteArray();
				baosOrg.flush();
				
				BufferedImage bufferedSmallProductImage= ImageUtils.resize(bufferedGalleryImage, 63, 63, true);
				ImageIO.write( bufferedSmallProductImage, "jpg", baosSmall );		
				byte[] imageSmallByte = baosSmall.toByteArray();
				baosSmall.flush();
				
				BufferedImage bufferedMediumProductImage= ImageUtils.resize(bufferedGalleryImage, 495, 362, true);
				ImageIO.write( bufferedMediumProductImage, "jpg", baosMed );		
				byte[] imageMediumByte = baosMed.toByteArray();
				baosMed.flush();
				
				BufferedImage bufferedLargeProductImage= ImageUtils.resize(bufferedGalleryImage, 584, 366, true);
				ImageIO.write( bufferedLargeProductImage, "jpg", baosMed );		
				byte[] imageLargeByte = baosMed.toByteArray();
				baosMed.flush();
				
				String uid=UUIDGenrator.getUniqueId();
				SolrInputDocument	solrDocument=new SolrInputDocument();
				solrDocument.addField("id", uid);
				solrDocument.addField("content_id",contentId);
				solrDocument.addField("orignal",Base64.encode(imageOriginalByte));
				solrDocument.addField("small",Base64.encode(imageSmallByte));
				solrDocument.addField("medium",Base64.encode(imageMediumByte));
				solrDocument.addField("large",Base64.encode(imageLargeByte));	
				solrDocument.addField("added_date", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
				if(userId!=null)
					solrDocument.addField("user_id",userId);	
					solrDocument.addField("type",type);	
				solrCommonClient.addObjectToSolr(UrlConstant.PHOTO_URL , solrDocument);
				mav.addObject("imageid",uid);
				
				
				if(type!= null && !type.equals("")) {
					if(type.equals("Venue")){
						//		 Create Feed card
						String updateId=UUIDGenrator.getUniqueId();
						solrDocument=new SolrInputDocument();
						solrDocument.addField("id", updateId);
						solrDocument.addField("type", UpdateTypeEnum.UpdateType.Feeds.toString());
						solrDocument.addField("subType", UpdateTypeEnum.UpdateSubType.Venue.toString());
						solrDocument.addField("property", UpdateTypeEnum.VenueUpdateProperty.AddedPhotos.toString());	
						if(userId!=null) {
							solrDocument.addField("toUser", userId);	
							solrDocument.addField("fromUser", userId);
						} else {
							solrDocument.addField("toUser", "null");	
							solrDocument.addField("fromUser", "null");
						}
						solrDocument.addField("content_id", contentId);
						PassVenue passVenue=(PassVenue)solrCommonClient. commonSolrSearch(UrlConstant.SEARCH_VENUE_URL, PassVenue.class, "venue_id:"+contentId	, 0, 1, "", "", "false");
						if(passVenue.getResponse().getDocs().size()>0){
						solrDocument.addField("search_name", passVenue.getResponse().getDocs().get(0).getVenueName().trim());
						}
						solrDocument.addField("image_url", uid);
						solrDocument.addField("privacy", MagicNumbers.ACTIVE);
						solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
						solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
					}
				}
				}catch(Exception e){
					e.printStackTrace();
					
				}
		return mav;
 }	

	@RequestMapping( value="/gethello" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView gethello(		
			HttpServletRequest request) {		
		ModelAndView mav = new ModelAndView();	
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedImage originalImage=null;
		boolean resu=false;
		try{
			 originalImage = ImageIO.read(new File("c:\\Koala.jpg"));
				
				ImageIO.write( originalImage, "jpg", baos );
				baos.flush();
				byte[] imageInByte = baos.toByteArray();			
						
				String img=Base64.encode(baos.toByteArray());
				String uid=UUIDGenrator.getUniqueId();
						SolrInputDocument	solrDocument=new SolrInputDocument();
						solrDocument.addField("id", uid);
						solrDocument.addField("name",img );
						resu=	solrCommonClient.addObjectToSolr(UrlConstant.PHOTO_URL , solrDocument);
						mav.addObject(uid);
			}catch (Exception e) {
				e.printStackTrace();
			}
		return mav;
	}
	

	@RequestMapping( value="/state-details" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getStateDetails(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "state_id", required = true) Integer stateId,	
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();		
		State statedetails=null;
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(stateId ==null){				
				mav.addObject(ErrorCode.getCustomeError(1001));
				return mav;	
			}else if(apiDao.validateApiKey(apikey)){
				statedetails=stateDao.getStateDetailsById(stateId);
				if(statedetails!=null){
					mav.addObject("stateDetails",statedetails);					
				}else{					
					mav.addObject(ErrorCode.getCustomeError(111));
					return mav;	
				}			
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));
				return mav;	
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(111));
			return mav;	

		}
		
		return mav;
	}
	
	@RequestMapping( value="/city-details" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getCityDetails(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "city_id", required = true) Integer cityId,	
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();		
		City cityDetails=null;
		try{
			if(apikey ==null || apikey.equals("")){				
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;			
			}else if(cityId ==null){				
				mav.addObject(ErrorCode.getCustomeError(1001));
				return mav;	
			}else if(apiDao.validateApiKey(apikey)){
				cityDetails=cityDao.getCityDetailsById(cityId);
				if(cityDetails!=null){
					mav.addObject("cities",cityDetails);
					
				}else{					
					mav.addObject(ErrorCode.getCustomeError(108));
					return mav;	
				}			
			}else{				
				mav.addObject(ErrorCode.getCustomeError(100));
				return mav;	
			}
		}catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(112));
			return mav;	
		}
		
		return mav;
	}
	
	
	@RequestMapping( value="/delete-all-images" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView deleteAllImages(			
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		boolean imagesDelStatus=false;		
		
		imagesDelStatus=solrCommonClient.deleteAllObject(UrlConstant.PHOTO_URL);
		if(imagesDelStatus)
			mav.addObject("Images Delete Succesfully");
		else
			mav.addObject("Issue while deleting Images");		
		
		return mav;
		}
	
	@RequestMapping( value="/delete-image-by-id" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView deleteImageById(		
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "id", required = true) String id,	
			@RequestParam(value = "token", required = true) String token,	
			@RequestParam(value = "user_id", required = true) String userId,	
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		String query = "id:"+id+" AND "+"user_id:"+userId;
		if(apikey ==null || apikey.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;			
		}else if(token==null || token.equals("")){
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;					
		}else if(userId ==null || userId.equals("") || id ==null || id.equals("")){
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;						
		}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().size()==0){				
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;	
		}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;	
		}else if(apiDao.validateApiKey(apikey)){
			boolean imagesDelStatus=false;		
			imagesDelStatus=solrCommonClient.deleteObject(UrlConstant.PHOTO_URL, query);
			if(imagesDelStatus) {
				mav.addObject("Image Delete Succesfully");
			} else {
				mav.addObject("Issue while deleting Images");	
			}
		}
		return mav;
	}
		
	@RequestMapping( value="/flag-image" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView flagImage(
			@RequestParam(value = "api_key", required = true) String apikey,			
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "image_id", required = true) String imageId,	
			@RequestParam(value = "is_flagged", required = true) String isFlagged,
			HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		if(apikey ==null || apikey.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;			
		}else if(token==null || token.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;					
		}else if(userId ==null || userId.equals("") || imageId ==null || imageId.equals("")
				|| isFlagged == null || isFlagged.equals("")){				
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;						
		}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "user_id", "", "false").getResponse().
				getDocs().size()<=0){				
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;	
		}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().
				getDocs().get(0).getToken().equals(token)){				
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;	
		}else if(apiDao.validateApiKey(apikey)){
			List<Flag> flagCommentList  = new ArrayList<Flag>();
			Query query=new Query();
			query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("typeName").is(UpdateTypeEnum.FlagType.photo_id).andOperator(
					Criteria.where("typeValue").is(imageId))));
			flagCommentList = (List<Flag>) mongoCommonClient.findByQuery(query, Flag.class);
			if(flagCommentList.size() > 0) {
				Update update = new Update();
				update.set("isFlagged", isFlagged);
				mongoCommonClient.saveOrUpdate(Query.query(Criteria.where("userId").is(userId).and("typeName").is(UpdateTypeEnum.FlagType.photo_id.toString()).
						and("typeValue").is(imageId)), Flag.class, update);
				mav.addObject("Image_Flagged", isFlagged);
				mav.addObject("image_id", imageId);
				return mav;
			} else {
				Flag flagVenue = new Flag();
				flagVenue.setAdminApproved("1");
				flagVenue.setIsFlagged(isFlagged);
				flagVenue.setUserId(userId);
				flagVenue.setTypeName(UpdateTypeEnum.FlagType.photo_id.toString());
				flagVenue.setTypeValue(imageId);
				flagVenue.setSubType("none");
				mongoTemplate.insert(flagVenue);
				mav.addObject("Image_Flagged", isFlagged);
				mav.addObject("image_id", imageId);
				return mav;
			}
		}
		return mav;
	}
	
	@RequestMapping( value="/get-update-enum-subtypes" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getEnumTypes(HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		List<String> updateEnumSubtypes = new ArrayList<String>();
		updateEnumSubtypes.add(UpdateTypeEnum.UpdateSubType.BioUpdate.toString());
		updateEnumSubtypes.add(UpdateTypeEnum.UpdateSubType.FriendRequest.toString());
		updateEnumSubtypes.add(UpdateTypeEnum.UpdateSubType.like.toString());
		updateEnumSubtypes.add(UpdateTypeEnum.UpdateSubType.ProfilePictureUpdate.toString());
		updateEnumSubtypes.add(UpdateTypeEnum.UpdateSubType.Venue.toString());
		updateEnumSubtypes.add(UpdateTypeEnum.UpdateSubType.VenueBookmarked.toString());
		updateEnumSubtypes.add(UpdateTypeEnum.UpdateSubType.VenueUnBookmarked.toString());
		mav.addObject("SubType Values", updateEnumSubtypes);
		return mav;
	}
	
	@RequestMapping( value="/get-update-enum-properties" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getEnumProperty(HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		List<String> updateEnumProperty = new ArrayList<String>();
		updateEnumProperty.add(UpdateTypeEnum.feedsUpdateProperty.comments.toString());
		updateEnumProperty.add(UpdateTypeEnum.feedsUpdateProperty.commentsLike.toString());
		updateEnumProperty.add(UpdateTypeEnum.feedsUpdateProperty.ProfileUpdate.toString());
		updateEnumProperty.add(UpdateTypeEnum.feedsUpdateProperty.VenueRating.toString());
		updateEnumProperty.add(UpdateTypeEnum.VenueUpdateProperty.AddedPhotos.toString());
		updateEnumProperty.add(UpdateTypeEnum.VenueUpdateProperty.AddedVenue.toString());
		updateEnumProperty.add(UpdateTypeEnum.VenueUpdateProperty.UpdatedVenue.toString());
		mav.addObject("Property Values", updateEnumProperty);
		return mav;
	}
	
	@RequestMapping(value="/getImageContentIds" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getImageContentIds(@RequestParam(value = "query", required = true) String query,
			@RequestParam(value = "fl", required = false) String fl,			
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "orderBy", required = false) String orderBy,
					 HttpServletRequest request) {	
			ModelAndView mav = new ModelAndView();
			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);		
			int startElementSolr = Integer.parseInt(startElement);
			int rowsSolr = Integer.parseInt(rows);		
			try{
				PassBinary imageBinary=(PassBinary)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_PHOTO_URL, PassBinary.class, query, startElementSolr, rowsSolr, fl, orderBy, "false");
				mav.addObject(imageBinary);
				}catch(Exception e){
					e.printStackTrace();
				}
			return mav;
	}
	
	@RequestMapping(value="/get-foursquare-imageby-venue-id" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getFourSquareImageById(@RequestParam(value = "venue-id", required = true) String venueId,			
					 HttpServletRequest request) {	
			ModelAndView mav = new ModelAndView();
			
			params =new HashMap<String, String>();
			
			params.put("venueid", venueId);
			params.put("authToken", foursquareAuthToken);
			params.put("version", foursquareVersion);
			try{
				
				FourSquarePhoto fourSquareVenuePhoto=thirdPartyRestClient.commonRestSearchClient(UrlConstant.FOUR_SQUARE_VENUE_PHOTOS, FourSquarePhoto.class,params);
				mav.addObject(fourSquareVenuePhoto);
				
				}catch(Exception e){
					e.printStackTrace();
				}
			return mav;
	}	
	@RequestMapping( value="/get-subcategory-by-id" , method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getSubCatgeoryById(
	@RequestParam(value = "api_key", required = true) String apikey,
	@RequestParam(value = "categoryId", required = true) String categoryId,
	HttpServletRequest request) {
	ModelAndView mav = new ModelAndView();
	List<SubCategory> subcategoryList = new ArrayList<SubCategory>();
	try {

	if (apikey == null || apikey.equals("")) {
	mav.addObject(ErrorCode.getCustomeError(104));
	return mav;
	} else if (apiDao.validateApiKey(apikey)) {
	subcategoryList = commonDao.getSubCategory(categoryId);
	mav.addObject("subCategory", subcategoryList);
	if (subcategoryList.size() > 0) {
	mav.addObject("subCategory", subcategoryList);
	} else {
	mav.addObject(ErrorCode.getCustomeError(109));
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


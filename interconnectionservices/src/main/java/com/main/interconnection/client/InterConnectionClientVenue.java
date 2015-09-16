package com.main.interconnection.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.main.interconnection.clientBo.BookmarkVenue;
import com.main.interconnection.clientBo.Category;
import com.main.interconnection.clientBo.ErrorCode;
import com.main.interconnection.clientBo.Flag;
import com.main.interconnection.clientBo.SolrBinaryData;
import com.main.interconnection.clientBo.SubCategory;
import com.main.interconnection.clientBo.UpdateRecord;
import com.main.interconnection.clientBo.Venue;
import com.main.interconnection.clientBo.VenueDetails;
import com.main.interconnection.clientBo.VenueHistoryDetails;
import com.main.interconnection.clientBo.VenueHistoryDetails2;
import com.main.interconnection.clientBo.VenueImageDetails;
import com.main.interconnection.clientBo.VenueRating;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.dao.CommonDao;
import com.main.interconnection.foursquare.venue.FourSquareVenue;
import com.main.interconnection.mongoDao.MongoCommonClient;
import com.main.interconnection.solr.response.binary.PassBinary;
import com.main.interconnection.solr.response.bookmark.PassBookmark;
import com.main.interconnection.solr.response.review.PassReview;
import com.main.interconnection.solr.response.session.PassSession;
import com.main.interconnection.solr.response.venue.PassVenue;
import com.main.interconnection.solr.response.venue.history.PassVenueHistory;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
import com.main.interconnection.solrUtil.WaspitSolrQuery;
import com.main.interconnection.util.MagicNumbers;
import com.main.interconnection.util.SolrDateUtil;
import com.main.interconnection.util.UUIDGenrator;
import com.main.interconnection.util.UpdateTypeEnum;

@Controller
@RequestMapping(value = "/venue/*")
public class InterConnectionClientVenue {

	private static final Logger logger = LoggerFactory
			.getLogger(InterConnectionClientVenue.class);

	@Autowired
	ApiDao apiDao;
	
	@Autowired
	CommonDao commonDao;

	@Autowired
	SolrCommonClient solrCommonClient;

	@Autowired
	SolrInputDocument solrDocument;

	@Autowired
	MongoCommonClient mongoCommonClient;

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	protected RestTemplate restTemplate;
	
	@Value("${foursquare.clientid}")
	String foursquareClientId;
	
	@Value("${foursquare.clientsecret}")
	String foursquareClientSecret;
	
	@Value("${foursquare.version}")
	String foursquareVersion;
	
	@Value("${foursquare.m}")
	String foursquareM;
	

	@RequestMapping(value = "/add-venue", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView addUserVenue(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "venue_name", required = false) String venueName,
			@RequestParam(value = "venue_address", required = false) String address,
			@RequestParam(value = "venue_city_id", required = false) String venueCityId,
			@RequestParam(value = "venue_state_id", required = false) String venueStateId,
			@RequestParam(value = "venue_zip_code", required = false) String venueZipcode,
			@RequestParam(value = "venue_lati", required = false) String venueLati,
			@RequestParam(value = "venue_long", required = false) String venueLong,
			@RequestParam(value = "venue_phone_no", required = false) String venuePhoneNo,
			@RequestParam(value = "hour_Of_Operation", required = false) String hourOfOperation,
			@RequestParam(value = "website", required = false) String website,
			@RequestParam(value = "venue_category_id", required = false) String venueCategoryId,
			@RequestParam(value = "venue_subcategory_id", required = false) String venueSubCategoryId,
			@RequestParam(value = "venue_photo_link", required = false) String venuePhotoLink,
			@RequestParam(value = "venue_info", required = false) String venueInfo,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String venueId = null;

		boolean venueAddStatus = false;
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (userId == null || userId.equals("") || venueName == null
					|| venueName.equals("") || venueCityId == null
					|| venueCityId.equals("") || venueStateId == null
					|| venueStateId.equals("") || venuePhoneNo == null
					|| venuePhoneNo.equals("")) {
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
				logger.info("Add venue Services");
				//add with key for category
				List<Category> categoryKey = commonDao.getCategoryKey(venueCategoryId);
				//categoryKey = venueCategoryId+":"+categoryKey;
				List<SubCategory> subCategoryKey = commonDao.getSubCategoryKey(venueSubCategoryId);
				venueId = UUIDGenrator.getUniqueId();
				solrDocument = new SolrInputDocument();
				solrDocument.addField("venue_id", venueId);
				solrDocument.addField("user_id", userId);
				solrDocument.addField("venue_name", venueName);
				solrDocument.addField("venue_add_date",
						SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
				solrDocument.addField("venue_address", address);
				solrDocument.addField("venue_city_id",
						venueCityId);
				solrDocument.addField("venue_state_id",
						venueStateId);
				solrDocument.addField("venue_info", venueInfo);
				solrDocument.addField("venue_zip_code", venueZipcode);
				solrDocument.addField("venue_lati", venueLati);
				solrDocument.addField("venue_long", venueLong);
				solrDocument.addField("venue_phone_no", venuePhoneNo);
				solrDocument.addField("hour_Of_Operation", hourOfOperation);
				solrDocument.addField("website", website);
				solrDocument.addField("venue_category_id", categoryKey.get(0).getCategoryName());
				solrDocument.addField("venue_category_key", categoryKey.get(0).getKey());
				solrDocument.addField("venue_subcategory_id", subCategoryKey.get(0).getName());//subcategory
				solrDocument.addField("venue_subcategory_key", subCategoryKey.get(0).getKey());//subcategory
				solrDocument.addField("type", "internal");//subcategory				
				solrDocument.addField("venue_photo_link", venuePhotoLink);
				solrDocument.addField("venue_status", MagicNumbers.ACTIVE_YES);
				solrDocument.addField("conform_venue",
						MagicNumbers.UNCONFORM_VENUE);
				solrDocument.addField("phone_no_flag", MagicNumbers.ACTIVE_YES);
				solrDocument.addField("address_flag", MagicNumbers.ACTIVE_YES);
				solrDocument.addField("website_flag", MagicNumbers.ACTIVE_YES);
				solrDocument.addField("photo_flag", MagicNumbers.ACTIVE_YES);
				solrDocument.addField("comment_flag", MagicNumbers.ACTIVE_YES);
				solrDocument.addField("store_loc", venueLati+","+venueLong);				
				
				venueAddStatus = solrCommonClient.addObjectToSolr(
						UrlConstant.VENUE_URL, solrDocument);
				if (venueAddStatus) {
					mav.addObject("Message", "Venue added sucessful");
					mav.addObject("venue_id", venueId);
					return mav;
				} else {
					mav.addObject(ErrorCode.getCustomeError(1001));
				}
			} else {
				mav.addObject(ErrorCode.getCustomeError(100));
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(3009));
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
		}
		return mav;
	}

	@RequestMapping(value = "/delete-venue-by-id", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView deleteVenueById(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "venue_id", required = true) String venueId,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		logger.info(""+session);
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (userId == null || userId.equals("") || venueId == null
					|| venueId.equals("") ) {
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
				solrDocument = new SolrInputDocument();
				solrCommonClient.deleteObject(UrlConstant.VENUE_URL,
						WaspitSolrQuery.DELETE_VENUE_QUERY + venueId);
				/*solrCommonClient.deleteObject(UrlConstant.PHOTO_URL,
						"content_id:" + venueId);*/
				mav.addObject("Venue Deleted succesfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/search-venue", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView searchVenue(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "query", required = true) String query,
			@RequestParam(value = "venue_id", required = false) String venueId,
			@RequestParam(value = "user_id", required = false) String userId,
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = true) String filterBy,
			@RequestParam(value = "sort_order", required = true) String sortOrder,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassVenue pass = new PassVenue();
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);
		
		try {

			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				pass = (PassVenue) solrCommonClient.commonSolrSearch(
						UrlConstant.SEARCH_VENUE_URL, PassVenue.class, query,
						startElementSolr, rowsSolr, filterBy, sortOrder,
						"false");
				if (venueId != null && !venueId.equals("") && userId != null
						&& !userId.equals("")) {
					solrDocument = new SolrInputDocument();
					solrDocument.addField("id", venueId + userId);
					solrDocument.addField("venue_id", venueId);
					solrDocument.addField("user_id", userId);
					solrDocument.addField("added_date",
							SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
					solrCommonClient.addObjectToSolr(
							UrlConstant.VENUE_HISTORY_URL, solrDocument);
				}
				mav.addObject(pass);
				mav.addObject("is_bookmarked", "0");
				mav.addObject("venue_name_flag", "0");
				mav.addObject("venue_phNo_flag", "0");
				mav.addObject("venue_website_flag", "0");
				mav.addObject("venue_category_flag", "0");
				mav.addObject("venue_address_flag", "0");
				mav.addObject("venue_photo_flag", "0");
				mav.addObject("venue_other_flag", "0");
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

	@RequestMapping(value = "/conform-venue", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView conformVenue(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "venue_id", required = true) String venueId,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassVenue pass = null;
		String updateId=null;
		boolean venueAddStatus = false;
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (userId == null || userId.equals("") || venueId == null
					|| venueId.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;
			} else if (solrCommonClient
					.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
							PassSession.class, "user_id:" + userId, 0, 1,
							"user_id", "", "false").getResponse().getDocs()
					.get(0).getUserId() == null) {
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
				pass = (PassVenue) solrCommonClient.commonSolrSearch(
						UrlConstant.SEARCH_VENUE_URL, PassVenue.class,
						"venue_id:" + venueId, 0, 1, "", "", "false");
				if (pass != null) {
					solrDocument = new SolrInputDocument();
					solrDocument.addField("venue_id", venueId);
					solrDocument.addField("user_id", userId);
					solrDocument.addField("venue_name", pass.getResponse()
							.getDocs().get(0).getVenueName().toLowerCase());
					solrDocument.addField("venue_add_date",
							SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
					solrDocument.addField("venue_address", pass.getResponse()
							.getDocs().get(0).getAddress());
					solrDocument.addField("venue_city_id", pass.getResponse()
							.getDocs().get(0).getVenueCityId());
					solrDocument.addField("venue_state_id", pass.getResponse()
							.getDocs().get(0).getVenueStatueId());
					solrDocument.addField("venue_zip_code", pass.getResponse()
							.getDocs().get(0).getVenueZipcode());
					solrDocument.addField("venue_lati", pass.getResponse()
							.getDocs().get(0).getVenueLati());
					solrDocument.addField("venue_long", pass.getResponse()
							.getDocs().get(0).getVenueLong());
					solrDocument.addField("venue_phone_no", pass.getResponse()
							.getDocs().get(0).getVenuePhoneNo());
					solrDocument.addField("hour_Of_Operation", pass
							.getResponse().getDocs().get(0)
							.getHourOfOperation());
					solrDocument.addField("website", pass.getResponse()
							.getDocs().get(0).getWebsite());
					solrDocument.addField("venue_category_id", pass
							.getResponse().getDocs().get(0).getVenueCategory());
					solrDocument.addField("venue_category_key", pass
							.getResponse().getDocs().get(0).getVenueCategoryKey());
					solrDocument.addField("venue_subcategory_id", pass
							.getResponse().getDocs().get(0).getVenueSubCategory());
					solrDocument.addField("venue_subcategory_key", pass
							.getResponse().getDocs().get(0).getVenueSubCategoryKey());
					solrDocument.addField("type", pass
							.getResponse().getDocs().get(0).getType());
					solrDocument
							.addField("venue_photo_link", pass.getResponse()
									.getDocs().get(0).getVenuePhotoLink());
					solrDocument.addField("venue_info", pass.getResponse()
							.getDocs().get(0).getVenueAdditionalInfo());
					solrDocument.addField("venue_status",
							MagicNumbers.ACTIVE_YES);
					solrDocument.addField("conform_venue",
							MagicNumbers.CONFORM_VENUE);					
					solrDocument.addField("store_loc", pass.getResponse()
							.getDocs().get(0).getVenueLati()+","+pass.getResponse()
							.getDocs().get(0).getVenueLong());	
					venueAddStatus = solrCommonClient.addObjectToSolr(
							UrlConstant.VENUE_URL, solrDocument);
					if (venueAddStatus) {
						updateId = UUIDGenrator.getUniqueId();
						solrDocument = new SolrInputDocument();
						solrDocument.addField("id", updateId);
						solrDocument.addField("type",
								UpdateTypeEnum.UpdateType.Feeds.toString());
						solrDocument.addField("subType",
								UpdateTypeEnum.UpdateSubType.Venue.toString());
						solrDocument.addField("property",
								UpdateTypeEnum.VenueUpdateProperty.AddedVenue.toString());
						solrDocument.addField("toUser", userId);
						solrDocument.addField("fromUser", userId);
						solrDocument.addField("venue_id", venueId);
						solrDocument.addField("search_name", pass.getResponse().getDocs().get(0).getVenueName().toLowerCase().trim());
						solrDocument.addField("privacy", MagicNumbers.ACTIVE);
						solrDocument.addField("addedDate",SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
						solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL, solrDocument);
						
						mav.addObject("Message", "Venue Conformed");
						mav.addObject("venue_id", venueId);
						return mav;
					} else {
						mav.addObject(ErrorCode.getCustomeError(1001));
					}

				} else {
					mav.addObject("ErrorMessage", "Wrong venue Id");
				}
			} else {
				mav.addObject(ErrorCode.getCustomeError(100));
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(3009));
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
		}
		return mav;
	}

	@RequestMapping(value = "/get-imageBy-venueId", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getImagesByVenueById(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "venue_id", required = true) String venueId,
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();

		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);
		BigDecimal avgRating = BigDecimal.ZERO;
		String query = null;
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (userId == null || userId.equals("") || venueId == null
					|| venueId.equals("")) {
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
				double averageRating = 0;
				double rating = 0;
				query = "id:" + venueId + " OR content_id:" + venueId;
				PassBinary imageIds = (PassBinary) solrCommonClient
						.commonSolrSearch(UrlConstant.SEARCH_PHOTO_URL,
								PassBinary.class, query, startElementSolr,
								rowsSolr, "id", "", "false");
				query = "venue_id:" + venueId;
				PassReview totalReview = (PassReview) solrCommonClient
						.commonSolrSearch(UrlConstant.SEARCH_REVIEW_URL,
								PassReview.class, query, startElementSolr,
								rowsSolr, "venue_id", "", "false");
				Query mongoQuery = new Query();
				mongoQuery.addCriteria(Criteria.where("venueId").is(venueId));
				List<VenueRating> venueRatingList = (List<VenueRating>) mongoCommonClient
						.findByQuery(mongoQuery, VenueRating.class);
				if (venueRatingList.size() > 0) {
					rating = venueRatingList.get(0).getRating();
					int totalNum = totalReview.getResponse().numFound;
					averageRating = (rating / totalNum);
					avgRating = new BigDecimal(averageRating).setScale(2,
							BigDecimal.ROUND_HALF_UP);
				}
				mav.addObject(imageIds);
				mav.addObject("Rating", rating);
				mav.addObject("Average_Rating", avgRating);
				mav.addObject("Photos", imageIds.getResponse().numFound);
				mav.addObject("Total_Review",
						totalReview.getResponse().numFound);
				mav.addObject("Deals", "Undefined");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/delete-all-venue", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView deleteAllVenue(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		boolean reviewDelStatus = false;

		reviewDelStatus = solrCommonClient
				.deleteAllObject(UrlConstant.VENUE_URL);
		if (reviewDelStatus)
			mav.addObject("Venue Delete Succesfully");
		else
			mav.addObject("Issue while deleting review");

		return mav;
	}

	@RequestMapping(value = "/delete-all-venue-history", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView deleteAllVenueHistory(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		boolean reviewDelStatus = false;

		reviewDelStatus = solrCommonClient
				.deleteAllObject(UrlConstant.VENUE_HISTORY_URL);
		if (reviewDelStatus)
			mav.addObject("Venue History Delete Succesfully");
		else
			mav.addObject("Issue while deleting Venue");

		return mav;
	}

	public PassBinary getVenueImageIds(String venueId) {
		String query = "id:" + venueId + " OR content_id:" + venueId;
		if (solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_PHOTO_URL,
				PassBinary.class, query, 0, 10, "id", "", "false") == null)
			return null;
		else
			return (PassBinary) solrCommonClient.commonSolrSearch(
					UrlConstant.SEARCH_PHOTO_URL, PassBinary.class, "id:"
							+ venueId + " OR content_id:" + venueId, 0, 10,
					"id", "", "false");
	}

	@RequestMapping(value = "/test", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView test(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();

		mav.addObject("Remote Address", request.getRemoteAddr());
		mav.addObject("X-FORWARDED-FOR", request.getHeader("X-FORWARDED-FOR"));

		return mav;
	}

	@RequestMapping(value = "/update-venue", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView updateVenue(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "venue_id", required = true) String venueId,
			@RequestParam(value = "venue_name", required = false) String venueName,
			@RequestParam(value = "venue_address", required = false) String address,
			@RequestParam(value = "venue_city_id", required = false) String venueCityId,
			@RequestParam(value = "venue_state_id", required = false) String venueStateId,
			@RequestParam(value = "venue_zip_code", required = false) String venueZipcode,
			@RequestParam(value = "venue_lati", required = false) String venueLati,
			@RequestParam(value = "venue_long", required = false) String venueLong,
			@RequestParam(value = "venue_phone_no", required = false) String venuePhoneNo,
			@RequestParam(value = "hour_Of_Operation", required = false) String hourOfOperation,
			@RequestParam(value = "website", required = false) String website,
			@RequestParam(value = "venue_category_id", required = false) String venueCategoryId,
			@RequestParam(value = "venue_photo_link", required = false) String venuePhotoLink,
			@RequestParam(value = "venue_info", required = false) String venueInfo,
			@RequestParam(value = "updatedVals", required = true) String updatedVals,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		boolean venueUpdateStatus = false;
		String updateId = null;
		String updateRecordId = null;
		String query = null;

		UpdateRecord updateRecord = null;
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (userId == null || userId.equals("") || venueName == null
					|| venueName.equals("") || venueCityId == null
					|| venueCityId.equals("") || venueStateId == null
					|| venueStateId.equals("") || venuePhoneNo == null
					|| venuePhoneNo.equals("")) {
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
				solrDocument = new SolrInputDocument();
				solrDocument.addField("venue_id", venueId);
				solrDocument.addField("user_id", userId);
				solrDocument.addField("venue_name", venueName);
				solrDocument.addField("venue_add_date",
						SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
				solrDocument.addField("venue_address", address);
				solrDocument.addField("venue_city_id", venueCityId);
				solrDocument.addField("venue_state_id", venueStateId);
				solrDocument.addField("venue_zip_code", venueZipcode);
				solrDocument.addField("venue_lati", venueLati);
				solrDocument.addField("venue_long", venueLong);
				solrDocument.addField("venue_phone_no", venuePhoneNo);
				solrDocument.addField("hour_Of_Operation", hourOfOperation);
				solrDocument.addField("website", website);
				solrDocument.addField("venue_category_id", venueCategoryId);
				solrDocument.addField("venue_photo_link", venuePhotoLink);
				solrDocument.addField("venue_info", venueInfo);
				solrDocument.addField("venue_status", MagicNumbers.ACTIVE_YES);
				solrDocument.addField("conform_venue",
						MagicNumbers.UNCONFORM_VENUE);
				solrDocument.addField("phone_no_flag", MagicNumbers.ACTIVE_YES);
				solrDocument.addField("address_flag", MagicNumbers.ACTIVE_YES);
				solrDocument.addField("website_flag", MagicNumbers.ACTIVE_YES);
				solrDocument.addField("photo_flag", MagicNumbers.ACTIVE_YES);
				solrDocument.addField("comment_flag", MagicNumbers.ACTIVE_YES);
				
				solrDocument.addField("store_loc", venueLati+","+venueLong);	
				
				venueUpdateStatus = solrCommonClient.addObjectToSolr(
						UrlConstant.VENUE_URL, solrDocument);
					// Insert record in mongo
					PassVenue passVenue = solrCommonClient.commonSolrSearch(
							UrlConstant.SEARCH_VENUE_URL, PassVenue.class,
							"venue_id:" + venueId, 0, 10, "", "", "false");
					HashMap<String, Object> map = new HashMap<String, Object>();
					updateRecord = new UpdateRecord();
					updateRecordId = UUIDGenrator.getUniqueId();
					map.put("Venue", passVenue);
					updateRecord.setFeedId(updateId);
					updateRecord.setAddedDate(new Date());
					updateRecord.setMap(map);
					mongoTemplate.insert(updateRecord);
					
					if (venueUpdateStatus) {
						// Create Notification
						/*if (!createVenueUpdateNotification(userId, venueId,
								updatedVals)) {
							mav.addObject(ErrorCode.ERROR_WHILE_PROCESSING_1001);
							return mav;
						}*/
						// Update Venue Of multipleRecord User
						if (!mongoTemplate.collectionExists(UpdateRecord.class)) {
							mongoTemplate.createCollection(UpdateRecord.class);
						}
						String vName=passVenue.getResponse().getDocs().get(0).getVenueName();
						System.out.println(vName+":::::::::::::::::::::::");
						// Create Feed
						updateId = createVenueFeed(updatedVals, userId, venueId,vName);
						if (updateId == null) {
							mav.addObject(ErrorCode.ERROR_WHILE_PROCESSING_1001);
							return mav;
						}
					mav.addObject("Message", "Venue Updated sucessful");
					mav.addObject("venue_id", venueId);
					return mav;
				} else {
					mav.addObject(ErrorCode.getCustomeError(1001));
				}
			} else {
				mav.addObject(ErrorCode.getCustomeError(100));
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(3009));
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
		}
		return mav;
	}

	private String createVenueFeed(String updatedVals, String userId,
			String venueId,String vName) {
		boolean feed = false;
		String updateId = null;
/*		String[] elements = updatedVals.split(",");
		for (String elememnt : elements) {*/
			updateId = UUIDGenrator.getUniqueId();
			solrDocument = new SolrInputDocument();
			solrDocument.addField("id", updateId);
			solrDocument.addField("type", UpdateTypeEnum.UpdateType.Feeds);
			solrDocument.addField("subType",
					UpdateTypeEnum.UpdateSubType.Venue.toString());
			solrDocument.addField("property", updatedVals);
			solrDocument.addField("fromUser", userId);
			solrDocument.addField("toUser", userId);
			solrDocument.addField("venue_id", venueId);
			solrDocument.addField("search_name", vName.toLowerCase().trim());
			solrDocument.addField("privacy", MagicNumbers.ACTIVE);
			solrDocument.addField("addedDate",
					SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
			feed = solrCommonClient.addObjectToSolr(
					UrlConstant.MANAGE_UPDATES_URL, solrDocument);
	//	}
		return updateId;

	}

	private boolean createVenueUpdateNotification(String userId,
			String venueId, String updatedVals) {
		String[] elements = updatedVals.split(",");
		boolean createNotificationStatus = false;
		for (String elememnt : elements) {
			String updateId = UUIDGenrator.getUniqueId();
			solrDocument = new SolrInputDocument();
			solrDocument.addField("id", updateId);
			solrDocument.addField("type",
					UpdateTypeEnum.UpdateType.Notifications);
			solrDocument.addField("subType",
					UpdateTypeEnum.UpdateSubType.Venue.toString());
			solrDocument.addField("property",
					UpdateTypeEnum.VenueUpdateProperty.UpdatedVenue + " For "
							+ elememnt);
			solrDocument.addField("fromUser", userId);
			solrDocument.addField("toUser", userId);
			solrDocument.addField("venue_id", venueId);
			solrDocument.addField("privacy", MagicNumbers.ACTIVE);
			solrDocument.addField("addedDate",
					SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
			createNotificationStatus = solrCommonClient.addObjectToSolr(
					UrlConstant.MANAGE_UPDATES_URL, solrDocument);
		}
		return createNotificationStatus;
	}

	@RequestMapping(value = "/flag-venue", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView flagVenue(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "venue_id", required = true) String venueId,
			@RequestParam(value = "venue_element", required = true) String venueElement,
			@RequestParam(value = "other", required = false) String other,
			@RequestParam(value = "comment", required = true) String comment,
			@RequestParam(value = "is_flagged", required = true) String isFlagged,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if (apikey == null || apikey.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;
		} else if (token == null || token.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;
		} else if (userId == null || userId.equals("") || venueId == null
				|| venueId.equals("") || venueElement == null
				|| venueElement.equals("") || isFlagged == null
				|| isFlagged.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(101));
			return mav;
		} else if (solrCommonClient
				.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
						PassSession.class, "user_id:" + userId, 0, 1,
						"user_id", "", "false").getResponse().getDocs().size() <= 0) {
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
			List<Flag> flagVenueList = new ArrayList<Flag>();
			Query query = new Query();
			query.addCriteria(Criteria
					.where("userId")
					.is(userId)
					.andOperator(
							Criteria.where("typeName")
									.is(UpdateTypeEnum.FlagType.venue_id
											.toString())
									.andOperator(
											Criteria.where("typeValue")
													.is(venueId)
													.andOperator(
															Criteria.where(
																	"subType")
																	.is(venueElement)))));
			flagVenueList = (List<Flag>) mongoCommonClient.findByQuery(query,
					Flag.class);
			if (flagVenueList.size() > 0) {
				Update update = new Update();
				update.set("isFlagged", isFlagged);
				mongoCommonClient.saveOrUpdate(Query.query(Criteria
						.where("userId").is(userId).and("typeName")
						.is(UpdateTypeEnum.FlagType.venue_id.toString())
						.and("typeValue").is(venueId).and("subType")
						.is(venueElement)), Flag.class, update);
				mav.addObject("Venue_Flagged", isFlagged);
				mav.addObject("venue_id", venueId);
				return mav;
			} else {
				Flag flagVenue = new Flag();
				flagVenue.setAdminApproved("1");
				flagVenue.setIsFlagged(isFlagged);
				flagVenue.setUserId(userId);
				flagVenue.setTypeName(UpdateTypeEnum.FlagType.venue_id
						.toString());
				flagVenue.setTypeValue(venueId);
				flagVenue.setSubType(venueElement);
				flagVenue.setComment(comment);
				if (other != null && !other.equals("")) {
					flagVenue.setOther(other);
				}
				mongoTemplate.insert(flagVenue);
				mav.addObject("Venue_Flagged", isFlagged);
				mav.addObject("venue_id", venueId);
				return mav;
			}
		}
		return mav;
	}

	@RequestMapping(value = "/venue-history", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView searchVenueHistory(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = true) String filterBy,
			@RequestParam(value = "sort_order", required = true) String sortOrder,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassVenueHistory passvenueHistory = null;
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);
		String solrQuery = "user_id:" + userId;
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (userId == null || userId.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				passvenueHistory = (PassVenueHistory) solrCommonClient
						.commonSolrSearch(UrlConstant.SEARCH_VENUE_HISTORY_URL,
								PassVenueHistory.class, solrQuery,
								startElementSolr, rowsSolr, filterBy,
								sortOrder, "false");
				mav.addObject(passvenueHistory);
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

	@RequestMapping(value = "/getbookmarked-venues", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getbookmarkedVenues(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "feed_id", required = true) String feedId,

			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
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
				Query mongoQuery = new Query();
				mongoQuery.addCriteria(Criteria.where("userId").is(userId));
				List<BookmarkVenue> bookmarkedVenueList = (List<BookmarkVenue>) mongoCommonClient
						.findByQuery(mongoQuery, BookmarkVenue.class);
				mav.addObject(bookmarkedVenueList);
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

	@RequestMapping(value = "/getupdateRecord-venues", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getupdateRecord(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "feed_Id", required = true) String feedId,
			@RequestParam(value = "user_id", required = true) String userId,

			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String updateRecordId = null;
		if (apikey == null || apikey.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;
		} else if (token == null || token.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(99));
			return mav;

		} else if (feedId == null || feedId.equals("")) {
			mav.addObject(ErrorCode.getCustomeError(104));
			return mav;
		} else if (solrCommonClient
				.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL,
						PassSession.class, "user_id:" + userId, 0, 1,
						"user_id", "", "false").getResponse().getDocs().size() <= 0) {
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
			List<UpdateRecord> updateRecord = null;
			Query query = new Query();
			query.addCriteria(Criteria.where("feedId").is(feedId));
			updateRecord = (List<UpdateRecord>) mongoCommonClient.findByQuery(
					query, UpdateRecord.class);
			if (null != updateRecord && updateRecord.size() > 0) {
				UpdateRecord update = updateRecord.get(0);
				return mav.addObject("Records", update);
			} else {
				mav.addObject("Message", "No Records Found");
			}
		}
		return mav;
	}
	
	
	@RequestMapping(value = "/search-venue-by-loc", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView searchVenueByLoc(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "query", required = true) String query,
			@RequestParam(value = "venue_id", required = false) String venueId,
			@RequestParam(value = "user_id", required = false) String userId,
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = true) String filterBy,
			@RequestParam(value = "sort_order", required = true) String sortOrder,
			@RequestParam(value = "filter_query", required = true) String fq,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassVenue pass = new PassVenue();
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);

		try {

			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				pass = (PassVenue) solrCommonClient.commonSolrSearchWithDistance(
						UrlConstant.SEARCH_VENUE_URL, PassVenue.class, query,
						startElementSolr, rowsSolr, filterBy, sortOrder,
						"false", fq);
				if (venueId != null && !venueId.equals("") && userId != null
						&& !userId.equals("")) {
					solrDocument = new SolrInputDocument();
					solrDocument.addField("id", venueId + userId);
					solrDocument.addField("venue_id", venueId);
					solrDocument.addField("user_id", userId);
					solrDocument.addField("added_date",
							SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
					solrCommonClient.addObjectToSolr(
							UrlConstant.VENUE_HISTORY_URL, solrDocument);
				}
				mav.addObject(pass);
				mav.addObject("is_bookmarked", "0");
				mav.addObject("venue_name_flag", "0");
				mav.addObject("venue_phNo_flag", "0");
				mav.addObject("venue_website_flag", "0");
				mav.addObject("venue_category_flag", "0");
				mav.addObject("venue_address_flag", "0");
				mav.addObject("venue_photo_flag", "0");
				mav.addObject("venue_other_flag", "0");
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
	@RequestMapping(value = "/share-venue", method = {	RequestMethod.POST, RequestMethod.GET })
	public ModelAndView shareVenue(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "venue_id", required = true) String venueId,
			@RequestParam(value = "venue_userId", required = true) String venueUserId,
			@RequestParam(value = "token", required = true) String token,
			HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		boolean shareVenue=false;
		String newUpdateId=null;
			try{
				if(apikey ==null || apikey.equals("")){				
					mav.addObject(ErrorCode.getCustomeError(104));
					return mav;			
				}else if(token==null || token.equals("")){
					mav.addObject(ErrorCode.getCustomeError(99));
					return mav;					
				}else if(userId ==null || userId.equals("") || venueId ==null || venueId.equals("") || venueUserId==null || venueUserId.equals("")){
					mav.addObject(ErrorCode.getCustomeError(101));
					return mav;						
				}else if(solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().size()==0){				
					mav.addObject(ErrorCode.getCustomeError(99));
					return mav;	
				}else if(!solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_SESSION_URL, PassSession.class, "user_id:"+userId, 0, 1, "token", "", "false").getResponse().getDocs().get(0).getToken().equals(token)){
					mav.addObject(ErrorCode.getCustomeError(99));
					return mav;	
				}else if(apiDao.validateApiKey(apikey)){
					
					PassVenue passVenue=(PassVenue)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_VENUE_URL, PassVenue.class, "venue_id:"+venueId, 0, 1, "", "", "false");
					String venueName=passVenue.getResponse().getDocs().get(0).getVenueName();
					// Create Feed
					solrDocument=new SolrInputDocument();
					newUpdateId=UUIDGenrator.getUniqueId();
					solrDocument.addField("id", newUpdateId);
					solrDocument.addField("type", UpdateTypeEnum.UpdateType.Feeds.toString());
					solrDocument.addField("subType", UpdateTypeEnum.UpdateSubType.Shared.toString());	
					solrDocument.addField("property", UpdateTypeEnum.VenueUpdateProperty.SharedVenue);
					solrDocument.addField("privacy", MagicNumbers.ACTIVE);
					solrDocument.addField("toUser", venueUserId);	
					solrDocument.addField("fromUser",userId );
					solrDocument.addField("search_name", venueName.toLowerCase().trim());
					solrDocument.addField("venue_id", venueId);	
					solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
					shareVenue=solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
					// 		Create Notification
					solrDocument=new SolrInputDocument();
					newUpdateId=UUIDGenrator.getUniqueId();
					solrDocument.addField("id", newUpdateId);
					solrDocument.addField("type", UpdateTypeEnum.UpdateType.Notifications.toString());
					solrDocument.addField("subType", UpdateTypeEnum.UpdateSubType.Shared.toString());	
					solrDocument.addField("property", UpdateTypeEnum.VenueUpdateProperty.SharedVenue);
					solrDocument.addField("privacy", MagicNumbers.ACTIVE);
					if(userId.equals(venueUserId)){
						solrDocument.addField("subTypes",UpdateTypeEnum.UpdateSubTypes.USEROWN);
					}
					solrDocument.addField("toUser",venueUserId);	
					solrDocument.addField("fromUser", userId );
					solrDocument.addField("venue_id", venueId);	
					solrDocument.addField("addedDate", SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
					solrCommonClient.addObjectToSolr(UrlConstant.MANAGE_UPDATES_URL , solrDocument);
					if(shareVenue){
						mav.addObject("Share Venue" , "Share Venue successfully");
						return mav;
					}else{					
						mav.addObject(ErrorCode.getCustomeError(1001));
					}
				}else{				
					mav.addObject(ErrorCode.getCustomeError(100));				
				}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	@RequestMapping(value = "/get-venue-Details", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getFeedDetailsById(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "venueId", required = true) String venueId,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassVenue pass = new PassVenue();
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (userId == null || userId.equals("") || venueId == null
					|| venueId.equals("")) {
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
				String query = "user_id:"+userId+" AND venue_id:"+venueId;
				pass = (PassVenue) solrCommonClient.commonSolrSearch(
						UrlConstant.SEARCH_VENUE_URL, PassVenue.class, query,
						0, 1, "", "",
						"false");
					//			User bookmark on venue
					String bookmark = "userId:"+userId+" AND type:venue AND bookId:"+venueId;
					PassBookmark bookmarkFlag=(PassBookmark)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_BOOKMARK_URL,
							PassBookmark.class, bookmark, 0, 1, "", "", "false");
					// 		 User Flag on venue  
					BasicQuery isFlag=new BasicQuery("{'typeValue' : "+"'"+venueId+"'"+","+"'userId' : "+"'"+userId+"'"+","+"'typeName' : "+"'"+UpdateTypeEnum.FlagType.venue_id.toString()+"'"+"}");
					
					mav.addObject("venueDetails",pass);
					 mav.addObject("bookmarkFlag",bookmarkFlag.getResponse().numFound);
					 mav.addObject("isFlag",mongoTemplate.count(isFlag, Flag.class));
		
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

	@RequestMapping(value = "/search-venue-details", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView searchVenueDetails(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "query", required = true) String query,
			@RequestParam(value = "venue_id", required = false) String venueId,
			@RequestParam(value = "user_id", required = false) String userId,
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = true) String filterBy,
			@RequestParam(value = "sort_order", required = true) String sortOrder,
			@RequestParam(value = "search_string", required = false) String searchString,
			
			@RequestParam(value = "lat", required = true) String lat,
			@RequestParam(value = "long", required = true) String laung,
			
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassVenue pass = new PassVenue();
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);
		PassBookmark passBookMark;
		VenueDetails venueDetails=null;
		BigDecimal avgRating = BigDecimal.ZERO;
		List<VenueDetails> venuede=new ArrayList<VenueDetails>();
		
		FourSquareVenue fourSquareVen=null;
		
		Map<String, String> params = null;
		
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {

				// Start All logic to get data from third party source fourSqusre 
				if( searchString!=null && searchString!=""){
				params = new HashMap<String, String>();
				
				params.put("clientid", foursquareClientId);
				params.put("clientsecret", foursquareClientSecret);
				params.put("ll", lat+","+laung);
				params.put("query", searchString);
				params.put("version", foursquareVersion);
				params.put("m", foursquareM);
				fourSquareVen=(FourSquareVenue)restTemplate.getForObject(UrlConstant.SEARCH_FOUR_SQUARE_VENUE, FourSquareVenue.class , params);

				if(fourSquareVen.getResponse().getVenues().size()>0){
					try{
				for(com.main.interconnection.foursquare.venue.Venue fourSquare:fourSquareVen.getResponse().getVenues()){
					solrDocument = new SolrInputDocument();
					solrDocument.addField("venue_id", fourSquare.getId());
					solrDocument.addField("user_id", "Four-Square");
					solrDocument.addField("venue_name", fourSquare.getName().toLowerCase());
					solrDocument.addField("venue_add_date",	SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
					solrDocument.addField("venue_address", fourSquare.getLocation().getAddress());
					solrDocument.addField("venue_city_id", fourSquare.getLocation().getCity());
					solrDocument.addField("venue_state_id", fourSquare.getLocation().getState());
					solrDocument.addField("venue_zip_code", fourSquare.getLocation().getPostalCode());
					solrDocument.addField("venue_lati", fourSquare.getLocation().getLat());
					solrDocument.addField("venue_long", fourSquare.getLocation().getLng());
					solrDocument.addField("venue_phone_no", "987654321");
					solrDocument.addField("hour_Of_Operation", "Not-Avilable");
					solrDocument.addField("website", "Not-Avilable");
					solrDocument.addField("venue_category_id", fourSquare.getCategories().get(0).getName());
					solrDocument.addField("venue_photo_link", "07B1FF81-5E25-4620-B632-9125B156D4FD");
					solrDocument.addField("venue_info", fourSquare.getCategories().get(0).getShortName());
					solrDocument.addField("venue_status",MagicNumbers.ACTIVE_YES);
					solrDocument.addField("conform_venue" ,MagicNumbers.CONFORM_VENUE);					
					solrDocument.addField("store_loc", fourSquare.getLocation().getLat()+","+fourSquare.getLocation().getLng());	
					solrCommonClient.addObjectToSolr(UrlConstant.VENUE_URL, solrDocument);									
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					}
				}
				pass = (PassVenue) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_VENUE_URL, PassVenue.class, query,	startElementSolr, rowsSolr, filterBy, sortOrder,"false");
				if (venueId != null && !venueId.equals("") && userId != null
						&& !userId.equals("")) {
					solrDocument = new SolrInputDocument();
					solrDocument.addField("id", venueId + userId);
					solrDocument.addField("venue_id", venueId);
					solrDocument.addField("user_id", userId);
					solrDocument.addField("added_date",SolrDateUtil.addDateToSolrWithUtilDate(new Date()));
					solrCommonClient.addObjectToSolr(UrlConstant.VENUE_HISTORY_URL, solrDocument);
				}
				if(pass.getResponse().getDocs().size()>0){
				for(Venue venueLocal: pass.getResponse().getDocs()){	
					venueDetails=new VenueDetails();
					venueDetails.setVenue(venueLocal);
					double averageRating = 0;
					double rating = 0;
					query = "id:" + venueLocal.getVenueId() + " OR content_id:" + venueLocal.getVenueId();
					int imageIds =  solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_PHOTO_URL,PassBinary.class, query, startElementSolr,rowsSolr, "id", "", "false").getResponse().getDocs().size();
					query = "venue_id:" +venueLocal.getVenueId() ;
					PassReview totalReview = (PassReview) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REVIEW_URL,PassReview.class, query, 0,	1, "venue_id", "", "false");
					Query mongoQuery = new Query();
					mongoQuery.addCriteria(Criteria.where("venueId").is(venueLocal.getVenueId() ));
					List<VenueRating> venueRatingList = (List<VenueRating>) mongoCommonClient
							.findByQuery(mongoQuery, VenueRating.class);
					if (venueRatingList.size() > 0) {
						rating = venueRatingList.get(0).getRating();
						int totalNum = totalReview.getResponse().numFound;
						averageRating = (rating / totalNum);
						venueDetails.setAverageRating(averageRating);
						 venueDetails.setRating(rating);
					}
					query="bookId: "+venueLocal.getVenueId() + " AND "+"userId: "+userId;
					passBookMark = (PassBookmark) solrCommonClient.commonSolrSearch(	UrlConstant.SEARCH_BOOKMARK_URL, PassBookmark.class, query,	0, 1, filterBy, "","false");
					 if(passBookMark.getResponse().getDocs().size()>0){						 
						 venueDetails.setIsBookMark("1");
				 }else{
					 venueDetails.setIsBookMark("0");
				 }
					 //			  User Flag on venue  
					BasicQuery isFlag=new BasicQuery("{'typeValue' : "+"'"+venueLocal.getVenueId() +"'"+","+"'userId' : "+"'"+userId+"'"+","+"'typeName' : "+"'"+UpdateTypeEnum.FlagType.venue_id.toString()+"'"+"}");
					if(mongoTemplate.count(isFlag, Flag.class)!=0){
						 venueDetails.setIsflag("1");
					}else{
						 venueDetails.setIsflag("0");
					}
					 venueDetails.setDeals("10");		//	static deal set 10
					 venueDetails.setTotalImage(imageIds);
					 venueDetails.setTotalReview(totalReview.getResponse().getNumFound());
					 venuede.add(venueDetails);
				}
				}
				mav.addObject("numFound",pass.getResponse().getNumFound());
				mav.addObject(venuede);				
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
	
	@RequestMapping(value = "/get-imageBy-venueId-details", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView getImagesByVenueByIdDetails(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "venue_id", required = true) String venueId,
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();

		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);
		BigDecimal avgRating = BigDecimal.ZERO;
		String query = null;
		VenueImageDetails venueImageDetails = null;
		List<VenueImageDetails> details = new ArrayList<VenueImageDetails>();
		
		
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (userId == null || userId.equals("") || venueId == null
					|| venueId.equals("")) {
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
				
				double averageRating = 0;
				double rating = 0;
				query = "id:" + venueId + " OR content_id:" + venueId;
				PassBinary imageIds = (PassBinary) solrCommonClient
						.commonSolrSearch(UrlConstant.SEARCH_PHOTO_URL,
								PassBinary.class, query, startElementSolr,
								rowsSolr, "id", "", "false");
				for(SolrBinaryData imageLocal :imageIds.getResponse().getDocs()){
					venueImageDetails = new VenueImageDetails();
					venueImageDetails.setSolrBinaryData(imageLocal);
					String imageId = imageLocal.getId();
					// 		isFlag
					BasicQuery isFlag=new BasicQuery("{'typeValue' : "+"'"+imageId +"'"+","+"'userId' : "+"'"+userId+"'"+","+"'typeName' : "+"'"+UpdateTypeEnum.FlagType.photo_id.toString()+"'"+"}");
					if(mongoTemplate.count(isFlag, Flag.class)!=0){
						venueImageDetails.setIsflag("1");
					}else{
						venueImageDetails.setIsflag("0");
					}
					
					//		total review
				query = "venue_id:" + venueId;
				PassReview totalReview = (PassReview) solrCommonClient
						.commonSolrSearch(UrlConstant.SEARCH_REVIEW_URL,
								PassReview.class, query, startElementSolr,
								rowsSolr, "venue_id", "", "false");
				Query mongoQuery = new Query();
				mongoQuery.addCriteria(Criteria.where("venueId").is(venueId));
				List<VenueRating> venueRatingList = (List<VenueRating>) mongoCommonClient
						.findByQuery(mongoQuery, VenueRating.class);
				if (venueRatingList.size() > 0) {
					rating = venueRatingList.get(0).getRating();
					int totalNum = totalReview.getResponse().numFound;
					averageRating = (rating / totalNum);
					avgRating = new BigDecimal(averageRating).setScale(2,
							BigDecimal.ROUND_HALF_UP);
				}
				venueImageDetails.setRating(rating);
				venueImageDetails.setAverageRating(averageRating);
				venueImageDetails.setTotalReview(totalReview.getResponse().numFound);
				venueImageDetails.setDeals(10);//static deals set 10
				details.add(venueImageDetails);
				}
				mav.addObject("numFound",imageIds.getResponse().numFound);
				mav.addObject("passBinary",details);
				return mav;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/venue-history-details", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView searchVenueHistoryDetails(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = false) String filterBy,
			@RequestParam(value = "sort_order", required = false) String sortOrder,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassVenueHistory passvenueHistory = null;
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);
		String solrQuery = "user_id:" + userId;
		VenueHistoryDetails2 historyDetails=null;
		String query=null;
		PassBookmark passBookMark=null;
		List<VenueHistoryDetails2> historyDetailsList=new ArrayList<VenueHistoryDetails2>();
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (userId == null || userId.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(101));
				return mav;
			} else if (apiDao.validateApiKey(apikey)) {
				passvenueHistory = (PassVenueHistory) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_VENUE_HISTORY_URL,PassVenueHistory.class, solrQuery,	startElementSolr, rowsSolr, "",sortOrder, "false");
				if(passvenueHistory.getResponse().getDocs().size()>0){
				for(VenueHistoryDetails venueHistoryLocal: passvenueHistory.getResponse().getDocs()){	
					historyDetails=new VenueHistoryDetails2();
					String VenueID=venueHistoryLocal.getVenueId();
					historyDetails.setVenueHistoryDetails(venueHistoryLocal);
					double averageRating = 0;
					double rating = 0;
				
					query = "id:" + VenueID + " OR content_id:" + VenueID;
					int imageIds =  solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_PHOTO_URL,PassBinary.class, query, startElementSolr,rowsSolr, "id", "", "false").getResponse().getDocs().size();
					query = "venue_id:" +VenueID ;
					//			Venue Object
					PassVenue	passVenue = (PassVenue) solrCommonClient.commonSolrSearch(	UrlConstant.SEARCH_VENUE_URL, PassVenue.class, query,	0, 1, "", "","false");
					historyDetails.setVenue(passVenue.getResponse().getDocs().get(0));
					// 			Total Review
					PassReview totalReview = (PassReview) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REVIEW_URL,PassReview.class, query, 0,	1, "venue_id", "", "false");
					Query mongoQuery = new Query();
					mongoQuery.addCriteria(Criteria.where("venueId").is(VenueID ));
					List<VenueRating> venueRatingList = (List<VenueRating>) mongoCommonClient
							.findByQuery(mongoQuery, VenueRating.class);
					if (venueRatingList.size() > 0) {
						rating = venueRatingList.get(0).getRating();
						int totalNum = totalReview.getResponse().numFound;
						averageRating = (rating / totalNum);
						historyDetails.setAverageRating(averageRating);
						historyDetails.setRating(rating);
					}
					//		User Bookmark flag
					query="bookId: "+VenueID + " AND "+"userId: "+userId;
					passBookMark = (PassBookmark) solrCommonClient.commonSolrSearch(	UrlConstant.SEARCH_BOOKMARK_URL, PassBookmark.class, query,	0, 1, filterBy, "","false");
					 if(passBookMark.getResponse().getDocs().size()>0){						 
						 historyDetails.setIsBookMark("1");
				 }else{
					 historyDetails.setIsBookMark("0");
				 }
					 //			  User Flag on venue  
					BasicQuery isFlag=new BasicQuery("{'typeValue' : "+"'"+VenueID +"'"+","+"'userId' : "+"'"+userId+"'"+","+"'typeName' : "+"'"+UpdateTypeEnum.FlagType.venue_id.toString()+"'"+"}");
					if(mongoTemplate.count(isFlag, Flag.class)!=0){
						historyDetails.setIsflag("1");
					}else{
						historyDetails.setIsflag("0");
					}
					historyDetails.setDeals("10");		//	static deal set 10
					//		Total Images
					historyDetails.setTotalImage(imageIds);
					//		Toal Reviews
					historyDetails.setTotalReview(totalReview.getResponse().getNumFound());
					historyDetailsList.add(historyDetails);
				}
			}
				mav.addObject("numFound",passvenueHistory.getResponse().getNumFound());
				mav.addObject(historyDetailsList);				
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


	
	
}
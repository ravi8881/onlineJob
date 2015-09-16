package com.main.interconnection.client;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.main.interconnection.clientBo.BookMarkDetails;
import com.main.interconnection.clientBo.BookmarkVenue;
import com.main.interconnection.clientBo.ErrorCode;
import com.main.interconnection.clientBo.Flag;
import com.main.interconnection.clientBo.VenueRating;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.mongoDao.MongoCommonClient;
import com.main.interconnection.solr.response.binary.PassBinary;
import com.main.interconnection.solr.response.bookmark.PassBookmark;
import com.main.interconnection.solr.response.deals.PassDeals;
import com.main.interconnection.solr.response.review.PassReview;
import com.main.interconnection.solr.response.session.PassSession;
import com.main.interconnection.solr.response.venue.PassVenue;
import com.main.interconnection.solrDao.SolrCommonClient;
import com.main.interconnection.solrUtil.UrlConstant;
import com.main.interconnection.util.MagicNumbers;
import com.main.interconnection.util.UUIDGenrator;
import com.main.interconnection.util.UpdateTypeEnum;

@Controller
@RequestMapping(value = "/bookmark/*")
public class InterconnectionClientBookmark {
	private static final Logger logger = LoggerFactory
			.getLogger(InterConnectionClientVenue.class);

	@Autowired
	SolrCommonClient solrCommonClient;

	@Autowired
	ApiDao apiDao;

	@Autowired
	MongoCommonClient mongoCommonClient;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	SolrInputDocument solrDocument;

	@RequestMapping(value = "/bookmark-venue", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView bookmarkVenue(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "bookId", required = true) String bookId,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "venue_userId", required = false) String venue_userId,
			@RequestParam(value = "bookmarkStatus", required = true) String bookmarkStatus,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String newUpdateId = null;
		boolean bookmark = false;
		PassDeals passDeals=null;
		PassVenue passVenue=null;
		try {
			if (apikey == null || apikey.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(104));
				return mav;
			} else if (token == null || token.equals("")) {
				mav.addObject(ErrorCode.getCustomeError(99));
				return mav;
			} else if (userId == null || userId.equals("") || bookId == null
					|| bookId.equals("")) {
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
				String query = "userId:" + userId + " AND bookId:" + bookId;
				if (bookmarkStatus.equals("0") || bookmarkStatus.length() == 0) {
					solrCommonClient.deleteObject(UrlConstant.BOOKMARK_URL,
							query);
					mav.addObject("Bookmark", "UnBookmark successfully");
				} else {
					if (solrCommonClient
							.commonSolrSearch(UrlConstant.SEARCH_BOOKMARK_URL,
									PassBookmark.class, query, 0, 1, "token",
									"", "false").getResponse().getDocs().size() <= 0) {
						solrDocument = new SolrInputDocument();
						newUpdateId = UUIDGenrator.getUniqueId();
						solrDocument.addField("id", newUpdateId);
						solrDocument.addField("bookId", bookId);
						solrDocument.addField("userId", userId);
						solrDocument.addField("fromUser", userId);
						solrDocument.addField("type", type);
						solrDocument.addField("bookmark_status", bookmarkStatus);
						if(type.equals("Deals")){
							passDeals=(PassDeals)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_INTERNAL_DEALS_URL, PassDeals.class, "deal_uuid:"+bookId, 0, 1, "", "", "false");
							solrDocument.addField("searchName",passDeals.getResponse().getDocs().get(0).getDealName());
						}else if(type.equals("venue")){
							passVenue=(PassVenue)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_VENUE_URL, PassVenue.class, "venue_id:"+bookId, 0, 1, "", "", "false");
							solrDocument.addField("searchName",passVenue.getResponse().getDocs().get(0).getVenueName());
						}
						solrDocument.addField("addedDate", new Date());
						solrDocument.addField("privacy", MagicNumbers.ACTIVE);
						bookmark = solrCommonClient.addObjectToSolr(UrlConstant.BOOKMARK_URL, solrDocument);
						if (bookmark) {
							mav.addObject("Bookmark","Added as Bookmarked successfully");
							return mav;
						} else {
							mav.addObject(ErrorCode.getCustomeError(1001));
						}
					} else {
						mav.addObject(ErrorCode.getCustomeError(100));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject(ErrorCode.getCustomeError(1002));
		}
		return mav;
	}

	@RequestMapping(value = "/delete-all-bookmark", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView deleteAllBookmark(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		boolean replyBookStatus = false;

		replyBookStatus = solrCommonClient
				.deleteAllObject(UrlConstant.BOOKMARK_URL);
		if (replyBookStatus)
			mav.addObject("Bookmark Delete Succesfully");
		else
			mav.addObject("Issue while deleting Bookmarked");

		return mav;
	}

	@RequestMapping(value = "/search-bookmark", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView searchBookmark(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = true) String filterBy,
			@RequestParam(value = "sort_order", required = true) String sortOrder,
			@RequestParam(value = "query", required = false) String query,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassBookmark passBookmark = new PassBookmark();
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);
		passBookmark = (PassBookmark) solrCommonClient.commonSolrSearch(
				UrlConstant.SEARCH_BOOKMARK_URL, PassBookmark.class, query,
				startElementSolr, rowsSolr, filterBy, sortOrder, "false");
		mav.addObject(passBookmark);
		return mav;
	}

	@RequestMapping(value = "/search-bookmark-details", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView searchBookmarkDetails(
			@RequestParam(value = "api_key", required = true) String apikey,
			@RequestParam(value = "start_element", required = true) String startElement,
			@RequestParam(value = "rows", required = true) String rows,
			@RequestParam(value = "filter_by", required = true) String filterBy,
			@RequestParam(value = "sort_order", required = true) String sortOrder,
			@RequestParam(value = "query", required = true) String query,
			@RequestParam(value = "userId", required = true) String userId,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		PassBookmark passBookmark = new PassBookmark();
		BookMarkDetails bookMarkDetails = null;
		int startElementSolr = Integer.parseInt(startElement);
		int rowsSolr = Integer.parseInt(rows);
		List<BookMarkDetails> venuede = new ArrayList<BookMarkDetails>();
		passBookmark = (PassBookmark) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_BOOKMARK_URL, PassBookmark.class, query,startElementSolr, rowsSolr, filterBy, sortOrder, "false");

		for (BookmarkVenue bookmarkLocal : passBookmark.getResponse().getDocs()) {
			bookMarkDetails = new BookMarkDetails();
			bookMarkDetails.setBookmarkVenue(bookmarkLocal);
			String venueID=bookmarkLocal.getBookId();
			double averageRating = 0;
			double rating = 0;
			//		total Image count
			query = "id:" + venueID+ " OR content_id:"+ venueID;
			int imageIds = solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_PHOTO_URL,PassBinary.class, query, startElementSolr,rowsSolr, "id", "", "false").getResponse().getDocs().size();
			bookMarkDetails.setTotalImage(imageIds);

			query = "venue_id:" + venueID;
			PassReview totalReview = (PassReview) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REVIEW_URL,PassReview.class, query, 0, 1, "venue_id", "","false");
			Query mongoQuery = new Query();
			mongoQuery.addCriteria(Criteria.where("venueId").is(venueID));
			List<VenueRating> venueRatingList = (List<VenueRating>) mongoCommonClient.findByQuery(mongoQuery, VenueRating.class);
			if (venueRatingList.size() > 0) {
				rating = venueRatingList.get(0).getRating();
				int totalNum = totalReview.getResponse().numFound;
				averageRating = (rating / totalNum);
			//		averageRating count
				bookMarkDetails.setAverageRating(averageRating);
			//		rating count
				bookMarkDetails.setRating(rating);
			}
			//		Bookmark count
			String bookQuery = "bookId: " + venueID+ " AND " + "userId: "+ userId;
			passBookmark = (PassBookmark) solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_BOOKMARK_URL, PassBookmark.class, bookQuery,startElementSolr, rowsSolr, filterBy, sortOrder, "false");
			if (passBookmark.getResponse().getDocs().size() > 0) {
				bookMarkDetails.setIsBookMark("1");
			} else {
				bookMarkDetails.setIsBookMark("0");
			}
			// User Flag on venue
			BasicQuery isFlag = new BasicQuery("{'typeValue' : " + "'"+ venueID + "'" + "," + "'userId' : " + "'"+ userId+ "'" + "," + "'typeName' : "+ "'" + UpdateTypeEnum.FlagType.venue_id.toString() + "'"+ "}");
			if (mongoTemplate.count(isFlag, Flag.class) != 0) {
				bookMarkDetails.setIsflag("1");
			} else {
				bookMarkDetails.setIsflag("0");
			}
			// static deal set 10
			bookMarkDetails.setDeals("10");
			
			bookMarkDetails.setNumFound(passBookmark.getResponse().getNumFound());
			//			total review count
			String totalReviewQuery = "venue_id:" + venueID;
			int totalRe = solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_REVIEW_URL,PassReview.class, totalReviewQuery, 0, 1,"", "", "false").getResponse().numFound;
			bookMarkDetails.setTotalReview(totalRe);
			
			if(bookmarkLocal.getType().equals("venue"))
			{
				totalReviewQuery = "venue_id:" + venueID;
				PassVenue passVenue =solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_VENUE_URL,PassVenue.class, totalReviewQuery, 0, 1,"", "", "false");
				if(passVenue.getResponse().getDocs().size()>0){
				bookMarkDetails.setVenue(passVenue.getResponse().getDocs().get(0));
				}
			}else {
				String dealQuery="deal_uuid:"+venueID;
				PassDeals passDeal=(PassDeals)solrCommonClient.commonSolrSearch(UrlConstant.SEARCH_INTERNAL_DEALS_URL,PassDeals.class, dealQuery, 0, 1,"", "", "false");
				if(passDeal.getResponse().getDocs().size()>0){
					bookMarkDetails.setDeal(	passDeal.getResponse().getDocs().get(0));
				}
			}
			venuede.add(bookMarkDetails);
		}
		mav.addObject(venuede);
		return mav;
	}
}
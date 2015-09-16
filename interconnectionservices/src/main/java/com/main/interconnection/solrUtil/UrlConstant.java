package com.main.interconnection.solrUtil;

public class UrlConstant {

	 public static final int DEFAULT_START = 0;
	    public static final int DEFAULT_END = 100;
	    public static final String hostIp = "localhost";	    
	    public static final String port = "8983";

	    public static final String SOLR_HTTP_URL = "http://" + hostIp + ":"+port;
	    public static final String VENUE_URL = "http://" + hostIp + ":"+port+"/solr/venue/";
	    public static final String REVIEW_URL = "http://" + hostIp + ":"+port+"/solr/review/";
	    public static final String PHOTO_URL = "http://" + hostIp + ":"+port+"/solr/photo/";
	    public static final String SESSION_URL = "http://" + hostIp + ":"+port+"/solr/session/";
	    public static final String USERS_URL = "http://" + hostIp + ":"+port+"/solr/users/";	    
	    public static final String REQUEST_URL = "http://" + hostIp + ":"+port+"/solr/request/";	    
	    public static final String COMMENT_URL = "http://" + hostIp + ":"+port+"/solr/comment/";
	    public static final String REPLY_URL = "http://" + hostIp + ":"+port+"/solr/reply/";
	    public static final String HELPFUL_URL = "http://" + hostIp + ":"+port+"/solr/helpful/";	    
	    public static final String MANAGE_UPDATES_URL = "http://" + hostIp + ":"+port+"/solr/manage-fed-not/";	    
	    public static final String FEED_COMMENT_URL = "http://" + hostIp + ":"+port+"/solr/manage-fed-not/";	    
	    public static final String VENUE_HISTORY_URL = "http://" + hostIp + ":"+port+"/solr/venue-history/";	    
	    public static final String CONTENT_URL = "http://" + hostIp + ":"+port+"/solr/content/";
	    
	    public static final String USER_LOC_URL = "http://" + hostIp + ":"+port+"/solr/user_loc/";
	    
	    public static final String INTERNAL_DEALS_URL = "http://" + hostIp + ":"+port+"/solr/deals/";
	 
	    public static final String MANAGE_UPDATES_STATUS_URL = "http://" + hostIp + ":"+port+"/solr/manage-fed-not-status/";	
	 
	    public static final String IMAGE_URL = "http://" + hostIp + ":"+port+"/solr/image/";
	    // Bookmark
	    public static final String BOOKMARK_URL = "http://" + hostIp + ":"+port+"/solr/bookmark/";
	    
	    public static final String VENUE_FOR_DEALS = "http://" + hostIp + ":"+port+"/solr/deals-venue/";
	        
	   // https://partner-api.groupon.com/deals.json?tsToken=3ba69902092a28fbde0e411e8b90e43a42336b3e&division_id=amarillo&offset=0&limit=1
	    
	    public static final String DEALS = "https://partner-api.groupon.com/deals.json?tsToken={token}&division_id={id}&offset={offset}&limit={limit}";
	    
	    public static final String YELP_DEALS = "http://api.yelp.com/v2/search";
	    
	    
	    public static final String FOUR_SQUARE_VENUE = "https://api.foursquare.com/v2/venues/search?";  	
	    
	    
	    public static final String FOUR_SQUARE_VENUE_PHOTOS ="https://api.foursquare.com/v2/venues/{venueid}/photos?oauth_token={authToken}&v={version}";
	    
	    public static final String FOUR_SQUARE_VENUE_SIMILAR="https://api.foursquare.com/v2/venues/{venueid}/similar?oauth_token={authToken}&v={version}";
	    
	    public static final String FOUR_SQUARE_TIPS ="https://api.foursquare.com/v2/venues/{venueid}/tips?sort=recent&oauth_token={outhToken}&v={version}";
	    
	    public static final String FOUR_SQUARE_FRIENDS ="https://api.foursquare.com/v2/users/{userid}/friends?oauth_token={outhToken}&v={version}";
	    
	    
	    // https://partner-api.groupon.com/deals.json?tsToken=US_AFF_0_201236_200897_0&lat=45.0&lng=-32.0&offset=0&limit=50
	    public static final String DEALS_LAT_LNG = "https://partner-api.groupon.com/deals.json?tsToken={token}&lat={lat}&lng={lng}&offset={offset}&limit={limit}";
	    
	    public static final String SEARCH_VENUE_URL = VENUE_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_REVIEW_URL = REVIEW_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_PHOTO_URL = PHOTO_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_SESSION_URL = SESSION_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_USERS_URL = USERS_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_REQUEST_URL = REQUEST_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_COMMENT_URL = COMMENT_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}"; 
	    
	    public static final String SEARCH_REPLY_URL = REPLY_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_HELPFUL_URL = HELPFUL_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_MANAGE_UPDATES_URL = MANAGE_UPDATES_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_MANAGE_UPDATES_STATUS_URL = MANAGE_UPDATES_STATUS_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_FEED_COMMENT_URL = FEED_COMMENT_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_VENUE_HISTORY_URL = VENUE_HISTORY_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_CONTENT_URL = CONTENT_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_IMAGE_URL = IMAGE_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_BOOKMARK_URL = BOOKMARK_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_INTERNAL_DEALS_URL = INTERNAL_DEALS_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_FOUR_SQUARE_VENUE = FOUR_SQUARE_VENUE + "client_id={clientid}&client_secret={clientsecret}&ll={ll}&query={query}&v={version}&m={m}";
	    
	    public static final String SEARCH_USER_LOC_URL = USER_LOC_URL + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String SEARCH_VENUE_FOR_DEALS = VENUE_FOR_DEALS + "select/?q={q}&version=2.2&start={start}&rows={rows}&indent=on&wt=json&fl={fl}&sort={sort}";
	    
	    public static final String DEALS_QUERY_URL = "https://partner-api.groupon.com/deals.json?tsToken={token}&lat={lat}&lng={lng}&offset={offset}&limit={limit}&query={id}&locale=en_US";
	    
	    // https://partner-api.groupon.com/deals.json?tsToken=US_AFF_0_201236_212556_0&lat=45.0&lng=-32.0&filters=category:food-and-drink&category2=contractors&category3=contractors&offset=0&limit=50
	    public static final String DEALS_BY_CATEGORY = "https://partner-api.groupon.com/deals.json?tsToken={tstoken}&lat={lat}&lng={lng}&filters=category:{cat1}&category2={cat2}&category3={cat3}&offset={offset}&limit={limit}";
	    
	    public static final String GOOGLE_PLUS_FRIENDS = "https://www.googleapis.com/plus/v1/people/me/people/visible";    
	    
}

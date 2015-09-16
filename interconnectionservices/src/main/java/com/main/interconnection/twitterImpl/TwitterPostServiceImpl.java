package com.main.interconnection.twitterImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.social.twitter.api.StatusDetails;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import com.main.interconnection.twitter.TwitterPostService;
import com.main.interconnection.util.TweetCache;
import com.main.interconnection.util.TweetComparator;

public class TwitterPostServiceImpl implements TwitterPostService {
	
	private static final Logger logger = LoggerFactory.getLogger(TwitterPostServiceImpl.class);

	@Override
	public String updateStatus(String accesstoken,String accessSecret, String status ,String twitterConsumerKey , String twitterConsumerSec) {
		Twitter twitter = new TwitterTemplate(twitterConsumerKey , twitterConsumerSec , accesstoken, accessSecret);
		TimelineOperations timelineoperation = twitter.timelineOperations();
		Tweet tweet=null;
		try{
			logger.info("status length" + status.length());	
		tweet = timelineoperation.updateStatus(status);
		return ""+tweet.getId();
		}catch (Exception ex){
			ex.printStackTrace();
			return null;
		}		
	}

	@Override
	public String postImage(String accesstoken, String accessSecret, String imageUrl, String status ,String twitterConsumerKey , String twitterConsumerSec) {
		Twitter twitter = new TwitterTemplate(twitterConsumerKey , twitterConsumerSec , accesstoken, accessSecret);	
		TimelineOperations timelineoperation = twitter.timelineOperations();
		Resource photo=null;
		URL url = null;
		try {
			if (imageUrl != null && imageUrl.contains("http")) {
				url = new URL(imageUrl);
				photo = new UrlResource(url);
			} else {
				//for the image on the server
				photo = new FileSystemResource(imageUrl);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		Tweet tweet = timelineoperation.updateStatus(status, photo);
		return ""+tweet.getId();
	}

	@Override
	public Tweet getTweetStatus(String accesstoken,long tweetId ,String twitterConsumerKey , String twitterConsumerSec){
		String[] accessTokens = accesstoken.split("_");
		Twitter twitter = new TwitterTemplate(twitterConsumerKey , twitterConsumerSec, accessTokens[0],accessTokens[1]);
	
		TimelineOperations timelineoperation = twitter.timelineOperations();
		Tweet t = timelineoperation.getStatus(tweetId);
		return t;
	}
	
	@Override
	public List<Tweet> getReplyofTweet(String tweetID, String accesstoken) {
		List<Tweet> replyList = new ArrayList<Tweet>();
		List<Tweet> tweetmentionList = TweetCache.getUserMentionsTweetMap().get(accesstoken);
		List<Tweet> tweetusertimelineList = TweetCache.getUserTweetStatusMap().get(accesstoken);
		Set<Tweet> alltweetlist = new TreeSet<Tweet>(new TweetComparator());
		try {
			alltweetlist.addAll(tweetmentionList);
			alltweetlist.addAll(tweetusertimelineList);
			long matchTweet = Long.parseLong(tweetID);
			Iterator<Tweet> itr = alltweetlist.iterator();
			while (itr.hasNext()) {
				Tweet t = itr.next();
				if (t.getInReplyToStatusId() != null
						&& t.getInReplyToStatusId() == matchTweet) {
					replyList.add(t);
					matchTweet = t.getId();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return replyList;
	}
	
	
	@Override
	public Tweet postReplyTweet(String accesstoken,String comment,String tweetId ,String twitterConsumerKey , String twitterConsumerSec) {
		String[] accessTokens = accesstoken.split("_");
		Twitter twitter = new TwitterTemplate(twitterConsumerKey , twitterConsumerSec, accessTokens[0],accessTokens[1]);
		TimelineOperations timelineoperation = twitter.timelineOperations();
		StatusDetails details = new StatusDetails();
		details.setInReplyToStatusId(Long.parseLong(tweetId));
		Tweet tweet = timelineoperation.updateStatus(comment, details);
		return tweet;
	}
	
	@Override
	public List<Tweet> getMentions(String accesstoken,long tweetId ,String twitterConsumerKey , String twitterConsumerSec){
		String[] accessTokens = accesstoken.split("_");
		Twitter twitter = new TwitterTemplate(twitterConsumerKey , twitterConsumerSec, accessTokens[0],accessTokens[1]);
		TimelineOperations timelineoperation = twitter.timelineOperations();
		List<Tweet> tweetmentionList=null;
		try{
			if(TweetCache.getUserMentionsTweetMap().get(accesstoken)!=null)
				tweetmentionList=TweetCache.getUserMentionsTweetMap().get(accesstoken);
			else
				tweetmentionList= timelineoperation.getMentions(200,tweetId, 0);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return tweetmentionList;
	}
	
	@Override
	public List<Tweet> getReTweet(String accesstoken,long startTweetId ){
		String[] accessTokens = accesstoken.split("_");
		Twitter twitter = new TwitterTemplate("getTWITTER_CONSUMER_KEY()" , "getTWITTER_CONSUMER_SECRET()", accessTokens[0],accessTokens[1]);
		TimelineOperations timelineoperation = twitter.timelineOperations();
		List<Tweet> reTweetList=null;
		try{
			if(TweetCache.getUserReTweetMap().get(accesstoken)!=null)
				reTweetList=TweetCache.getUserReTweetMap().get(accesstoken);
			else
				reTweetList= timelineoperation.getRetweets(startTweetId);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return reTweetList;
	}
	
	@Override
	public List<Tweet> getUserTweet(String accesstoken ,String twitterConsumerKey , String twitterConsumerSec){
		String[] accessTokens = accesstoken.split("_");
		Twitter twitter = new TwitterTemplate(twitterConsumerKey , twitterConsumerSec, accessTokens[0],accessTokens[1]);
		TimelineOperations timelineoperation = twitter.timelineOperations();
		List<Tweet> userTweetList=null;
		try{
			userTweetList= timelineoperation.getUserTimeline(200);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return userTweetList;
	}

	
	@Override
	public List<Tweet> getUserMentionsTweet(String accesstoken ,String twitterConsumerKey , String twitterConsumerSec) {
		String[] accessTokens = accesstoken.split("_");
		Twitter twitter = new TwitterTemplate(twitterConsumerKey , twitterConsumerSec, accessTokens[0],accessTokens[1]);
		TimelineOperations timelineoperation = twitter.timelineOperations();
		List<Tweet> userTweetMentionList=null;
		try{
			userTweetMentionList= timelineoperation.getMentions(200);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return userTweetMentionList;
	}

	public static void main (String[] args){
		String accesstoken="1569615919-zRND9bswpomqN5vC7ZHFphivvKPWrKvVdi9bcii_shHyey1wyPPSQhI8N7PEtaVatVQOAtM9SXgPNA5xg";
		long tweetId=Long.parseLong("382357654695206912");
		String[] accessTokens = accesstoken.split("_");
		Twitter twitter = new TwitterTemplate(
				"uD4iq07kXJjQofCJNpzvbw",
				"vhVIn5efzbWml0xKbmzPwesFUJP4SmNWWqFMjs", accessTokens[0],accessTokens[1]);
	//	String imageUrl="http://www.palolemgreeninn.com/images/1.png"; 
	
		TimelineOperations timelineoperation = twitter.timelineOperations();
		try {
			List<Tweet> tweetmentionList= timelineoperation.getMentions(50,tweetId, 0);
			logger.info("TweetList"+tweetmentionList);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		}

}
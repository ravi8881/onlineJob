package com.main.interconnection.twitter;

import java.util.List;

import org.springframework.social.twitter.api.Tweet;

public interface TwitterPostService {

	String updateStatus(String accesstoken, String accessSecret, String status , String twitterConsumerKey , String twitterConsumerSec);    
	String postImage(String accesstoken,String accessSecret,String imageUrl,String status ,String twitterConsumerKey , String twitterConsumerSec);    
	Tweet getTweetStatus(String accesstoken,long tweetId ,String twitterConsumerKey , String twitterConsumerSec);
	List<Tweet> getReplyofTweet(String tweetID,String accesstoken);
	Tweet postReplyTweet(String accesstoken,String comment,String tweetId ,String twitterConsumerKey , String twitterConsumerSec);
	List<Tweet> getMentions(String accesstoken,long tweetId ,String twitterConsumerKey , String twitterConsumerSec);
	List<Tweet> getReTweet(String accesstoken,long startTweetId);
	List<Tweet> getUserTweet(String accesstoken ,String twitterConsumerKey , String twitterConsumerSec);
	List<Tweet> getUserMentionsTweet(String accesstoken ,String twitterConsumerKey , String twitterConsumerSec);
	
}
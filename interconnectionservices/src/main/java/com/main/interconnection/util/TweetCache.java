package com.main.interconnection.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.social.twitter.api.Tweet;

public class TweetCache {
	private static Map<String,List<Tweet>> userTweetStatusMap=new HashMap<String, List<Tweet>>();
	private static Map<String,List<Tweet>> userMentionsTweetMap=new HashMap<String, List<Tweet>>();
	private static Map<String,List<Tweet>> userReTweetMap=new HashMap<String, List<Tweet>>();
	private static Map<String,List<Tweet>> userFavouritesTweetMap=new HashMap<String, List<Tweet>>();
	
	public static Map<String, List<Tweet>> getUserTweetStatusMap() {
		return userTweetStatusMap;
	}
	public static void setUserTweetStatusMap(
			String accessToken,List<Tweet> userTweetlist) {
		userTweetStatusMap.put(accessToken, userTweetlist);
	}
	public static Map<String, List<Tweet>> getUserMentionsTweetMap() {
		return userMentionsTweetMap;
	}
	public static void setUserMentionsTweetMap(
			String accessToken,List<Tweet> tweetMentionList ) {
		userMentionsTweetMap.put(accessToken, tweetMentionList);
	}
	
	public static Map<String, List<Tweet>> getUserReTweetMap() {
		return userReTweetMap;
	}
	public static void setUserReTweetMap(String accessToken,List<Tweet> userReTweetList) {
		userReTweetMap.put(accessToken, userReTweetList);
	}
	public static Map<String, List<Tweet>> getUserFavouritesTweetMap() {
		return userFavouritesTweetMap;
	}
	public static void setUserFavouritesTweetMap(
			String accessToken,List<Tweet> userFavouritesTweetList) {
		userFavouritesTweetMap.put(accessToken, userFavouritesTweetList);
	}
}
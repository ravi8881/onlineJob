package com.main.interconnection.twitter;

import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.TwitterProfile;

public interface TwitterUserService {	
	public TwitterProfile getUserInfo(String accesstoken , String accessSecret, String twitterConsumerKey , String twitterConsumerSec);
	public CursoredList<TwitterProfile> getUserFriends(String userId, String accesstoken , String accessSecret, String twitterConsumerKey , String twitterConsumerSec);
	public CursoredList<TwitterProfile> getUserFollowers(String userId, String accesstoken , String accessSecret, 
			String twitterConsumerKey , String twitterConsumerSec, long cursor);
	public CursoredList<TwitterProfile> getUsersAllFollowers(String userId, String accesstoken , String accessSecret, 
			String twitterConsumerKey , String twitterConsumerSec);
}
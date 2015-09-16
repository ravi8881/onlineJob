package com.main.interconnection.twitterImpl;

import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.FriendOperations;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.UserOperations;
import org.springframework.social.twitter.api.impl.TwitterTemplate;





import com.main.interconnection.twitter.TwitterUserService;

public class TwitterUserServiceImpl implements TwitterUserService {

	@Override
	public TwitterProfile getUserInfo(String accesstoken , String accessSecret, String twitterConsumerKey , String twitterConsumerSec) {
		TwitterTemplate twitterTemplate = new TwitterTemplate(twitterConsumerKey, twitterConsumerSec, accesstoken, accessSecret);
		UserOperations userOperations=twitterTemplate.userOperations();
		TwitterProfile twitterProfile=userOperations.getUserProfile();
		return twitterProfile;		
	}
	
	@Override
	public CursoredList<TwitterProfile> getUserFriends(String userId, String accesstoken , String accessSecret, String twitterConsumerKey , String twitterConsumerSec) {
		TwitterTemplate twitterTemplate = new TwitterTemplate(twitterConsumerKey, twitterConsumerSec, accesstoken, accessSecret);
		FriendOperations friendOperations = twitterTemplate.friendOperations();
		CursoredList<TwitterProfile> friendList = friendOperations.getFriends();
		return friendList;
	}
	
	@Override
	public CursoredList<TwitterProfile> getUserFollowers(String userId, String accesstoken , String accessSecret, 
			String twitterConsumerKey , String twitterConsumerSec, long cursor) {
		TwitterTemplate twitterTemplate = new TwitterTemplate(twitterConsumerKey, twitterConsumerSec, accesstoken, accessSecret);
		FriendOperations friendOperations = twitterTemplate.friendOperations();
		CursoredList<TwitterProfile> followersList = friendOperations.getFollowersInCursor(cursor);
		return followersList;
	}
	
	@Override
	public CursoredList<TwitterProfile> getUsersAllFollowers(String userId, String accesstoken , String accessSecret, 
			String twitterConsumerKey , String twitterConsumerSec) {
		TwitterTemplate twitterTemplate = new TwitterTemplate(twitterConsumerKey, twitterConsumerSec, accesstoken, accessSecret);
		FriendOperations friendOperations = twitterTemplate.friendOperations();
		CursoredList<TwitterProfile> followersList = friendOperations.getFollowers();
		return followersList;
	}
}
package com.main.interconnection.googleImpl;

import java.util.List;

import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.Activity.PlusObject.Plusoners;
import com.google.api.services.plus.model.Comment;
import com.main.interconnection.google.GooglePostService;
import com.main.interconnection.util.GoogleClient;

public class GooglePostServiceImpl implements GooglePostService {

	@Override
	public Activity readPost(String accesstoken, String googleApiId , String googleApiKey , String googleAppName , String postId) {
		Activity activity = null;
		try {
			activity = GoogleClient.getActivityById(accesstoken , googleApiId ,googleApiKey , googleAppName , postId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activity;
	}

	@Override
	public List<Comment> getCommentsofPost(String accesstoken, String googleAppName , String googleApiId , String googleApiKey  ,String postId) {
		List<Comment> comments = null;
		try {
			comments = GoogleClient.getCommentsByProviderPostId(accesstoken,googleAppName , googleApiId , googleApiKey  ,postId,  20l);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comments;
	}

	@Override
	public Plusoners getLikesofPost(String accesstoken, String postId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Activity> getPosts(String accesstoken ,String googleAppName ,String googleApiId , String googleApiKey) {
		List<Activity> activities = null;
		try {
			activities = GoogleClient.getAppActivities(accesstoken, googleAppName ,googleApiId , googleApiKey , 5l);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activities;
	}
}

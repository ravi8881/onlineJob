package com.main.interconnection.google;

import java.util.List;

import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.Activity.PlusObject.Plusoners;
import com.google.api.services.plus.model.Comment;



public interface GooglePostService {
	
	public Activity readPost(String accesstoken, String googleApiId , String googleApiKey , String googleAppName ,String postId);

	public List<Comment> getCommentsofPost(String accesstoken, String googleAppName , String googleApiId , String googleApiKey , String postId);

	public Plusoners getLikesofPost(String accesstoken,	String postId);
	
	public List<Activity> getPosts(String accesstoken , String googleAppName ,String googleApiId , String googleApiKey);
}

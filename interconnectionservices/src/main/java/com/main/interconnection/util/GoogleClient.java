package com.main.interconnection.util;


import java.util.ArrayList;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.Comment;
import com.google.api.services.plus.model.CommentFeed;

public class GoogleClient {

	public static List<Activity> getAllActivities(String accessToken, String googleAppName , String googleApiId , String googleApiKey ,
			Long maxResults) throws Exception {

		GoogleCredential credential = 
				getGoogleCredential(accessToken , googleApiId , googleApiKey);
		Plus plus = new Plus.Builder(GoogleConstants.TRANSPORT,
				GoogleConstants.JSON_FACTORY, credential).setApplicationName(
						googleAppName).build();
		Plus.Activities.List listActivities = plus
				.activities()
				.list(GoogleConstants.CURRENT_USER_SESSION_KEY,
						GoogleConstants.ACTIVITY_LIST)
				.setMaxResults(maxResults);
		ActivityFeed activityFeed = listActivities.execute();
		List<Activity> activities = activityFeed.getItems();
		List<Activity> response = new ArrayList<Activity>();
		while (activities != null) {
			for (Activity activity : activities) {
				response.add(activity);
			}
			if (activityFeed.getNextPageToken() == null) {
				break;
			}

			listActivities.setPageToken(activityFeed.getNextPageToken());
			activityFeed = listActivities.execute();
			activities = activityFeed.getItems();
		}
		return response;
	}

	public static List<Activity> getActivitiesWithUrls(String accessToken, String googleAppName , String googleApiId , String googleApiKey ,
			Long maxResults) throws Exception {

		GoogleCredential credential = 
				getGoogleCredential(accessToken , googleApiId , googleApiKey);
		Plus plus = new Plus.Builder(GoogleConstants.TRANSPORT,
				GoogleConstants.JSON_FACTORY, credential).setApplicationName(
						googleAppName).build();
		Plus.Activities.List listActivities = plus
				.activities()
				.list(GoogleConstants.CURRENT_USER_SESSION_KEY,
						GoogleConstants.ACTIVITY_LIST)
				.setMaxResults(maxResults);
		ActivityFeed activityFeed = listActivities.execute();
		List<Activity> activities = activityFeed.getItems();
		List<Activity> response = new ArrayList<Activity>();
		while (activities != null) {
			for (Activity activity : activities) {
				try {
					activity.getObject().getAttachments().get(0).getUrl();
				} catch (Exception e) {
					continue;
				}
				response.add(activity);
				if(response.size()>30){
					return response;
				}
			}
			if (activityFeed.getNextPageToken() == null) {
				break;
			}

			listActivities.setPageToken(activityFeed.getNextPageToken());
			activityFeed = listActivities.execute();
			activities = activityFeed.getItems();
		}
		return response;
	}

	public static List<Activity> getAppActivities(String accessToken, String googleAppName ,String googleApiId , String googleApiKey ,
			Long maxResults) throws Exception {

		GoogleCredential credential = 
				getGoogleCredential(accessToken , googleApiId , googleApiKey);
		Plus plus = new Plus.Builder(GoogleConstants.TRANSPORT,
				GoogleConstants.JSON_FACTORY, credential).setApplicationName(
						googleAppName).build();
		
		Plus.Activities.List listActivities = plus
				.activities()
				.list(GoogleConstants.CURRENT_USER_SESSION_KEY,
						GoogleConstants.ACTIVITY_LIST)
				.setMaxResults(maxResults);
			ActivityFeed activityFeed = listActivities.execute();
		List<Activity> activities = activityFeed.getItems();
		List<Activity> response = new ArrayList<Activity>();
		while (activities != null) {
			for (Activity activity : activities) {
				try {
					activity.getObject().getAttachments().get(0).getUrl();
				} catch (Exception e) {
					continue;
				}
				if(activity.getProvider().getTitle().equalsIgnoreCase(googleAppName)){
				response.add(activity);
				}
				if(response.size()>4){
					return response;
				}
			}
			if (activityFeed.getNextPageToken() == null) {
				break;
			}

			listActivities.setPageToken(activityFeed.getNextPageToken());
			activityFeed = listActivities.execute();
			activities = activityFeed.getItems();
		}
		return response;
	}

	public static List<Comment> getCommentsByProviderPostId(String accessToken, String googleAppName , String googleApiId , String googleApiKey ,
			String providerPostId, Long maxResults) throws Exception {
		GoogleCredential credential = 
				getGoogleCredential(accessToken , googleApiId ,  googleApiKey);
		Plus plus = new Plus.Builder(GoogleConstants.TRANSPORT,
				GoogleConstants.JSON_FACTORY, credential).setApplicationName(
						googleAppName).build();

		Plus.Comments.List listComments = plus.comments().list(providerPostId);
		listComments.setMaxResults(maxResults);

		CommentFeed commentFeed = listComments.execute();
		List<Comment> comments = commentFeed.getItems();
		List<Comment> response = new ArrayList<Comment>();
		while (comments != null) {
			for (Comment comment : comments) {
				response.add(comment);

			}

			if (commentFeed.getNextPageToken() == null) {
				break;
			}

			listComments.setPageToken(commentFeed.getNextPageToken());

			commentFeed = listComments.execute();
			comments = commentFeed.getItems();
		}
		return response;
	}
	
	public static GoogleCredential getGoogleCredential(final String accessToken , String googleApiId , String googleApiKey) {
		GoogleCredential credential = new GoogleCredential.Builder()
				.setJsonFactory(GoogleConstants.JSON_FACTORY).setTransport(GoogleConstants.TRANSPORT)
				.setClientSecrets(googleApiId, googleApiKey).build()
				.setAccessToken(accessToken);
		/*GoogleCredential credential = new GoogleCredential.Builder()
		.setJsonFactory(GoogleConstants.JSON_FACTORY).setTransport(GoogleConstants.TRANSPORT)
		.setClientSecrets(GoogleConstants.getCLIENT_ID(), GoogleConstants.getCLIENT_SECRET()).setRequestInitializer((new HttpRequestInitializer(){
            @Override
            public void initialize(HttpRequest request)
                    throws IOException {
                request.getHeaders().put("Authorization", "Bearer " + accessToken);
            }
        })).build();*/
		
		
		return credential;
	}
	
	public static Activity getActivityById(String accessToken, String googleApiId , String googleApiKey , String googleAppName ,String postId) throws Exception{
		GoogleCredential credential = 
			getGoogleCredential(accessToken , googleApiId , googleApiKey);
		Plus plus = new Plus.Builder(GoogleConstants.TRANSPORT,
			GoogleConstants.JSON_FACTORY, credential).setApplicationName(
					googleAppName).build();
		
		return  plus.activities().get(postId).execute();
	}
}

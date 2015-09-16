package com.main.interconnection.googleImpl;


import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.gson.Gson;
import com.main.interconnection.clientBo.GoogleFriends;
import com.main.interconnection.clientBo.GoogleUser;
import com.main.interconnection.google.GoogleUserService;
import com.main.interconnection.util.GoogleConstants;
import com.main.interconnection.util.GoogleRestClient;

public class GoogleUserServiceImpl implements GoogleUserService {
	final Logger logger = Logger.getLogger(getClass().getName());
	
	
	private static final java.io.File DATA_STORE_DIR =
		      new java.io.File(System.getProperty("user.home"), ".store/oauth2_sample");


	@Override
	public GoogleUser getUserInfo(String accesstoken , String googleApiId , String googleApiKey , String userInfoUrl) {
		String userInfo = null;
		GoogleTokenResponse tokenResponse = null;
		try {
			tokenResponse = new GoogleAuthorizationCodeTokenRequest(GoogleConstants.TRANSPORT, GoogleConstants.JSON_FACTORY,googleApiId,googleApiKey, accesstoken, "postmessage").execute();

			userInfo = GoogleRestClient.httpAuthConnection(userInfoUrl,tokenResponse.getAccessToken());
		} catch (Exception e) {
			e.printStackTrace();
		}
		GoogleUser user = new Gson().fromJson(userInfo, GoogleUser.class);
		user.setAccesstoken(tokenResponse.getAccessToken());
		if (tokenResponse.getRefreshToken() != null
				&& tokenResponse.getRefreshToken() != "") {
			logger.info("inside refresh token-->"+tokenResponse.getRefreshToken()) ;
			user.setRefreshToken(tokenResponse.getRefreshToken());
		}
		return user;
	}
	@Override
	public GoogleUser getUserInfoWithAccessToken(String accesstoken , String userInfoUrl) {
		String userInfo = null;
		try {
			userInfo = GoogleRestClient.httpAuthConnection(userInfoUrl,accesstoken);
			
			System.out.println("------------->"+userInfoUrl);
			
		} catch (Exception e) {
			logger.info("Error Occured to get google user info...");
			e.printStackTrace();
		}
		GoogleUser user = new Gson().fromJson(userInfo, GoogleUser.class);
		return user;
	}

	public GoogleUser getCirclesWithAccessToken(String accesstoken , String userInfoUrl) {
		String userInfo = null;
		try {
			userInfo = GoogleRestClient.httpAuthConnection(userInfoUrl,accesstoken);
		} catch (Exception e) {
			logger.info("Error Occured to get google user info...");
			e.printStackTrace();
		}
		GoogleUser user = new Gson().fromJson(userInfo, GoogleUser.class);
		return user;
	}

	@Override
	public boolean validateToken(String accessToken , String googleApiKey , String googleApiId) {
		try {
			GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(GoogleConstants.JSON_FACTORY).setTransport(GoogleConstants.TRANSPORT).setClientSecrets(googleApiId,googleApiKey).build().setAccessToken(accessToken);
				
			Oauth2 oauth2 = new Oauth2.Builder(GoogleConstants.TRANSPORT, GoogleConstants.JSON_FACTORY, credential).build();
			Tokeninfo tokenInfo = oauth2.tokeninfo().setAccessToken(credential.getAccessToken()).execute();
			logger.info("valiadet tokenInfo "+tokenInfo) ;
			
			if (tokenInfo.containsKey("error")) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public String refreshToken(String refreshToken , String googleApiId , String googleApiKey) {
		GoogleTokenResponse tokenResponse = null;
		try {
			tokenResponse = new GoogleRefreshTokenRequest(GoogleConstants.TRANSPORT, GoogleConstants.JSON_FACTORY, refreshToken, googleApiId,googleApiKey).execute();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return tokenResponse.getAccessToken();
	}

	@Override
	public boolean disConnectApp(String accessToken , String disconnectUrl) {
		try {
			GoogleRestClient.httpGETConnection(disconnectUrl+accessToken);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void main(String[] args) {
		GoogleUserService googleUserService=new GoogleUserServiceImpl();
		String clientId="1032098201181.apps.googleusercontent.com";
		String clientSecret="AIzaSyD93TKmPyafcPdH9ewdbp85uvDpyKdxQNU";
		String accessToken="ya29.3ACRI6Spnw0m-qJnD1CYMrEt76JAkMDRFEQsIS1xSEnI3TBL2pipxhttvm4LS-6E8ocnlYZVdktdQQ";
		boolean check=googleUserService.validateToken(accessToken,clientId,clientSecret);
		if(!check)
			accessToken=googleUserService.refreshToken(accessToken,clientId,clientSecret);
		GoogleUser user=googleUserService.getUserInfoWithAccessToken(accessToken, "https://www.googleapis.com/oauth2/v1/userinfo");
		System.out.println("The user info is==="+user.getEmail());
		
		GoogleFriends friends= googleUserService.getUserFriends(accessToken, "https://www.googleapis.com/plus/v1/people/me/people/visible");
		
		System.out.println("---->"+friends.getItems().get(15).getDisplayName());
	}
	
	
	@Override
	public GoogleFriends getUserFriends(String accesstoken,	String userFriendsUrl) {
		String userFriendInfo = null;
		try {
			userFriendInfo = GoogleRestClient.httpAuthConnection(userFriendsUrl,accesstoken);
		} catch (Exception e) {
			logger.info("Error Occured to get google user Friends...");
			e.printStackTrace();
		}
		GoogleFriends friends = new Gson().fromJson(userFriendInfo, GoogleFriends.class);
		return friends;
	}
}

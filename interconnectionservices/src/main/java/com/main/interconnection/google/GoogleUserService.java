package com.main.interconnection.google;

import com.main.interconnection.clientBo.GoogleFriends;
import com.main.interconnection.clientBo.GoogleUser;


public interface GoogleUserService {
	public GoogleUser getUserInfo(String accesstoken , String googleApiId , String googleApiKey , String userInfoUrl);
	
	public boolean validateToken(String accessToken , String googleApiKey , String googleApiId);
	
	public String refreshToken(String refreshToken , String googleApiId , String googleApiKey);
	
	public boolean disConnectApp(String accessToken , String disconnectUrl);
	
	public GoogleUser getUserInfoWithAccessToken(String accesstoken , String userInfoUrl);
	
	public GoogleFriends getUserFriends(String accesstoken , String userFriendsUrl);
}

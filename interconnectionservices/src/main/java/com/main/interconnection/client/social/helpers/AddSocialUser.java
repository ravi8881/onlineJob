package com.main.interconnection.client.social.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.TwitterProfile;

import com.main.interconnection.clientBo.ConnectSocialUser;
import com.main.interconnection.clientBo.FriendRequest;
import com.main.interconnection.clientBo.GoogleUser;
import com.main.interconnection.clientBo.User;
import com.main.interconnection.util.MagicNumbers;

import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactUser;
import fi.foyt.foursquare.api.entities.CompleteUser;
import fi.foyt.foursquare.api.entities.UserGroup;

public class AddSocialUser {
	
	
	private static final Logger logger = LoggerFactory.getLogger(AddSocialUser.class);
	
	private  List<CompleteUser> foursquareFriendEmails= new ArrayList<CompleteUser>();
	/**
	 * set the User BO for register user as an foursquare user
	 * @param completeUser
	 * @return
	 */
	public ConnectSocialUser setFourSquareUser(CompleteUser completeUser) {
		ConnectSocialUser user=new ConnectSocialUser();
		user.setSocialId(completeUser.getId());
		user.setUserName(completeUser.getFirstName()+" "+completeUser.getLastName());
		user.setUserPhoto(completeUser.getPhoto());
		user.setSocialType(SocialTypesEnum.SocialTypes.FourSquar.toString());
		//user.setBirthday(completeUser.);
		user.setUserCity(completeUser.getHomeCity());
		//user.setAboutUs(completeUser.g);
		//user.setUserGeneder(completeUser.getContact().getEmail());
		//user.setMobileNumber(completeUser.getContact().getPhone());
		//user.setSecurityCode(securityCode);
		//user.setDetailsOnEmail(MagicNumbers.EMAIL_DETAILS_YES);
		if(null!=completeUser.getGender()&&"Male".equalsIgnoreCase(completeUser.getGender()))
			user.setUserGeneder(MagicNumbers.USER_MALE);
		else
			user.setUserGeneder(MagicNumbers.USER_FEMALE);
		//user.setIagree(MagicNumbers.IAGREE);
		//user.setPassword("Yellow");
		//user.setEmailverifyStatus(1);
		//user.setMobileverifyStatus(1);
		return user;
	}
	public void setFourSquareFrndDetails(Result<UserGroup> result,
			String accessToken) throws FoursquareApiException{
		if (null != result && result.getResult().getCount() > 0) {
			FourSquareAPI fourSquareAPI=new FourSquareAPI();
			for (CompactUser compactUser : result.getResult().getItems()) {
				Result<CompleteUser> results = fourSquareAPI
						.getFourSquareUserDetails(compactUser.getId(),
								accessToken);
				if (null != results) {
					foursquareFriendEmails.add(results.getResult());
					
				}

			}
			logger.info("====Friends from foursquare are==="+foursquareFriendEmails.toString());
		}
	}
	public static StringBuffer getSolrFrndInQuery(List<CompleteUser> list){
		StringBuffer buffer=new StringBuffer("emailId:(");
		for(CompleteUser user : list){
			if(user!=null&&user.getContact()!=null&&user.getContact().getEmail()!=null){
				buffer.append(user.getContact().getEmail());
				buffer.append(" ");
			}
		}
		buffer.replace(buffer.lastIndexOf(" "), buffer.lastIndexOf(" "),")");
		logger.info("====Solr in query ==="+buffer.toString());
		return buffer;
	}
	public static StringBuffer getSolrFrndInQueryForUserId(List<FriendRequest> list){
		StringBuffer buffer=new StringBuffer("user_id:(");
		for(FriendRequest request : list){
			buffer.append(request.getToUser());
			buffer.append(" ");
		}
		buffer.replace(buffer.lastIndexOf(" "), buffer.lastIndexOf(" "),")");
		logger.info("====Solr in query ==="+buffer.toString());
		return buffer;
	}
	public static StringBuffer getSoclFrindNotFrndOnWaspitQuery(List<User> list,String fromUser){
		StringBuffer buffer=new StringBuffer("from_user:");
		buffer.append(fromUser);
		buffer.append(" * AND * ");
		buffer.append("-to_user:(");
		for(User user : list){
			buffer.append(user.getUserId());
			buffer.append(" ");
		}
		buffer.replace(buffer.lastIndexOf(" "),buffer.lastIndexOf(" "),")");
		logger.info("====Query For get socl friends taht are not friend on waspit==="+buffer.toString());
		return buffer;
	}
	public  List<CompleteUser> getfourSquareFriendsDeatils() {
		return foursquareFriendEmails;
	}
	
	public ConnectSocialUser setTwitterUserUser(TwitterProfile profile) {
		ConnectSocialUser user=new ConnectSocialUser();
		user.setSocialId(String.valueOf(profile.getId()));
		user.setUserName(profile.getName());
		user.setUserPhoto(profile.getProfileImageUrl());
		user.setSocialType(SocialTypesEnum.SocialTypes.Twitter.toString());
		user.setUserCity(profile.getLocation());
		user.setAddedDate(profile.getCreatedDate());
		return user;
	}
	
	public ConnectSocialUser setFacebookUser(com.restfb.types.User fbUser) {
		ConnectSocialUser user=new ConnectSocialUser();
		user.setSocialId(fbUser.getId());
		user.setUserName(fbUser.getName());
		user.setSocialType(SocialTypesEnum.SocialTypes.FaceBook.toString());
		user.setAddedDate(fbUser.getUpdatedTime());
		return user;
	}
	public ConnectSocialUser setGoogleUser(GoogleUser fbUser) {
		ConnectSocialUser user=new ConnectSocialUser();
		user.setSocialId(fbUser.getId());
		user.setUserName(fbUser.getName());
		user.setSocialType(SocialTypesEnum.SocialTypes.Google.toString());
		user.setAddedDate(new Date());
		return user;
	}
}

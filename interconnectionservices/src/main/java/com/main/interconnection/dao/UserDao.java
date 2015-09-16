package com.main.interconnection.dao;


import java.util.List;

import com.main.interconnection.clientBo.User;

public interface UserDao {
	
	public boolean registerUser(User user);
	
	public boolean registerSocialLoginUser(User user);
	
	public User getRegisterUserDetails(String userId);
	
	public User getRegisterUserAllDetails(String userId);
	
	public String getUserIdByEmail(String emailId);
	
	public boolean deleteUser(String userId);
	
	public boolean findUserByEmail(String userEmailId);
	
	public int validateMobileCode(String userId , String activationCode);
	
	public int validateEmailCode(String userId , String activationCode);
	
	public User validateUser(String userEmail , String password);
	
	//Added to validate user by its ID
	public User validateUser(String userId);
	
	public int waspitUserEmailExist(String email) ;
	
	public boolean updateSecurityCode(String emailId, String securityCode);
	
	public int verifySecureCode(String emailId, String securityCode);
	
	public int updateTmpToken(String emailId , String tempToken);
	
	public int verifyEmailIdandTmpToken(String emailId , String tempToken);
	
	public boolean resetPassword(String emailId , String password);
	
	public void resetTempToken(String emailId , String tempToken);

	public int updateUser(User user);

	
	public boolean registerSocialUser(User user);

	
	public int updateExistingUser(User user);
	
	public int updateFacebookUser(User user);
	
	public User verifyActivationStatus(String userId);

	public User getUserByEmailId(String userEmail);//getUserByEmailId(String userEmail)

	public Integer getAttemptLogin(User beUser);//getAttempt of Login
	
	public int updateUserAttemptLogin(User beUser);//updateUserAttemptLogin

	public String getMobileNo(User user);//Mobile No get by using user

	public List<User> findAllUserAttemptMoreZero();// Find all user which more than zero Attempt

}

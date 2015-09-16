package com.main.interconnection.daoImpl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import com.main.interconnection.clientBo.User;
import com.main.interconnection.dao.UserDao;
import com.main.interconnection.util.MagicNumbers;

public class UserDaoImpl implements UserDao {

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	String sql=null;
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public boolean registerUser(User user) {
		sql = "INSERT INTO users " +  "(userId, name, emailId , mobileNo , password , imageUrl , aboutUs , city , gendre , birthDay , iAgree , detailsOnEmail , emailVerifyCode , mobileVerifyCode, state , createDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
		jdbcTemplate = new JdbcTemplate(dataSource);
		try{
			jdbcTemplate.update(sql, new Object[] { user.getUserId(), user.getName(), user.getEmailId(), user.getMobileNumber(), user.getPassword() , user.getImageUrl() , user.getAboutUs() , user.getCity() , user.getGendre() , user.getBirthday() , user.getIagree() , user.getDetailsOnEmail() , user.getEmailVerifyCode()  , user.getMobileVerifyCode(),user.getState(),user.getCreateDate() });
			return true;
		}catch (Exception e) {
			e.printStackTrace();			
		}		
		return false;
	}
	
	@Override
	public User getRegisterUserDetails(String userId) {
		User user=null;
		sql = "SELECT userId, name, password, emailId , mobileNo, gendre, imageUrl, emailVerifyStatus , mobileverifyStatus , emailVerifyCode , mobileVerifyCode, accessToken, detailsOnEmail, isConnected, birthday, mobileNo ,city ,state ,createDate  FROM users WHERE userId = ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);
		try{
			user = (User) jdbcTemplate.queryForObject(sql, new Object[] { userId }, new BeanPropertyRowMapper(User.class));	
		}catch (Exception e) {
		e.printStackTrace();
		}
		return user;
		}
	
	@Override
	public User getRegisterUserAllDetails(String userId) {
		User user=null;
		sql = "select *  from users where userId = ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);
		try{
			user = (User) jdbcTemplate.queryForObject(sql, new Object[] { userId }, new BeanPropertyRowMapper(User.class));	
		}catch (Exception e) {
		e.printStackTrace();
		}
		return user;
		}
	
	@Override
	public String getUserIdByEmail(String emailId) {
		sql = "SELECT userId FROM users WHERE emailId = ?";
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> userIdMapList = new ArrayList<Map<String, Object>>();
		String userId = null;
		try{
			userIdMapList = jdbcTemplate.queryForList(sql, new Object[] { emailId });
			if(userIdMapList.size() > 0) {
				userId = (String) userIdMapList.get(0).get("userId");
			}
		 return userId;
		}catch (Exception e) {
			e.printStackTrace();
			return userId;
		}
	}
	
	@Override
	public boolean deleteUser(String userId) {
		// TODO Auto-generated method stub
		sql = "Delete FROM users WHERE userId = ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);
		int[] type= {Types.VARCHAR};
		try{
			int i = jdbcTemplate.update(sql, new Object[] { userId }, type);
			if(i>0)
				return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean findUserByEmail(String userEmailId) {

		sql = "SELECT userId FROM users WHERE emailId = ?";
		jdbcTemplate = new JdbcTemplate(dataSource);
		String userId=null;
		try{
			userId = (String) jdbcTemplate.queryForObject(sql, new Object[] { userEmailId },String.class);
		 return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		}

	@Override
	public int validateEmailCode(String userId, String activationCode) {
		sql = "UPDATE users SET emailVerifyStatus="+MagicNumbers.ACTIVE_YES+" WHERE userId=? &&  emailVerifyCode=?";
		
		jdbcTemplate = new JdbcTemplate(dataSource);
		int recordUpdate=0;
		try{				
		return jdbcTemplate.update(sql, new Object[] {userId , activationCode});
		}catch (Exception e) {
			e.printStackTrace();
			return recordUpdate;
		}
	}

	@Override
	public int validateMobileCode(String userId, String activationCode) {
		sql = "UPDATE users SET mobileverifyStatus="+MagicNumbers.ACTIVE_YES+" WHERE userId=? &&  mobileVerifyCode=?";
		jdbcTemplate = new JdbcTemplate(dataSource);
		int recordUpdate=0;
		try{				
			return jdbcTemplate.update(sql, new Object[] {userId , activationCode});
		}catch (Exception e) {
			e.printStackTrace();
			return recordUpdate;
		}
	}

	@Override
	public User validateUser(String userEmail, String password) {
		User user=null;
		sql = "SELECT userId ,name, emailId , mobileNo, imageUrl , aboutUs,emailVerifyStatus, mobileverifyStatus FROM users WHERE emailId = ? && password = ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);
		try{
			user = (User) jdbcTemplate.queryForObject(sql, new Object[] {userEmail,password}, new BeanPropertyRowMapper(User.class));	
		}catch (Exception e) {
		e.printStackTrace();
		}
		return user;
	}
	
	@Override
	public int waspitUserEmailExist(String email) {
		sql = "SELECT emailId FROM users WHERE emailId = ?";
		jdbcTemplate = new JdbcTemplate(dataSource);
		String emailId=null;
		try{
			emailId = (String) jdbcTemplate.queryForObject(sql, new Object[] { email },String.class);	
		 return MagicNumbers.ACTIVE_YES;
		}catch (Exception e) {
			e.printStackTrace();
			return MagicNumbers.INACTIVE_NO;
		}
	}
	
	@Override
	public boolean updateSecurityCode(String emailId, String securityCode) {
		sql = "UPDATE users SET securityCode="+"'"+securityCode+"'"+" WHERE emailId=? ";
		jdbcTemplate = new JdbcTemplate(dataSource);		
		try{				
		jdbcTemplate.update(sql, new Object[] {emailId });
		return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		}

	@Override
	public int verifySecureCode(String emailId, String securityCode) {
		sql = "SELECT COUNT(userId) FROM users WHERE emailId=? && securityCode=?";
		jdbcTemplate = new JdbcTemplate(dataSource);		
		try{				
		return jdbcTemplate.queryForInt(sql, new Object[] {emailId , securityCode});
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int updateTmpToken(String emailId , String tempToken) {
		sql = "UPDATE users SET tmpToken="+"'"+tempToken+"'"+" WHERE emailId=? ";
		jdbcTemplate = new JdbcTemplate(dataSource);
		int recordUpdate=0;
		try{				
		return jdbcTemplate.update(sql, new Object[] {emailId});
		}catch (Exception e) {
			e.printStackTrace();
			return recordUpdate;
		}
	}

	@Override
	public int verifyEmailIdandTmpToken(String emailId, String tempToken) {
		sql = "SELECT COUNT(userId) FROM users WHERE emailId=? && tmpToken=?";
		jdbcTemplate = new JdbcTemplate(dataSource);		
		try{				
		return jdbcTemplate.queryForInt(sql, new Object[] {emailId , tempToken});
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public boolean resetPassword(String emailId, String password) {
		sql = "UPDATE users SET password="+"'"+password+"'"+" WHERE emailId=? ";
		jdbcTemplate = new JdbcTemplate(dataSource);		
		try{				
		jdbcTemplate.update(sql, new Object[] {emailId });
		return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void resetTempToken(String emailId, String tempToken) {
		sql = "UPDATE users SET tmpToken="+"'"+tempToken+"'"+" WHERE emailId=? ";
		jdbcTemplate = new JdbcTemplate(dataSource);		
		try{				
		jdbcTemplate.update(sql, new Object[] {emailId });		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int updateUser(User user) {
		int i = 0;
		sql = "UPDATE users SET name=?,emailId=?,mobileNo=?,imageUrl=?,aboutUs=?,city=?,gendre=?,birthDay=?,iAgree=?,detailsOnEmail=?,socialType=?,accessToken=?, password=? where userId= '"
				+ user.getUserId() + "'";
		Object[] objArray = new Object[] { user.getName(), user.getEmailId(),
				user.getMobileNumber(), user.getImageUrl(),
				user.getAboutUs(), user.getCity(), user.getGendre(),
				user.getBirthday(), user.getIagree(), user.getDetailsOnEmail(), 
				user.getSocialType(), user.getAccessToken(), user.getPassword() };
		jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			i = jdbcTemplate.update(sql, objArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	
	@Override
	public boolean registerSocialLoginUser(User user) {
		sql = "INSERT INTO users " +  "(userId) VALUES (?)";
		jdbcTemplate = new JdbcTemplate(dataSource);
		try{
			jdbcTemplate.update(sql, new Object[] { user.getUserId() });
			return true;
		}catch (Exception e) {
			e.printStackTrace();			
		}		
		return false;
	}
	//Check whether user already exist in Waspit database by its ID 
	@Override
	public User validateUser(String userId) {
		User user=null;
		sql = "SELECT * FROM users WHERE userID = ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);
		try{
			user = (User) jdbcTemplate.queryForObject(sql, new Object[] {userId}, new BeanPropertyRowMapper(User.class));	
		}catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	@Override
	public boolean registerSocialUser(User user) {
		sql = "INSERT INTO users " +  "(userId, name, emailId , mobileNo , password , imageUrl , aboutUs , city , gendre , birthDay , iAgree , detailsOnEmail , emailverifyStatus , mobileverifyStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate = new JdbcTemplate(dataSource);
		try{
			jdbcTemplate.update(sql, new Object[] { user.getUserId(), user.getName(), user.getEmailId(), user.getMobileNumber(), user.getPassword() , user.getImageUrl() , user.getAboutUs() , user.getCity() , user.getGendre() , user.getBirthday() , user.getIagree() , user.getDetailsOnEmail() , user.getEmailverifyStatus()  , user.getMobileverifyStatus() });
			return true;
		}catch (Exception e) {
			e.printStackTrace();			
		}		
		return false;
	}
	
	@Override
	public int updateFacebookUser(User user) {
		int i = 0;
		sql = "UPDATE users SET accessToken=? where userId= '"
				+ user.getUserId() + "'";
		Object[] objArray = new Object[] { user.getAccessToken() };
		jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			i = jdbcTemplate.update(sql, objArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	
	
	@Override
	public int updateExistingUser(User user) {
		int i = 0;
		sql = "UPDATE users SET name=?,emailId=?,mobileNo=?,password=?,imageUrl=?,aboutUs=?,city=?,state=?,gendre=?,birthDay=?,iAgree=?,"
				+ "detailsOnEmail=?,socialType=?,accessToken=?,emailVerifyCode=?,mobileVerifyCode=?, isConnected=? ,createDate=? where userId= '"
				+ user.getUserId() + "'";
		Object[] objArray = new Object[] { user.getName(), user.getEmailId(),
				user.getMobileNumber(), user.getPassword(), user.getImageUrl(),
				user.getAboutUs(), user.getCity(), user.getState(), user.getGendre(),
				user.getBirthday(), user.getIagree(), user.getDetailsOnEmail(), 
				user.getSocialType(), user.getAccessToken(), user.getEmailVerifyCode(), 
				user.getMobileVerifyCode(), user.getIsConnected() ,user.getCreateDate() };
		jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			i = jdbcTemplate.update(sql, objArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public User verifyActivationStatus(String userId) {
		User user=null;
		sql = "SELECT name , emailId , emailVerifyStatus , mobileverifyStatus FROM users WHERE userID = ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);
		try{
			user = (User) jdbcTemplate.queryForObject(sql, new Object[] {userId}, new BeanPropertyRowMapper(User.class));	
		}catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	
	/*
	 * @author Name:Rajiv Kumar
	 * @Created Date:15/10/2014
	 * @update Date:15/10/2014
	 * @purpose:getUserByEmailId
	 *  
	 */
	@Override
	public User getUserByEmailId(String userEmail) {
		// TODO Auto-generated method stub
		User user=null;
		sql = "SELECT * FROM users WHERE emailId = ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);
		try{
			user = (User) jdbcTemplate.queryForObject(sql, new Object[] {userEmail}, new BeanPropertyRowMapper(User.class));	
		}catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	/*
	 * @author Name:Rajiv Kumar
	 * @Created Date:15/10/2014
	 * @update Date:15/10/2014
	 * @purpose:getAttemptNumber Attempt
	 *  
	 */
	@Override
	public Integer getAttemptLogin(User beUser) {
		// TODO Auto-generated method stub
		logger.info("Integer getAttemptLogin(User beUser)::::"+UserDaoImpl.class);
		sql = "SELECT attemptNumber FROM users WHERE userId=?";
		jdbcTemplate = new JdbcTemplate(dataSource);		
		try{				
		return jdbcTemplate.queryForInt(sql, new Object[] {beUser.getUserId()});
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	/*
	 * @author Name:Rajiv Kumar
	 * @Created Date:16/10/2014
	 * @update Date:16/10/2014
	 * @purpose:updateUserAttemptLogin ())
	 *  
	 */
	@Override
	public int updateUserAttemptLogin(User beUser) {
		// TODO Auto-generated method stub
		logger.info("updateUserAttemptLogin(User beUser)::::"+UserDaoImpl.class);
		int i = 0;
		sql = "UPDATE users SET attemptNumber=? where userId= '"
				+ beUser.getUserId() + "'";
		Object[] objArray = new Object[] { beUser.getAttemptNumber() };
		jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			i = jdbcTemplate.update(sql, objArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;

	}

	/*
	 * @author Name:Rajiv Kumar
	 * @Created Date:16/10/2014
	 * @update Date:16/10/2014
	 * @purpose:getMobileNo by user
	 *  
	 */
	@Override
	public String getMobileNo(User user) {
		// TODO Auto-generated method stub
		sql = "SELECT mobileNo FROM users u WHERE u.userId = ?";
		jdbcTemplate = new JdbcTemplate(dataSource);
		String mbNumber = (String)jdbcTemplate.queryForObject(sql, new Object[] { user.getUserId() }, String.class);
		return mbNumber;
	}
	/*
	 * @author Name:Rajiv Kumar
	 * @Created Date:17/10/2014
	 * @update Date:17/10/2014
	 * @purpose:Find all user which more than zero Attempt
	 *  
	 */
	@Override
	public List<User> findAllUserAttemptMoreZero() {
		// TODO Auto-generated method stub
		logger.info("List<User> findAllUserAttemptMoreZero()"+UserDaoImpl.class);
		sql = "SELECT * FROM users u WHERE u.attemptNumber > 0";
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<User> userList  = jdbcTemplate.query(sql,new BeanPropertyRowMapper(User.class));
		return userList;

	}
}

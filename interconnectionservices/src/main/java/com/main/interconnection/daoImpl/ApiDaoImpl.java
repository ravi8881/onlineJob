package com.main.interconnection.daoImpl;

import javax.sql.DataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.main.interconnection.clientBo.ApiKey;
import com.main.interconnection.dao.ApiDao;
import com.main.interconnection.util.MagicNumbers;

public class ApiDaoImpl implements ApiDao{

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	String sql=null;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}



	@Override
	public boolean registerApiKey(ApiKey apiKey) {		
		sql = "INSERT INTO api " +  "(developerId, userEmailId, apiKey , apiSalt, addedDate , modifyDate) VALUES (?, ?, ?, ?, ?, ?)";		
		jdbcTemplate = new JdbcTemplate(dataSource);	
		try{
			jdbcTemplate.update(sql, new Object[] { apiKey.getDeveloperKey(), apiKey.getEmailId(), apiKey.getApikey(), apiKey.getApiSalt() , apiKey.getAddeddate(), apiKey.getModifydate() });
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;	
		}
	}
	
	@Override
	public ApiKey findRegisterEmail(String email) {	
		ApiKey apiKey=null;
		sql = "SELECT apiKey , apiSalt FROM api WHERE userEmailId = ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);				
		apiKey = (ApiKey) jdbcTemplate.queryForObject(sql, new Object[] { email }, new BeanPropertyRowMapper(ApiKey.class));	
		return apiKey;
	}



	@Override
	public boolean userEmailExist(String email) {
		sql = "SELECT apiKey FROM api WHERE userEmailId = ?";
		jdbcTemplate = new JdbcTemplate(dataSource);
		String apiKey=null;
		try{
		 apiKey = (String) jdbcTemplate.queryForObject(sql, new Object[] { email },String.class);	
		 return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean validateApiKey(String apiKey) {
		sql = "SELECT apiKey FROM api WHERE apiKey = ? and status= ?";
		jdbcTemplate = new JdbcTemplate(dataSource);		
		String apiKeyDataBase=null;
		try{
			apiKeyDataBase = (String) jdbcTemplate.queryForObject(sql, new Object[] { apiKey , MagicNumbers.API_ENABLE_YES_STATUS },String.class);	
		 return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String getValidationUrl(String apiKey) {
		sql = "SELECT activationLink FROM api WHERE apiKey = ? and status= ?";
		jdbcTemplate = new JdbcTemplate(dataSource);	
		String activationKey=null;
		try{
			return activationKey = (String) jdbcTemplate.queryForObject(sql, new Object[] { apiKey , MagicNumbers.API_ENABLE_YES_STATUS },String.class);
		 	}catch (Exception e) {
			e.printStackTrace();
		return null;
	}
	}


	/*
	 * @author Name:Rajiv Kumar
	 * @Created Date:16/10/2014
	 * @update Date:16/10/2014
	 * @purpose:find MaxLogin Hit Attempt for this Api
	 *  
	 */
	@Override
	public Integer findMaxLoginHit(String apikey) {
		// TODO Auto-generated method stub
		sql = "SELECT maxLoginHit FROM api a where a.apiKey = ?";
		jdbcTemplate = new JdbcTemplate(dataSource);
		try{				
			return jdbcTemplate.queryForInt(sql, new Object[]{apikey});
			}catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
	}

}

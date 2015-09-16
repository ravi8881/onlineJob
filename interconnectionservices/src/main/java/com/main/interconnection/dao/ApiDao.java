package com.main.interconnection.dao;

import com.main.interconnection.clientBo.ApiKey;

public interface ApiDao {

	public boolean registerApiKey(ApiKey apiKey);
	public ApiKey findRegisterEmail(String email);
	public boolean userEmailExist(String email);
	public boolean validateApiKey(String apiKey);	
	public String getValidationUrl(String apiKey);
	public Integer findMaxLoginHit(String apikey);//find MaxLogin Hit
}

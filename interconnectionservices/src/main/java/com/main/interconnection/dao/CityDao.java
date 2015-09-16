package com.main.interconnection.dao;

import java.util.List;

import com.main.interconnection.clientBo.City;


public interface CityDao {

	public List<City> getCityBasedOnStateId(int stateId);
	
	public List<City> getAllCity();
	
	public City getCityDetailsById(int cityId);
	
}

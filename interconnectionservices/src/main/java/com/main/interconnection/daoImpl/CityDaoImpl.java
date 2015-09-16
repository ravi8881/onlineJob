package com.main.interconnection.daoImpl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.main.interconnection.clientBo.City;
import com.main.interconnection.dao.CityDao;
import com.main.interconnection.util.MagicNumbers;

public class CityDaoImpl implements CityDao {
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	String sql=null;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}	

	@Override
	public List<City> getCityBasedOnStateId(int stateId) {
		List<City> cityList=new ArrayList<City>();
		sql = "SELECT cityId , cityName , latitde , longitude FROM city WHERE stateId=? && cityStatus = ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);				
		cityList = (List<City>) jdbcTemplate.query(sql, new Object[] { stateId, MagicNumbers.ACTIVE_YES }, new BeanPropertyRowMapper(City.class));	
		return cityList;		
	}

	@Override
	public List<City> getAllCity() {
		List<City> cityList=new ArrayList<City>();
		sql = "SELECT cityId , cityName FROM city WHERE cityStatus = ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);				
		cityList = (List<City>) jdbcTemplate.query(sql, new Object[] { MagicNumbers.ACTIVE_YES }, new BeanPropertyRowMapper(City.class));	
		return cityList;
	}

	@Override
	public City getCityDetailsById(int cityId) {
		City cityDetails=null;
		sql = "SELECT * FROM city WHERE cityId = ? && cityStatus= ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);				
		cityDetails = (City) jdbcTemplate.queryForObject(sql, new Object[] { cityId, MagicNumbers.ACTIVE }, new BeanPropertyRowMapper(City.class));	
		return cityDetails;
	}

}

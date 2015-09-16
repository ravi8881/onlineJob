package com.main.interconnection.daoImpl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import com.main.interconnection.clientBo.State;
import com.main.interconnection.dao.StateDao;
import com.main.interconnection.util.MagicNumbers;

public class StateDaoImpl implements StateDao{
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	String sql=null;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public List<State> getAllState() {
		List<State> stateList=new ArrayList<State>();
		sql = "SELECT stateId , stateName , latitde , longitude FROM state WHERE stateStatus = ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);				
		stateList = (List<State>) jdbcTemplate.query(sql, new Object[] { MagicNumbers.ACTIVE_YES }, new BeanPropertyRowMapper(State.class));	
		return stateList;

	}

	@Override
	public State getStateDetailsById(int stateId) {
		State stateDetails=null;		
		sql = "SELECT * FROM state WHERE stateId=? && stateStatus = ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);				
		stateDetails = (State) jdbcTemplate.queryForObject(sql, new Object[] { stateId,  MagicNumbers.ACTIVE }, new BeanPropertyRowMapper(State.class));	
		return stateDetails;
	}

}

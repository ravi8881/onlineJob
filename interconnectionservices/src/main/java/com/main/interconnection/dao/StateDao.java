package com.main.interconnection.dao;

import java.util.List;

import com.main.interconnection.clientBo.State;

public interface StateDao {

	public List<State> getAllState();
	
	public State getStateDetailsById(int stateId);
}

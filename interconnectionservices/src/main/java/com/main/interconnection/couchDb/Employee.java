package com.main.interconnection.couchDb;

import java.util.List;

public interface Employee {
	public boolean addDocument(EmployeeBO emp);
	public boolean update(String name,String lastname);
	public List<EmployeeBO> getAllDocuments();
	public Boolean getDelete(String name);
	public boolean getDocumentById(String id);
	
	}

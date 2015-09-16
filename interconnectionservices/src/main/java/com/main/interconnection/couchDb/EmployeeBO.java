package com.main.interconnection.couchDb;

import java.util.Map;

import org.jcouchdb.document.Attachment;
import org.jcouchdb.document.Document;
import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

public class EmployeeBO  {
	private String id;
	private String revision;
	private String empId;
    private String firstName;
    private String lastName;

public EmployeeBO() {
	
}
 

	public EmployeeBO(String empId, String firstName, String lastName) {
	super();
	this.empId = empId;
	this.firstName = firstName;
	this.lastName = lastName;
}


	public EmployeeBO(String firstName, String lastName) {
	this.firstName = firstName;
	this.lastName = lastName;
}


	public String getEmpId() {
		return empId;
	}


	public void setEmpId(String empId) {
		this.empId = empId;
	}


	@JSONProperty(value = "_id", ignoreIfNull = true)
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

    @JSONProperty(value="_rev",ignoreIfNull = true)
    public String getRevision()
    {
        return revision;
    }


    public void setRevision(String revision)
    {
        this.revision = revision;
    }

   

}

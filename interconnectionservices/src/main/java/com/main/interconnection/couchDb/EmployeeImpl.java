package com.main.interconnection.couchDb;

import java.util.ArrayList;
import java.util.List;

import org.jcouchdb.db.Database;
import org.jcouchdb.db.Options;
import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.document.ValueAndDocumentRow;
import org.jcouchdb.document.ViewAndDocumentsResult;



public class EmployeeImpl implements Employee{
	
	final String dbName = "garv";
    Database db = new Database("localhost", dbName);

	@Override
	public List getAllDocuments() {
		List list=new ArrayList();
		  ViewAndDocumentsResult<Object,BaseDocument> docs = db.query("_all_docs", Object.class, BaseDocument.class, new Options(), null, null);

		  for (ValueAndDocumentRow<Object,BaseDocument> r : docs.getRows())
		  {
			 
			 	  System.out.println(r.getDocument().getProperty("empId")+"\t"+r.getDocument().getProperty("firstName")+" \t "+r.getDocument().getProperty("lastName"));
		
			  }
		  
		return list;
	}


	@Override
	public boolean addDocument(EmployeeBO emp) {
	//emp=new EmployeeBO();
	emp.setId(emp.getEmpId());
//	System.out.println(emp.getFirstName());
//	emp.setFirstName(emp.getFirstName());
//	emp.setLastName(emp.getLastName());
//	emp.setProperty(emp.getFirstName(), emp.getLastName());
	
	
	
	db.createDocument(emp);
		return true;
	}


	@Override
	public Boolean getDelete(String name) {
	
		 ViewAndDocumentsResult<Object,BaseDocument> docs = db.query("_all_docs", Object.class, BaseDocument.class, new Options(), null, null);
//		 docs.getProperty(firstName);
//		 System.out.println(firstName);
		 for (ValueAndDocumentRow<Object,BaseDocument> r : docs.getRows())
		  {
			 if(r.getDocument().getId().equals(name)){
				  db.delete(r.getDocument());
			 }
		 }
		return true;
	}


	@Override
	public boolean update(String name,String lastname) {
		EmployeeBO am=new EmployeeBO();
		ViewAndDocumentsResult<Object,BaseDocument> docs = db.query("_all_docs", Object.class, BaseDocument.class, new Options(), null, null);

		  for (ValueAndDocumentRow<Object,BaseDocument> r : docs.getRows())
		  {
			  if(r.getDocument().getId().equals(name)){
			 	 // System.out.println(r.getDocument().getProperty("firstName")+" \t "+r.getDocument().getProperty("lastName"));
		System.out.println(r.getDocument().getId().equals(name));
		r.getDocument().setProperty("lastName", lastname);
				am.setLastName(lastname);
				am.setId(name);
				am.setFirstName("qqqq");
				am.setRevision(r.getDocument().getRevision());
				
				
		db.updateDocument(am);
			  }}
		return true;
	}


	@Override
	public boolean getDocumentById(String id) {
		 		  
	    BaseDocument docs =db.findDocument(BaseDocument.class, "7",null);
	    if(docs.getId().equals(id)){
			 
	    System.out.println(docs.getProperty("empId")+"\t"+docs.getProperty("firstName")+"\t"+docs.getProperty("lastName"));
	}else{
		System.out.println("no document found");
	}
	    return true;
	}


	

}

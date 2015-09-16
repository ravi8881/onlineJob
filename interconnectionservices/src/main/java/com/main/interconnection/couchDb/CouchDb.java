package com.main.interconnection.couchDb;

import org.jcouchdb.db.Database;
import org.jcouchdb.db.Server;

public class CouchDb {
public static void main(String[] args) {
	
	
	 final String dbName = "main";
    Database db = new Database("localhost", dbName);
    // create a database object pointing to the database "mycouchdb" on the local host 
    
  Server server = db.getServer();
     if (!server.listDatabases().contains(dbName))
     {
         System.out.println("Create Database " + dbName);
         server.createDatabase(dbName);
     }
      
     
    //		create Document

    EmployeeImpl eo=new EmployeeImpl();
     //  EmployeeBO bs=new EmployeeBO("7","gahk7u","yup");
    // boolean a=eo.addDocument(bs);
    
   // delete document 
 // boolean bp=eo.getDelete("15");
    
    // update document
//    boolean up=eo.update("15","Gauravchugh");


    // 	get documemt by id
    eo.getDocumentById("7");
  // eo.getAllDocuments();
 }
}

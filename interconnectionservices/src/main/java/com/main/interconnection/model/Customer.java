package com.main.interconnection.model;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;




@XmlRootElement(name = "customer")
public class Customer implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public String name;
	
	public int getId() {
		return id;
	}
	
	@XmlElement
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}
}

package com.main.interconnection.clientBo;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;


@SuppressWarnings("deprecation")
@XmlRootElement(name = "subCategory")
@JsonWriteNullProperties(false)
public class SubCategory {

	private String subcategoryid;
	private String categoryId;
	private String name;
	private String key;
	
	public String getSubcategoryid() {
		return subcategoryid;
	}
	public void setSubcategoryid(String subcategoryid) {
		this.subcategoryid = subcategoryid;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	
}

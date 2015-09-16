package com.main.interconnection.clientBo;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;


@SuppressWarnings("deprecation")
@XmlRootElement(name = "category")
@JsonWriteNullProperties(false)
public class Category {

	private String categoryId;
	private String categoryName;
	private String key;
	
	
	public String getCategoryId() {
		return categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
}

package com.main.interconnection.dao;

import java.util.List;

import com.main.interconnection.clientBo.Category;
import com.main.interconnection.clientBo.SubCategory;

public interface CommonDao {

	
	 public List<Category> getAllCategory();
	 public List<SubCategory> getSubCategory(String categoryId); 
	 public List<SubCategory> getSubCategoryKey(String subCategoryId);
	 public List<Category> getCategoryKey(String categoryId);
}

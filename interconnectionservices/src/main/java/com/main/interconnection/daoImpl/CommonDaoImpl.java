package com.main.interconnection.daoImpl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.main.interconnection.clientBo.Category;
import com.main.interconnection.clientBo.SubCategory;
import com.main.interconnection.dao.CommonDao;
import com.main.interconnection.util.MagicNumbers;

public class CommonDaoImpl implements CommonDao {
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	String sql=null;
	List<Category> categoryList = null;
	List<SubCategory> subCategoriesList = null;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public List<Category> getAllCategory() {
		List<Category> categoryList=new ArrayList<Category>();
		sql = "SELECT * FROM category WHERE categoryStatus = ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);				
		categoryList = (List<Category>) jdbcTemplate.query(sql, new Object[] { MagicNumbers.ACTIVE_YES }, new BeanPropertyRowMapper(Category.class));	
		return categoryList;
	}	
	 public List<SubCategory> getSubCategory(String categoryId) {
		List<SubCategory> subCategoriesList = new ArrayList<SubCategory>();
		sql = "SELECT * FROM sub_category WHERE categoryId = ? and status = ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);				
		subCategoriesList = (List<SubCategory>) jdbcTemplate.query(sql, new Object[] { categoryId ,String.valueOf(MagicNumbers.ACTIVE_YES) }, new BeanPropertyRowMapper(SubCategory.class));	
			return subCategoriesList;
		}

	@Override
	public List<SubCategory> getSubCategoryKey(String subCategoryId) {
		// TODO Auto-generated method stub
		sql = "SELECT * FROM sub_category WHERE subcategoryid = ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);
		subCategoriesList = (List<SubCategory>) jdbcTemplate.query(sql, new Object[] { subCategoryId }, new BeanPropertyRowMapper(SubCategory.class));	
		return subCategoriesList;
	}

	@Override
	public List<Category> getCategoryKey(String categoryId) {
		// TODO Auto-generated method stub
		sql = "SELECT * FROM category WHERE categoryId = ?";				
		jdbcTemplate = new JdbcTemplate(dataSource);
		categoryList = (List<Category>) jdbcTemplate.query(sql, new Object[] { categoryId}, new BeanPropertyRowMapper(Category.class));	
		return categoryList;
	} 

}

package com.main.interconnection.mongoDao;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.main.interconnection.mongoBo.HelpfulReview;



public interface MongoCommonClient {

	public <T> boolean createDocument(Class<T> document, HelpfulReview doc , String collectionName);
	public <T> void deleteObject(String id,String deleteBy,Class<T> cls);
	public <T> void deleteObjectByQuery(Query query,Class<T> cls);
    public <T> Object saveOrUpdate(Query query ,Class<T> cls,Update update);
    public <T> Object findById(String id,String findBy ,Class<T> cls);
    public <T> Object findByQuery(Query query,Class<T> cls);
}

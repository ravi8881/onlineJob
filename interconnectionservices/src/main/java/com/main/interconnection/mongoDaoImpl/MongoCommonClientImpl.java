package com.main.interconnection.mongoDaoImpl;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.main.interconnection.mongoBo.HelpfulReview;
import com.main.interconnection.mongoDao.MongoCommonClient;

@Repository
public class MongoCommonClientImpl implements MongoCommonClient {

	MongoTemplate mongoTemplate;

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public <T> boolean createDocument(Class<T> document, HelpfulReview doc, String collectionName) {
		if (!mongoTemplate.collectionExists(HelpfulReview.class)) {
			mongoTemplate.createCollection(HelpfulReview.class);
		}				
		mongoTemplate.insert(doc, collectionName);
		return true;
	}
    
	//Where delete by is the field for which deletion happen in the passed class
	@Override
	public <T> void deleteObject(String id,String deleteBy,Class<T> cls) {
		mongoTemplate.remove(new Query(Criteria.where(deleteBy).is(id)),cls);
	}
	
	// First Check the document already present then modify, if not present then save new document
	@Override
	public <T> Object saveOrUpdate(Query query, Class<T> cls,Update update) {
		mongoTemplate.upsert(query,update,cls);
		return  (Object) mongoTemplate.findOne(query, cls);
	}
	
	//Where findBy is the unique field for which the records must be find
	@Override
	public <T> Object findById(String id, String findBy, Class<T> cls) {
		return mongoTemplate.find(new Query(Criteria.where(findBy).is(id)), cls);
	}
	
	//Pass query object to find the document for particular persistent class in mongodb
	@Override
	public <T> Object findByQuery(Query query, Class<T> cls) {
		return mongoTemplate.find(query,cls);
	}

	@Override
	public <T> void deleteObjectByQuery(Query query, Class<T> cls) {
		mongoTemplate.remove(query,cls);
		
	}	
	
}

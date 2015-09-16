package com.main.interconnection.mongo.pagination.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.main.interconnection.mongoBo.LikeFeeds;
/**@author amit.singh
 * MongoRepository Implementation with Spring Data to get pagination for mongo results
 */
@Repository
public interface LikeFeedRepository extends MongoRepository<LikeFeeds, String>{
	//custom method to get paginated records on bases of user id
	//TODO can implement more custom method here.... 
	public Page<LikeFeeds> findByUserIdAndFeedIdAndSubTypeLikeOrderByAddedDateDesc(String UserId,String feedId,String subType,Pageable pageable);
	public Page<LikeFeeds> findByFeedIdAndSubTypeLikeOrderByAddedDateDesc(String feedId,String subType,Pageable pageable);
}




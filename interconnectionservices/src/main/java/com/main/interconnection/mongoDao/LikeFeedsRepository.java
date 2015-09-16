package com.main.interconnection.mongoDao;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.main.interconnection.mongoBo.LikeFeeds;

public interface LikeFeedsRepository extends
		PagingAndSortingRepository<LikeFeeds, String> {

	public static class PageSpecification {
		/**
		 * 
		 * @param pageIndex
		 * @return
		 */
		public static Pageable constructPageSpecification(final int pageIndex,
				final int pageSize) {
			Pageable pageSpecification = new PageRequest(pageIndex, pageSize);
			return pageSpecification;
		}

	}

}

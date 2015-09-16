package com.main.interconnection.util;

import java.util.List;

import com.main.interconnection.clientBo.FriendRequest;

public class QueryBuilder {
	public static StringBuffer getSolrFrndInQueryForUserId(
			List<FriendRequest> list) {
		StringBuffer buffer = new StringBuffer("user_id:(");
		for (FriendRequest request : list) {
			buffer.append(request.getFromUser());
			buffer.append(" ");
		}
		buffer.replace(buffer.lastIndexOf(" "), buffer.lastIndexOf(" "), ")");
		return buffer;
	}

}

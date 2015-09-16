package com.main.interconnection.util;

public class UpdateTypeEnum {

	public enum UpdateType {
		Feeds, Notifications
	}

	public enum UpdateSubType {
		FriendRequest, Venue , like, BioUpdate , ProfilePictureUpdate, VenueBookmarked, VenueUnBookmarked, Tagged, Shared,Facebook
	}
	
	public enum UpdateSubTypes {
		USEROWN
	}

	public enum FriendRequestUpdateProperty {
		AcceptsRequest, RejectRequest, DeclineRequest , ReceivedRequest
	}
	
	public enum VenueUpdateProperty {
		AddedVenue, DeletedVenue, AddedPhotos, UpdatedVenue,SharedVenue
	}
	
	public enum feedsUpdateProperty {
		comments , commentsLike ,ProfileUpdate, VenueReview, VenueRating,feedsLike,reply,HelpfulReview
	}
	
	public enum FlagType {
		review_id, venue_id, comment_id, photo_id;
	}
	
	public enum FlagSubType {
		name, phone_number, website, category, address, other;
	}
}

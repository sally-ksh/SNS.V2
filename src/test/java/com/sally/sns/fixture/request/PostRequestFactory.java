package com.sally.sns.fixture.request;

import com.sally.sns.controller.reuqest.PostRequest;
import com.sally.sns.fixture.entity.TestPostEntity;

public class PostRequestFactory {

	public static PostRequest.Creation getPostCreationRequest(String title, String content, Designation designation) {
		switch (designation) {
			// case EMPTY -> new PostRequest.Creation(title, content, new PostRequest.PlaceInfo());  // java 13
			case NOT_EMPTY:
				return new PostRequest.Creation(title, content,
					new PostRequest.PlaceInfo(TestPostEntity.location.getAddressName(),
						TestPostEntity.location.getLongitude(), TestPostEntity.location.getLatitude()));
			default:
				return new PostRequest.Creation(title, content, new PostRequest.PlaceInfo());
		}
	}

	public enum Designation {
		NOT_EMPTY, EMPTY
	}
}

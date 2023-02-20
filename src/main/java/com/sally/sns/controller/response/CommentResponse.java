package com.sally.sns.controller.response;

import com.sally.sns.model.Comment;
import com.sally.sns.model.Member;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CommentResponse {
	@Getter
	@AllArgsConstructor
	public static class Lists {
		private Long commentId;
		private String comment;
		private Member member;
		private LocalDateTime createdAt;

		public static CommentResponse.Lists of(Comment comment) {
			return new CommentResponse.Lists(comment.getId(), comment.getBody(), comment.getMember(),
				comment.getCreatedAt());
		}
	}

	@Getter
	@AllArgsConstructor
	public static class Body {
		private String comment;
	}
}

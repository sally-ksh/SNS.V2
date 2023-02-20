package com.sally.sns.controller.reuqest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentRequest {
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Creation {
		private String body;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Modification {
		private String body;
	}
}

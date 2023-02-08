package com.sally.sns.controller.reuqest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequest {
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Join {
		private String email;
		private String nickname;
		private String password;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Login {
		private String nickname;
		private String password;
	}
}

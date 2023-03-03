package com.sally.sns.model.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmKeywordArgument {
	private Long recipientId;
	private Long senderId;
	private Long postId;
	private Long commentId;

	public static AlarmKeywordArgument addAlarmForComment(
		Long recipientId,
		Long senderId,
		Long postId,
		Long commentId) {
		return new AlarmKeywordArgument(recipientId, senderId, postId, commentId);
	}
}

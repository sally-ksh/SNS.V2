package com.sally.sns.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.sally.sns.model.AlarmKeywordArgument;
import com.sally.sns.model.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@TypeDef(name = "json", typeClass = JsonStringType.class)
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Where(clause = "deleted = false")
@Table(name = "alarm_tb")
@Entity(name = "alarm")
public class AlarmEntity extends BaseEntity {
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private UserEntity recipient;
	@Enumerated(EnumType.STRING)
	private AlarmType type;
	@Type(type = "json")
	@Column(columnDefinition = "json")
	private AlarmKeywordArgument keywordArgument;  // no FK

	@Builder(access = PRIVATE)
	public AlarmEntity(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted,
		UserEntity recipient, AlarmType type, AlarmKeywordArgument keywordArgument) {
		super(id, createdAt, updatedAt, deleted);
		this.recipient = recipient;
		this.type = type;
		this.keywordArgument = keywordArgument;
	}

	public static AlarmEntity of(AlarmType alarmType, UserEntity recipient, AlarmKeywordArgument keywordArgument) {
		return AlarmEntity.builder()
			.recipient(recipient)
			.type(alarmType)
			.keywordArgument(keywordArgument)
			.build();
	}

	public Long recipientId() {
		return this.recipient.getId();
	}

	public String typeText() {
		return this.type.getMessage();
	}

	public AlarmKeywordArgument keywordArgument() {
		return this.keywordArgument;
	}

	public void softlyDelete() {
		toDelete();
	}
}
/**
 * 누가 -의 -에 -을 달았다. (sender -> recipient)
 * 	검색 - -의
 * 	메시지 : 누가 -에 -을 달았습니다.
 * 	  누가 (댓글작성자), -에 -을(ALARM_TYPE : 댓글, 좋아요..)
 *                    -에 : 포스트명 이 길면 ? ->  포스트에 (postId) (default : 내가 작성한 포스트) -> 님이 댓글을 달았습니다. -> 댓글이 달렸습니다.?
 *	  user flow: click alarm's message  -> show 포스트 or 댓글  & click the part of content  ->  sender
 *
 * 	   ALARM_TYPE.COMMENT -> 댓글을 달았습니다.
 * 	   ALARM_TYPE.POST_LIKE -> 포스트에 좋아요를 눌렀습니다.
 * 	   ALARM_TYPE.COMMENT_LIKE -> 댓글에 좋아요를 눌렀습니다. (댓글 작성자가 recipient)
 * 	   ALARM_TYPE.FOLLOW -> 팔로우/구독? 합니다.
 *
 * 	   댓글 - 댓글 보러가기 -> commentId (포스트의 댓글 목록에 해당 댓글만 맨 위에) TODO
 * 	   						& postId : /api/v2/sns/posts/{postId}/comments
 * 	   좋아요 - 포스트 보러가기 postId, 댓글 보러가기 commentId
 * 	   팔로우 - senderId 포스트 목록 보러가기   TODO
 */

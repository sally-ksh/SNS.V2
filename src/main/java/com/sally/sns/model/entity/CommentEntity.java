package com.sally.sns.model.entity;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.sally.sns.exception.ErrorCode;
import com.sally.sns.exception.SnsApplicationException;

import org.hibernate.annotations.Where;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = true)
@Where(clause = "deleted = false")
@Table(name = "comment_tb")
@Entity(name = "comment")
public class CommentEntity extends BaseEntity {
	@Column(columnDefinition = "MEDIUMTEXT", nullable = false)
	private String comment;
	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	private UserEntity member;
	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "post_id", nullable = false, updatable = false)
	private PostEntity post;

	@Builder(access = PRIVATE)
	public CommentEntity(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted,
		String comment, UserEntity member, PostEntity post) {
		super(id, createdAt, updatedAt, deleted);
		this.comment = comment;
		this.member = member;
		this.post = post;
	}

	public CommentEntity(String comment, UserEntity member, PostEntity post) {
		this.comment = comment;
		this.member = member;
		this.post = post;
	}

	public static CommentEntity of(String comment, UserEntity userEntity, PostEntity postEntity) {
		return CommentEntity.builder()
			.comment(comment)
			.member(userEntity)
			.post(postEntity)
			.build();
	}

	public boolean hasNotPost(Long postId) {
		return !this.post.isSameAs(postId);
	}

	public boolean isNotCommenter(Long commenterId) {
		return !this.member.hasId(commenterId);
	}

	public void update(String body) {
		if (!StringUtils.hasText(body)) {
			throw new SnsApplicationException(ErrorCode.NO_TEXT, "CommentEntity");
		}
		this.comment = body;
	}

	public String content() {
		return this.comment;
	}

	public void deleteSoftly() {
		super.toDelete();
	}
}

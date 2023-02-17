package com.sally.sns.model.entity;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
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
}

package com.sally.sns.model.entity;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.sally.sns.model.Location;

import org.hibernate.annotations.Where;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
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
@Table(name = "post_tb")
@Entity(name = "post")
public class PostEntity extends BaseEntity {
	@Column(nullable = false)
	private String title;
	@Column(columnDefinition = "MEDIUMTEXT", nullable = false)
	private String content;
	@ManyToOne(fetch = LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	private UserEntity author;
	@Embedded
	@Column(nullable = false)
	private Location location;

	@Builder(access = PRIVATE)
	public PostEntity(Long id, LocalDateTime createdAt, LocalDateTime modifiedAt, boolean deleted, String title,
		String content, UserEntity userEntity, Location location) {
		super(id, createdAt, modifiedAt, deleted);
		this.title = title;
		this.content = content;
		this.author = userEntity;
		this.location = location;
	}

	public static PostEntity of(String title, String content, UserEntity userEntity, Location location) {
		Location locationData = getLocationOrNull(location);
		return PostEntity.builder()
			.title(title)
			.content(content)
			.location(locationData)
			.userEntity(userEntity)
			.build();
	}

	private static Location getLocationOrNull(Location location) {
		return (ObjectUtils.isEmpty(location)) ? null : location;
	}

	public String title() {
		return this.title;
	}

	public String content() {
		return this.content;
	}

	public Location location() {
		return this.location;
	}

	public boolean isNotAuthor(Long userId) {
		return !this.author.hasId(userId);
	}

	public void update(String title, String content) {
		this.title = Objects.requireNonNull(title);
		this.content = content;
	}

	public void softlyDelete() {
		toDelete();
	}
}

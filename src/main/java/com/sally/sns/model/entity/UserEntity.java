package com.sally.sns.model.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.sally.sns.model.UserRole;

import org.hibernate.annotations.Where;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = true)
@Where(clause = "deleted = false")
@Table(name = "user_tb")
@Entity
public class UserEntity extends BaseEntity {
	private String nickName;
	private String email;
	private String password;
	@Enumerated(EnumType.STRING)
	private UserRole role = UserRole.USER;

	@Builder(access = PRIVATE)
	public UserEntity(Integer id, LocalDateTime createdAt, LocalDateTime modifiedAt, boolean deleted,
		String nickName, String email, String password, UserRole role) {
		super(id, createdAt, modifiedAt, deleted);
		this.nickName = nickName;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public static UserEntity of(String nickName, String email, String password) {
		return UserEntity.builder()
			.nickName(nickName)
			.email(email)
			.password(password)
			.role(UserRole.USER)
			.build();
	}

	public boolean isMatchUpPassword(BCryptPasswordEncoder passwordEncoder, String password) {
		return passwordEncoder.matches(password, this.password);
	}

	public String nickName() {
		return this.nickName;
	}

	public String roleText() {
		return this.role.name();
	}

	public boolean hasNickName(String nickName) {
		return this.nickName.equals(nickName);
	}
}

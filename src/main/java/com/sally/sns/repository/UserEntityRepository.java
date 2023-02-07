package com.sally.sns.repository;

import com.sally.sns.model.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
	boolean existsUserByNickNameOrEmail(@Param("nickName") String nickName, @Param("email") String email);
}

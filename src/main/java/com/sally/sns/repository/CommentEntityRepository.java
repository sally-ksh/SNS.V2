package com.sally.sns.repository;

import com.sally.sns.model.entity.CommentEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {
}

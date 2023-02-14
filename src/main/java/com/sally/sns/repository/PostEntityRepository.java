package com.sally.sns.repository;

import com.sally.sns.model.entity.PostEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {
}

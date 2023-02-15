package com.sally.sns.repository;

import com.sally.sns.model.PostView;
import com.sally.sns.model.entity.PostEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {
	@Query(value =
		"SELECT  new com.sally.sns.model.PostView("
			+ " p.id, p.title, p.content, u.id, u.nickName, p.location, p.createdAt "
			+ ") "
			+ "FROM post p inner join UserEntity u on p.author.id = u.id "
			+ "where p.deleted = false "
			+ "order by p.createdAt desc ")
	Page<PostView> findPostViews(Pageable pageable);

	Page<PostEntity> findAllByAuthorId(@Param("userId") Long userId, Pageable pageable);
}

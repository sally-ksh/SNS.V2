package com.sally.sns.repository;

import com.sally.sns.model.CommentView;
import com.sally.sns.model.entity.CommentEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {
	@Query(value =
		"SELECT new com.sally.sns.model.CommentView("
			+ " c.id, c.comment, c.createdAt, u.id, u.nickName "
			+ ") "
			+ "FROM comment c inner join UserEntity u on c.member.id = u.id "
			+ "where c.post.id = :postId and c.deleted = false "
			+ "order by c.createdAt desc ")
	Page<CommentView> findCommentViewsBy(@Param("postId") Long postId, Pageable pageable);
}

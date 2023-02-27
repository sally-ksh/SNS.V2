package com.sally.sns.repository.alarm;

import com.sally.sns.model.entity.AlarmEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlarmEntityRepository extends JpaRepository<AlarmEntity, Long> {
	List<AlarmEntity> findAllByRecipientId(@Param("userId") Long recipientId);
}

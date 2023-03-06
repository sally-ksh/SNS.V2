package com.sally.sns.repository.alarm;

import com.sally.sns.model.alarm.AlarmView;
import com.sally.sns.model.entity.AlarmEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AlarmEntityRepository extends JpaRepository<AlarmEntity, Long> {
	@Query(value =
		"SELECT new com.sally.sns.model.alarm.AlarmView("
			+ " u.id, a.type, a.keywordArgument, a.createdAt "
			+ ")"
			+ "FROM alarm a inner join UserEntity u on a.recipient.id = u.id "
			+ "WHERE a.deleted = false and a.recipient.id = :userId "
			+ "order by a.createdAt asc ")
	List<AlarmView> getAlarmsByRecipientId(@Param("userId") Long recipientId);

	@Transactional
	@Modifying
	@Query(value = "update alarm set deleted = true where recipient.id = :userId")
	int updateAllSendStatusByRecipientId(@Param("userId") Long recipientId);
}

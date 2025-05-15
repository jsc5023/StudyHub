package com.studyhub.studyhub_backend.domain.notification.repository;

import com.studyhub.studyhub_backend.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByMemberId(Long memberId, Pageable pageable);

    Page<Notification> findByMemberIdAndIsRead(Long memberId, boolean isRead, Pageable pageable);

    int countByMemberIdAndIsRead(Long memberId, boolean isRead);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.member.id = :memberId AND n.isRead = false")
    void updateAllAsRead(@Param("memberId") Long memberId);
}
package com.studyhub.studyhub_backend.domain.notification.entity;

import com.studyhub.studyhub_backend.domain.member.entity.Member;
import com.studyhub.studyhub_backend.domain.notification.enums.NotificationType;
import com.studyhub.studyhub_backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification",
        indexes = {
                @Index(name = "idx_notification_member_read", columnList = "member_id,is_read")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(name = "resource_id")
    private Long resourceId;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private boolean isRead = false;  // Boolean -> boolean 변경

    // 비즈니스 메서드
    public void markAsRead() {
        this.isRead = true;
    }

    // 정적 팩토리 메서드
    public static Notification create(Member member, String message, NotificationType type, Long resourceId) {
        return Notification.builder()
                .member(member)
                .message(message)
                .type(type)
                .resourceId(resourceId)
                .isRead(false)
                .build();
    }
}
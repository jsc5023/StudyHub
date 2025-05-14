package com.studyhub.studyhub_backend.domain.application.entity;

import com.studyhub.studyhub_backend.domain.member.entity.Member;
import com.studyhub.studyhub_backend.domain.project.entity.Project;
import com.studyhub.studyhub_backend.domain.project.entity.RoleSlot;
import com.studyhub.studyhub_backend.domain.application.enums.ApplicationStatus;
import com.studyhub.studyhub_backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "application",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "project_id"})
        },
        indexes = {
                @Index(name = "idx_application_status", columnList = "status")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Application extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_slot_id", nullable = false)
    private RoleSlot roleSlot;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(name = "reject_reason", columnDefinition = "TEXT")
    private String rejectReason;

    // 비즈니스 메서드
    public void approve() {
        if (this.status != ApplicationStatus.PENDING) {
            throw new IllegalStateException("대기 중인 지원만 승인할 수 있습니다.");
        }
        this.status = ApplicationStatus.APPROVED;
    }

    public void reject(String reason) {
        if (this.status != ApplicationStatus.PENDING) {
            throw new IllegalStateException("대기 중인 지원만 거절할 수 있습니다.");
        }
        this.status = ApplicationStatus.REJECTED;
        this.rejectReason = reason;
    }

    public void cancel() {
        if (this.status != ApplicationStatus.PENDING) {
            throw new IllegalStateException("대기 중인 지원만 취소할 수 있습니다.");
        }
        this.status = ApplicationStatus.CANCELED;
    }

    public static Application create(Member member, Project project, RoleSlot roleSlot, String message) {
        return Application.builder()
                .member(member)
                .project(project)
                .roleSlot(roleSlot)
                .message(message)
                .status(ApplicationStatus.PENDING)
                .build();
    }
}
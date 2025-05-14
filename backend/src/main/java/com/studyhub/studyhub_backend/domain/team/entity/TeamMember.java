package com.studyhub.studyhub_backend.domain.team.entity;

import com.studyhub.studyhub_backend.domain.application.entity.Application;
import com.studyhub.studyhub_backend.domain.member.entity.Member;
import com.studyhub.studyhub_backend.domain.project.entity.Project;
import com.studyhub.studyhub_backend.domain.project.entity.RoleSlot;
import com.studyhub.studyhub_backend.domain.team.enums.TeamMemberStatus;
import com.studyhub.studyhub_backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "team_member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"project_id", "member_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TeamMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_slot_id", nullable = false)
    private RoleSlot roleSlot;

    @Column(name = "joined_at", nullable = false)
    @Builder.Default
    private LocalDateTime joinedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TeamMemberStatus status = TeamMemberStatus.ACTIVE;

    // 비즈니스 메서드
    public void activate() {
        this.status = TeamMemberStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = TeamMemberStatus.INACTIVE;
    }

    public static TeamMember createFromApplication(Application application) {
        return TeamMember.builder()
                .project(application.getProject())
                .member(application.getMember())
                .roleSlot(application.getRoleSlot())
                .status(TeamMemberStatus.ACTIVE)
                .build();
    }
}
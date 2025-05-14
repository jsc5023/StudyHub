package com.studyhub.studyhub_backend.domain.project.entity;

import com.studyhub.studyhub_backend.domain.application.entity.Application;
import com.studyhub.studyhub_backend.domain.team.entity.TeamMember;
import com.studyhub.studyhub_backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role_slot",
        indexes = {
                @Index(name = "idx_role_project", columnList = "project_id,role_name")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoleSlot extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "role_name", length = 50, nullable = false)
    private String roleName;

    @Column(name = "max_count", nullable = false)
    private Integer maxCount;

    @Column(name = "current_count", nullable = false)
    @Builder.Default
    private Integer currentCount = 0;

    @OneToMany(mappedBy = "roleSlot")
    @Builder.Default
    private List<Application> applications = new ArrayList<>();

    @OneToMany(mappedBy = "roleSlot")
    @Builder.Default
    private List<TeamMember> teamMembers = new ArrayList<>();

    // 비즈니스 메서드
    public boolean isAvailable() {
        return currentCount < maxCount;
    }

    public void incrementCount() {
        if (!isAvailable()) {
            throw new IllegalStateException("해당 역할의 모집이 마감되었습니다.");
        }
        this.currentCount++;
    }

    public void decrementCount() {
        if (currentCount <= 0) {
            throw new IllegalStateException("현재 인원수가 0 이하입니다.");
        }
        this.currentCount--;
    }

    public Integer getRemainingSlots() {
        return maxCount - currentCount;
    }

    public static RoleSlot create(Project project, String roleName, Integer maxCount) {
        return RoleSlot.builder()
                .project(project)
                .roleName(roleName)
                .maxCount(maxCount)
                .currentCount(0)
                .build();
    }
}

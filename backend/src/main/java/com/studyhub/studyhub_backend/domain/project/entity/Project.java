package com.studyhub.studyhub_backend.domain.project.entity;

import com.studyhub.studyhub_backend.domain.application.entity.Application;
import com.studyhub.studyhub_backend.domain.member.entity.Member;
import com.studyhub.studyhub_backend.domain.project.enums.ProjectStatus;
import com.studyhub.studyhub_backend.domain.team.entity.TeamMember;
import com.studyhub.studyhub_backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project",
        indexes = {
                @Index(name = "idx_project_deadline", columnList = "deadline"),
                @Index(name = "idx_project_status", columnList = "status")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.RECRUITING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;

    // 연관관계 매핑
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProjectTechStack> techStacks = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RoleSlot> roleSlots = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    @Builder.Default
    private List<Application> applications = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    @Builder.Default
    private List<TeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ProjectMilestone> milestones = new ArrayList<>();

    // 비즈니스 메서드
    public void updateProject(String title, String description, LocalDate deadline) {
        if (title != null) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        if (deadline != null) {
            this.deadline = deadline;
        }
    }

    public void updateStatus(ProjectStatus status) {
        this.status = status;
    }

    public void addTechStack(ProjectTechStack techStack) {
        this.techStacks.add(techStack);
        techStack.setProject(this);
    }

    public void removeTechStack(ProjectTechStack techStack) {
        this.techStacks.remove(techStack);
        techStack.setProject(null);
    }

    public void addRoleSlot(RoleSlot roleSlot) {
        this.roleSlots.add(roleSlot);
        roleSlot.setProject(this);
    }

    public void removeRoleSlot(RoleSlot roleSlot) {
        this.roleSlots.remove(roleSlot);
        roleSlot.setProject(null);
    }

    public boolean isOwner(Long memberId) {
        return this.owner.getId().equals(memberId);
    }

    public boolean isRecruiting() {
        return this.status == ProjectStatus.RECRUITING &&
                this.deadline.isAfter(LocalDate.now());
    }

    public boolean isDeadlinePassed() {
        return this.deadline.isBefore(LocalDate.now());
    }
}
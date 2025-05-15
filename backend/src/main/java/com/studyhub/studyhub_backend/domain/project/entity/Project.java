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
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (description != null && !description.isBlank()) {
            this.description = description;
        }
        if (deadline != null) {
            this.deadline = deadline;
        }
    }

    public void updateStatus(ProjectStatus status) {
        // 상태 변경 검증 추가
        validateStatusTransition(status);
        this.status = status;
    }

    private void validateStatusTransition(ProjectStatus newStatus) {
        if (this.status == ProjectStatus.COMPLETED && newStatus == ProjectStatus.RECRUITING) {
            throw new IllegalStateException("완료된 프로젝트는 다시 모집 상태로 변경할 수 없습니다.");
        }
    }

    // 기술 스택 관리 (orphanRemoval 활용)
    public void addTechStack(ProjectTechStack techStack) {
        this.techStacks.add(techStack);
        // 양방향 관계는 팩토리 메서드에서 처리
    }

    public void removeTechStack(ProjectTechStack techStack) {
        this.techStacks.remove(techStack);
        // orphanRemoval = true이므로 자동 삭제
    }

    public void clearTechStacks() {
        this.techStacks.clear();
    }

    // 기술 스택을 문자열로 업데이트
    public void updateTechStacks(List<String> techStackNames) {
        this.techStacks.clear();
        techStackNames.forEach(name ->
                ProjectTechStack.create(this, name)
        );
    }

    // 역할 슬롯 관리 (orphanRemoval 활용)
    public void addRoleSlot(RoleSlot roleSlot) {
        this.roleSlots.add(roleSlot);
        // 양방향 관계는 팩토리 메서드에서 처리
    }

    public void removeRoleSlot(RoleSlot roleSlot) {
        this.roleSlots.remove(roleSlot);
        // orphanRemoval = true이므로 자동 삭제
    }

    public void removeRoleSlotByName(String roleName) {
        this.roleSlots.removeIf(roleSlot ->
                roleSlot.getRoleName().equals(roleName));
    }

    // 팀원 관련 메서드
    public void addTeamMember(TeamMember teamMember) {
        this.teamMembers.add(teamMember);
    }

    // 권한 체크
    public boolean isOwner(Long memberId) {
        return this.owner.getId().equals(memberId);
    }

    // 상태 체크
    public boolean isRecruiting() {
        return this.status == ProjectStatus.RECRUITING
                && this.deadline.isAfter(LocalDate.now());
    }

    public boolean isDeadlinePassed() {
        return this.deadline.isBefore(LocalDate.now());
    }

    public boolean canApply() {
        return isRecruiting() && !isDeadlinePassed();
    }

    // 통계 정보
    public int getTotalSlots() {
        return this.roleSlots.stream()
                .mapToInt(RoleSlot::getMaxCount)
                .sum();
    }

    public int getFilledSlots() {
        return this.roleSlots.stream()
                .mapToInt(RoleSlot::getCurrentCount)
                .sum();
    }

    public boolean isFull() {
        return getFilledSlots() >= getTotalSlots();
    }
}
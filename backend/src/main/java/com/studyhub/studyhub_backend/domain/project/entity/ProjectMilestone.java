package com.studyhub.studyhub_backend.domain.project.entity;

import com.studyhub.studyhub_backend.domain.member.entity.Member;
import com.studyhub.studyhub_backend.domain.project.enums.MilestoneStatus;
import com.studyhub.studyhub_backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "project_milestone")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectMilestone extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private MilestoneStatus status = MilestoneStatus.TODO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Member createdBy;

    // 비즈니스 메서드
    public void updateMilestone(String title, String description, LocalDate dueDate) {
        if (title != null) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        if (dueDate != null) {
            this.dueDate = dueDate;
        }
    }

    public void updateStatus(MilestoneStatus status) {
        this.status = status;
    }

    public void startProgress() {
        if (this.status != MilestoneStatus.TODO) {
            throw new IllegalStateException("TODO 상태에서만 진행중으로 변경할 수 있습니다.");
        }
        this.status = MilestoneStatus.IN_PROGRESS;
    }

    public void complete() {
        if (this.status != MilestoneStatus.IN_PROGRESS) {
            throw new IllegalStateException("진행중 상태에서만 완료로 변경할 수 있습니다.");
        }
        this.status = MilestoneStatus.DONE;
    }

    public static ProjectMilestone create(Project project, Member createdBy, String title,
                                          String description, LocalDate dueDate) {
        return ProjectMilestone.builder()
                .project(project)
                .createdBy(createdBy)
                .title(title)
                .description(description)
                .dueDate(dueDate)
                .status(MilestoneStatus.TODO)
                .build();
    }
}
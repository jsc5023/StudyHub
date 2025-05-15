package com.studyhub.studyhub_backend.domain.project.entity;

import com.studyhub.studyhub_backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_tech_stack",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"project_id", "tech_stack"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectTechStack extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "tech_stack", length = 50, nullable = false)
    private String techStack;

    // 정적 팩토리 메서드
    public static ProjectTechStack create(Project project, String techStack) {
        ProjectTechStack projectTechStack = ProjectTechStack.builder()
                .project(project)
                .techStack(techStack)
                .build();

        // 양방향 관계 설정
        project.addTechStack(projectTechStack);

        return projectTechStack;
    }

    // 같은 패키지 내에서만 접근 가능
    void assignProject(Project project) {
        this.project = project;
    }
}
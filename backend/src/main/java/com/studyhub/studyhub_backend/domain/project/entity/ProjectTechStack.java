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
@Setter
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

    public static ProjectTechStack create(Project project, String techStack) {
        return ProjectTechStack.builder()
                .project(project)
                .techStack(techStack)
                .build();
    }
}
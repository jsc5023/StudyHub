package com.studyhub.studyhub_backend.domain.project.repository;

import com.studyhub.studyhub_backend.domain.project.entity.ProjectTechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTechStackRepository extends JpaRepository<ProjectTechStack, Long> {

    List<ProjectTechStack> findByProjectId(Long projectId);

    void deleteByProjectId(Long projectId);
}
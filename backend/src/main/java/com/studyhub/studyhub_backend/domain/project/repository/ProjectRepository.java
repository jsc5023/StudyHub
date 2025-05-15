package com.studyhub.studyhub_backend.domain.project.repository;

import com.studyhub.studyhub_backend.domain.project.entity.Project;
import com.studyhub.studyhub_backend.domain.project.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByOwnerId(Long ownerId, Pageable pageable);

    Page<Project> findByOwnerIdAndStatus(Long ownerId, ProjectStatus status, Pageable pageable);
}
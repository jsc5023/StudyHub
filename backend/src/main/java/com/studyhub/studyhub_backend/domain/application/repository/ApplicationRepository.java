package com.studyhub.studyhub_backend.domain.application.repository;

import com.studyhub.studyhub_backend.domain.application.entity.Application;
import com.studyhub.studyhub_backend.domain.application.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Page<Application> findByMemberId(Long memberId, Pageable pageable);

    Page<Application> findByMemberIdAndStatus(Long memberId, ApplicationStatus status, Pageable pageable);

    int countByProjectId(Long projectId);
}
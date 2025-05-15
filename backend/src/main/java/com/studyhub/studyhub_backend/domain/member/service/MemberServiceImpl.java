package com.studyhub.studyhub_backend.domain.member.service;

import com.studyhub.studyhub_backend.domain.application.entity.Application;
import com.studyhub.studyhub_backend.domain.application.enums.ApplicationStatus;
import com.studyhub.studyhub_backend.domain.application.repository.ApplicationRepository;
import com.studyhub.studyhub_backend.domain.member.dto.request.MemberUpdateRequest;
import com.studyhub.studyhub_backend.domain.member.dto.response.MemberApplicationsResponse;
import com.studyhub.studyhub_backend.domain.member.dto.response.MemberInfoResponse;
import com.studyhub.studyhub_backend.domain.member.dto.response.MemberNotificationsResponse;
import com.studyhub.studyhub_backend.domain.member.dto.response.MemberProjectsResponse;
import com.studyhub.studyhub_backend.domain.member.entity.Member;
import com.studyhub.studyhub_backend.domain.member.entity.MemberTechStack;
import com.studyhub.studyhub_backend.domain.member.repository.MemberRepository;
import com.studyhub.studyhub_backend.domain.member.repository.MemberTechStackRepository;
import com.studyhub.studyhub_backend.domain.notification.entity.Notification;
import com.studyhub.studyhub_backend.domain.notification.repository.NotificationRepository;
import com.studyhub.studyhub_backend.domain.project.entity.Project;
import com.studyhub.studyhub_backend.domain.project.entity.ProjectTechStack;
import com.studyhub.studyhub_backend.domain.project.entity.RoleSlot;
import com.studyhub.studyhub_backend.domain.project.enums.ProjectStatus;
import com.studyhub.studyhub_backend.domain.project.repository.ProjectRepository;
import com.studyhub.studyhub_backend.domain.project.repository.ProjectTechStackRepository;
import com.studyhub.studyhub_backend.global.exception.BusinessException;
import com.studyhub.studyhub_backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberTechStackRepository memberTechStackRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTechStackRepository projectTechStackRepository;
    private final ApplicationRepository applicationRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public MemberInfoResponse getMyInfo(Long memberId) {
        log.debug("회원 정보 조회 시작 - memberId: {}", memberId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<String> techStacks = memberTechStackRepository.findByMemberId(memberId)
                .stream()
                .map(MemberTechStack::getTechStack)
                .toList();

        return MemberInfoResponse.from(member, techStacks);
    }

    @Override
    @Transactional
    public MemberInfoResponse updateMyInfo(Long memberId, MemberUpdateRequest request) {
        log.debug("회원 정보 수정 시작 - memberId: {}", memberId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 기본 정보 업데이트
        member.updateInfo(request.name(), request.bio(), request.profileImageUrl());

        // 기술 스택 업데이트
        updateMemberTechStacks(member, request.techStacks());

        List<String> updatedTechStacks = memberTechStackRepository.findByMemberId(memberId)
                .stream()
                .map(MemberTechStack::getTechStack)
                .toList();

        return MemberInfoResponse.from(member, updatedTechStacks);
    }

    @Override
    public MemberProjectsResponse getMyProjects(Long memberId, String status, int page, int size) {
        log.debug("회원 프로젝트 목록 조회 - memberId: {}, status: {}, page: {}, size: {}",
                memberId, status, page, size);

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Project> projects;
        if (status != null) {
            projects = projectRepository.findByOwnerIdAndStatus(memberId,
                    ProjectStatus.valueOf(status), pageRequest);
        } else {
            projects = projectRepository.findByOwnerId(memberId, pageRequest);
        }

        List<MemberProjectsResponse.ProjectSummary> projectSummaries = projects.getContent().stream()
                .map(project -> {
                    List<String> techStacks = projectTechStackRepository.findByProjectId(project.getId())
                            .stream()
                            .map(ProjectTechStack::getTechStack)
                            .toList();

                    int applicantCount = applicationRepository.countByProjectId(project.getId());

                    return new MemberProjectsResponse.ProjectSummary(
                            project.getId(),
                            project.getTitle(),
                            project.getStatus().name(),
                            project.getDeadline().toString(),
                            techStacks,
                            applicantCount,
                            project.getCreatedAt()
                    );
                })
                .toList();

        return MemberProjectsResponse.of(projectSummaries, projects);
    }

    @Override
    public MemberApplicationsResponse getMyApplications(Long memberId, String status, int page, int size) {
        log.debug("회원 지원 내역 조회 - memberId: {}, status: {}, page: {}, size: {}",
                memberId, status, page, size);

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Application> applications;
        if (status != null) {
            applications = applicationRepository.findByMemberIdAndStatus(memberId,
                    ApplicationStatus.valueOf(status), pageRequest);
        } else {
            applications = applicationRepository.findByMemberId(memberId, pageRequest);
        }

        List<MemberApplicationsResponse.ApplicationSummary> applicationSummaries =
                applications.getContent().stream()
                        .map(application -> {
                            Project project = application.getProject();
                            RoleSlot roleSlot = application.getRoleSlot();

                            return new MemberApplicationsResponse.ApplicationSummary(
                                    application.getId(),
                                    project.getId(),
                                    project.getTitle(),
                                    new MemberApplicationsResponse.RoleSlotInfo(
                                            roleSlot.getId(),
                                            roleSlot.getRoleName()
                                    ),
                                    application.getStatus().name(),
                                    application.getCreatedAt()
                            );
                        })
                        .toList();

        return MemberApplicationsResponse.of(applicationSummaries, applications);
    }

    @Override
    public MemberNotificationsResponse getMyNotifications(Long memberId, Boolean isRead, int page, int size) {
        log.debug("회원 알림 목록 조회 - memberId: {}, isRead: {}, page: {}, size: {}",
                memberId, isRead, page, size);

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Notification> notifications;
        if (isRead != null) {
            notifications = notificationRepository.findByMemberIdAndIsRead(memberId, isRead, pageRequest);
        } else {
            notifications = notificationRepository.findByMemberId(memberId, pageRequest);
        }

        int unreadCount = notificationRepository.countByMemberIdAndIsRead(memberId, false);

        List<MemberNotificationsResponse.NotificationSummary> notificationSummaries =
                notifications.getContent().stream()
                        .map(notification -> new MemberNotificationsResponse.NotificationSummary(
                                notification.getId(),
                                notification.getMessage(),
                                notification.getType().name(),
                                notification.getResourceId(),
                                notification.isRead(),
                                notification.getCreatedAt()
                        ))
                        .toList();

        return MemberNotificationsResponse.of(notificationSummaries, notifications, unreadCount);
    }

    @Override
    @Transactional
    public void markAllNotificationsAsRead(Long memberId) {
        log.debug("전체 알림 읽음 처리 - memberId: {}", memberId);

        notificationRepository.updateAllAsRead(memberId);
    }

    private void updateMemberTechStacks(Member member, List<String> techStacks) {
        // 기존 기술 스택 제거
        member.clearTechStacks();
        memberTechStackRepository.deleteByMemberId(member.getId());

        // 새로운 기술 스택 추가
        if (techStacks != null && !techStacks.isEmpty()) {
            techStacks.stream()
                    .distinct()  // 중복 제거
                    .forEach(tech -> {
                        MemberTechStack techStack = MemberTechStack.builder()
                                .member(member)
                                .techStack(tech)
                                .build();
                        member.addTechStack(techStack);
                        memberTechStackRepository.save(techStack);
                    });
        }
    }
}
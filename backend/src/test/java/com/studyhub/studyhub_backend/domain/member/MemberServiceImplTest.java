package com.studyhub.studyhub_backend.domain.member;

import com.studyhub.studyhub_backend.domain.application.entity.Application;
import com.studyhub.studyhub_backend.domain.application.enums.ApplicationStatus;
import com.studyhub.studyhub_backend.domain.application.repository.ApplicationRepository;
import com.studyhub.studyhub_backend.domain.member.dto.response.MemberApplicationsResponse;
import com.studyhub.studyhub_backend.domain.member.dto.response.MemberInfoResponse;
import com.studyhub.studyhub_backend.domain.member.dto.response.MemberNotificationsResponse;
import com.studyhub.studyhub_backend.domain.member.dto.response.MemberProjectsResponse;
import com.studyhub.studyhub_backend.domain.member.entity.Member;
import com.studyhub.studyhub_backend.domain.member.entity.MemberTechStack;
import com.studyhub.studyhub_backend.domain.member.repository.MemberRepository;
import com.studyhub.studyhub_backend.domain.member.repository.MemberTechStackRepository;
import com.studyhub.studyhub_backend.domain.member.service.MemberServiceImpl;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;
    @Mock private MemberTechStackRepository memberTechStackRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private ProjectTechStackRepository projectTechStackRepository;
    @Mock private ApplicationRepository applicationRepository;
    @Mock private NotificationRepository notificationRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .name("홍길동")
                .bio("백엔드 개발자")
                .profileImageUrl("https://image")
                .build();
    }

    @Test
    @DisplayName("회원 정보 조회 성공")
    void getMyInfo_success() {
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(memberTechStackRepository.findByMemberId(1L)).willReturn(List.of(
                new MemberTechStack(member, "Java"),
                new MemberTechStack(member, "Spring")
        ));

        MemberInfoResponse result = memberService.getMyInfo(1L);

        assertThat(result.name()).isEqualTo("홍길동");
        assertThat(result.techStacks()).contains("Java", "Spring");
    }

    @Test
    @DisplayName("회원 정보 조회 실패 - 존재하지 않는 사용자")
    void getMyInfo_fail() {
        given(memberRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.getMyInfo(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("회원 프로젝트 목록 조회")
    void getMyProjects_success() {
        Project project = Project.builder()
                .id(100L)
                .title("Study Project")
                .status(ProjectStatus.RECRUITING)
                .deadline(LocalDateTime.now().plusDays(10))
                .owner(member)
                .createdAt(LocalDateTime.now())
                .build();

        Page<Project> projectPage = new PageImpl<>(List.of(project));
        given(projectRepository.findByOwnerId(1L, PageRequest.of(0, 10))).willReturn(projectPage);
        given(projectTechStackRepository.findByProjectId(100L)).willReturn(List.of(
                new ProjectTechStack(project, "Java")
        ));
        given(applicationRepository.countByProjectId(100L)).willReturn(3);

        MemberProjectsResponse response = memberService.getMyProjects(1L, null, 0, 10);
        assertThat(response.projects()).hasSize(1);
    }

    @Test
    @DisplayName("회원 알림 조회")
    void getMyNotifications_success() {
        Notification noti = Notification.builder()
                .id(1L)
                .message("알림 메시지")
                .type(Notification.Type.APPLY)
                .resourceId(100L)
                .isRead(false)
                .member(member)
                .createdAt(LocalDateTime.now())
                .build();

        Page<Notification> page = new PageImpl<>(List.of(noti));
        given(notificationRepository.findByMemberId(1L, PageRequest.of(0, 10))).willReturn(page);
        given(notificationRepository.countByMemberIdAndIsRead(1L, false)).willReturn(1);

        MemberNotificationsResponse response = memberService.getMyNotifications(1L, null, 0, 10);
        assertThat(response.notifications()).hasSize(1);
        assertThat(response.unreadCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("전체 알림 읽음 처리")
    void markAllNotificationsAsRead_success() {
        willDoNothing().given(notificationRepository).updateAllAsRead(1L);
        memberService.markAllNotificationsAsRead(1L);
        verify(notificationRepository).updateAllAsRead(1L);
    }

    @Test
    @DisplayName("회원 지원 내역 조회")
    void getMyApplications_success() {
        Project project = Project.builder().id(100L).title("Title").build();
        RoleSlot slot = RoleSlot.builder().id(10L).roleName("백엔드").build();
        Application app = Application.builder().id(1L).project(project).roleSlot(slot)
                .status(ApplicationStatus.PENDING).member(member).createdAt(LocalDateTime.now()).build();

        Page<Application> appPage = new PageImpl<>(List.of(app));
        given(applicationRepository.findByMemberId(1L, PageRequest.of(0, 10))).willReturn(appPage);

        MemberApplicationsResponse response = memberService.getMyApplications(1L, null, 0, 10);
        assertThat(response.applications()).hasSize(1);
    }
}

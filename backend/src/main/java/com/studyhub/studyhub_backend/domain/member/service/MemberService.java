package com.studyhub.studyhub_backend.domain.member.service;

import com.studyhub.studyhub_backend.domain.member.dto.request.MemberUpdateRequest;
import com.studyhub.studyhub_backend.domain.member.dto.response.MemberApplicationsResponse;
import com.studyhub.studyhub_backend.domain.member.dto.response.MemberInfoResponse;
import com.studyhub.studyhub_backend.domain.member.dto.response.MemberNotificationsResponse;
import com.studyhub.studyhub_backend.domain.member.dto.response.MemberProjectsResponse;

public interface MemberService {
    // 내 정보 조회
    MemberInfoResponse getMyInfo(Long memberId);

    // 내 정보 수정
    MemberInfoResponse updateMyInfo(Long memberId, MemberUpdateRequest request);

    // 내가 등록한 프로젝트 목록
    MemberProjectsResponse getMyProjects(Long memberId, String status, int page, int size);

    // 내가 지원한 프로젝트 목록
    MemberApplicationsResponse getMyApplications(Long memberId, String status, int page, int size);

    // 알림 목록
    MemberNotificationsResponse getMyNotifications(Long memberId, Boolean isRead, int page, int size);

    // 전체 알림 읽음 처리
    void markAllNotificationsAsRead(Long memberId);
}

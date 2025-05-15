package com.studyhub.studyhub_backend.domain.member.dto.response;

import com.studyhub.studyhub_backend.global.dto.response.PageInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "회원 알림 목록 응답")
public record MemberNotificationsResponse(
        @Schema(description = "알림 목록")
        List<NotificationSummary> content,

        @Schema(description = "페이지 정보")
        PageInfo pageable,

        @Schema(description = "읽지 않은 알림 수", example = "1")
        int unreadCount
) {

    public static MemberNotificationsResponse of(List<NotificationSummary> content, Page<?> page, int unreadCount) {
        return new MemberNotificationsResponse(
                content,
                PageInfo.from(page),
                unreadCount
        );
    }

    @Schema(description = "알림 요약 정보")
    public record NotificationSummary(
            @Schema(description = "알림 ID", example = "1")
            Long id,

            @Schema(description = "알림 메시지", example = "MSA 아키텍처 스터디에 지원이 승인되었습니다.")
            String message,

            @Schema(description = "알림 유형", example = "APPROVE")
            String type,

            @Schema(description = "관련 리소스 ID", example = "3")
            Long resourceId,

            @Schema(description = "읽음 여부", example = "false")
            boolean isRead,

            @Schema(description = "생성일시", example = "2025-03-16T14:30:00Z")
            LocalDateTime createdAt
    ) {}
}
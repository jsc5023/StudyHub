package com.studyhub.studyhub_backend.domain.member.dto.response;

import com.studyhub.studyhub_backend.global.dto.response.PageInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "회원이 지원한 프로젝트 목록 응답")
public record MemberApplicationsResponse(
        @Schema(description = "지원 내역 목록")
        List<ApplicationSummary> content,

        @Schema(description = "페이지 정보")
        PageInfo pageable
) {

    public static MemberApplicationsResponse of(List<ApplicationSummary> content, Page<?> page) {
        return new MemberApplicationsResponse(
                content,
                PageInfo.from(page)
        );
    }

    @Schema(description = "지원 내역 요약 정보")
    public record ApplicationSummary(
            @Schema(description = "지원서 ID", example = "1")
            Long id,

            @Schema(description = "프로젝트 ID", example = "3")
            Long projectId,

            @Schema(description = "프로젝트 제목", example = "MSA 아키텍처 스터디")
            String projectTitle,

            @Schema(description = "지원한 역할")
            RoleSlotInfo roleSlot,

            @Schema(description = "지원 상태", example = "APPROVED")
            String status,

            @Schema(description = "지원일시", example = "2025-03-15T11:20:00Z")
            LocalDateTime appliedAt
    ) {}

    @Schema(description = "역할 정보")
    public record RoleSlotInfo(
            @Schema(description = "역할 ID", example = "5")
            Long id,

            @Schema(description = "역할 이름", example = "백엔드")
            String roleName
    ) {}
}
package com.studyhub.studyhub_backend.domain.member.dto.response;

import com.studyhub.studyhub_backend.global.dto.response.PageInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "회원이 등록한 프로젝트 목록 응답")
public record MemberProjectsResponse(
        @Schema(description = "프로젝트 목록")
        List<ProjectSummary> content,

        @Schema(description = "페이지 정보")
        PageInfo pageable
) {

    public static MemberProjectsResponse of(List<ProjectSummary> content, Page<?> page) {
        return new MemberProjectsResponse(
                content,
                PageInfo.from(page)
        );
    }

    @Schema(description = "프로젝트 요약 정보")
    public record ProjectSummary(
            @Schema(description = "프로젝트 ID", example = "1")
            Long id,

            @Schema(description = "프로젝트 제목", example = "Spring Boot 프로젝트")
            String title,

            @Schema(description = "프로젝트 상태", example = "RECRUITING")
            String status,

            @Schema(description = "마감일", example = "2025-06-30")
            String deadline,

            @Schema(description = "기술 스택")
            List<String> techStacks,

            @Schema(description = "현재 지원자 수", example = "5")
            int currentApplicants,

            @Schema(description = "생성일시", example = "2025-03-10T10:00:00Z")
            LocalDateTime createdAt
    ) {}
}
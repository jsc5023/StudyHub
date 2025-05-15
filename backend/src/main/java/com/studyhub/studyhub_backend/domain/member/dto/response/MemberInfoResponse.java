package com.studyhub.studyhub_backend.domain.member.dto.response;

import com.studyhub.studyhub_backend.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "회원 정보 응답")
public record MemberInfoResponse(
        @Schema(description = "회원 ID", example = "1")
        Long id,

        @Schema(description = "이메일", example = "member@example.com")
        String email,

        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "자기소개", example = "백엔드 개발자입니다.")
        String bio,

        @Schema(description = "프로필 이미지 URL", example = "https://storage.studyhub.com/profiles/member1.jpg")
        String profileImageUrl,

        @Schema(description = "기술 스택", example = "[\"Java\", \"Spring Boot\", \"React\"]")
        List<String> techStacks,

        @Schema(description = "생성일시", example = "2025-03-15T09:30:00")
        LocalDateTime createdAt,

        @Schema(description = "수정일시", example = "2025-03-20T14:25:00")
        LocalDateTime updatedAt
) {
    public static MemberInfoResponse from(Member member, List<String> techStacks) {
        return new MemberInfoResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getBio(),
                member.getProfileImageUrl(),
                techStacks,
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}
package com.studyhub.studyhub_backend.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "회원 정보 수정 요청")
public record MemberUpdateRequest(
        @Schema(description = "이름", example = "홍길동 (수정)")
        @NotBlank(message = "이름은 필수입니다.")
        @Size(max = 50, message = "이름은 50자 이하여야 합니다.")
        String name,

        @Schema(description = "자기소개", example = "백엔드 개발자 & 데브옵스 엔지니어")
        @Size(max = 1000, message = "자기소개는 1000자 이하여야 합니다.")
        String bio,

        @Schema(description = "기술 스택", example = "[\"Java\", \"Spring Boot\", \"React\", \"Docker\"]")
        List<String> techStacks,

        @Schema(description = "프로필 이미지 URL", example = "https://storage.studyhub.com/profiles/member1_new.jpg")
        String profileImageUrl
) {

}
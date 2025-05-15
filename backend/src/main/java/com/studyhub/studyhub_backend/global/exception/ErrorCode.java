package com.studyhub.studyhub_backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통 에러
    INVALID_REQUEST("400-1", "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR("500-1", "서버 내부 오류가 발생했습니다."),

    // 인증 관련
    UNAUTHORIZED("401-1", "인증에 실패했습니다."),
    FORBIDDEN("403-1", "접근 권한이 없습니다."),
    TOKEN_EXPIRED("401-2", "토큰이 만료되었습니다."),

    // 회원 관련
    MEMBER_NOT_FOUND("404-1", "회원을 찾을 수 없습니다."),
    DUPLICATE_EMAIL("409-1", "이미 사용중인 이메일입니다."),

    // 프로젝트 관련
    PROJECT_NOT_FOUND("404-2", "프로젝트를 찾을 수 없습니다."),
    PROJECT_CLOSED("409-2", "모집이 마감된 프로젝트입니다."),

    // 지원 관련
    APPLICATION_NOT_FOUND("404-3", "지원서를 찾을 수 없습니다."),
    ALREADY_APPLIED("409-3", "이미 지원한 프로젝트입니다."),
    ROLE_FULL("409-4", "해당 역할의 모집이 마감되었습니다."),

    // 동시성 제어
    CONCURRENT_MODIFICATION("409-5", "동시 수정 충돌이 발생했습니다.");

    private final String code;
    private final String message;
}
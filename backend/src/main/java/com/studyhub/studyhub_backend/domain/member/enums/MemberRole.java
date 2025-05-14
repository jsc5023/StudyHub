package com.studyhub.studyhub_backend.domain.member.enums;

public enum MemberRole {
    MEMBER("ROLE_MEMBER"),
    ADMIN("ROLE_ADMIN");

    private final String authority;

    MemberRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
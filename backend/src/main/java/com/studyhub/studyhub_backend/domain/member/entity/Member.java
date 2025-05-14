package com.studyhub.studyhub_backend.domain.member.entity;

import com.studyhub.studyhub_backend.domain.bookmark.entity.Bookmark;
import com.studyhub.studyhub_backend.domain.member.enums.MemberRole;
import com.studyhub.studyhub_backend.domain.notification.entity.Notification;
import com.studyhub.studyhub_backend.domain.project.entity.Project;
import com.studyhub.studyhub_backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member",
        indexes = {
                @Index(name = "idx_member_email", columnList = "email"),
                @Index(name = "UK_provider_provider_id", columnList = "provider,provider_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private MemberRole role = MemberRole.MEMBER;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(length = 20)
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    // 연관관계 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MemberTechStack> techStacks = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    @Builder.Default
    private List<Project> ownedProjects = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Bookmark> bookmarks = new ArrayList<>();

    // 비즈니스 메서드

    public void updateProfile(String name, String bio, String profileImageUrl) {
        if (name != null) {
            this.name = name;
        }
        if (bio != null) {
            this.bio = bio;
        }
        if (profileImageUrl != null) {
            this.profileImageUrl = profileImageUrl;
        }
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void addTechStack(MemberTechStack techStack) {
        this.techStacks.add(techStack);
        techStack.setMember(this);
    }

    public void removeTechStack(MemberTechStack techStack) {
        this.techStacks.remove(techStack);
        techStack.setMember(null);
    }

    public void clearTechStacks() {
        this.techStacks.clear();
    }

    // 북마크 관련 메서드
    public void addBookmark(Bookmark bookmark) {
        this.bookmarks.add(bookmark);
        bookmark.setMember(this);
    }

    public void removeBookmark(Bookmark bookmark) {
        this.bookmarks.remove(bookmark);
        bookmark.setMember(null);
    }

    public boolean hasBookmarked(Long projectId) {
        return this.bookmarks.stream()
                .anyMatch(bookmark -> bookmark.getProject().getId().equals(projectId));
    }

    // 소셜 로그인 관련 메서드
    public void updateSocialInfo(String provider, String providerId) {
        this.provider = provider;
        this.providerId = providerId;
    }

    // 권한 체크
    public boolean isAdmin() {
        return this.role == MemberRole.ADMIN;
    }
}
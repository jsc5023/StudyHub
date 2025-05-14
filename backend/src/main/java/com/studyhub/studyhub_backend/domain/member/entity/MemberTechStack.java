package com.studyhub.studyhub_backend.domain.member.entity;

import com.studyhub.studyhub_backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_tech_stack",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "tech_stack"})
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberTechStack extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "tech_stack", length = 50, nullable = false)
    private String techStack;

    public static MemberTechStack create(Member member, String techStack) {
        return MemberTechStack.builder()
                .member(member)
                .techStack(techStack)
                .build();
    }
}
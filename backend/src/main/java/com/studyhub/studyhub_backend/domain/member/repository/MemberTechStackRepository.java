package com.studyhub.studyhub_backend.domain.member.repository;

import com.studyhub.studyhub_backend.domain.member.entity.MemberTechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberTechStackRepository extends JpaRepository<MemberTechStack, Long> {

    List<MemberTechStack> findByMemberId(Long memberId);

    @Modifying
    @Query("DELETE FROM MemberTechStack mt WHERE mt.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);
}
package com.szs.yongil.repository.scrap;

import com.szs.yongil.domain.member.MemberEntity;
import com.szs.yongil.domain.scrap.ScrapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<ScrapEntity, Long> {

    @Query("select s from ScrapEntity s where s.memberEntity = :member")
    Optional<ScrapEntity> findByMember(@Param("member") MemberEntity argMemberEntity);
}

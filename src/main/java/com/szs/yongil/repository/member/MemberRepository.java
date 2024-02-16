package com.szs.yongil.repository.member;

import com.szs.yongil.domain.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity,Long> {

    @Query("select m from MemberEntity m where m.name = :name")
    List<MemberEntity> findByName(@Param("name") String argName);

    Optional<MemberEntity> findByUserId(String argUserId);
}

package com.szs.yongil.domain.member;

import com.szs.yongil.domain.scrap.ScrapEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {

    /**
     *
     * Entity 생성시 생성자는 필 수 있기 때문에 기본 생성자는 PROTECTED로 생성 했습니다.
     * PROTECTED로 설정한 이유는 생성자를 통해서 값 변경 목적으로 접근하는 방법을 차단하기 위해서 입니다.
     *
     */

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String regNo;

    @OneToOne(mappedBy = "memberEntity")
    private ScrapEntity scrapEntity;

    @Builder
    public MemberEntity(String userId, String name, String password, String regNo) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.regNo = regNo;
    }

    public void setScrapEntity(ScrapEntity scrapEntity) {
        this.scrapEntity = scrapEntity;
    }

}

package com.szs.yongil.domain.scrap;

import com.szs.yongil.domain.member.MemberEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;


@Entity
@Getter
@Table(name = "scrap")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapEntity {

    /**
     *
     * Entity 생성시 생성자는 필 수 있기 때문에 기본 생성자는 PROTECTED로 생성 했습니다.
     * PROTECTED로 설정한 이유는 생성자를 통해서 값 변경 목적으로 접근하는 방법을 차단하기 위해서 입니다.
     *
     */

    @Id
    @Column(name = "scrap_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @Column(precision = 19, scale = 6)
    private BigDecimal taxCredit;

    @Column(precision = 19, scale = 6)
    private BigDecimal premium;

    @Column(precision = 19, scale = 6)
    private BigDecimal medical;

    @Column(precision = 19, scale = 6)
    private BigDecimal education;

    @Column(precision = 19, scale = 6)
    private BigDecimal donation;

    @Column(precision = 19, scale = 6)
    private BigDecimal pension;

    @Column(precision = 19, scale = 6)
    private BigDecimal salaryT;

    @Builder
    public ScrapEntity(BigDecimal taxCredit, BigDecimal premium, BigDecimal medical,
                       BigDecimal education, BigDecimal donation, BigDecimal pension, BigDecimal salaryT) {
        this.taxCredit = taxCredit;
        this.premium = premium;
        this.medical = medical;
        this.education = education;
        this.donation = donation;
        this.pension = pension;
        this.salaryT = salaryT;
    }

    public void setMemberData(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
        memberEntity.setScrapEntity(this);
    }

}

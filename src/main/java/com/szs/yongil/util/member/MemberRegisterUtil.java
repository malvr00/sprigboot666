package com.szs.yongil.util.member;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Getter
@Component
public class MemberRegisterUtil {

    private HashMap<String, String> userTableMap = new HashMap<>();

    @PostConstruct
    public void init() {
        userTableMap.put("홍길동", "860824-1655068");
        userTableMap.put("김둘리", "921108-1582816");
        userTableMap.put("마징가", "880601-2455116");
        userTableMap.put("베지터", "910411-1656116");
        userTableMap.put("손오공", "820326-2715702");
    }
}

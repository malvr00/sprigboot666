package com.szs.yongil.common;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Const {
    /**
     * Enum 관리
     */

    /**
     * JWT 관련
     */
    public interface JWT_PAYLOAD {

        public static final String BEARER = "Bearer ";
        public static final String HEADER = "Authorization";

    }

    /**
     * 암호화 관련
     */
    public interface AE128 {

        public static final Charset ENCODING_TYPE = StandardCharsets.UTF_8;
        public static final String INSTANCE_TYPE = "AES/CBC/PKCS5Padding";

    }

    /**
     * Scrap 관련
     */
    public interface SCRAP {

        public static final String INCOME_CLASSIFICATION = "소득구분";

        public static final String PREMIUM = "보험료";

        public static final String MEDICAL_EXPENSES = "의료비";

        public static final String EDUCATION_EXPENSES = "교육비";

        public static final String DONATION = "기부금";

        public static final String RETIREMENT_PENSION = "퇴직연금";

        public static final String AMOUNT = "금액";

        public static final String T_AMOUNT = "총납임금액";

        public static final String INCOME_DEDUCTION = "소득공제";

        public static final String TAX_CREDIT = "산출세액";

        public static final String JSON_DATA = "data";

        public static final String JSON_LIST = "jsonList";

        public static final String CASTING_COMMA = ",";

    }

}

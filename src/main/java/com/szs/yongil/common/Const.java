package com.szs.yongil.common;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Const {
    /**================================================================
     * Enum 관리
     ================================================================*/

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

        public static final String SALARY = "급여";

        public static final String TOTAL_SALARY = "총지급액";

        public static final String INCOME_DEDUCTION = "소득공제";

        public static final String TAX_CREDIT = "산출세액";

        public static final String JSON_DATA = "data";

        public static final String JSON_LIST = "jsonList";

        public static final String CASTING_COMMA = ",";

    }

    public interface CALCULATE {

        public static final BigDecimal CREDIT = new BigDecimal("0.55");

        public static final BigDecimal INSURANCE_DEDUCTION = new BigDecimal("0.12");

        public static final BigDecimal MEDICAL_PERCENT_1 = new BigDecimal("0.03");

        public static final BigDecimal MEDICAL_PERCENT_2 = new BigDecimal("0.15");

        public static final BigDecimal BASE_PRICE = new BigDecimal("130000");

    }

    public interface PATTERN {

        public static final String TEN_MILLION_PATTERN = "###,###";

    }


}

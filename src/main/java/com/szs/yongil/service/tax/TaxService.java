package com.szs.yongil.service.tax;

import com.szs.yongil.util.exception.ApiCustomException;
import com.szs.yongil.vo.tax.TaxVO;

public interface TaxService {

    TaxVO getRefundData() throws ApiCustomException;
}

package com.szs.yongil.service.client;


import com.szs.yongil.util.exception.ApiCustomException;
import com.szs.yongil.vo.scrap.ScrapVO;

public interface ClientService {

    ScrapVO getScrapData() throws ApiCustomException;
}

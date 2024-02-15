package com.szs.yongil.client;

import com.szs.yongil.dto.client.ScrapClientDto;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "SzsClient", url = "https://codetest.3o3.co.kr")
public interface SzsClient {

    @PostMapping("/v2/scrap")
    Response getScrap(@RequestBody ScrapClientDto scrapClientDto);

}

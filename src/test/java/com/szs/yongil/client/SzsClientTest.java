package com.szs.yongil.client;

import com.szs.yongil.dto.client.ScrapClientDto;
import com.szs.yongil.service.client.ClientService;
import feign.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class SzsClientTest {

    @Autowired
    SzsClient szsClient;

    @Test
    public void getScrapClientTest() {
        // given
        ScrapClientDto scrapClientDto = ScrapClientDto
                .builder()
                .name("홍길동")
                .regNo("860824-1655068")
                .build();
        // when
        Response scrap = szsClient.getScrap(scrapClientDto);

        // then
        Assertions.assertThat(scrap.status()).isEqualTo(200);
    }

}

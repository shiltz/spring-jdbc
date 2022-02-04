package za.co.shilton.springjdbc.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import za.co.shilton.springjdbc.dto.OrderRequestDto;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

  @Autowired
  private TestRestTemplate testRestTemplate;

  @LocalServerPort
  private int randomServerPort;

  private String url = "http://localhost:%s/orders/";

  @Test
  void createOrder() {
    var response = testRestTemplate.postForEntity(getURL(), new OrderRequestDto(), String.class);
    assertNotNull(response);
    log.info("response is :" + response);

  }

  @Test
  void getOrder() {
    var request = new OrderRequestDto();
    request.setProductName("Custome");
    var response = testRestTemplate.postForEntity(getURL(), request, java.util.UUID.class);
    assertNotNull(response);
    log.info("response is :" + response.getBody());


    var responseEntity = testRestTemplate.getForEntity(getURL() + response.getBody(),
        String.class);
    assertNotNull(responseEntity);
    log.info("response is :" + responseEntity);
  }

  private String getURL() {
    return String.format(url, randomServerPort);
  }
}
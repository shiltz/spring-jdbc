package za.co.shilton.springjdbc.controller;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.shilton.springjdbc.dto.OrderDto;
import za.co.shilton.springjdbc.dto.OrderRequestDto;
import za.co.shilton.springjdbc.service.OrderService;


@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @PostMapping
  public UUID createOrder(@RequestBody OrderRequestDto orderRequest) {
    var id = orderService.createOrder(orderRequest);
    log.info("created order with id {}", id);
    return id;
  }

  @GetMapping("/{orderId}")
  public OrderDto getOrder(@PathVariable UUID orderId) {
    var order = orderService.getOrder(orderId);
    log.info("retrieved order with id {}", order.getId());
    return order;
  }

}

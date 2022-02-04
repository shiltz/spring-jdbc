package za.co.shilton.springjdbc.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.shilton.springjdbc.dto.OrderDto;
import za.co.shilton.springjdbc.dto.OrderRequestDto;
import za.co.shilton.springjdbc.repository.JdbcRepo;

@Service
public class OrderService {

  @Autowired
  private JdbcRepo jdbcRepo;

  public UUID createOrder(OrderRequestDto orderRequestDto){
    var id = jdbcRepo.createOrder(orderRequestDto.getProductName());
    return id;
  }

  public OrderDto getOrder(UUID uuid) {
    return jdbcRepo.getOrder(uuid);
  }


}

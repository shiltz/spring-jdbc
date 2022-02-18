package za.co.shilton.springjdbc.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

  public int getSuccess(LocalDateTime startDate, LocalDateTime endate) {

    return jdbcRepo.getSuccess(Timestamp.valueOf(startDate), Timestamp.valueOf(endate));
  }

  public int getErrors(LocalDateTime startDate, LocalDateTime endate) {
    return jdbcRepo.getErrors(Timestamp.valueOf(startDate), Timestamp.valueOf(endate));
  }

  public List<String> getDetailedErrors(LocalDateTime startDate, LocalDateTime endate) {
    return jdbcRepo.getDetailedErrors(Timestamp.valueOf(startDate), Timestamp.valueOf(endate));
  }




}

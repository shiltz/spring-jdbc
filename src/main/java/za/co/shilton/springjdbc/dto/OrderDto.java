package za.co.shilton.springjdbc.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class OrderDto {

  private UUID id;
  private String productName;
}

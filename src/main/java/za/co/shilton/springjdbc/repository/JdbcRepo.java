package za.co.shilton.springjdbc.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import za.co.shilton.springjdbc.dto.OrderDto;

@Repository
public class JdbcRepo {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public UUID createOrder(String productName) {
    var sql = "insert into vas.order_mine (id , product_name) values (? , ?)";
    var uuid = UUID.randomUUID();
    jdbcTemplate.update(sql, uuid, productName);
    return uuid;
  }

  public OrderDto getOrder(UUID id) {
    var orders = jdbcTemplate.query("select * from vas.order_mine where id = ?", this::mapRow, id);
    return orders.get(0);
  }

  public OrderDto mapRow(ResultSet rs, int rowNum) throws SQLException {
    var order = new OrderDto();
    order.setId(rs.getObject("id", UUID.class));
    order.setProductName(rs.getString("product_name"));
    return order;
  }
}

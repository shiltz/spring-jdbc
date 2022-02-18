package za.co.shilton.springjdbc.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
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

  public int getSuccess(Timestamp startDate, Timestamp endate) {
    var sql = "select count(*) from vas_lotto_order.lotto_order_detail d where transaction_success = true " +
        "and creation_date > ? and creation_date < ?";

    return jdbcTemplate.query(sql, (ResultSetExtractor<Integer>) (rs) -> {
      if (rs.next()) {
        return rs.getInt(1);
      } else {
        return -1;
      }
    }, startDate, endate);
  }

  public int getErrors(Timestamp startDate, Timestamp endate) {
    var sql = "select  count(*) " +
        "from vas_lotto_order.lotto_order_detail d where (transaction_success is null or transaction_success = false) " +
        " and  creation_date >? and creation_date < ?";

    return jdbcTemplate.query(sql, (ResultSetExtractor<Integer>) (rs) -> {
      if (rs.next()) {
        return rs.getInt(1);
      } else {
        return -1;
      }
    }, startDate, endate);
  }

  public List<String> getDetailedErrors(Timestamp startDate, Timestamp endate) {
    var sql = "select d.transaction_reference_number, d.creation_date, oss.* from vas_lotto_order.lotto_order_detail d, vas_lotto_order.lotto_order_state os, " +
        "                        vas_lotto_order.lotto_order_state_outcome oss " +
        "where " +
        "        d.id = os.lotto_order_detail_id " +
        "  and os.state_outcome_id = oss.id " +
        "  and oss.outcome = 'FAILURE' " +
        "  and d.creation_date > ?" +
        "  and d.creation_date < ?" +
        "order by oss.start_date";

    return jdbcTemplate.query(sql, (rs, row) -> {
      var result = "";
      do {
        result += rs.getString(1) + "|" + rs.getString(2) + "|" + rs.getString(6) + "|" + rs.getString(7) + "|" +
            rs.getString(8) + "|<br>\n";
      } while (rs.next());
      return result;
    }, startDate, endate);
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

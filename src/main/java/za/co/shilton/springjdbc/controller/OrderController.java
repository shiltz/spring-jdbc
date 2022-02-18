package za.co.shilton.springjdbc.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import za.co.shilton.springjdbc.annotations.Performance;
import za.co.shilton.springjdbc.dto.OrderDto;
import za.co.shilton.springjdbc.dto.OrderRequestDto;
import za.co.shilton.springjdbc.service.OrderService;
import za.co.shilton.springjdbc.service.TeamsNotificationService;


@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @Autowired
  private TeamsNotificationService teamsNotificationService;

  @PostMapping
  public UUID createOrder(@RequestBody OrderRequestDto orderRequest) {
    var id = orderService.createOrder(orderRequest);
    log.info("created order with id {}", id);
    return id;
  }

  @Performance
  @GetMapping("/{orderId}")
  public OrderDto getOrder(@PathVariable UUID orderId) {
    var order = orderService.getOrder(orderId);
    log.info("retrieved order with id {}", order.getId());
    return order;
  }


  @GetMapping("/status")
  public String getStatus(@RequestParam String startTime, @RequestParam String endTime) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime nowMinus30 = now.minusMinutes(30L);
    if (startTime != null && endTime != null) {
        now = LocalDateTime.parse(endTime);
        nowMinus30 = LocalDateTime.parse(startTime);
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    var startOfDay = LocalDate.now().atStartOfDay();
    var endOfDay = LocalDate.now().plusDays(1).atStartOfDay().minusNanos(1L);


    var countSuccess = orderService.getSuccess(nowMinus30, now);
    var countSuccessForDay = orderService.getSuccess(startOfDay, endOfDay);
    var countErrors = orderService.getErrors(nowMinus30, now);
    var result = """
        <br/>\n Lotto status update for %s - %s
        <br/>\n Success: %s
        <br/>\n Errors: %s
        <br/>\n Total success for day(%s-%s): %s
        <br/><br/>\n\n""";

    var summary =
        String.format(result, nowMinus30.format(formatter), now.format(timeFormatter), countSuccess, countErrors,
            startOfDay.format(formatter), endOfDay.format(timeFormatter), countSuccessForDay);

    var detailedErrors = orderService.getDetailedErrors(nowMinus30, now);
    if (detailedErrors!= null && !CollectionUtils.isEmpty(detailedErrors)) {
      detailedErrors = detailedErrors.stream().map(s -> s + "<br>\n").collect(Collectors.toList());
    }

    return summary + "<br>Detailed Errors:<br>\n" + detailedErrors.toString();
  }

  @Scheduled(cron = "0 0/55 6-21 * * *")
  public void scheduleStatus() {
    var status = getStatus(null, null);
    System.out.println(status);
    teamsNotificationService.generateAlert("Summary", status, "Shil-localPC");
  }


}

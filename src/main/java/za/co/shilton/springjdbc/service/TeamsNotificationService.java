package za.co.shilton.springjdbc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class TeamsNotificationService {

  @Value("${comms.teams}")
  private String endpoint;

  @Autowired
  private RestTemplate restTemplate;

  public boolean generateAlert(String subject, String content, String application) {
    try {
      var messageBody = application + ": " + subject + "::" + content;
      var body = (new ObjectMapper()).writeValueAsString(
          TeamsNotificationService.TeamsMessage.builder().text(messageBody).build());
      var response = restTemplate.postForEntity(endpoint, body, String.class);
      log.info("sent teams message:" + response);
      return Objects.nonNull(response) && response.getStatusCode().is2xxSuccessful();
    } catch (Exception var6) {
      log.error("Teams notification service is unable send request to server: {}", var6.getMessage());
      return false;
    }
  }

  public static class TeamsMessage {
    private String text;

    TeamsMessage(final String text) {
      this.text = text;
    }

    public static TeamsNotificationService.TeamsMessage.TeamsMessageBuilder builder() {
      return new TeamsNotificationService.TeamsMessage.TeamsMessageBuilder();
    }

    public String getText() {
      return this.text;
    }

    public static class TeamsMessageBuilder {
      private String text;

      TeamsMessageBuilder() {
      }

      public TeamsNotificationService.TeamsMessage.TeamsMessageBuilder text(final String text) {
        this.text = text;
        return this;
      }

      public TeamsNotificationService.TeamsMessage build() {
        return new TeamsNotificationService.TeamsMessage(this.text);
      }

      public String toString() {
        return "TeamsNotificationService.TeamsMessage.TeamsMessageBuilder(text=" + this.text + ")";
      }
    }
  }


}

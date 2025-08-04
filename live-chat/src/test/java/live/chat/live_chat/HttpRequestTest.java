package live.chat.live_chat;

import java.util.List;

import live.chat.live_chat.model.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldSendMessageAndReceiveIt() {
        // ✅ Create message
        Message messageToSend = new Message();
        messageToSend.setContent("Hello from Test!");
        messageToSend.setSender("TestUser");
        messageToSend.setTimestamp(System.currentTimeMillis() + "");

        // ✅ POST /api/messages
        Message sentMessage = restTemplate.postForObject(
            "http://localhost:" + port + "/api/messages",
            messageToSend,
            Message.class
        );

        // ✅ Validate POST response
        assertThat(sentMessage).isNotNull();
        assertThat(sentMessage.getId()).isNotNull();
        assertThat(sentMessage.getContent()).isEqualTo("Hello from Test!");

        // ✅ GET /api/messages
        ResponseEntity<List<Message>> response = restTemplate.exchange(
            "http://localhost:" + port + "/api/messages",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {}
        );

        List<Message> receivedMessages = response.getBody();

        // ✅ Validate received messages
        assertThat(receivedMessages).isNotNull();
        assertThat(receivedMessages).isNotEmpty();
        assertThat(receivedMessages)
            .anyMatch(m -> "Hello from Test!".equals(m.getContent()));

        // ✅ Print messages for visibility
        System.out.println("Received messages: " + receivedMessages.size());
        for (Message message : receivedMessages) {
            System.out.println("Message ID: " + message.getId()
                + ", Sender: " + message.getSender()
                + ", Content: " + message.getContent());
        }
    }
}

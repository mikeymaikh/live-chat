package live.chat.live_chat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import live.chat.live_chat.model.Message;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldSendMessageAndReceiveIt() {
        String url = "http://localhost:" + port + "/api/messages";

        // Create a message object
        Message messageToSend = new Message();
        messageToSend.setContent("Hello, Bob!");
        messageToSend.setTimestamp(System.currentTimeMillis());
        messageToSend.setSenderId(1L);
        messageToSend.setReceiverId(2L);

        // Send the message
        Message sentMessage = restTemplate.postForObject(url, messageToSend, Message.class);
        // Verify the response
        assert sentMessage != null;
        assert sentMessage.getContent().equals("Hello, Bob!");
        assert sentMessage.getTimestamp() != null;
        assert sentMessage.getSenderId().equals(1L);
        assert sentMessage.getReceiverId().equals(2L);

        // Print the message details
        System.out.println("Sender ID: " + sentMessage.getSenderId()
            + ", Receiver ID: " + sentMessage.getReceiverId());
        System.out.println("Content: " + sentMessage.getContent());
        System.out.println("Timestamp: " + sentMessage.getTimestamp());

    }
}

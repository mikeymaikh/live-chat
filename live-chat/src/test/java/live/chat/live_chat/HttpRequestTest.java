package live.chat.live_chat;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod; 

import live.chat.live_chat.model.Message;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldSendMessageAndReceiveIt() {
        Message messageToSend = new Message();
        messageToSend.setContent("Hello from Test!");
        messageToSend.setSender("TestUser");

        Message sentMessage = this.restTemplate.postForObject(
            "http://localhost:" + port + "/messages", 
            messageToSend,                            
            Message.class                             
        );
        assertThat(sentMessage).isNotNull();
        assertThat(sentMessage.getId()).isNotNull();
        assertThat(sentMessage.getContent()).isEqualTo("Hello from Test!");
        
        List<Message> receivedMessages = this.restTemplate.exchange(
            "http://localhost:" + port + "/messages", 
            HttpMethod.GET,                           
            null,                                     
            new ParameterizedTypeReference<List<Message>>() {}
        ).getBody(); 

        
        assertThat(receivedMessages).isNotNull();
        assertThat(receivedMessages).isNotEmpty();
        
        assertThat(receivedMessages).anyMatch(m -> m.getContent().equals("Hello from Test!"));

        System.out.println("Received messages: " + receivedMessages.size());
        for (Message message : receivedMessages) {
            System.out.println("Message ID: " + message.getId() + ", Sender: " + message.getSender() + ", Content: " + message.getContent());
        }
    }
}

package live.chat.live_chat.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import live.chat.live_chat.model.Message;


@RestController
@RequestMapping("/messages")
public class MessageController {
    private final List<Message> messages = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    @PostMapping
    public Message sendMessage(@RequestBody Message message) {
        message.setId(idCounter.incrementAndGet());
        if (message.getTimestamp() == null) {
            message.setTimestamp(String.valueOf(System.currentTimeMillis()));
        }
        messages.add(message);
        return message;
    }
    
    @GetMapping
    public List<Message> getMessages() {
        return messages;
    }

}

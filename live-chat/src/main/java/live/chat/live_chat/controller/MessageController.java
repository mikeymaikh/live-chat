package live.chat.live_chat.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import live.chat.live_chat.model.Message;
import live.chat.live_chat.model.MessageRepository;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    // Support both multipart and form data
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> sendMessage(
            @RequestParam("senderId") Long senderId,
            @RequestParam("receiverId") Long receiverId,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Message message = new Message();
            message.setSenderId(senderId);
            message.setReceiverId(receiverId);
            message.setContent(content);
            message.setTimestamp(System.currentTimeMillis());

            if (file != null && !file.isEmpty()) {
                // Store file data in database
                message.setMessageType("file");
                message.setFileName(file.getOriginalFilename());
                message.setFileType(file.getContentType());
                message.setFileSize(String.valueOf(file.getSize()));
                message.setFileData(file.getBytes());
            } else {
                message.setMessageType("text");
            }

            Message savedMessage = messageRepository.save(message);
            return ResponseEntity.status(201).body(savedMessage);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error saving message: " + e.getMessage());
        }
    }
    
    @GetMapping
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/receiver/{receiverId}")
    public List<Message> getMessagesByReceiverId(@PathVariable Long receiverId) {
        return messageRepository.findByReceiverId(receiverId);
    }

    // Download file from database
    @GetMapping("/download/{messageId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long messageId) {
        Message message = messageRepository.findById(messageId).orElse(null);
        
        if (message == null || message.getFileData() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + message.getFileName() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, message.getFileType());

        return ResponseEntity.ok()
                .headers(headers)
                .body(message.getFileData());
    }
}
package live.chat.live_chat.controller;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.aspectj.apache.bcel.classfile.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public Message sendMessage(@RequestBody Message message) {
        return messageRepository.save(message);
    }
    
    @GetMapping
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/receiver/{receiverId}")
    public List<Message> getMessagesByReceiverId(@PathVariable Long receiverId) {
        return messageRepository.findByReceiverId(receiverId);
    }

    @PostMapping("/api/messages/upload")
    public Message uploadFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("senderId") Long senderId,
        @RequestParam("receiverId") Long receiverId
    ) throws IllegalStateException, IOException {
        if (file.isEmpty() || senderId == null || receiverId == null) {
            return null;
        }

        String fileName = file.getOriginalFilename();
        fileName = fileName + "_" + new Random().nextInt(1000);

        String fileType = file.getContentType();
        Long fileSize = file.getSize();
        String filePath = "/uploads/files/";
        File uploadPath = new File(filePath);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        File serverFile = new File(uploadPath, fileName);
        file.transferTo(serverFile);

        
        Message message = new Message(senderId, receiverId, fileName, fileSize, fileType, filePath, null, null);
        return messageRepository.save(message);
    }
       

}

package com.example.whatsapp_clone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    // This is a temporary method to load initial data
    @GetMapping("/load-data")
    public String loadData() {
        Message msg1 = new Message();
        // Use the new camelCase method names
        msg1.setWaId("919876543210"); 
        msg1.setName("Raju's Test Contact 1");
        msg1.setBody("Hello, this is a test message!");
        msg1.setTimestamp(new Date(Long.parseLong("1653467664") * 1000));
        msg1.setMetaMsgId("wamid.HBgMOTE5ODc2NTQzMjEwFQIAERgSQTgyQTQzMDlEMEEyMTZGMzdGAA==");
        messageRepository.save(msg1);

        Message msg2 = new Message();
        // Use the new camelCase method names
        msg2.setWaId("911234567890");
        msg2.setName("Raju's Test Contact 2");
        msg2.setBody("Hi there! How are you?");
        msg2.setTimestamp(new Date(Long.parseLong("1653467665") * 1000));
        msg2.setMetaMsgId("wamid.HBgMOTE5ODc2NTQzMjEwFQIAERgSQTgyQTQzMDlEMEEyMTZGMzdGAB==");
        messageRepository.save(msg2);

        return "Sample data loaded successfully!";
    }


    @GetMapping("/conversations")
    public List<Conversation> getConversations() {
        return messageRepository.findAll().stream()
                .collect(Collectors.groupingBy(Message::getWaId)) // <-- Updated here
                .entrySet().stream()
                .map(entry -> {
                    Message lastMessage = entry.getValue().stream()
                            .max((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()))
                            .orElse(null);
                    return new Conversation(lastMessage.getName(), lastMessage.getWaId(), lastMessage.getBody(), lastMessage.getTimestamp()); // <-- Updated here
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/messages/{wa_id}")
    public List<Message> getMessagesByUser(@PathVariable String wa_id) {
        // Updated to the correct repository method name
        return messageRepository.findByWaId(wa_id);
    }

    @PostMapping("/messages")
    public Message sendMessage(@RequestBody Message message) {
        message.setTimestamp(new Date());
        message.setStatus("sent");
        return messageRepository.save(message);
    }
}

// Updated this helper class as well
class Conversation {
    private String name;
    private String waId; // <-- Updated here
    private String lastMessage;
    private Date timestamp;

    public Conversation(String name, String waId, String lastMessage, Date timestamp) { // <-- Updated here
        this.name = name;
        this.waId = waId; // <-- Updated here
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getWaId() { return waId; } // <-- Updated here
    public void setWaId(String waId) { this.waId = waId; } // <-- Updated here
    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
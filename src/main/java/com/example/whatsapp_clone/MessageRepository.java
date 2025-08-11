package com.example.whatsapp_clone;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    
    // --- THIS IS THE CHANGE ---
    // Changed back to findByWaId to match the new variable name in Message.java
    List<Message> findByWaId(String waId);
    // --------------------------
}
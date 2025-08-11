package com.example.whatsapp_clone;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field; // <-- IMPORT THIS
import java.util.Date;

@Data
@Document("processed_messages")
public class Message {

    @Id
    private String id;

    // --- THIS IS THE CHANGE ---
    @Field("wa_id") // Links this variable to the "wa_id" field in MongoDB
    private String waId; // Changed from wa_id to waId (Java convention)
    // -------------------------

    private String name;
    private String body;
    private Date timestamp;
    private String status = "sent";

    // --- THIS IS ALSO CHANGED ---
    @Field("meta_msg_id")
    private String metaMsgId; // Changed from meta_msg_id to metaMsgId
    // -------------------------
}
package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

/*
Supposes a header.
 */
@JsonAutoDetect
@NoArgsConstructor
public class EncodedMessage {
    @JsonProperty("Message")
    // Consists hex symbols.
    public String message;
}

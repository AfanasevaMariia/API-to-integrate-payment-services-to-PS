package com.example.demo.models;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@JsonAutoDetect
@NoArgsConstructor
public class ParsedMessage {
    public String header;
    public String mti;
    public HashMap<Integer, ParsedField> fields = new HashMap<Integer, ParsedField>();

    public void addField(ParsedField field) {
        fields.put(field.id, field);
    }
}

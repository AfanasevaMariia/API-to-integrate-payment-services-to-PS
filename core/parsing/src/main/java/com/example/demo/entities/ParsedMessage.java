package com.example.demo.entities;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@JsonAutoDetect
@NoArgsConstructor
public class ParsedMessage {
    private String header;
    private String mti;
    private HashMap<Integer, ParsedField> fields = new HashMap<Integer, ParsedField>();

    // Getters and Setters.
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public HashMap<Integer, ParsedField> getFields() {
        return fields;
    }

    public void addField(ParsedField field) {
        fields.put(field.getId(), field);
    }
    // The end of the Getters and Setters.
}

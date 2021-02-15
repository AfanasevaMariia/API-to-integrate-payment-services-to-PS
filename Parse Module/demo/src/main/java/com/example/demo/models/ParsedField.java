package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imohsenb.ISO8583.enums.FIELDS;
import com.imohsenb.ISO8583.exceptions.ISOException;
import com.imohsenb.ISO8583.utils.StringUtil;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@JsonAutoDetect
@NoArgsConstructor
public class ParsedField {
    public int id;
    public String type;
    public String body;
    public boolean hasElements = false;
    public HashMap<Integer, Element> elements = new HashMap<Integer, Element>();

    @JsonIgnore
    public String getBodyOrElementsHexStr() throws ISOException {
        if (hasElements)
            // The parsedField contains content in its elements.
            return getElementsHexStr();
        else
            // The parsedField contains content in the body directly.
            return StringUtil.asciiToHex(body);
    }

    @JsonIgnore
    public String getBodyOrElementsStr() throws ISOException {
        if (hasElements)
            // The parsedField contains content in its elements.
            return getElementsStr();
        else
            // The parsedField contains content in the body directly.
            return body;
    }

    private String getElementsHexStr() throws ISOException {
        if (!hasElements)
            throw new ISOException("Field has not elements!");
        StringBuilder elementsStr = new StringBuilder();
        for (Integer id : elements.keySet())
            elementsStr.append(elements.get(id).getHexString());
        return elementsStr.toString();
    }

    private String getElementsStr() throws ISOException {
        if (!hasElements)
            throw new ISOException("Field has not elements!");
        StringBuilder elementsStr = new StringBuilder();
        for (Integer id : elements.keySet())
            elementsStr.append(elements.get(id).toString());
        return elementsStr.toString();
    }

    /*
    Returns the prefix of the length of the field if it has the mutable length.
    The length of a mutable field is represented by 4 decimal numbers.
    If the length takes not all the ranks, it has the front zeros.
     */
    @JsonIgnore
    public String getFieldLengthStr() throws ISOException {
        FIELDS fieldImage = FIELDS.valueOf(id);
        if (fieldImage.isFixed())
            throw new ISOException("Field has not the prefix of the length!");
        if (body == null)
            throw new ISOException("Field has not a body!");
        int length = body.length();
        StringBuilder lengthStr = new StringBuilder(Integer.toString(length));
        while(lengthStr.length() < 4)
            lengthStr.insert(0, 0);
        return lengthStr.toString();
    }
}

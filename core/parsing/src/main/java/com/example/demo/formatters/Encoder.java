package com.example.demo.formatters;

import com.example.demo.entities.*;
import com.imohsenb.ISO8583.builders.ISOMessageBuilder;
import com.imohsenb.ISO8583.entities.ISOMessage;
import com.imohsenb.ISO8583.enums.FIELDS;
import com.imohsenb.ISO8583.exceptions.ISOException;
import com.imohsenb.ISO8583.utils.StringUtil;

import java.util.HashMap;
import java.util.Set;

public class Encoder {

    // The header which is added to the beginning of a message
    // because in the program the headers are not used
    // but are required by the library library ISO-8583 Java Lib.
    // It seems that a message can be built without header but it needs to be acknowledged.
    //
    private static String defaultHeader = "0000000000";

    /*
    Converts the EncodedMessage into the ParsedMessage.
    At first into the ISOMessage and than into the ParsedMessage.
     */
    public ParsedMessage getParsedMessage(EncodedMessage encodedMessage) throws ISOException {
        // Transformation of the encodeMessage to the isoMessage.
        String encodedMessageWithHeader = encodedMessage.message;
        ISOMessage isoMessage;
        try {
            isoMessage = ISOMessageBuilder.Unpacker()
                    .setMessage(encodedMessageWithHeader)
                    .build();
            System.out.println("isoMessage.toString() = " + isoMessage);
        } catch (ISOException ex) {
            throw new ISOException("ISO-message has an incorrect format! " +
                    "It can be the using of letters in mti," +
                    "the using of unallowable letters in the body" +
                    "or the too short length of the message.");
        }
        // Transformation of the parsedMessage into the getParsedMessage.
        ParsedMessage parsedMessage = getParsedMessage(isoMessage);
        return parsedMessage;
    }

    /*
   Returns the encodedMessage the folders of which match to
   the values of the transmitted parsedMessage.
   Accepts a parsedMessage.
    */
    public EncodedMessage getEncodedMessage(ParsedMessage parsedMessage) throws ISOException {
        StringBuilder message = new StringBuilder();
        message.append(parsedMessage.getHeader());
        message.append(parsedMessage.getMti());
        // Formation of the primaryBitmap and extracting of the hex bodies of fields into the content.
        byte[] primaryBitmap = new byte[64];
        StringBuilder content = new StringBuilder();
        HashMap<Integer, ParsedField> parsedFields = parsedMessage.getFields();
        for (Integer id : parsedFields.keySet()) {
            ParsedField parsedField = parsedFields.get(id);
            // Marking of the bit.
            primaryBitmap[id - 1] = 1;
            // Addition of the length prefix.
            FIELDS fieldImage = FIELDS.valueOf(parsedField.getId());
            if (!fieldImage.isFixed()) {
                content.append(parsedField.getFieldLengthStr());
            }
            // Addition of the body or elements.
            content.append(parsedField.getBodyOrElementsHexStr());
        }
        String primaryBitmapHexStr = convertBinPrimaryBitmapToHexStr(primaryBitmap);
        // The transformation of the bits array into the byte array and then into the hex String.
        message.append(primaryBitmapHexStr);
        message.append(content);

        EncodedMessage encodedMessage = new EncodedMessage();
        encodedMessage.message = message.toString();
        return encodedMessage;
    }

    private static String convertBinPrimaryBitmapToHexStr(byte[] primaryBitmapBin) {
        StringBuilder primaryBitmapHex = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            //System.out.println(primaryBitmapBin[i * 4 + 3]);
            StringBuilder bin = new StringBuilder();
            bin.append(primaryBitmapBin[i * 4]);
            bin.append(primaryBitmapBin[i * 4 + 1]);
            bin.append(primaryBitmapBin[i * 4 + 2]);
            bin.append(primaryBitmapBin[i * 4 + 3]);
            primaryBitmapHex.append(convertBinToHex(bin.toString()));
        }
        return primaryBitmapHex.toString();
    }

    /*
    Converts an ISOMessage into a ParsedMessage.
     */
    private ParsedMessage getParsedMessage(ISOMessage isoMessage) throws ISOException {
        ParsedMessage parsedMessage = new ParsedMessage();
        parsedMessage.setHeader(StringUtil.fromByteArray(isoMessage.getHeader()));
        parsedMessage.setMti(isoMessage.getMti());
        // Fulling of the parsedMessage by parsedFields.
        // The first element means the presence of the secondaryBitmap
        // which is not used in this project.
        for (int numField = 2; numField < 64 + 1; numField++) {
            ParsedField parsedField = new ParsedField();
            parsedField.setId(numField);
            // Getting of the type of the data keeping in the field.
            parsedField.setType(FIELDS.valueOf(numField).getType());
            // Extracting of the body of the field.
            try {
                parsedField.setBody(isoMessage.getStringField(numField, true));
                System.out.println("parsedField.body = " + parsedField.getBody());
                // This field is not present in this message.
            } catch (ISOException ex) {
                System.out.println("Field with the number " + numField + " was omitted.");
                continue;
            }
            // If the field has elements.
            if (FIELDS_WITH_ELEMENTS.isBelong(parsedField.getId())) {
                parsedField.setHasElements(true);
                parsedField.setElements(parseField(parsedField));

                // Print!
                HashMap<Integer, ParsedElement> elements = parsedField.getElements();
                Set keys = elements.keySet();
                System.out.println("elements:");
                for (Object key : keys) {
                    System.out.println("\telem:");
                    ParsedElement parsedElement = elements.get((Integer)key);
                    System.out.println("\t\tid = " + parsedElement.getId());
                    System.out.println("\t\ttype = " + parsedElement.getType());
                    System.out.println("\t\tlength = " + parsedElement.getLength());
                    System.out.println("\t\tbody = " + parsedElement.getBody());
                }
            }
            // Adding of the new parsedField to the parsedMessage.
            parsedMessage.addField(parsedField);
        }
        return parsedMessage;
    }

    /*
    Returns the HashMap of the elements of the field if it has these.
     */
    private HashMap<Integer, ParsedElement> parseField(ParsedField parsedField) throws ISOException {
        if (!parsedField.getHasElements())
            throw new ISOException("Field has no elements!");
        HashMap<Integer, ParsedElement> elements = new HashMap<Integer, ParsedElement>();
        String bodyField = parsedField.getBody();
        // Formation of the elements.
        int indSym = 0;
        while (indSym < bodyField.length()) {
            ParsedElement parsedElement = new ParsedElement();
            // The type of an parsedElement takes 0 positions.
            parsedElement.setType(bodyField.substring(indSym, indSym + 1));
            // The id of an parsedElement takes 1-2 positions.
            parsedElement.setId(Integer.parseInt(bodyField.substring(indSym + 1, indSym + 3), 16));
            // The length of an parsedElement takes 3-4 positions.
            parsedElement.setLength(Integer.parseInt(bodyField.substring(indSym + 3, indSym + 5), 16));
            // The body of an parsedElement takes positions begin at the 5th.
            parsedElement.setBody(bodyField.substring(indSym + 5, indSym + 5 + parsedElement.getLength()));
            // The offset of the indSym to make it the first index of the next parsedElement.
            indSym += 5 + parsedElement.getLength();
            elements.put(parsedElement.getId(), parsedElement);
        }
        return elements;
    }

    /*
   Accepts a binary number which has 4 ranks and converts into a hex number with one rank.
    */
    private static char convertBinToHex(String numBin) {
        switch(numBin) {
            case "0000":
                return '0';
            case "0001":
                return '1';
            case "0010":
                return '2';
            case "0011":
                return '3';
            case "0100":
                return '4';
            case "0101":
                return '5';
            case "0110":
                return '6';
            case "0111":
                return '7';
            case "1000":
                return '8';
            case "1001":
                return '9';
            case "1010":
                return 'A';
            case "1011":
                return 'B';
            case "1100":
                return 'C';
            case "1101":
                return 'D';
            case "1110":
                return 'E';
            case "1111":
                return 'F';
            default:
                return 'Z';
        }
    }
}

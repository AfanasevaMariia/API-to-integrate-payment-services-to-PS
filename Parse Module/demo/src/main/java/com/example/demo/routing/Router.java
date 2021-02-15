package com.example.demo.routing;

import com.example.demo.formatters.Encoder;
import com.example.demo.models.EncodedMessage;
import com.example.demo.models.ParsedField;
import com.example.demo.models.ParsedMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imohsenb.ISO8583.exceptions.ISOException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class Router {

    /*
	Returns the String which is the parsedMessage in the JSON format.
	The parsedMessage is given from an encodedMessage (in the JSON format too).
	 */
    public static String processParseRequest(String encodedMessageJSON) throws IOException, ISOException {
        // Deserialization of the encodedMessage.
        StringReader reader = new StringReader(encodedMessageJSON);
        ObjectMapper mapper = new ObjectMapper();
        EncodedMessage encodedMessage = mapper.readValue(reader, EncodedMessage.class);
        // Formation of the parsedMessage.
        Encoder encoder = new Encoder();
        ParsedMessage parsedMessage = encoder.getParsedMessage(encodedMessage);

        // Print for debugging.
        printParsedMessage(parsedMessage);

        // Serialization of the parsedMessage.
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, parsedMessage);
        String parsedMessageJSON = writer.toString();
        return parsedMessageJSON;
    }

    /*
    Returns the String which is the encodedMessage in the JSON format.
	The encodedMessage is given from a parsedMessage (in the JSON format too).
     */
    public static String processEncodeRequest(String parsedMessageJSON) throws IOException, ISOException {
        // Deserialization of the parsedMessageJSON in the parsedMessage.
        StringReader reader = new StringReader(parsedMessageJSON);
        ObjectMapper mapper = new ObjectMapper();
        ParsedMessage parsedMessage = mapper.readValue(reader, ParsedMessage.class);
        // Formation of the encodedMessage.
        Encoder encoder = new Encoder();
        EncodedMessage encodedMessage = encoder.getEncodedMessage(parsedMessage);

        // Print for debugging.
        printEncodedMessage(encodedMessage);

        // Serialization of the encodedMessage.
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, encodedMessage);
        String encodedMessageJSON = writer.toString();
        return encodedMessageJSON;
    }

    /*
	For debugging. Print of a parseMessage.
	 */
    static void printParsedMessage(ParsedMessage parsedMessage) {
        System.out.println("parsedMessage:");
        System.out.println("mti = " + parsedMessage.mti);
        System.out.println("folders:");
        for (ParsedField parsedField : parsedMessage.fields.values()) {
            System.out.println("\tfield:");
            System.out.println("\t\tid = " + parsedField.id);
            System.out.println("\t\ttype = " + parsedField.type);
            System.out.println("\t\tbody = " + parsedField.body);
        }
    }

    /*
    For debugging. Print of an encodedMessage.
     */
    static void printEncodedMessage(EncodedMessage encodedMessage) {
        System.out.println("encodedMessage = " + encodedMessage.message);
    }
}

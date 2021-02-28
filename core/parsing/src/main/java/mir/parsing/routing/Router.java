package mir.parsing.routing;

import mir.parsing.formatters.Encoder;
import mir.models.EncodedMessage;
import mir.models.ParsedField;
import mir.models.ParsedMessage;
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
    public static ParsedMessage getParsedMessage(String encodedMessageJSON) throws IOException, ISOException {
        // Getting of the encodedMessage.
        EncodedMessage encodedMessage = extractEncodedMessage(encodedMessageJSON);
        // Formation of the parsedMessageM.
        Encoder encoder = new Encoder();
        ParsedMessage parsedMessage = encoder.getParsedMessage(encodedMessage);

        // Print for debugging.
        printParsedMessage(parsedMessage);

        return parsedMessage;
    }

    /*
    Returns the encodedMessage extracted from the JSON format.
     */
    public static EncodedMessage extractEncodedMessage(String encodedMessageJSON) throws IOException {
        // Deserialization of the encodedMessage.
        StringReader reader = new StringReader(encodedMessageJSON);
        ObjectMapper mapper = new ObjectMapper();
        EncodedMessage encodedMessage = mapper.readValue(reader, EncodedMessage.class);
        return encodedMessage;
    }

    /*
    Returns the String which is the encodedMessage in the JSON format.
	The encodedMessage is given from a parsedMessageM.
     */
    public static String formEncodedMessage(ParsedMessage parsedMessage) throws IOException, ISOException {
        // Formation of the encodedMessage.
        Encoder encoder = new Encoder();
        EncodedMessage encodedMessage = encoder.getEncodedMessage(parsedMessage);

        // Print for debugging.
        printEncodedMessage(encodedMessage);
        // Serialization of the encodedMessage.
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(writer, encodedMessage);
        String encodedMessageJSON = writer.toString();
        return encodedMessageJSON;
    }

    /*
	For debugging. Print of a parseMessage.
	 */
    static void printParsedMessage(ParsedMessage parsedMessage) {
        System.out.println("parsedMessageM:");
        System.out.println("mti = " + parsedMessage.getMti());
        System.out.println("folders:");
        for (ParsedField parsedField : parsedMessage.getFields().values()) {
            System.out.println("\tfield:");
            System.out.println("\t\tid = " + parsedField.getId());
            System.out.println("\t\ttype = " + parsedField.getType());
            System.out.println("\t\tbody = " + parsedField.getBody());
        }
    }

    /*
    For debugging. Print of an encodedMessage.
     */
    static void printEncodedMessage(EncodedMessage encodedMessage) {
        System.out.println("encodedMessage = " + encodedMessage.message);
    }
}

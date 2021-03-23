package mir.parsing.routing;

import mir.parsing.formatters.Encoder;
import mir.models.ParsedMessage;
import com.imohsenb.ISO8583.exceptions.ISOException;

import java.io.IOException;

public class Router {

    /*
	Returns the parsedMessage which is given from the hex.
	 */
    public static ParsedMessage getParsedMessage(String hex) throws ISOException {
        // Deserialization of the hex.
        /*StringReader reader = new StringReader(hex);
        ObjectMapper mapper = new ObjectMapper();
        EncodedMessage hex = mapper.readValue(reader, EncodedMessage.class);*/
        // Formation of the parsedMessageM.


        Encoder encoder = new Encoder();
        ParsedMessage parsedMessage = encoder.getParsedMessageFromHex(hex);
        return parsedMessage;
    }

    /*
    Returns the hex which is given from a parsedMessage.
     */
    public static String getEncodedMessage(ParsedMessage parsedMessage) throws IOException, ISOException {
        Encoder encoder = new Encoder();
        String hex = encoder.getHexFromParsedMessage(parsedMessage);
        return hex;

        // Formation of the encodedMessage.
        // Encoder encoder = new Encoder();
        //EncodedMessage encodedMessage = encoder.getHex(parsedMessage);
        // Print for debugging.
        //printEncodedMessage(encodedMessage);
        // Serialization of the encodedMessage.
        //StringWriter writer = new StringWriter();
        //ObjectMapper mapper = new ObjectMapper();
        //mapper.writeValue(writer, encodedMessage);
        //String encodedMessageJSON = writer.toString();
        //return encodedMessageJSON;
    }

    /*
    For debugging. Print of an encodedMessage.
    */
    /*static void printEncodedMessage(EncodedMessage encodedMessage) {
        System.out.println("encodedMessage = " + encodedMessage.message);
    }*/
}

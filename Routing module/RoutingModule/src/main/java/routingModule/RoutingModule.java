package routingModule;

import routingModule.exception.UnsupportedMessageTypeIDException;

public class RoutingModule {
    private static final int MTI_LENGTH = 4;

    /**
     * Router receives message, sends it to Parsing module and then sends to
     * issuer/acquirer module depending on its message type ID.
     * @param message Encoded message
     */
    public static void sendMessage(String message) {
        // Get parsed message (JSON) from Parsing Module to get message type ID.
        Router router = new Router();
        String parsedMessage = router.processParseRequest(message);

        // Get message type ID from parsed message
        String messageTypeID = getMessageTypeID(parsedMessage);

        // Get encoded message from Parsing Module which will be sent to other module.
        String encodedMessage = router.processEncodeRequest(parsedMessage);

        try {
            // Send message to other module.
            sendMessage(encodedMessage, messageTypeID);
        } catch (UnsupportedMessageTypeIDException ex) {
            // Failed to send message.
            // TODO: how to react to the exception?
        }
    }

    /**
     * Router sends message to issuer or acquirer depending on parsed message type ID.
     * @param message Encoded message
     * @param mti Message type ID
     */
    private static void sendMessage(String message, String mti) throws UnsupportedMessageTypeIDException {
        switch (mti) {
            case "0100":
                // issuerModule.getMessage(message);
                return;
            case "0110":
                // acquirerModule.getMessage(message);
                return;
            default:
                throw new UnsupportedMessageTypeIDException(String.format("Message type IDs supported: 0100, 0110. Received: %s", mti));
        }
    }

    /**
     * Returns parsed message type ID.
     * @param message Parsed message.
     * @return Four-digit message type ID.
     */
    private static String getMessageTypeID(String message) {
        // Index where message type ID field content starts.
        int mtiIndex = message.indexOf("mti") + 6;

        return message.substring(mtiIndex, mtiIndex + MTI_LENGTH);
    }
}

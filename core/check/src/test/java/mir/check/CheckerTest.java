package mir.check;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imohsenb.ISO8583.builders.ISOMessageBuilder;
import com.imohsenb.ISO8583.entities.ISOMessage;
import com.imohsenb.ISO8583.enums.*;
import com.imohsenb.ISO8583.exceptions.ISOException;
import com.imohsenb.ISO8583.utils.StringUtil;
import mir.models.*;
import mir.parsing.routing.Router;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckerTest {

    @Test
    void testParsedMessage() throws IOException, ISOException, NoSuchFieldException, IllegalAccessException {

        String encodedMessageJSON = generateEncodedMessageJSONWithPrint();
        ParsedMessage parsedMessage = Router.getParsedMessage(encodedMessageJSON);
        System.out.println("getParsedMessage is ended");

        parsedMessage.setTransactionDate(LocalDateTime.now());
        Checker checker = new Checker();
        List<MessageError> errors = checker.checkParsedMessage(parsedMessage);
        System.out.println("Errors:");
        for (MessageError error : errors)
            System.out.println(error.getMessage());
    }

    /*
    Tests the correctness of the mti.
    The mti value must belong to the allowable set of values.
    There this condition is provided.
    The test must fail if the mistake about the mti is included in errors list.
     */
    @Test
    void testParsedMessageMtiCorrect() throws IOException, ISOException, NoSuchFieldException, IllegalAccessException {
        // 3, 48.
        // String probaMessage = "0100200000000001000092000000092536313034696E666F";
        // String probaMessage = "010000000001000100000023200022536313332";

        // Test of the table.
        // 22 (n, 3)
        //String probaMessage = "010000000400000000008108"; // +
        // 32 (n, 1-11, LL) - Here the length of a number is twice less than its length according to the MIP (1 num = 1/2 byte).
         String probaMessage = "01000000000100000000030123"; //
        // 52 (b, 8)
        // String probaMessage = "0100000000000000100088888888"; //
        // 39 (an, 2) - Here the length of a number is equal to its length according to the MIP (1 num = 1 byte).
        // String probaMessage = "010000000000020000003339"; //
        // 41 (ans, 8)
        // String probaMessage = "01000000000000800000303132333435363738"; //
        // 35 (ans, 1-37, LL)
        // String probaMessage = "01000000000020000000023337"; //
        // 48 (ans, 6-999, LLL)
        // The 97th subfield of the 48th field.
        // String probaMessage = "010000000000000100000009253631999999999"; // len of the 48th field = 0009. //
        ISOMessage isoMessageProba = null;
        try {
            isoMessageProba = ISOMessageBuilder.Unpacker()
                    .setMessage(probaMessage)
                    .build();
        }
        catch (StringIndexOutOfBoundsException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        catch (ISOException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        System.out.println(isoMessageProba.toString());
        int fieldId = 32;
        String fieldStr = null;
        if (FIELDS.valueOf(fieldId).getType().compareTo("n") == 0 ||
                FIELDS.valueOf(fieldId).getType().compareTo("b") == 0) {
            fieldStr = isoMessageProba.getStringField(fieldId);
            System.out.println(Integer.parseInt(fieldStr));
        }
        else
            fieldStr = StringUtil.hexToAscii(isoMessageProba.getStringField(fieldId));
        System.out.println(fieldStr);

        //System.out.println(isoMessageProba.getStringField(48));

        // The 48th field. Without the 2th subfield of tfe 63th field.
        //String initialHex = "0100000000000001000000165E303130333131315E30333033313135"; // LL before the 48th field.
        // The 97th subfield of the 48th field.
        //String initialHex = "0100000000000001000000092536313034696E666F"; // len of the 48th field = 0009.

        // String encodedMessageJSON = generateEncodedMessageJSON(initialHex);

        //String encodedMessageJSON = generateEncodedMessageJSON(probaMessage);

        ParsedMessage parsedMessage = Router.getParsedMessage(probaMessage);
        parsedMessage.setTransactionDate(LocalDateTime.now());
        printParsedMessage(parsedMessage);

        Checker checker = new Checker();
        List<MessageError> errors = checker.checkParsedMessage(parsedMessage);
        System.out.println("Errors:");
        for (MessageError error : errors) {
            System.out.println("error: " + error.getMessage());
            if (error.getMessage().compareTo("The value of the mti of the ParsedMessage" +
                    " is not one from the allowable set!") == 0)
                fail("The error of the incorrect mti.");
        }
        // Additional information.
        // String encodedMessageJSOMBack = Router.getEncodedMessage(parsedMessage);
        // System.out.println("getEncodedMessage is ended");
    }

    /*
    Tests the correctness of the mti.
    The mti value must belong to the allowable set of values.
    There this condition is not provided.
    The test must be successful if the mistake about the mti is included in errors list.
    */
    @Test
    void testParsedMessageMtiIncorrect()
            throws IOException, ISOException, NoSuchFieldException, IllegalAccessException {
        ParsedMessage parsedMessage2 = new ParsedMessage();
        System.out.println(parsedMessage2.getClass().getSimpleName());

        // The 48th field. Without the 2th subfield of tfe 63th field.
        String initialHex = "0800000000000001000000165E303130333131315E30333033313135"; // LL before the 48th field.
        String encodedMessageJSON = generateEncodedMessageJSON(initialHex);
        ParsedMessage parsedMessage = Router.getParsedMessage(encodedMessageJSON);
        System.out.println("getParsedMessage is ended");

        Checker checker = new Checker();
        List<MessageError> errors = checker.checkParsedMessage(parsedMessage);
        System.out.println("Errors:");
        for (MessageError error : errors) {
            System.out.println("error: " + error.getMessage());
            if (error.getMessage().compareTo("The value of the mti of the ParsedMessage" +
                    " is not one from the allowable set!") == 0)
                fail("The error of the incorrect mti.");
        }
        // Additional information.
        String encodedMessageJSOMBack = Router.getEncodedMessage(parsedMessage);
        System.out.println("getEncodedMessage is ended");
    }

    /*
    Tests the correctness of transactionNumber.
    It provides also the check of the presence of the 2th subfield of the 63th field
    which keeps the transaction number set by an external service.
     */
    @Test
    void testParsedMessageCorrectTransactionNumber() throws ISOException, IOException, NoSuchFieldException, IllegalAccessException {
        // 22 (n, 3)
        String probaMessage = "01000000000000000002";
        ISOMessage isoMessageProba = ISOMessageBuilder.Unpacker()
                .setMessage(probaMessage)
                .build();
        //System.out.println(isoMessageProba.toString());
        int fieldId = 22;
        String fieldStr = null;
        if (FIELDS.valueOf(fieldId).getType().compareTo("n") == 0) {
            fieldStr = isoMessageProba.getStringField(fieldId);
            System.out.println(Integer.parseInt(fieldStr));
        }
        else
            fieldStr = StringUtil.hexToAscii(isoMessageProba.getStringField(fieldId));
        System.out.println(fieldStr);

        //System.out.println(isoMessageProba.getStringField(48));

        // The 48th field. Without the 2th subfield of tfe 63th field.
        //String initialHex = "0100000000000001000000165E303130333131315E30333033313135"; // LL before the 48th field.
        // The 97th subfield of the 48th field.
        //String initialHex = "0100000000000001000000092536313034696E666F"; // len of the 48th field = 0009.

        // String encodedMessageJSON = generateEncodedMessageJSON(initialHex);
        String encodedMessageJSON = generateEncodedMessageJSON(probaMessage);

        ParsedMessage parsedMessage = Router.getParsedMessage(encodedMessageJSON);
        parsedMessage.setTransactionDate(LocalDateTime.now());
        printParsedMessage(parsedMessage);

        Checker checker = new Checker();
        List<MessageError> errors = checker.checkParsedMessage(parsedMessage);
        System.out.println("Errors:");
        for (MessageError error : errors) {
            System.out.println("error: " + error.getMessage());
            if (error.getMessage().compareTo("The value of the mti of the ParsedMessage" +
                    " is not one from the allowable set!") == 0)
                fail("The error of the incorrect mti.");
        }
    }

    private static String generateEncodedMessageJSON(String hex) throws IOException {
        EncodedMessage myEncodedMessage = new EncodedMessage();
        myEncodedMessage.message = hex;

        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(writer, myEncodedMessage);
        // It is that a service sends to the Platform.
        String encodedMessageJSON = writer.toString();
        return encodedMessageJSON;
    }

    private static void allProcess() throws ISOException, IOException {
        // ---------------------------------------------------------------------------------------
        // On the side of the services.

        // Creation an unparsed ISO-message.
        ISOMessage isoMessageService = ISOMessageBuilder.Unpacker()
                .setMessage(generateISOMessage())
                .build();
        // Creation of the JSON-wrapper for the ISO-message.
        EncodedMessage encodedServiceMessage = new EncodedMessage();
        // isoMessage without a header like it some service makes.
        encodedServiceMessage.message =
                isoMessageService.toString()
                        .substring(10 /*after the end of a header*/, isoMessageService.toString().length());
        System.out.println("encodedServiceMessage.message = " + encodedServiceMessage.message);
        System.out.println();


        String encodedMessageJSON = generateEncodedMessageJSONWithPrint();
        // ----------------------------------------------------------------------------------------

        // ----------------------------------------------------------------------------------------
        // On the side of the Platform.

        // For parsing.
        // Like we has accepted this from the routing module.
        String encodedMessageJSONFromService = encodedMessageJSON;
        Router router = new Router();
        // This is that will be sent to routing module back.
        ParsedMessage parsedMessage = router.getParsedMessage(encodedMessageJSONFromService);

        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        // The serialization for debugging.
        writer = new StringWriter();
        mapper.writeValue(writer, parsedMessage);

        // For encoding.
        String parsedMessageJSON2 = writer.toString();
        String encodedMessageJSON2 = router.getEncodedMessage(parsedMessage);
    }

    /*
    Generates an ISO-message without a header.
     */
    private static String generateEncodedMessageJSONWithPrint() throws ISOException, IOException {
        // Without a header.
        // Fields: 48
        // String myMessage = "0800000000000001000000165E303130333131315E30333033313135"
        // Fields: 37, 48
        // 37: "YJJJHHNNNNNN" - 136500
        // 37 requires 07 - "MMDDhhmmss" to get HH from hh
        // 37 requires 11 - to get NNNNNN from
        // Fields: /*11! (STAN)*/, 48
        String myMessage = "0800000000000001000000165E303130333131315E30333033313135"; // LL before the 48th field.
        //String myMessage = "080020380000000000009200000000010231200332"; // Fields: 3, 11!, 12, 13
        // String myMessage = "080020380000000010009200000000010231200332"
        System.out.println("myMessage = " + myMessage);
        ISOMessage myISOMessage = ISOMessageBuilder.Unpacker()
                .setMessage(myMessage)
                .build();
        System.out.println("1 = " + StringUtil.fromByteArray(myISOMessage.getBody()));


        String myMessage2 = "08002038000000010000920000000001023120033200165E303130333131315E30333033313135";
        ISOMessage myISOMessage2 = ISOMessageBuilder.Unpacker()
                .setMessage(myMessage2)
                .build();
        System.out.println("2 = " + StringUtil.fromByteArray(myISOMessage2.getBody()));


        System.out.println("F48. Hex = " + myISOMessage.getStringField(48, false));
        System.out.println("F48. ASCII = " + myISOMessage.getStringField(48, true));
        System.out.println("F48. Length = " + myISOMessage.getStringField(48, true).length());
        /*System.out.println("Hex = " + myISOMessage.getStringField(3, false));
        System.out.println("ASCII = " + myISOMessage.getStringField(3, true));
        System.out.println("Length = " + myISOMessage.getStringField(3, true).length());*/
        // System.out.println((int)'^');
        // System.out.println((int)'%');
        EncodedMessage myEncodedMessage = new EncodedMessage();
        myEncodedMessage.message = myMessage;

        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        //mapper.writeValue(writer, encodedServiceMessage);
        mapper.writeValue(writer, myEncodedMessage);
        // It is that a service sends to the Platform.
        String encodedMessageJSON = writer.toString();
        return encodedMessageJSON;
    }

    /*
	For debugging. Returns a generated ISOMessage.
	 */
    private static String generateISOMessage() throws ISOException {
        ISOMessage isoMessageService = new ISOMessage();
        try {
            isoMessageService = ISOMessageBuilder.Packer(VERSION.V1987)
                    .networkManagement()
                    .mti(MESSAGE_FUNCTION.Request, MESSAGE_ORIGIN.Acquirer)
                    .processCode("920000")
                    .setField(FIELDS.F11_STAN,  "1")
                    .setField(FIELDS.F12_LocalTime,  "023120")
                    .setField(FIELDS.F13_LocalDate,  "0332")
                    //.setField(FIELDS.F32_AcquiringInstitutionIdCode, "22222")
                    //.setField(FIELDS.F45_Track1, "7777777777777777777777777777777777777777777777777777777777777777777777777777")
                    .setField(FIELDS.F48_AddData_Private, "^0103222^97043937") // "^0103222"
                    //.setField(FIELDS.F57_Reserved_National, "%^222")
                    //.setField(FIELDS.F24_NII_FunctionCode,  "333")
                    .setHeader("0000000000") // "0000000000"
                    .build();
        } catch (ISOException e) {
            e.printStackTrace();
        }
        System.out.println("generated isoMessage = " + isoMessageService.toString());
        System.out.println("generated isoMessage. The initial form of the 48th field = "
                + isoMessageService.getStringField(48, true));
        System.out.println("primaryBitmap = " + StringUtil.fromByteArray(isoMessageService.getPrimaryBitmap()));
        System.out.println("length of the 48th field in the initial representation = " +
                isoMessageService.getField(48).length);
        System.out.println("The 48th field = " + isoMessageService.getStringField(48));
        return isoMessageService.toString();
    }

    private static void printParsedMessage(ParsedMessage parsedMessage) {
        System.out.println("ParsedMessage:");
        System.out.println("\tid = " + parsedMessage.getId());
        System.out.println("\tmti = " + parsedMessage.getMti());
        System.out.println("\thex = " + parsedMessage.getHex());
        System.out.println("\tedited = " + parsedMessage.getEdited());
        System.out.println("\ttransactionDate = " + parsedMessage.getTransactionDate());
        System.out.println("\ttransactionNumber = " + parsedMessage.getTransactionNumber());
        System.out.println("\tParsedFields:");
        printParsedFields(parsedMessage);
    }

    private static void printParsedFields(ParsedMessage parsedMessage) {
        HashMap<Integer, ParsedField> fields = parsedMessage.getFields();
        for (ParsedField parsedField : fields.values()) {
            System.out.println("\t\tParsedField:");
            System.out.println("\t\t\tid = " + parsedField.getId());
            System.out.println("\t\t\ttype = " + parsedField.getType());
            System.out.println("\t\t\tlengthMIP = " + parsedField.getLengthMIP());
            // Todo: remove the print.
            // System.out.println("\t\t\tlengthReal = " + parsedField.getLengthInSymbolsReal());
            System.out.println("\t\t\tcontent = " + parsedField.getContent());
            System.out.println("\t\t\thasSubfields = " + parsedField.getHasSubfields());
            System.out.println("\t\t\thasElements = " + parsedField.getHasElements());
            System.out.println("\t\t\tSubfields:");
            printParsedSubfields(parsedField);
            System.out.println("\t\t\tElements:");
            printParsedElements(parsedField);
        }
    }

    private static void printParsedSubfields(ParsedField parsedField) {
        HashMap<Integer, ParsedSubfield> subfields = parsedField.getSubfields();
        for (ParsedSubfield parsedSubfield : subfields.values()) {
            System.out.println("\t\t\t\tSubfield:");
            System.out.println("\t\t\t\t\tid = " + parsedSubfield.getId());
            System.out.println("\t\t\t\t\ttype = " + parsedSubfield.getType());
            System.out.println("\t\t\t\t\tlengthMIP = " + parsedSubfield.getLengthMIP());
            // Todo: remove this print.
            //System.out.println("\t\t\t\t\tlengthReal = " + parsedSubfield.getLengthInSymbolsReal());
            System.out.println("\t\t\t\t\tcontent = " + parsedSubfield.getContent());
        }
    }

    private static void printParsedElements(ParsedField parsedField){
        HashMap<Integer, ParsedElement> elements = parsedField.getElements();
        Set keys = elements.keySet();
        for (Object key : keys) {
            System.out.println("\t\t\t\tElement:");
            ParsedElement parsedElement = elements.get(key);
            System.out.println("\t\t\t\t\tid = " + parsedElement.getId());
            System.out.println("\t\t\t\t\ttype = " + parsedElement.getType());
            System.out.println("\t\t\t\t\tlengthMIP = " + parsedElement.getLengthMIP());
            System.out.println("\t\t\t\t\tlengthReal = " + parsedElement.getLengthMIP());
            System.out.println("\t\t\t\t\tcontent = " + parsedElement.getContent());
        }
    }
}
package mir.parsing.formatters;

import com.imohsenb.ISO8583.exceptions.ISOException;
import mir.models.EncodedMessage;
import mir.models.ParsedField;
import mir.models.ParsedMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class EncoderTest {

    Encoder encoder;
    EncodedMessage encodedMessage;
    ParsedMessage parsedMessage;

    @BeforeEach
    void setUp(){
        encoder = new Encoder();
        encodedMessage = new EncodedMessage();
        parsedMessage = new ParsedMessage();
    }

    @Test
    void getParsedMessageWrong() {
        encodedMessage.message = "wrong message";
        Exception ex = assertThrows(ISOException.class, () -> encoder.getParsedMessage(encodedMessage));
        assertTrue(ex.getMessage().contains("the using of unallowable letters in the body"));
    }

    @Test
    void getEncodedMessageFieldWithNullBody() {
        //Wrong message
        parsedMessage.setId(0);
        parsedMessage.setMti("0110");
        parsedMessage.setTransactionNumber("123");
        parsedMessage.setTransactionDate(LocalDateTime.now());
        parsedMessage.setEdited(false);

        ParsedField pf = new ParsedField();
        pf.setId(63);
        pf.setContent(null);
        HashMap<Integer, ParsedField> fields = new HashMap<>();
        fields.put(1, pf);

        parsedMessage.setFields(fields);

        encodedMessage.message = "080020380000000000009200000000010231200332";

        Exception ex = assertThrows(ISOException.class, () -> encoder.getEncodedMessage(parsedMessage));
        assertTrue(ex.getMessage().contains("Field has not a body!"));
    }

    @Test
    void getParsedMessageCorrect(){
        //Correct Message
        encodedMessage.message = "080020380000000000009200000000010231200332";

        ParsedField pf_1 = new ParsedField();
        ParsedField pf_2 = new ParsedField();
        ParsedField pf_3 = new ParsedField();
        ParsedField pf_4 = new ParsedField();

        pf_1.setId(3);
        pf_1.setType("n");
        pf_1.setContent("920000");

        pf_2.setId(11);
        pf_2.setType("n");
        pf_2.setContent("000001");

        pf_3.setId(12);
        pf_3.setType("n");
        pf_3.setContent("023120");

        pf_4.setId(13);
        pf_4.setType("n");
        pf_4.setContent("0332");

        HashMap<Integer, ParsedField> fields = new HashMap<>();
        fields.put(3, pf_1);
        fields.put(11, pf_2);
        fields.put(12, pf_3);
        fields.put(13, pf_4);

        parsedMessage.setId(null);
        parsedMessage.setMti("0800");
        parsedMessage.setHex("080020380000000000009200000000010231200332");
        parsedMessage.setTransactionNumber("000001");
        parsedMessage.setTransactionDate(null);
        parsedMessage.setEdited(false);
        parsedMessage.setFields(fields);

        ParsedMessage result = new ParsedMessage();
        try {
            result = encoder.getParsedMessage(encodedMessage);
        } catch (ISOException ignored) { }

        assertEquals(result, parsedMessage);
    }

    @Test
    void getEncodedMessageCorrect() {
        ParsedField pf_1 = new ParsedField();
        ParsedField pf_2 = new ParsedField();
        ParsedField pf_3 = new ParsedField();
        ParsedField pf_4 = new ParsedField();

        pf_1.setId(3);
        pf_1.setType("n");
        pf_1.setContent("920000");

        pf_2.setId(11);
        pf_2.setType("n");
        pf_2.setContent("000001");

        pf_3.setId(12);
        pf_3.setType("n");
        pf_3.setContent("023120");

        pf_4.setId(13);
        pf_4.setType("n");
        pf_4.setContent("0332");

        HashMap<Integer, ParsedField> fields = new HashMap<>();
        fields.put(3, pf_1);
        fields.put(11, pf_2);
        fields.put(12, pf_3);
        fields.put(13, pf_4);

        parsedMessage.setId(null);
        parsedMessage.setMti("0800");
        parsedMessage.setHex("080020380000000000009200000000010231200332");
        parsedMessage.setTransactionNumber("000001");
        parsedMessage.setTransactionDate(null);
        parsedMessage.setEdited(false);
        parsedMessage.setFields(fields);

        encodedMessage.message = "080020380000000000009200000000010231200332";

        EncodedMessage result = new EncodedMessage();
        try {
            result = encoder.getEncodedMessage(parsedMessage);
        } catch (ISOException ignored) {}

        assertEquals(result, encodedMessage);
    }
}
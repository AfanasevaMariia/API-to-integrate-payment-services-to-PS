package mir.parsing.formatters;

import com.imohsenb.ISO8583.builders.ISOMessageBuilder;
import com.imohsenb.ISO8583.entities.ISOMessage;
import com.imohsenb.ISO8583.enums.FIELDS;
import com.imohsenb.ISO8583.enums.SUBFIELDS;
import com.imohsenb.ISO8583.exceptions.ISOException;
import com.imohsenb.ISO8583.utils.StringUtil;
import mir.models.*;

import java.util.HashMap;
import java.util.TreeMap;

public class Encoder {

    /*
    Converts the hex into the ParsedMessage.
     */
    public ParsedMessage getParsedMessageFromHex(String hex) throws ISOException {
        // Transformation of the hex to the isoMessage.
        ISOMessage isoMessage = ISOMessageBuilder.Unpacker()
                    .setMessage(hex)
                    .build();
        // Transformation of the parsedMessage into the getParsedMessage.
        ParsedMessage parsedMessage = getParsedMessageFromISO(isoMessage);
        return parsedMessage;
    }

    /*
    Returns the encodedMessage the folders of which match to
    the values of the transmitted parsedMessage.
    Accepts a parsedMessage.
    */
    public String getHexFromParsedMessage(ParsedMessage parsedMessage) throws ISOException {
        StringBuilder message = new StringBuilder();
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

        return message.toString();
    }

    private static String convertBinPrimaryBitmapToHexStr(byte[] primaryBitmapBin) {
        StringBuilder primaryBitmapHex = new StringBuilder();
        for (int i = 0; i < 16; i++) {
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
    private ParsedMessage getParsedMessageFromISO(ISOMessage isoMessage) throws ISOException {
        ParsedMessage parsedMessage = new ParsedMessage();
        parsedMessage.setMti(isoMessage.getMti());
        parsedMessage.setHex(StringUtil.fromByteArray(isoMessage.getBody()));
        TreeMap<Integer, byte[]> fields = isoMessage.getFields();
        for (int fieldId : fields.keySet()) {
            ParsedField parsedField = new ParsedField();
            // Setting of the id.
            parsedField.setId(fieldId);
            FIELDS field = FIELDS.valueOf(fieldId);
            // Setting of the type.
            parsedField.setType(field.getType());
            // Setting of the content.
            parsedField.setContent(isoMessage.getStringField(fieldId, true));
            // TODO: remove println.
            System.out.println("content before the conversion= " + parsedField.getContent());
            // TODO: check the correctness of the method below.
            parsedField.setSubfields(getContentOfSubfields(parsedField));
            parsedField.setElements(getContentOfElements(parsedField));
            // TODO: remove println.
            System.out.println("content after the conversion= " + parsedField.getContent());
            // Setting of the lengthMIP.
            parsedField.setLengthMIP(getLengthMIPOfParsedField(isoMessage, parsedField));
            // Todo: remove two links below.
            // Setting of the lengthRealBytes.
            //parsedField.setLengthInSymbolsReal(getLengthRealBytesForParsedField(isoMessage, parsedField, field));

            parsedMessage.addField(parsedField);
        }
        // Setting of the transaction number must happen after the parsing of fields
        // because this value is taken from one of fields.
        setTransactionNumber(parsedMessage);
        return parsedMessage;
    }

    private static int getLengthMIPOfParsedField(ISOMessage isoMessage, ParsedField parsedField) {
        if (!parsedField.getHasSubfields() && !parsedField.getHasElements()) {
            int fieldId = parsedField.getId();
            FIELDS field = FIELDS.valueOf(fieldId);
            if (field.isFixed())
                return field.getMaxLength();
            else
                return isoMessage.getFieldLength(fieldId);
        }
        int length = 0;
        if (parsedField.getHasSubfields()) {
            for (ParsedSubfield parsedSubfield : parsedField.getSubfields().values())
                length += parsedSubfield.getLengthMIP();
        }
        // parsedField.getHasElements()
        else {
            for (ParsedElement parsedElement : parsedField.getElements().values())
                // type + id + length + content.
                length += 1 + 2 + 2 + parsedElement.getLengthMIP();
        }
        return length;
    }

    private static int getLengthRealBytesForParsedField(ISOMessage isoMessage, ParsedField parsedField, FIELDS field) {
        int length;
        if (!parsedField.getHasSubfields() && !parsedField.getHasElements())
            length = getLengthRealBytesForParsedFieldWithoutSubfieldsOrElements(isoMessage, parsedField, field);
        else {
            length = 0;
            // TODO: add to the documentation.
            // Only fixed fields are considered!
            if (parsedField.getHasSubfields())
                for (ParsedSubfield parsedSubfield : parsedField.getSubfields().values())
                    length += parsedSubfield.getContent().length();
            // parsedField.getHasElements()
            else {
                for (ParsedElement parsedElement : parsedField.getElements().values())
                    length += parsedElement.getContent().length();
            }
        }
        return length;
    }

    private static int getLengthRealBytesForParsedFieldWithoutSubfieldsOrElements
            (ISOMessage isoMessage, ParsedField parsedField, FIELDS field) {
        int length;
        // Getting of the length from FIELDS.
        if (field.isFixed()) {
            // TODO: remove the link below.
            //parsedField.setLengthMIP(field.getMaxLength());
            // In compressed format.
            if (field.getType().compareTo("n") == 0 ||
                field.getType().compareTo("b") == 0)
                // +1 to consider the first additional zero.
                length = (field.getMaxLength() + 1) / 2;
            else
                length = field.getMaxLength();
        }
        // field is unfixed.
        // Getting of the length from  the lengths of the isoMessage.
        else {
            // TODO: remove the link below.
            // parsedField.setLengthMIP(isoMessage.getFieldLength(parsedField.getId()));
            // In compressed format.
            if (field.getType().compareTo("n") == 0 ||
                field.getType().compareTo("b") == 0)
                // +1 to consider the first additional zero.
                length = (isoMessage.getFieldLength(parsedField.getId()) + 1) / 2;
            else
                length = isoMessage.getFieldLength(parsedField.getId());
        }
        return length;
    }

    /*
    If the transmitted field has subfields, this method sets its parsed subfields to this.
    The sequence of the bodies of the subfields are setting as the content of the parsedField.
    Note, in this moment the conversion of the parsedField content from the hex format is happening.
    */
    private static HashMap<Integer, ParsedSubfield> getContentOfSubfields(ParsedField parsedField) throws ISOException {
        int fieldId = parsedField.getId();
        FIELDS fields = FIELDS.valueOf(fieldId);
        HashMap<Integer, ParsedSubfield> subfields = new HashMap<>();
        // The field has subfields.
        if (fields.getHasSubfields()) {
            parsedField.setHasSubfields(true);
            subfields = parseSubfields(parsedField);
            // TODO: check that links below can be removed and remove them.
            /*for (ParsedSubfield parsedSubfield : subfields.values()) {
                int subfieldId = parsedSubfield.getId();
                if (SUBFIELDS.valueOf(fieldId, subfieldId) == null)
                    throw new IllegalArgumentException("The information of the subfield №" + subfieldId +
                            " of the field №" + fieldId + " is not provided by the Lib" +
                            " on the strength of the project features or because the MIP does not suggest this!");
                parsedField.setSubfields(parseSubfields(parsedField));
            }*/
            // Todo: the end.
        }
        return subfields;
    }

    private static HashMap<Integer, ParsedElement> getContentOfElements(ParsedField parsedField) throws ISOException {
        int fieldId = parsedField.getId();
        FIELDS fields = FIELDS.valueOf(fieldId);
        HashMap<Integer, ParsedElement> elements = parsedField.getElements();
        // The field has elements.
        if (fields.getHasElements()) {
            parsedField.setHasElements(true);
            elements = parseElements(parsedField);
            // Todo: check that links below cna be removed and remove them.
            /*for (ParsedElement parsedElement : elements.values()) {
                int elemId = parsedElement.getId();
                if (SUBFIELDS.valueOf(fieldId, elemId) == null)
                    throw new IllegalArgumentException("The information of the element №" + elemId +
                            " of the field №" + fieldId + " is not provided by the Lib" +
                            " on the strength of the project features or because the MIP does not suggest this!");
                parsedField.setElements(parseElements(parsedField));
            }*/
            // Todo: the end.
        }
        return elements;
    }

    /*
    Sets the sequence of the subfields contents as the content of the parsedField.
    */
    private static void setSubfieldsAsParsedFieldContent(ParsedField parsedField) {
        StringBuilder fieldContent = new StringBuilder();
        HashMap<Integer, ParsedSubfield> subfields = parsedField.getSubfields();
        for (ParsedSubfield subfield : subfields.values())
            fieldContent.append(subfield.getContent());
        parsedField.setContent(fieldContent.toString());
        return;
    }

    /*
    Sets the sequence of the elements contents as the content of the parsedField.
    */
    private static void setElementsAsParsedFieldContent(ParsedField parsedField) {
        StringBuilder fieldContent = new StringBuilder();
        HashMap<Integer, ParsedElement> elements = parsedField.getElements();
        for (ParsedElement elem : elements.values()) {
            fieldContent.append(elem.getType());
            fieldContent.append(elem.getHexId());
            fieldContent.append(elem.getHexLengthMIP());
            fieldContent.append(elem.getContent());
        }
        parsedField.setContent(fieldContent.toString());
        return;
    }

    /*
    Returns the HashMap of the elements of the field if it has these.
    The content of the parsedField is represented in hex format.
    */
    private static HashMap<Integer, ParsedElement> parseElements(ParsedField parsedField) throws ISOException {
        HashMap<Integer, ParsedElement> elements = new HashMap<Integer, ParsedElement>();
        String fieldContent = parsedField.getContent();
        // Formation of the parsed elements.
        int indSym = 0;
        while (indSym < fieldContent.length()) {
            ParsedElement parsedElement = new ParsedElement();
            // The type of an parsedElement takes positions 0-1.
            String typeHex = fieldContent.substring(indSym, indSym + 2);
            parsedElement.setType(StringUtil.hexToAscii(typeHex));
            // The id of an parsedElement takes positions 2-5.
            String id = fieldContent.substring(indSym + 2, indSym + 6);
            parsedElement.setId(Integer.parseInt(StringUtil.hexToAscii(id), 16));
            // The length of an parsedElement takes positions 6-9.
            String lengthHex = fieldContent.substring(indSym + 6, indSym + 10);
            parsedElement.setLengthMIP(Integer.parseInt(StringUtil.hexToAscii(lengthHex), 16));
            // The real length.
            int contentLengthInSymbols = getLengthInSymbolsOfElem(parsedElement);
            // Todo: remove the link below.
            //parsedElement.setLengthInSymbolsReal(contentLengthInSymbols);
            // Setting of the content.
            parsedElement.setContent(getElemContent(parsedField, parsedElement, indSym, contentLengthInSymbols));
            // The offset of the indSym to make it the first index of the next parsedElement.
            elements.put(parsedElement.getId(), parsedElement);
            indSym += 10 + contentLengthInSymbols;
        }
        return elements;
    }

    /*
    Returns the real length (the quantity of symbols) of the content of the transmitted parsedElement.
    The first additional zero is considered.
    */
    private static int getLengthInSymbolsOfElem(ParsedElement parsedElement) {
        int elemLength = parsedElement.getLengthMIP();
        // Compressed format.
        if (parsedElement.getType().compareTo("%") == 0) {
            // The first additional zero is considered.
            if (elemLength % 2 != 0)
                elemLength++;
        }
        // Uncompressed format.
        // parsedElement.getType().compareTo("^") == 0.
        else
            elemLength *= 2; // Every element takes 2 hexadecimal symbols.
        return elemLength;
    }

    /*
    Returns the content of the transmitted element.
    If the type of this is equal to "^", conversion from hex happens.
    In otherwise the conversion does not happen.
    */
    private static String getElemContent
    (ParsedField parsedField, ParsedElement parsedElement, int indSym, int contentSymbolsRealCount) {
        String fieldContent = parsedField.getContent();
        // The content of an parsedElement takes positions begin at the 10th.
        String elemContent = fieldContent.substring(indSym + 10, indSym + 10 + contentSymbolsRealCount);
        // The content of the parsedElement has been converted from the hex format already.
        if (parsedElement.getType().compareTo("%") == 0)
            return elemContent;
        // The content of the parsedElement needs in the conversion from the hex format.
        return StringUtil.hexToAscii(elemContent);
    }

    /*
    Returns the subfields which contains parsed subfields.
    This method does not consider unfixed parsed fields!
    */
    private static HashMap<Integer, ParsedSubfield> parseSubfields(ParsedField parsedField)
            throws StringIndexOutOfBoundsException, ISOException {
        HashMap<Integer, ParsedSubfield> subfields = new HashMap<Integer, ParsedSubfield>();
        // Formation of the parsed subfields.
        int fieldId = parsedField.getId();
        int maxCountSubfields = FIELDS.valueOf(fieldId).getMaxSubfieldsId();
        for (int subfieldId = 1; subfieldId <= maxCountSubfields; subfieldId++)  {
            ParsedSubfield parsedSubfield = formParsedSubfield(parsedField, subfieldId);
            subfields.put(subfieldId, parsedSubfield);
        }
        return subfields;
    }

    /*
    Returns the formed parsedSubfield.
    This method does not consider unfixed parsed fields!
    */
    private static ParsedSubfield formParsedSubfield(ParsedField parsedField, int subfieldId) throws ISOException {
        int fieldId = parsedField.getId();
        SUBFIELDS subfieldSample = SUBFIELDS.valueOf(fieldId, subfieldId);
        if (subfieldSample == null)
            throw new ISOException("The information about the subfield №" + subfieldId +
                    " of the field №" + fieldId + " is not provided by the Lib" +
                    " on the strength of the project features or because the MIP does not suggest this!");
        ParsedSubfield parsedSubfield = new ParsedSubfield();
        parsedSubfield.setId(subfieldId);
        parsedSubfield.setType(subfieldSample.getType());
        // The length according to the MIP.
        // The subfields with the variable length is not taken into account!!!
        int length = subfieldSample.getLength();
        parsedSubfield.setLengthMIP(length);
        // The real length (the quantity of symbols) of the parsedSubfield.
        length = getParsedSubfieldRealLength(parsedField, parsedSubfield, length);
        // Todo: remove the link below.
        // parsedSubfield.setLengthInSymbolsReal(length);
        parsedSubfield.setContent(getSubfieldContent(parsedField, subfieldSample, length));
        return parsedSubfield;
    }

    private static String getSubfieldContent
            (ParsedField parsedField, SUBFIELDS subfieldSample, int lengthRealOfParsedSubfield) {
        int beginInd = subfieldSample.getBeginInd();
        // The content of the subfield has been converted from the hex format already.
        if (parsedField.getType().compareTo("n") == 0 ||
                parsedField.getType().compareTo("b") == 0)
            return parsedField.getContent().substring(beginInd, beginInd + lengthRealOfParsedSubfield);
            // The content of the subfield needs in the conversion from the hex format.
        else {
            String subfieldHexContent
                    = parsedField.getContent().substring(beginInd, beginInd + lengthRealOfParsedSubfield);
            return StringUtil.hexToAscii(subfieldHexContent);
        }
    }

    /*
    Returns the real length (the quantity of symbols) of the transmitted parsedSubfield.
     */
    private static int getParsedSubfieldRealLength
    (ParsedField parsedField, ParsedSubfield parsedSubfield, int length) {
        // Compressed format.
        if (parsedSubfield.getType().compareTo("n") == 0 ||
                parsedSubfield.getType().compareTo("b") == 0) {
            // The first additional zero is considered.
            if (length % 2 != 0)
                length++;
            // TODO: define with /= 2 or without /= 2.
            //length /= 2;
        }
        // Uncompressed format.
        else
            length *= 2;
        return length;
    }

    // TODO: change the returned type from void to int
    //  which will be used to return the transaction number.
    /*
    Sets the transaction number of the parsedMessage if it has the 2th element
    of the 63th field (Transaction Reference Number (TRN)).
     */
    private static void setTransactionNumber(ParsedMessage parsedMessage) {
        int fieldId = 63;
        int elemId = 2;
        int elemContentLength = 16;
        // If the required field is set.
        if (parsedMessage.getFields().containsKey(fieldId)) {
            ParsedField parsedField = parsedMessage.getFields().get(fieldId);
            // If the subfield/element is set.
            if (parsedField.getElements().containsKey(elemId)) {
                ParsedElement parsedElement = parsedField.getElements().get(elemId);
                String transactionNumber = parsedElement.getContent();
                parsedMessage.setTransactionNumber(transactionNumber);
            }
            else
                parsedMessage.setTransactionNumber(null);
        }
        else
            // Setting of the mark that the transactionNumber is not set.
            parsedMessage.setTransactionNumber(null);
        return;
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

package mir.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imohsenb.ISO8583.exceptions.ISOException;
import com.imohsenb.ISO8583.utils.StringUtil;
import lombok.NoArgsConstructor;

@JsonAutoDetect
@NoArgsConstructor
public class ParsedElement {

    @JsonIgnore
    // ^ (0x5E) – means that symbols in the field (not object Field!) of the data
    // of an element are represented by ASCII.
    public final static String separatorSym = "^";
    @JsonIgnore
    // % (0x25) means that binary data in the field (not object Field!) of an element
    // are represented by bytes 8 bits each.
    public final static String separatorBin = "%";

    // Binary data ("%") or ASCII symbols ("^").
    private String type;
    // From 01 to ZZ in the hex system.
    private int id;
    // The length of the direct content without the type, the id, the length.
    // According to the MIP.
    private int lengthMIP;
    // Is different from the lengthMIP if the type of the field is "%".
    private int lengthReal;
    // Content.
    private String content;

    // Getters and Setters.
    public String getType() {
        return type;
    }

    public void setType(String dataFormat) {
        if (dataFormat.compareTo(separatorSym) == 0)
            type = separatorSym;
            // dataFormat.compareTo(separatorBin) == 0.
        else
            type = separatorBin;
    }

    public int getId() {
        return id;
    }

    public String getHexId() { return StringUtil.intToHexString(id); }

    public void setId(int id) {
        this.id = id;
    }

    public int getLengthMIP() {
        return lengthMIP;
    }

    public int getLengthReal() {
        return lengthReal;
    }

    public void setLengthReal(int lengthReal) {
        this.lengthReal = lengthReal;
    }

    public String getHexLengthMIP() { return StringUtil.intToHexString(lengthMIP); }

    public void setLengthMIP(int lengthMIP) {
        this.lengthMIP = lengthMIP;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    // The end of the Getters and Setters.

    @JsonIgnore
    public String getHexString() throws ISOException {
        StringBuilder elementStr = new StringBuilder();
        // The type.
        if (type.compareTo(separatorSym) == 0)
            elementStr.append("5E");
        else {
            if (type.compareTo(separatorBin) == 0)
                elementStr.append("25");
            else
                throw new ISOException("ParsedElement has not a type!");
        }
        // The id.
        String idStr = StringUtil.asciiToHex(Integer.toString(id));
        if (idStr.length() == 4)
            elementStr.append(idStr);
        else
            // idStr.length() == 2
            elementStr.append("30" + idStr);
        // The length.
        String lengthStr = StringUtil.asciiToHex(Integer.toString(lengthMIP));
        if (lengthStr.length() == 4)
            elementStr.append(lengthStr);
        else
            // lengthStr.length() == 2
            elementStr.append("30" + lengthStr);
        // The content.
        elementStr.append(StringUtil.asciiToHex(content));
        return elementStr.toString();
    }

    /*
    Returns the String representation of the element.
     */
    @JsonIgnore
    public String getString() {
        StringBuilder elementStr = new StringBuilder();
        // The type.
        elementStr.append(type);
        // The id.
        String idStr = Integer.toString(id);
        if (idStr.length() == 2)
            elementStr.append(id);
        else
            // idStr.length() == 1
            elementStr.append("0" + id);
        // The length.
        String lengthStr = Integer.toString(lengthMIP);
        if (lengthStr.length() == 2)
            elementStr.append(lengthMIP);
        else
            // lengthStr.length() == 1
            elementStr.append("0" + lengthMIP);
        // The body.
        elementStr.append(content);
        return elementStr.toString();
    }
}

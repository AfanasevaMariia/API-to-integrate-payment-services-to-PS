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
    // ^ (5E in hex) – means that the data of the content represent ASCII symbols.
    public final static String separatorSym = "^";
    @JsonIgnore
    // % (25 in hex) - means that the data of the content represent binary data.
    public final static String separatorBin = "%";

    // Binary data ("%") or ASCII symbols ("^").
    private String type;
    // From 01 to ZZ as hex symbols.
    private int id;
    // The length of the direct content without the type, the id, the length.
    // According to the MIP.
    private int lengthMIP;
    // Todo: remove the lengthInSymbolsReal.
    // The length of the direct content without the type, the id, the length.
    // Is different from the lengthMIP if the type of the field is "%".
    // The length of the content converted from hex without the additional zero.
    private int lengthInSymbolsReal;

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

    // Todo: remove links below.
    /*public int getLengthInSymbolsReal() {
        return lengthInSymbolsReal;
    }

    public void setLengthInSymbolsReal(int lengthInSymbolsReal) {
        this.lengthInSymbolsReal = lengthInSymbolsReal;
    }*/
    // Todo: the end.

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

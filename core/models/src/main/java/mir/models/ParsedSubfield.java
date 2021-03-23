package mir.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imohsenb.ISO8583.exceptions.ISOException;
import com.imohsenb.ISO8583.utils.StringUtil;
import lombok.NoArgsConstructor;

@JsonAutoDetect
@NoArgsConstructor
public class ParsedSubfield {

    private int id;
    private String type;
    // According to the MIP.
    private int lengthMIP;
    // Todo: remove the field.
    // Which the field has in the message.
    // Is different from the lengthMIP if the type of the field is "n" or "b".
    // The length of the content converted from hex without the additional zero.
    private int lengthInSymbolsReal;

    private String content;

    // Getters and Setters.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLengthMIP() {
        return lengthMIP;
    }

    public void setLengthMIP(int lengthMIP) {
        this.lengthMIP = lengthMIP;
    }

    // Todo: remove links below.
    /*public int getLengthInSymbolsReal() {
        return lengthInSymbolsReal;
    }
    // Todo: the end.

    public void setLengthInSymbolsReal(int lengthInSymbolsReal) {
        this.lengthInSymbolsReal = lengthInSymbolsReal;
    }*/
    // Todo: the end.

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // The end of the Getters and Setters.

    @JsonIgnore
    public String getHexContent() throws ISOException {
        String elementStr = StringUtil.asciiToHex(content);
        return elementStr;
    }
}

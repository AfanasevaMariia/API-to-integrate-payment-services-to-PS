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
    // Which the field has in the message.
    // Is different from the lengthMIP if the type of the field is "n" or "b".
    private int lengthReal;
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

    public int getLengthReal() {
        return lengthReal;
    }

    public void setLengthReal(int lengthReal) {
        this.lengthReal = lengthReal;
    }

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

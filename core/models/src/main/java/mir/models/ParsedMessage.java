package mir.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashMap;

@Entity
@JsonAutoDetect
public class ParsedMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String mti;
    private String hex;
    private String header;
    private boolean edited;
    private LocalDateTime transactionDate;
    private String transactionNumber;

    @Transient
    private HashMap<Integer, ParsedField> fields = new HashMap<>();

    public ParsedMessage() {
    }

    //region Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    // For fields.
    public HashMap<Integer, ParsedField> getFields() {
        return fields;
    }

    public void setFields(HashMap<Integer, ParsedField> fields) {
        this.fields = fields;
    }

    public void addField(ParsedField field) {
        fields.put(field.getId(), field);
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
    //endregion
}
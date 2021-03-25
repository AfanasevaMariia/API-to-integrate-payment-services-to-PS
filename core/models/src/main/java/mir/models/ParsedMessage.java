package mir.models;

import com.fasterxml.jackson.annotation.*;
import com.imohsenb.ISO8583.enums.FIELDS;
import mir.models.check_annotations.AnyOf;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashMap;

@Entity
@JsonAutoDetect
public class ParsedMessage {

    private static final String defaultTransactionNumber = "000000";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @AnyOf(values = {"0100", "0110", "0400", "0410"})
    private String mti;
    // Only hexadecimal numbers.
    // The first symbol can be equal to only zero because the secondary bitmap is not used in this project.
    private String hex;
    private boolean edited;
    private LocalDateTime transactionDate;
    @Pattern(regexp = "[0-9]{16}")
    private String transactionNumber;

    @Transient
    @JsonIgnore
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
        if (transactionNumber == null)
            this.transactionNumber = defaultTransactionNumber;
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

    public boolean getEdited() {
        return this.edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }
    //endregion
}
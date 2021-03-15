package mir.models;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;

@Entity
@JsonAutoDetect
public class ParsedMessage {

    private static final String defaultTransactionNumber = "000000";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String mti;
    private String hex;
    private boolean edited;
    private LocalDateTime transactionDate;
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

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ParsedMessage)) {
            return false;
        }

        ParsedMessage pm = (ParsedMessage) o;


        boolean equalsOrNot;
        equalsOrNot =
                (getId() == null && pm.getId() == null || getId().equals(pm.getId())) &&
                getMti().equals(pm.getMti()) &&
                getHex().equals(pm.getHex()) &&
                (getTransactionDate() == null && pm.getTransactionDate() == null
                        || getTransactionDate().equals(pm.getTransactionDate())) &&
                getTransactionNumber().equals(pm.getTransactionNumber()) &&
                getEdited() == pm.getEdited();

        if (equalsOrNot) {
            equalsOrNot = equalFieldsOrNot(pm);
        }

        return equalsOrNot;
    }

    private boolean equalFieldsOrNot(ParsedMessage other) {
        if (getFields().size() == other.getFields().size()) {
            return getFields().entrySet().stream()
                    .allMatch(e -> e.getValue().equals(other.getFields().get(e.getKey())));
        } else {
            return false;
        }
    }
}
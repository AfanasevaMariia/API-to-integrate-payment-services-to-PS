package mir.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

/*
Supposes a header.
 */
@JsonAutoDetect
@NoArgsConstructor
public class EncodedMessage {
    @JsonProperty("ParsedMessage")
    // Consists hex symbols.
    public String message;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof EncodedMessage)) {
            return false;
        }

        EncodedMessage em = (EncodedMessage) o;

        return message.equals(em.message);
    }
}

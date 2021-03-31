package mir.routing.emulator;

import com.imohsenb.ISO8583.exceptions.ISOException;

import java.util.List;

import mir.change.Changer;
import mir.check.Checker;
import mir.models.MessageError;
import mir.models.ParsedMessage;
import mir.parsing.routing.Router;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping
public class Acquirer {

    private final String URI = "http://localhost:8080/api"; // TODO: change to actual URI.

    private String sendRequest(String hex) {
        // Form new Http-request to Platform and get response from it.
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(URI)
                .queryParam("Payload", hex);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                null,
                String.class
        );

        return responseEntity.getBody();
    }

    @GetMapping(path = "/api")
    public ResponseEntity<String> getRequest(@RequestParam(name = "Payload") String payload) {
        System.out.println(payload);
        if (payload != null && !payload.isBlank()) {
            try {
                // Check.
                ParsedMessage parsedMessage = Router.getParsedMessage(payload);
                List<MessageError> errorsList = Checker.checkParsedMessage(parsedMessage);

                String respText;
                if (errorsList.size() == 0) {
                    // --- CORRECT PAYLOAD CONTENT --- //
                    ParsedMessage formedMessage = Changer.completeParsedMessageRequest(parsedMessage);

                    // TODO: save formedMessage to DB.

                    // Send request to platform and get response.
                    respText = sendRequest(formedMessage.getHex());

                    // Return response from Platform.
                    return new ResponseEntity<>(respText, HttpStatus.OK);
                } else {
                    // --- INCORRECT PAYLOAD CONTENT --- //
                    StringBuilder errors = new StringBuilder();

                    for (var error: errorsList) {
                        errors.append(error.getMessage()).append("\n");
                    }
                    respText = String.format("Incorrect payload content format.\n%s", errors.toString());

                    // Return error response immediately.
                    return new ResponseEntity<>(respText, HttpStatus.BAD_REQUEST); // TODO: Is status ok?
                }
            } catch (ISOException ex) {
                return new ResponseEntity<>("ISOException", HttpStatus.BAD_REQUEST); // TODO: Is status ok?
            } catch (NoSuchFieldException ex) {
                return new ResponseEntity<>("NoSuchFieldException", HttpStatus.BAD_REQUEST); // TODO: Is status ok?
            } catch (IllegalAccessException ex) {
                return new ResponseEntity<>("IllegalAccessException", HttpStatus.BAD_REQUEST); // TODO: Is status ok?
            }
        } else {
            return new ResponseEntity<>("Message is empty", HttpStatus.BAD_REQUEST); // TODO: Is status ok?
        }
    }
}

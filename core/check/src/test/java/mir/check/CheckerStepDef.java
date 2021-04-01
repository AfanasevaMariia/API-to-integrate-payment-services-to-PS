package mir.check;

import com.imohsenb.ISO8583.exceptions.ISOException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mir.models.MessageError;
import mir.models.ParsedMessage;
import mir.parsing.routing.Router;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;

public class CheckerStepDef {

    ParsedMessage parsedMessage;

    @Given("^Created a checker$")
    public void createdAChecker() { }

    @When("Get a parsed message from hex = {string}")
    public void getAParsedMessageFromHex(String hex) throws ISOException {
        parsedMessage = Router.getParsedMessage(hex);
        parsedMessage.setTransactionDate(LocalDateTime.now());
    }

    @Then("Resulting errors should match")
    public void doSomething() throws NoSuchFieldException, IllegalAccessException {
        List<MessageError> errors = Checker.checkParsedMessage(parsedMessage);
        System.out.println("Errors:");
        for (MessageError error : errors) {
            System.out.println("error: " + error.getMessage());
            if (error.getMessage().compareTo("The value of the mti of the ParsedMessage" +
                    " is not one from the allowable set!") == 0)
                fail("The error of the incorrect mti.");
        }
    }
}


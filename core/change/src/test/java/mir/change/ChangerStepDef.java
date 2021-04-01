package mir.change;

import com.imohsenb.ISO8583.exceptions.ISOException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mir.models.ParsedField;
import mir.models.ParsedMessage;
import mir.parsing.routing.Router;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static mir.change.Changer.*;
import static org.junit.jupiter.api.Assertions.*;

public class ChangerStepDef {

    ParsedMessage initialParsedMessage;
    ParsedMessage changedParsedMessage;
    ParsedField field;
    LocalDateTime dateTime;
    LocalDate date;
    String res;

    @Given("Hex message = {string}")
    public void hexMessage(String hex) throws ISOException {
        initialParsedMessage = Router.getParsedMessage(hex);
    }

    @When("Fields F7 F11 F37 are added")
    public void fieldsFFFAreAdded() {
        changedParsedMessage = Changer.completeParsedMessageRequest(initialParsedMessage);
    }

    @Then("Contents of those fields shouldn't be null")
    public void contentsOfThoseFieldsShouldnTBeNull() {
        HashMap<Integer, ParsedField> changedFields = changedParsedMessage.getFields();
        assertNotEquals(null, changedFields.get(7));
        assertNotEquals(null, changedFields.get(11));
        assertNotEquals(null, changedFields.get(37));
        assertNotEquals(null, changedParsedMessage.getTransactionDate());
        assertTrue(changedParsedMessage.getEdited());
    }

    @Given("Date and time of {int} {int} {int} at {int}:{int}:{int}")
    public void dateAndTimeOfAt(int year, int month, int day, int hour, int minute, int second) {
        dateTime = LocalDateTime.of(year, month, day, hour, minute, second);
    }

    @When("Get content of F7")
    public void getContentOfF() {
        res = get_F7_content(dateTime);
    }

    @Then("It should equal to {string}")
    public void itShouldEqualTo(String F7_content) {
        assertEquals(F7_content, res);
    }

    @When("Get content of field F7")
    public void getContentOfFieldF() {
        String F7_content = get_F7_content(dateTime);
        field = get_F7(F7_content);
    }

    @Then("Content of the field should be")
    public void contentOfFShouldBe(DataTable arg) {
        List<List<String>> table = arg.cells();

        assertEquals(Integer.parseInt(table.get(1).get(0)), field.getId());
        assertEquals(table.get(1).get(1), field.getType());
        assertEquals(Integer.parseInt(table.get(1).get(2)), field.getLengthMIP());
        assertEquals(table.get(1).get(3), field.getContent());
    }

    @Given("Date of {int} {int} {int}")
    public void dateOf(int year, int month, int day) {
        date = LocalDate.of(year, month, day);
    }

    @When("current Parsed Message Date is set to given date")
    public void currentParsedMessageDateIsSetToGivenDate() {
        Changer.currentParsedMessageDate = date;
        res = get_F11_content(date);
    }

    @Then("F11 content should be {string}")
    public void fieldContentShouldBe(String F11_content) {
        assertEquals(F11_content, res);
    }

    @And("Get F11 content with date of the next day")
    public void getF11ContentWithDateOfTheNextDay() {
        assertEquals("000001", res);
        LocalDate date_second = date.plusDays(1);
        res = get_F11_content(date_second);
    }

    @When("global MIPT transaction number is set to {int}")
    public void globalMIPTTransactionNumberIsSetTo(int amount) {
        Changer.currentGlobalMIPTransactionNumber = amount;
    }

    @And("Get content of F11")
    public void getContentOfF11() {
        String F11_content = get_F11_content(date);
        field = get_F11(F11_content);
    }
}

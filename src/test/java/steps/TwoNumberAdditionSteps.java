package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class TwoNumberAdditionSteps {

    private int numberOne;
    private int numberTwo;
    private int actualResult;

    @Given("user has number one as {int}")
    public void userHasNumberOneAs(int num1) {
        this.numberOne = num1;
    }

    @And("user has number two as {int}")
    public void userHasNumberTwoAs(int num2) {
        this.numberTwo = num2;
    }

    @When("user adds number one and number two")
    public void userAddsNumberOneAndNumberTwo() {
        this.actualResult = this.numberOne + this.numberTwo;
    }

    @Then("the result is {int}")
    public void theResultIs(int expectedResult) {
        Assertions.assertEquals(expectedResult, this.actualResult, "Error in calculation");
    }
}
package test.update;

import arguments.holders.CardIdValidationArgumentsHolder;
import arguments.providers.CardIdValidationArgumentsProvider;
import consts.CardsEndpoints;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import base.BaseTest;

public class UpdateCardValidationTest extends BaseTest {

    @ParameterizedTest
    @ArgumentsSource(CardIdValidationArgumentsProvider.class)
    public void checkUpdateCardWithInvalidId(CardIdValidationArgumentsHolder validationArguments) {
        Response response = requestWithAuth()
                .pathParams(validationArguments.getPathParams())
                .put(CardsEndpoints.UPDATE_CARD_URL);
        response
                .then()
                .statusCode(validationArguments.getStatusCode());
        Assertions.assertEquals(validationArguments.getErrorMessage(), response.body().asString());
    }



}
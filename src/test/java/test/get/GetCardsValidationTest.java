package test.get;

import arguments.holders.AuthValidationArgumentsHolder;
import arguments.holders.CardIdValidationArgumentsHolder;
import arguments.providers.AuthValidationArgumentsProvider;
import arguments.providers.CardIdValidationArgumentsProvider;
import consts.CardsEndpoints;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import base.BaseTest;

public class GetCardsValidationTest extends BaseTest {

    @ParameterizedTest
    @ArgumentsSource(CardIdValidationArgumentsProvider.class)
    public void checkGetCardWithInvalidId(CardIdValidationArgumentsHolder validationArguments) {
        Response response = requestWithAuth()
                .pathParams(validationArguments.getPathParams())
                .get(CardsEndpoints.GET_CARD_URL);
        response
                .then()
                .statusCode(validationArguments.getStatusCode());
        Assertions.assertEquals(validationArguments.getErrorMessage(), response.body().asString());
    }

    @ParameterizedTest
    @ArgumentsSource(AuthValidationArgumentsProvider.class)
    public void checkGetCardWithInvalidAuth(AuthValidationArgumentsHolder validationArguments) {
        Response response = requestWithoutAuth()
                .queryParams(validationArguments.getAuthParams())
                .pathParam("id", "6a33e2329970ac8cfed11ded")
                .get(CardsEndpoints.GET_CARD_URL);

        response
                .then()
                .statusCode(401);

        String actualMessage = response.body().asString();
        Assertions.assertTrue(
                actualMessage.equals("unauthorized card permission requested") ||
                        actualMessage.equals("missing scopes") ||
                        actualMessage.equals("invalid key"),
                "Unexpected error message from Trello: " + actualMessage
        );
    }


}

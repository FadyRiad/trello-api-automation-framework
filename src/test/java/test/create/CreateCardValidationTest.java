package test.create;

import arguments.holders.AuthValidationArgumentsHolder;
import arguments.holders.CardBodyValidationArgumentsHolder;
import arguments.providers.AuthValidationArgumentsProvider;
import arguments.providers.CardBodyValidationArgumentsProvider;
import consts.BoardsEndpoints;
import consts.CardsEndpoints;
import consts.UrlParamValues;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import base.BaseTest;

import java.util.Map;

public class CreateCardValidationTest extends BaseTest {

    @ParameterizedTest
    @ArgumentsSource(CardBodyValidationArgumentsProvider.class)
    public void checkCreateCardWithInvalidName(CardBodyValidationArgumentsHolder validationArguments) {
        Response response = requestWithAuth()
                .body(validationArguments.getBodyParams())
                .contentType(ContentType.JSON)
                .post(CardsEndpoints.CREATE_CARD_URL);
        response.then().statusCode(400);
        String contentType = response.contentType();
        String actualMessage;
        if (contentType != null && contentType.contains("application/json")) {
            actualMessage = response.jsonPath().getString("message");
        } else {
            actualMessage = response.body().asString();
        }
        Assertions.assertEquals(validationArguments.getErrorMessage(), actualMessage);
    }

    @ParameterizedTest
    @ArgumentsSource(AuthValidationArgumentsProvider.class)
    public void checkCreateCardWithInvalidAuth(AuthValidationArgumentsHolder validationArguments) {
        Response response = requestWithoutAuth()
                .queryParams(validationArguments.getAuthParams())
                .body(Map.of("name", "new board",
                        "idList", UrlParamValues.EXISTING_LIST_ID
                ))
                .contentType(ContentType.JSON)
                .post(BoardsEndpoints.CREATE_BOARD_URL);
        response
                .then()
                .statusCode(401);

        String actualMessage = response.body().asString();
        Assertions.assertTrue(
                actualMessage.equals("unauthorized permission requested") ||
                        actualMessage.equals("missing scopes") ||
                        actualMessage.equals("invalid key"),
                "Unexpected error message from Trello: " + actualMessage
        );
    }
}

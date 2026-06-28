package test.get;

import arguments.holders.AuthValidationArgumentsHolder;
import arguments.holders.BoardIdValidationArgumentsHolder;
import arguments.providers.AuthValidationArgumentsProvider;
import arguments.providers.BoardIdValidationArgumentsProvider;
import consts.BoardsEndpoints;
import consts.UrlParamValues;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import base.BaseTest;

import java.util.Map;

public class GetBoardsValidationTest extends BaseTest {

    @ParameterizedTest
    @ArgumentsSource(BoardIdValidationArgumentsProvider.class)
    public void checkGetBoardWithInvalidId(BoardIdValidationArgumentsHolder validationArguments) {
        Response response = requestWithAuth()
                .pathParams(validationArguments.getPathParams())
                .get(BoardsEndpoints.GET_BOARD_URL);
        response
                .then()
                .statusCode(validationArguments.getStatusCode());
        Assertions.assertEquals(validationArguments.getErrorMessage(), response.body().asString());
    }

    @ParameterizedTest
    @ArgumentsSource(AuthValidationArgumentsProvider.class)
    public void checkGetBoardWithInvalidAuth(AuthValidationArgumentsHolder validationArguments) {
        Response response = requestWithoutAuth()
                .queryParams(validationArguments.getAuthParams())
                .pathParam("id",UrlParamValues.EXISTING_BOARD_ID)
                .get(BoardsEndpoints.GET_BOARD_URL);
        response
                .then()
                .statusCode(401);

        String actualMessage = response.body().asString();
        Assertions.assertTrue(
                actualMessage.equals("unauthorized permission requested") ||
                        actualMessage.equals("missing scopes") ||
                        actualMessage.equals("invalid key"),
                "Unexpected error message from Trello: " + actualMessage
        );    }

    @Test
    public void checkGetBoardWithAnotherCredentials() {
        Response response = requestWithoutAuth()
                .queryParams(Map.of(
                        "key", "ff9b892e25dd6efcf5edf82e8a014055",
                        "token", "ATTAf6208dffdfd67ae282787f9ec5c6eab9f6761d4ddfb1fa3fed0ca170c96562120218f57A9FA5DF"
                ))
                .pathParam("id",UrlParamValues.EXISTING_BOARD_ID)
                .get(BoardsEndpoints.GET_BOARD_URL);
        response
                .then()
                .statusCode(401);
        Assertions.assertEquals("invalid app token", response.body().asString());
    }


}

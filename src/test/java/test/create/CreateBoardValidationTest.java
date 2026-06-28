package test.create;

import arguments.holders.AuthValidationArgumentsHolder;
import arguments.providers.AuthValidationArgumentsProvider;
import arguments.providers.BoardNameValidationArgumentProvider;
import consts.BoardsEndpoints;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import base.BaseTest;

import java.util.Map;

public class CreateBoardValidationTest extends BaseTest {

    @ParameterizedTest
    @ArgumentsSource(BoardNameValidationArgumentProvider.class)
    public void checkCreateBoardWithInvalidName(Map bodyParams) {
        Response response = requestWithAuth()
                .body(bodyParams)
                .contentType(ContentType.JSON)
                .post(BoardsEndpoints.CREATE_BOARD_URL);
        response.then().statusCode(400);
        String actualMessage = response.jsonPath().getString("message");
        Assertions.assertEquals("invalid value for name", actualMessage);
    }

    @ParameterizedTest
    @ArgumentsSource(AuthValidationArgumentsProvider.class)
    public void checkCreateBoardWithInvalidAuth(AuthValidationArgumentsHolder validationArguments) {
        Response response = requestWithoutAuth()
                .body(Map.of("name", "new board"))
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

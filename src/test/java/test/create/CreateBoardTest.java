package test.create;

import consts.BoardsEndpoints;
import consts.UrlParamValues;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import base.BaseTest;

import java.time.LocalDateTime;
import java.util.Map;

public class CreateBoardTest extends BaseTest {

    private String createBoardId;

    @Test
    public void checkCreateBoard() {
        String boardName = "New Board" + LocalDateTime.now();
        Response response = requestWithAuth()
                .body(Map.of("name", boardName))
                .contentType(ContentType.JSON)
                .post(BoardsEndpoints.CREATE_BOARD_URL);

        // Validate status BEFORE parsing, and print the raw response if it is an error
        response.then()
                .log().ifError()
                .statusCode(200);

        createBoardId = response.body().jsonPath().getString("id");

        response.then()
                .body("name", Matchers.equalTo(boardName));

        requestWithAuth()
                .queryParam("fields", "id,name")
                .pathParam("member", UrlParamValues.USER_NAME)
                .get(BoardsEndpoints.GET_ALL_BOARDS_URL)
                .then()
                .body("name", Matchers.hasItem(boardName));
    }

    @AfterEach
    public void deleteCreatedBoard() {
        // If creation failed there is nothing to delete
        if (createBoardId == null) {
            return;
        }
        requestWithAuth()
                .pathParam("id", createBoardId)
                .delete(BoardsEndpoints.DELETE_BOARD_URL)
                .then()
                .statusCode(200);
    }
}
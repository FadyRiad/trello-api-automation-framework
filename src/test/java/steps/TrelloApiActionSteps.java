package steps;

import consts.Endpoint;
import consts.UrlParamValues;
import io.cucumber.core.options.CurlOption;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrelloApiActionSteps {
    private final ScenarioContext scenarioContext;

    public TrelloApiActionSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    private RequestSpecification requestWithAuth() {
        return requestWithoutAuth().queryParams(UrlParamValues.AUTH_QUERY_PARAMS);
    }

    private RequestSpecification requestWithoutAuth() {
        RestAssured.baseURI = "https://api.trello.com";
        return RestAssured.given();
    }

    @Given("a request {with} authorization")
    public void aRequestWithAuthorization(boolean withAuth) {
        scenarioContext.setRequest(withAuth ? requestWithAuth() : requestWithoutAuth());
    }

    @And("the request has query param:")
    public void theRequestHasQueryParam(Map<String, String> queryParams) {
        scenarioContext.setRequest(scenarioContext.getRequest().queryParams(queryParams));
    }

    @And("the request has body param:")
    public void theRequestHasBodyParam(DataTable dataTable) {
        Map<String, String> body = new HashMap<>();
        dataTable.asMap().forEach((k, v) -> body.put(k, v == null ? "" : v));
        scenarioContext.setRequest(
                scenarioContext.getRequest()
                        .contentType("application/json")
                        .body(body));
    }

    @And("the request has headers:")
    public void theRequestHasHeader(DataTable dataTable) {
        scenarioContext.setRequest(scenarioContext.getRequest().headers(dataTable.asMap()));
    }

    @And("the request has path params:")
    public void theRequestHasPathParam(DataTable dataTable) {
        Map<String, String> pathParams = new HashMap<>();
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            String name = row.get("name");
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException(
                        "Path params table needs a 'name | value' header row");
            }
            pathParams.put(name, row.get("value"));
        }
        scenarioContext.setRequest(scenarioContext.getRequest().pathParams(pathParams));
    }

    @When("the '{}' request is sent to '{endpoint}' endpoint")
    public void theRequestIsSentToEndpoint(CurlOption.HttpMethod method, Endpoint endpoint) {
        String url = endpoint.getUrl();
        switch (method) {
            case GET -> scenarioContext.setResponse(scenarioContext.getRequest().get(url));
            case PUT -> scenarioContext.setResponse(scenarioContext.getRequest().put(url));
            case POST -> scenarioContext.setResponse(scenarioContext.getRequest().post(url));
            default -> throw new RuntimeException();
        }
    }

    @When("the board ID from the response is remembered")
    public void theBoardIdFromTheResponseIsRemembered() {
        String createdBoardId = scenarioContext.getResponse().body()
                .jsonPath().getString("id");
        scenarioContext.setBoardId(createdBoardId);
    }

}
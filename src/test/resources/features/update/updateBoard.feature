@happy
Feature: Update Board
  As a Trello API user
  I want to update my board
  So that I want to call a single endpoint that will update my board

  Scenario: Check Update Board
    #Update board
    Given a request with authorization
    And the request has path params:
      | name | value                    |
      | id   | 6a2ddebef77e01bdc427b398 |
    And the request has body param:
      | name | Updated Name |
    And the request has headers:
      | content-type | application/json |
    When the 'PUT' request is sent to 'UPDATE_A_BOARD' endpoint
    Then the response status code is 200
    And body value has the following values by paths:
      | path | expected_value |
      | name | Updated Name   |
    #Check Board name updated
    Given a request with authorization
    And the request has query param:
      | name | Updated Name |
    And the request has path params:
      | name | value                    |
      | id   | 6a2ddebef77e01bdc427b398 |
    When the 'GET' request is sent to 'UPDATE_A_BOARD' endpoint
    Then the response status code is 200
    And body value has the following values by paths:
      | path | expected_value |
      | name | Updated Name   |

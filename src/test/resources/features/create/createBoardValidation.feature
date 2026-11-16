@sad
Feature: Create Board Validation
  As a Trello API user
  I want to create my board safely
  So that I want to create board endpoint to allow create only with valid request

  Scenario: Check Create Board With Invalid Id
    Given a request with authorization
    And the request has body param:
      | name |  |
    When the 'POST' request is sent to 'CREATE_A_BOARD' endpoint
    Then the response status code is 400
    And body value has the following values by paths:
      | path    | expected_value         |
      | message | invalid value for name |

  Scenario Outline: Check Create Board With Invalid Auth
    Given a request without authorization
    And the request has query param:
      | key   | <key>   |
      | token | <token> |
    And the request has body param:
      | name | new board |
    And the request has headers:
      | Content-Type | application/json |
    When the 'POST' request is sent to 'CREATE_A_BOARD' endpoint
    Then the response status code is 401
    And the response body is equal to '<error_message>'

    Examples:
      | key                              | token                                                                        | error_message  |
      |                                  |                                                                              | missing scopes |
      | fb04999a731923c2e3137153b1ad5de0 |                                                                              | missing scopes |
      |                                  | ATTAf620867ae282787f9ec5c6eab9f6761d4ddfb1fa3fed0ca170c96562120218f57A9FA5DF | invalid key    |
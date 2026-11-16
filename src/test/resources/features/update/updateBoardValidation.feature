@sad
Feature: Update Board Validation
  As a Trello API user
  I want to update my board safely
  So that I want to update board endpoint to allow update only with valid request

  Scenario Outline: Check Update Board With Invalid Id
    Given a request with authorization
    And the request has path params:
      | name | value |
      | id   | <id>  |
    When the 'PUT' request is sent to 'UPDATE_A_BOARD' endpoint
    Then the response status code is <status_code>
    And the response body is equal to '<error_message>'

    Examples:
      | id                       | status_code | error_message                         |
      | invalid                  | 400         | invalid id                            |
      | 6a2ddc8074cebf3f62872be1 | 404         | The requested resource was not found. |

  Scenario Outline: Check Update Board With Invalid Auth
    Given a request without authorization
    And the request has query param:
      | key   | <key>   |
      | token | <token> |
    And the request has path params:
      | name | value                    |
      | id   | 6a2ddc8074cebf3f62872be2 |
    When the 'PUT' request is sent to 'UPDATE_A_BOARD' endpoint
    Then the response status code is 401
    And the response body is equal to '<error_message>'

    Examples:
      | key                              | token                                                                        | error_message  |
      |                                  |                                                                              | missing scopes |
      | fb04999a731923c2e3137153b1ad5de0 |                                                                              | missing scopes |
      |                                  | ATTAf620867ae282787f9ec5c6eab9f6761d4ddfb1fa3fed0ca170c96562120218f57A9FA5DF | invalid key    |
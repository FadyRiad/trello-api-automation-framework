@happy
Feature: Get Boards
  As a Trello API user
  I want to access all boards
  So that i want to call a single endpoint that with return all my boards

  Background: a request with authorization and fields query params
    Given a request with authorization
    And the request has query param:
      | fields | id,name |

  Scenario: Check Get Boards
    Given the request has path params:
      | name   | value     |
      | member | fadyriad4 |
    When the 'GET' request is sent to 'GET_ALL_BOARDS' endpoint
    Then the response status code is 200
    And the response matches 'get_boards.json' schema

  Scenario: Check Get Board
    Given the request has path params:
      | name | value                    |
      | id   | 6a2ddc8074cebf3f62872be2 |
    When the 'GET' request is sent to 'GET_A_BOARD' endpoint
    Then the response status code is 200
    And body value has the following values by paths:
      | path | expected_value |
      | name | MyBoard        |
    And the response matches 'get_board.json' schema
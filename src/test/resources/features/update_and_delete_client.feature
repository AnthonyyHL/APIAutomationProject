Feature: Update and delete a new client
  Scenario: Update and delete a new client
    When I create a new client sending a POST request
    And I search for the new client in the system
    And I update the "city" parameter of the new client with the following value: "Guayaquil"
    And I remove the new client from the system
    Then the response should have a status code of 200

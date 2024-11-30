Feature: Update last resource
  Scenario: Update the last created resource in the system
    Given there are 15 or more resources
    When I search for the last resource in the system
    And I update all the parameters of this found resource
    Then the last resource response should have a status code of 200
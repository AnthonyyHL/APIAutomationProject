Feature: Get active resources testing

  Scenario: Get the list of all active resources
    Given there are 5 or more active resources
    When I search for all active resources
    And I update them as inactive
    Then the active resources response should have a status code of 200
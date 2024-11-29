Feature: Update client number testing

  Scenario: Change phone number of first client named Laura
    Given there are 10 or more registered clients
    When I search for the first client named "Laura" in the system
    And I store her current phone number
    And I update her phone number to "(593) 855-530-782"
    Then the response should have a status code of 200
    And the stored number should be different from "(593) 855-530-782"
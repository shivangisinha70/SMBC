Feature: Validate Apple Stock Price
  As a user
  I want to validate the Apple stock price
  So that I can confirm if it meets the acceptance criteria

  Scenario: Verify stock price and take a screenshot
    Given I navigate to the Investopedia website
    When I search for "APPLE INC"
    Then I capture the stock title and price
    And I validate if the price is below 150 or above 170
    And I take a screenshot for test evidence

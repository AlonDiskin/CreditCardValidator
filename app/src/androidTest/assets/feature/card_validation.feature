Feature: Card Validation
  In order to have a secure payment transaction
  As the product owner
  I want to have a credit card validation procedure

  Background:
    Given User is in device home screen
    And User launch application

  @card-validated
  Scenario Outline: Credit card is validated
    And User entered card number "<number>",type "<type>", cvc "<cvc>", expiry month "<expiry month>" year "<expiry year>"
    And User Rotates device to landscape
    Then Card should be validated
    When User submits card detail
    Then Detail input fields should be cleared

    Examples:
      | type             | number           | cvc  | expiry month | expiry year |
      | Visa             | 4111111111111111 | 231  | 10           | 2025        |
      | MasterCard       | 5500000000000004 | 472  | 2            | 2022        |
      | American Express | 340000000000009  | 4291 | 7            | 2026        |

  @card-not-validated
  Scenario Outline: Credit card not validated
    And User entered card number "<number>",type "<type>", cvc "<cvc>", expiry month "<expiry month>" year "<expiry year>"
    And User Rotates device to landscape
    Then Card should not be validated by "<wrong detail>"

    Examples:
      | type             | number           | cvc  | expiry month | expiry year | wrong detail |
      | Visa             | 4111111111111111 | 2314 | 10           | 2025        | cvc          |
      | MasterCard       | 7500000000000004 | 473  | 12           | 2025        | number       |
      | American Express | 340000000000009  | 4276 | 3            | 2012        | expiry       |
      | Visa             | 9111111111111111 | 2314 | 11           | 2005        | all          |
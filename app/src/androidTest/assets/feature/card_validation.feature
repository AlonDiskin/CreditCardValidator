Feature: Card Validation
  In order to have a secure payment transaction
  As the product owner
  I want to have a credit card validation procedure

  Background:
    Given User is in device home screen
    And User launch application

  @card-validated
  Scenario Outline: Credit card is validated
    Given User enters a valid card "<number>" of type "<type>"
    And Enters "<cvc>" and "<expires>" date
    Then Card should be validated

    Examples:
      | type             | number           | expires | cvc  |
      | Visa             | 4111111111111111 | 12/25   | 231  |
      | MasterCard       | 5500000000000004 | 11/22   | 4723 |
      | American Express | 340000000000009  | 06/24   | 429  |

  @card-not-validated
  Scenario Outline: Credit card not validated
    Given User enters an invalid card "<number>" of type "<type>"
    And Enters "<cvc>" and "<expires>" date
    Then Card should not be validated

    Examples:
      | type             | number           | expires | cvc  |
      | Visa             | 7111111111111111 | 12/25   | 231  |
      | Visa             | 41111111         | 12/25   | 231  |
      | MasterCard       | 5500000000000004 | 11/22   | 473  |
      | American Express | 940000000000009  | 06/24   | 429  |
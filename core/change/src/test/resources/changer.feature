Feature: Changer methods

  Scenario: Add fields: F7 F11 F37, to parsed message
    Given Hex message = "0100200000000001000092000000092536313034696E"
    When Fields F7 F11 F37 are added
    Then Contents of those fields shouldn't be null

  Scenario: Get contents transaction Date with alignment
    Given Date and time of 2021 1 1 at 01:01:01
    When Get content of F7
    Then It should equal to "0101010101"

  Scenario: Get contents transaction Date without alignment
    Given Date and time of 2021 10 10 at 10:10:10
    When Get content of F7
    Then It should equal to "1010101010"

  Scenario: Get contents of field F7
    Given Date and time of 2021 1 1 at 01:01:01
    When Get content of field F7
    Then Content of the field should be
      |id|type|MIP_length|content     |
      |7 |n   |10        |0101010101  |

  Scenario: Get content field F11 on the same day
    Given Date of 2021 10 10
    When current Parsed Message Date is set to given date
    Then F11 content should be "000001"

  Scenario:  Get content field F11 on the next day
    Given Date of 2021 10 10
    When current Parsed Message Date is set to given date
    And Get F11 content with date of the next day
    Then F11 content should be "000001"

    Scenario: Get F11 content with overloaded index limit
      Given Date of 2021 10 10
      When global MIPT transaction number is set to 1000000
      And current Parsed Message Date is set to given date
      Then F11 content should be "000001"

    Scenario: Get content of field F11
      Given Date of 2021 10 10
      When current Parsed Message Date is set to given date
      And Get content of F11
      Then Content of the field should be
        |id|type|MIP_length|content |
        |11|n   |6         |000001  |

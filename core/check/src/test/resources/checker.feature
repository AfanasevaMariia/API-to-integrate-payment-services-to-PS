Feature: Checker checking parsed messages

  Scenario: Parsed message with fields 3, 48
    Given Created a checker
    When Get a parsed message from hex = "0100200000000001000092000000092536313034696E"
    Then Resulting errors should match

  Scenario: Parsed message with field 22
    Given Created a checker
    When Get a parsed message from hex = "010000000400000000008108"
    Then Resulting errors should match

  Scenario: Parsed message with field 32
    Given Created a checker
    When Get a parsed message from hex = "01000000000100000000030123"
    Then Resulting errors should match

  Scenario: Parsed message with field 52
    Given Created a checker
    When Get a parsed message from hex = "0100000000000000100088888888"
    Then Resulting errors should match

  Scenario: Parsed message with field 39
    Given Created a checker
    When Get a parsed message from hex = "010000000000020000003339"
    Then Resulting errors should match

  Scenario: Parsed message with field 41
    Given Created a checker
    When Get a parsed message from hex = "010000000000008000003031323334353637"
    Then Resulting errors should match

  Scenario: Parsed message with field 35
    Given Created a checker
    When Get a parsed message from hex = "01000000000020000000023337"
    Then Resulting errors should match

  Scenario: Parsed message with field 48
    Given Created a checker
    When Get a parsed message from hex = "01000000000000010000000925363130390999999999"
    Then Resulting errors should match

  Scenario: Parsed message with field 63
    Given Created a checker
    When Get a parsed message from hex = "010000000000000000023030303023232323232323234D4D4D"
    Then Resulting errors should match
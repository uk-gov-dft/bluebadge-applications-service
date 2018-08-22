@application-find-org
Feature: Verify find newly created org badge

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * def createResult = callonce read('./application-create-org-ok.feature')
    * def createdAppNo = createResult.applicationId

  Scenario: Verify find by name
    Given path 'applications'
    And param name = 'Trotters'
    When method GET
    Then status 200
    And match $.data[*].applicationId contains createdAppNo

  Scenario: Verify find by name no result
    Given path 'applications'
    And param name = 'TrottersZZZSSSS'
    When method GET
    Then status 200
    And match $.data[*].applicationId !contains createdAppNo

  Scenario: Verify find by postcode
    Given path 'applications'
    And param postcode = 'SK6 8GH '
    When method GET
    Then status 200
    And match $.data[*].applicationId contains createdAppNo

  Scenario: Verify find by postcode no result
    Given path 'applications'
    And param postcode = 'SK6'
    When method GET
    Then status 200
    And match $.data[*].applicationId !contains createdAppNo

  Scenario: Verify find by postcode and type
    Given path 'applications'
    And param postcode = 'SK6 8GH '
    When method GET
    Then status 200
    And match $.data[*].applicationId contains createdAppNo

  Scenario: Verify find by postcode but wrong type no result
    Given path 'applications'
    And param postcode = 'SK6'
    When method GET
    Then status 200
    And match $.data[*].applicationId !contains createdAppNo

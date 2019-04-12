@version
Feature: Verify incorrect version fails

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-la-editor-aberd.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify bean validation gives common response wrapped error
    Given path 'applications/4cf7be77-cfe7-4c9f-a229-ea61e903fb3a/transfers'
    And request '{"transferToLaShortCode" : "ABERD" }'
    When method POST
    Then status 406

  Scenario: Verify 406 when mismatched versions
    * header Accept = 'application/vnd.bluebadge-api.v0+json, application/json'
    Given path 'applications/4cf7be77-cfe7-4c9f-a229-ea61e903fb3a/transfers'
    And request '{"transferToLaShortCode" : "ABERD" }'
    When method POST
    Then status 406
    And match $.apiVersion contains '#notnull'

  Scenario: Verify 406 when missing header
    Given path 'applications/4cf7be77-cfe7-4c9f-a229-ea61e903fb3a/transfers'
    And request '{"transferToLaShortCode" : "ABERD" }'
    When method POST
    Then status 406
    And match $.apiVersion contains '#notnull'

  Scenario: Verify OK when matching header and version in common response
    Given path 'applications/4cf7be77-cfe7-4c9f-a229-ea61e903fb3a/transfers'
    When method GET
    Then status 200
    And match $.apiVersion contains '#notnull'

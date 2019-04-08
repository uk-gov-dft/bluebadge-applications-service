@version
Feature: Verify incorrect version fails

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-la-editor-aberd.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = 'application/vnd.bluebadge-api.v0+json, application/json'

  Scenario: Verify bean validation gives common response wrapped error
    Given path 'applications/' + createdAppNo + '/transfers'
    And request '{"transferToLaShortCode" : "ABERD" }'
    When method POST
    Then status 406
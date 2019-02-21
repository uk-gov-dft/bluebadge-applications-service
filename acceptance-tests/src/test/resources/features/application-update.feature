@application-update
Feature: Verify update

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * def createResult = callonce read('./application-create-org-ok.feature')
    * def createdAppNo = createResult.applicationId
    # Added below so that subsequent requests use the correct creds
    * def result = callonce read('./oauth2-la-editor.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify update ok
    Given path 'applications/' + createdAppNo
    And request '{"applicationStatus" : "IN_PROGRESS" }'
    When method PUT
    Then status 200

    # Read the updated application
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'applications/' + createdAppNo
    When method GET
    Then status 200
    And match $.data.applicationId contains createdAppNo
    And match $.data.applicationStatus == 'IN_PROGRESS'

  Scenario: Verify update 400 invalid uuid
    Given path 'applications/' + 'ABC'
    And request '{"applicationStatus" : "IN_PROGRESS" }'
    When method PUT
    Then status 400

  Scenario: Verify update 400 app not exists
    Given path 'applications/' + 'a305706c-99ca-4e2a-ba0e-96d198de42f3'
    And request '{"applicationStatus" : "IN_PROGRESS" }'
    When method PUT
    Then status 400

  Scenario: Verify update - App exists in different authority
    Given path 'applications/' + '4cf7be77-cfe7-4c9f-a229-ea61e903fb3a'
    And request '{"applicationStatus" : "IN_PROGRESS" }'
    When method PUT
    Then status 403

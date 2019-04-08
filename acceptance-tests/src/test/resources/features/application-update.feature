@application-update
Feature: Verify update

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-la-editor.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

  Scenario: Verify update 400 invalid uuid
    Given path 'applications/' + 'ABC'
    And request '{"applicationStatus" : "INPROGRESS" }'
    When method PUT
    Then status 400

  Scenario: Verify update 404 app not exists
    Given path 'applications/a305706c-99ca-4e2a-ba0e-96d198deffff'
    And request '{"applicationStatus" : "INPROGRESS" }'
    When method PUT
    Then status 404

  Scenario: Verify update - App exists in different authority
    Given path 'applications/' + '4cf7be77-cfe7-4c9f-a229-ea61e903fb3a'
    And request '{"applicationStatus" : "INPROGRESS" }'
    When method PUT
    Then status 403

  Scenario: Verify update ok
    Given path 'applications/11114c39-02d5-4197-b032-1d9ce22c24b5'
    And request '{"applicationStatus" : "INPROGRESS" }'
    When method PUT
    Then status 200

    # Read the updated application
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    Given path 'applications/11114c39-02d5-4197-b032-1d9ce22c24b5'
    When method GET
    Then status 200
    And match $.data.applicationId contains '11114c39-02d5-4197-b032-1d9ce22c24b5'
    And match $.data.applicationStatus == 'INPROGRESS'

@application-transfer
Feature: Verify transfer

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * header Content-Type = 'application/json'
    * def createResult = callonce read('./application-create-org-ok.feature')
    * def createdAppNo = createResult.applicationId

  Scenario: Verify transfer ok
    * def result = callonce read('./oauth2-la-editor-aberd.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'applications/' + createdAppNo + '/transfers'
    And request '{"transferToLaShortCode" : "ANGL" }'
    When method POST
    Then status 200

    # Read the updated application
    * def result = callonce read('./oauth2-la-editor-aberd.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'applications/' + createdAppNo
    When method GET
    Then status 403

    # Read the updated application
    * def result = callonce read('./oauth2-3rd-party-angl.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'applications/' + createdAppNo
    When method GET
    Then status 200
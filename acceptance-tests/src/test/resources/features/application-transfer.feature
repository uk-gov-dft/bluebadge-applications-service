@application-transfer
Feature: Verify transfer

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * header Content-Type = 'application/json'
    * header Accept = jsonVersionHeader
    * def createResult = callonce read('./application-create-org-ok.feature')
    * def createdAppNo = createResult.applicationId

  Scenario: Verify transfer doesn't happen for same LA
    * def result = callonce read('./oauth2-la-editor-aberd.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    Given path 'applications/' + createdAppNo + '/transfers'
    And request '{"transferToLaShortCode" : "ABERD" }'
    When method POST
    Then status 400

  Scenario: Verify transfer only happens for valid LA
    * def result = callonce read('./oauth2-la-editor-aberd.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    Given path 'applications/' + createdAppNo + '/transfers'
    And request '{"transferToLaShortCode" : "KEN" }'
    When method POST
    Then status 400

  Scenario: Verify transfer ok
    * def result = callonce read('./oauth2-la-editor-aberd.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    Given path 'applications/' + createdAppNo + '/transfers'
    And request '{"transferToLaShortCode" : "ANGL" }'
    When method POST
    Then status 200

    # Read the updated application
    * def result = callonce read('./oauth2-la-editor-aberd.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    Given path 'applications/' + createdAppNo
    When method GET
    Then status 403

    # Read the updated application
    * def result = callonce read('./oauth2-3rd-party-angl.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    Given path 'applications/' + createdAppNo
    When method GET
    Then status 200

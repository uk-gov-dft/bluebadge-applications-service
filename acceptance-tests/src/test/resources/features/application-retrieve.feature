@application-retrieve
Feature: Verify find newly created org badge

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

  Scenario: Verify retrieve ok
    Given path 'applications/' + createdAppNo
    When method GET
    Then status 200
    And match $.data.applicationId contains createdAppNo
    And match $.data.party.organisation.vehicles contains {registrationNumber:"VK61VZZ", typeCode:"#notnull", usageFrequency:"#notnull"}
    And match $.data.party.organisation.vehicles contains {registrationNumber:"VK62VZZ", typeCode:"#notnull", usageFrequency:"#notnull"}

  Scenario: Verify retrieve 400 invalid uuid
    Given path 'applications/' + 'ABC'
    When method GET
    Then status 400

  Scenario: Verify retrieve 404 app not exists
    Given path 'applications/' + 'a305706c-99ca-4e2a-ba0e-96d198de42f3'
    When method GET
    Then status 404

  Scenario: App exists in different authority
    Given path 'applications/' + '4cf7be77-cfe7-4c9f-a229-ea61e903fb3a'
    When method GET
    Then status 404
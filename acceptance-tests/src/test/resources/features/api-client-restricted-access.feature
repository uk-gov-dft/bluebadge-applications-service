@api-client
Feature: Verify API users do not have access to create applications

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Create application denied
    Given path 'applications'
    And request {applicationTypeCode: " "}
    When method POST
    Then status 403

  Scenario: Verify all other end points are blocked
    Given path 'something'
    When method GET
    Then status 403

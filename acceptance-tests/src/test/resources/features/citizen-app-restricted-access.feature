@api-client
Feature: Verify citizen app client does not have access services other than create application

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-citizen-app.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

  Scenario: Find application denied
    Given path 'applications'
    And param name = 'Delete'
    When method GET
    Then status 403

  Scenario: Verify retrieve denied
    Given path 'applications/someappno'
    When method GET
    Then status 403

  Scenario: Verify all other end points are blocked
    Given path 'something'
    When method GET
    Then status 403

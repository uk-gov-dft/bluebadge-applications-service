@application-create-mandatory-missing
Feature: Verify Create application validation exceptions for mandatory fields

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-citizen-app.feature')
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify invalid create person with blank request body
    * def application =
    """
    {}
    """

    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotNull.application.party", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotNull.application.applicationTypeCode", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotNull.application.localAuthorityCode", location:"#null", locationType:"#null"}

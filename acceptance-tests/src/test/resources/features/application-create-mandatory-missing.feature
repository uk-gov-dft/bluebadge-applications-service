@application-create-mandatory-missing
Feature: Verify Create application validation exceptions for mandatory fields

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
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

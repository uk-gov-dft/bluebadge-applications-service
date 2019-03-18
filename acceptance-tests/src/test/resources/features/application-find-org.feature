@application-find-org
Feature: Verify find org

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

  Scenario: Verify find by name
    Given path 'applications'
    And param name = 'Delete'
    When method GET
    Then status 200
    And match $.data[*].applicationId contains createdAppNo
    And match $.pagingInfo.count == 1
    And match $.pagingInfo.total == 1
    And match $.pagingInfo.pageNum == 1
    And match $.pagingInfo.pageSize == 50
    And match $.pagingInfo.pages == 1

  Scenario: Verify find by name second page
    Given path 'applications'
    And param name = 'Delete'
    And param pageNum = 2
    When method GET
    Then status 200
    And match $.pagingInfo.count == 0
    And match $.pagingInfo.total == 1
    And match $.pagingInfo.pageNum == 2
    And match $.pagingInfo.pageSize == 50
    And match $.pagingInfo.pages == 1

  Scenario: Verify find by name no result
    Given path 'applications'
    And param name = 'TrottersZZZSSSS'
    When method GET
    Then status 200
    And match $.data[*].applicationId !contains createdAppNo

  Scenario: Verify find by postcode
    Given path 'applications'
    And param postcode = 'SW1P 4DR'
    When method GET
    Then status 200
    And match $.data[*].applicationId contains createdAppNo

  Scenario: Verify find by postcode no result
    Given path 'applications'
    And param postcode = 'zz999zz'
    When method GET
    Then status 200
    And match $.data[*].applicationId !contains createdAppNo

  Scenario: Verify find by postcode and type
    Given path 'applications'
    And param postcode = 'SW1P 4DR'
    And param applicationTypeCode = 'NEW'
    When method GET
    Then status 200
    And match $.data[*].applicationId contains createdAppNo

  Scenario: Verify find by postcode but wrong type no result
    Given path 'applications'
    And param postcode = 'SW1P 4DR'
    And param applicationTypeCode = 'CANCEL'
    When method GET
    Then status 200
    And match $.data[*].applicationId !contains createdAppNo

  Scenario: Verify find by submission date from
    Given path 'applications'
    And param postcode = 'SW1P 4DR'
    And param from = '2018-01-01T12:30:45Z'
    When method GET
    Then status 200
    And match $.data[*].applicationId contains createdAppNo

  Scenario: Verify find by submission date from no result
    Given path 'applications'
    And param postcode = 'SW1P 4DR'
    And param from = '2050-11-25T12:30:45Z'
    When method GET
    Then status 200
    And match $.data[*].applicationId !contains createdAppNo

  Scenario: Verify find by submission date to
    Given path 'applications'
    And param postcode = 'SW1P 4DR'
    And param to = '2050-01-01T12:30:45.123Z'
    When method GET
    Then status 200
    And match $.data[*].applicationId contains createdAppNo

  Scenario: Verify find by submission date to no result
    Given path 'applications'
    And param postcode = 'SW1P 4DR'
    And param to = '2017-11-25T12:30:45Z'
    When method GET
    Then status 200
    And match $.data[*].applicationId !contains createdAppNo

  Scenario: Verify find all filters set
    Given path 'applications'
    And param name = 'Delete'
    And param postcode = 'SW1P 4DR'
    And param from = '2018-01-01T12:30:45Z'
    And param to = '2050-01-01T12:30:45.123Z'
    And param applicationTypeCode = 'NEW'
    When method GET
    Then status 200
    And match $.data[*].applicationId contains createdAppNo
    And match $.data[0] contains {submissionDate:"#notnull"}

  Scenario: Verify find by wrong application type code
    Given path 'applications'
    And param applicationTypeCode = 'NEWWRONG'
    When method GET
    Then status 400
    And match $.error.message contains 'Invalid applicationTypeCode: NEWWRONG'
    And match $.error.reason contains 'applicationTypeCode'

  Scenario: Verify invalid paging params (pageNum) results in 400
    Given path 'applications'
    And param name = 'Delete'
    And param pageNum = 0
    When method GET
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"Min.pagingParams.pageNum", location:"#null", locationType:"#null"}

  Scenario: Verify invalid paging params (pageSize = 0) results in 400
    Given path 'applications'
    And param name = 'Delete'
    And param pageSize = 0
    When method GET
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"Min.pagingParams.pageSize", location:"#null", locationType:"#null"}

  Scenario: Verify invalid paging params (pageSize > 200) results in 400
    Given path 'applications'
    And param name = 'Delete'
    And param pageSize = 201
    When method GET
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"Max.pagingParams.pageSize", location:"#null", locationType:"#null"}

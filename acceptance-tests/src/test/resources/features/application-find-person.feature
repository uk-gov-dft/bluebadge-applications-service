@application-find-person
Feature: Verify find person

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-la-editor-aberd.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify find by name
    Given path 'applications'
    And param name = 'Holder Name'
    When method GET
    Then status 200
    And match $.data[*].applicationId contains only ['11114c39-02d5-4197-b032-1d9ce22c24b5', '89ca4c39-02d5-4197-b032-1d9ce22c24b5', '7d93fdb5-56bf-41b3-8af0-147696711410']
    And match $.pagingInfo.count == 3
    And match $.pagingInfo.total == 3
    And match $.pagingInfo.pageNum == 1
    And match $.pagingInfo.pageSize == 50
    And match $.pagingInfo.pages == 1

  Scenario: Verify find by name second page
    Given path 'applications'
    And param name = 'Holder Name'
    And param pageNum = 2
    When method GET
    Then status 200
    And match $.pagingInfo.count == 0
    And match $.pagingInfo.total == 3
    And match $.pagingInfo.pageNum == 2
    And match $.pagingInfo.pageSize == 50
    And match $.pagingInfo.pages == 1

  Scenario: Verify find by name no result
    Given path 'applications'
    And param name = 'TrottersZZZSSSS'
    When method GET
    Then status 200
    And match $.pagingInfo.count == 0
    And match $.pagingInfo.total == 0
    And match $.pagingInfo.pageNum == 1
    And match $.pagingInfo.pageSize == 50
    And match $.pagingInfo.pages == 0

  Scenario: Verify find by postcode
    Given path 'applications'
    And param postcode = 'ZZ111ZZ'
    When method GET
    Then status 200
    And match $.data[*].applicationId contains only ['11114c39-02d5-4197-b032-1d9ce22c24b5', '89ca4c39-02d5-4197-b032-1d9ce22c24b5', '7d93fdb5-56bf-41b3-8af0-147696711410']
    And match $.pagingInfo.count == 3
    And match $.pagingInfo.total == 3
    And match $.pagingInfo.pageNum == 1
    And match $.pagingInfo.pageSize == 50
    And match $.pagingInfo.pages == 1

  Scenario: Verify find by postcode no result
    Given path 'applications'
    And param postcode = 'zz999zz'
    When method GET
    Then status 200
    And match $.pagingInfo.count == 0
    And match $.pagingInfo.total == 0
    And match $.pagingInfo.pageNum == 1
    And match $.pagingInfo.pageSize == 50
    And match $.pagingInfo.pages == 0

  Scenario: Verify find by postcode and type
    Given path 'applications'
    And param postcode = 'ZZ111ZZ'
    And param applicationTypeCode = 'NEW'
    When method GET
    Then status 200
    And match $.data[*].applicationId contains only ['11114c39-02d5-4197-b032-1d9ce22c24b5', '89ca4c39-02d5-4197-b032-1d9ce22c24b5', '7d93fdb5-56bf-41b3-8af0-147696711410']
    And match $.pagingInfo.count == 3
    And match $.pagingInfo.total == 3
    And match $.pagingInfo.pageNum == 1
    And match $.pagingInfo.pageSize == 50
    And match $.pagingInfo.pages == 1

  Scenario: Verify find by postcode but wrong type no result
    Given path 'applications'
    And param postcode = 'ZZ111ZZ'
    And param applicationTypeCode = 'CANCEL'
    When method GET
    Then status 200
    And match $.pagingInfo.count == 0
    And match $.pagingInfo.total == 0
    And match $.pagingInfo.pageNum == 1
    And match $.pagingInfo.pageSize == 50
    And match $.pagingInfo.pages == 0

  Scenario: Verify find by submission date from
    Given path 'applications'
    And param postcode = 'ZZ111ZZ'
    And param from = '2018-12-25T12:30:45Z'
    When method GET
    Then status 200
    And match $.data[*].applicationId contains only ['11114c39-02d5-4197-b032-1d9ce22c24b5', '89ca4c39-02d5-4197-b032-1d9ce22c24b5']
    And match $.pagingInfo.count == 2
    And match $.pagingInfo.total == 2
    And match $.pagingInfo.pageNum == 1
    And match $.pagingInfo.pageSize == 50
    And match $.pagingInfo.pages == 1

  Scenario: Verify find by submission date from no result
    Given path 'applications'
    And param postcode = 'ZZ111ZZ'
    And param from = '2050-11-25T12:30:45Z'
    When method GET
    Then status 200
    And match $.pagingInfo.count == 0
    And match $.pagingInfo.total == 0
    And match $.pagingInfo.pageNum == 1
    And match $.pagingInfo.pageSize == 50
    And match $.pagingInfo.pages == 0

  Scenario: Verify find by submission date to
    Given path 'applications'
    And param postcode = 'ZZ111ZZ'
    And param to = '2050-01-01T12:30:45.123Z'
    When method GET
    Then status 200
    And match $.data[*].applicationId contains only ['11114c39-02d5-4197-b032-1d9ce22c24b5', '89ca4c39-02d5-4197-b032-1d9ce22c24b5', '7d93fdb5-56bf-41b3-8af0-147696711410']
    And match $.pagingInfo.count == 3
    And match $.pagingInfo.total == 3
    And match $.pagingInfo.pageNum == 1
    And match $.pagingInfo.pageSize == 50
    And match $.pagingInfo.pages == 1

  Scenario: Verify find by submission date to no result
    Given path 'applications'
    And param postcode = 'ZZ111ZZ'
    And param to = '2010-11-25T12:30:45Z'
    When method GET
    Then status 200
    And match $.pagingInfo.count == 0
    And match $.pagingInfo.total == 0
    And match $.pagingInfo.pageNum == 1
    And match $.pagingInfo.pageSize == 50
    And match $.pagingInfo.pages == 0

  Scenario: Verify find all filters set
    Given path 'applications'
    And param name = 'Holder Name'
    And param postcode = 'ZZ111ZZ'
    And param from = '2018-01-01T12:30:45Z'
    And param to = '2050-01-01T12:30:45.123Z'
    And param applicationTypeCode = 'NEW'
    When method GET
    Then status 200
    And match $.data[*].applicationId contains only ['11114c39-02d5-4197-b032-1d9ce22c24b5', '89ca4c39-02d5-4197-b032-1d9ce22c24b5']

  Scenario: Verify find by wrong application type code
    Given path 'applications'
    And param applicationTypeCode = 'NEWWRONG'
    When method GET
    Then status 400
    And match $.error.message contains 'Invalid applicationTypeCode: NEWWRONG'
    And match $.error.reason contains 'applicationTypeCode'

  Scenario: Verify invalid paging params (pageNum) results in 400
    Given path 'applications'
    And param name = 'Holder Name'
    And param pageNum = 0
    When method GET
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"Min.pagingParams.pageNum", location:"#null", locationType:"#null"}

  Scenario: Verify invalid paging params (pageSize = 0) results in 400
    Given path 'applications'
    And param name = 'Holder Name'
    And param pageSize = 0
    When method GET
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"Min.pagingParams.pageSize", location:"#null", locationType:"#null"}

  Scenario: Verify invalid paging params (pageSize > 200) results in 400
    Given path 'applications'
    And param name = 'Holder Name'
    And param pageSize = 201
    When method GET
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"Max.pagingParams.pageSize", location:"#null", locationType:"#null"}

  Scenario: Verify invalid paging params (pageSize = '') results in 400
    Given path 'applications'
    And param name = 'Holder Name'
    And param pageSize = ''
    And param pageNum = 1
    When method GET
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotNull.pagingParams.pageSize", location:"#null", locationType:"#null"}

  Scenario: Verify invalid paging params (pageSize = only spaces string) results in 400
    Given path 'applications'
    And param name = 'Holder Name'
    And param pageSize = '     '
    And param pageNum = 1
    When method GET
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotNull.pagingParams.pageSize", location:"#null", locationType:"#null"}

  Scenario: Verify invalid paging params (pageNum = only spaces string) results in 400
    Given path 'applications'
    And param name = 'Holder Name'
    And param pageSize = 10
    And param pageNum = '     '
    When method GET
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotNull.pagingParams.pageNum", location:"#null", locationType:"#null"}

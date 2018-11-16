@application-retrieve-childU3
Feature: Verify retrieve childU3

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify retrieve ok
    Given path 'applications/89ca4c39-02d5-4197-b032-1d9ce22c24b5'
    When method GET
    Then status 200
    And match $.data.applicationId contains '89ca4c39-02d5-4197-b032-1d9ce22c24b5'
    # Value in array of bulkyMedicalEquipmentTypeCode
    And match $.data.eligibility.childUnder3.bulkyMedicalEquipmentTypeCodes contains "SUCTION"
    # Value in deprecated bulkyMedicalEquipmentTypeCode field - This can be deleted after deprecation
    And match $.data.eligibility.childUnder3 contains {bulkyMedicalEquipmentTypeCode:"SUCTION"}
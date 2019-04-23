@application-delete
Feature: Verify delete

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    * def cantDeleteAppNo = '0bd06c01-a193-4255-be0b-0fbee253ee5e'
    * def canDeleteAppNo = '7d93fdb5-56bf-41b3-8af0-147696711410'
    * def System = Java.type('java.lang.System')
    * def env = System.getenv('bb_env')
    * def S3Utils = Java.type('uk.gov.service.bluebadge.test.utils.S3Utils')
    * def s3 = new S3Utils('uk-gov-dft-' + (env == null ? 'ci' : env) +'-applications')
    * def s3File = s3.putFile('/small.png', 'appArtifactsTestFile.png')
 
    
   Scenario: Verify delete ok
    Given path 'applications/' + canDeleteAppNo
     And assert s3.fileExists('appArtifactsTestFile.png') == true
    When method DELETE
    Then status 200
     And assert s3.fileExists('appArtifactsTestFile.png') == false



  Scenario: Verify delete 400 invalid uuid
    Given path 'applications/' + 'ABC'
    When method DELETE
    Then status 400
    
   Scenario: Verify delete 404 application not exists
    Given path 'applications/' + '8971c8b1-2601-4025-b339-facbd555a4c2'
    When method DELETE
    Then status 404
    
   Scenario: Application exists in different authority
    Given path 'applications/' + cantDeleteAppNo
    When method DELETE
    Then status 403   
    
    
  Scenario: Verify delete 403 NO PERMISSION
    * def result_citizen = callonce read('./oauth2-citizen-app.feature')
    * header Authorization = 'Bearer ' + result_citizen.accessToken
    Given path 'applications/' + canDeleteAppNo
    When method DELETE
    Then status 403
    
    
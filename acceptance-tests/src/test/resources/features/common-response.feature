@common-response
Feature: Verify responses wrap a CommonResponse

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-citizen-app.feature')
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify 400 common response when @Valid fails
    * def application =
    """
    {
  applicationId: '',
  applicationTypeCode: 'NEW',
  localAuthorityCode: 'ABERD',
  paymentTaken: true,
  submissionDate: '2018-12-25T12:30:45Z',
  existingBadgeNumber: 'KKKJJJ',
  party: {
    typeCode: 'ORG',
    contact: {
      fullName: 'Mabel Jones',
      buildingStreet: '65 Basil Chambers',
      line2: 'Northern Quarter',
      townCity: 'Manchester',
      postCode: 'ZZ111ZZ',
      primaryPhoneNumber: 175154771,
      secondaryPhoneNumber: '07970777111',
      emailAddress: 'nobody@blancmange.com'
    },
    organisation: {
      badgeHolderName: 'TestDeleteMe',
      isCharity: true,
      charityNumber: '12345',
      numberOfBadges: -1
    }
  },
  paymentTaken: true,
  paymentReference: "paymentref"
}
    """

    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"party.organisation.numberOfBadges",reason:"must be greater than or equal to 1",message:"Min.application.party.organisation.numberOfBadges",location:null,locationType:null}

  Scenario: Jackson deserialize error in body returns common response
    * def application =
    """
    {
  applicationId: '',
  applicationTypeCode: 'NEW',
  localAuthorityCode: 'ABERD',
  paymentTaken: true,
  submissionDate: '2018-12-25T12:30:45Z',
  existingBadgeNumber: 'KKKJJJ',
  party: {
    typeCode: 'ORG',
    contact: {
      fullName: 'Mabel Jones',
      buildingStreet: '65 Basil Chambers',
      line2: 'Northern Quarter',
      townCity: 'Manchester',
      postCode: 'ZZ111ZZ',
      primaryPhoneNumber: 175154771,
      secondaryPhoneNumber: '07970777111',
      emailAddress: 'nobody@blancmange.com'
    },
    organisation: {
      badgeHolderName: 'TestDeleteMe',
      isCharity: true,
      charityNumber: '12345',
      numberOfBadges: 'A string'
    }
  },
  paymentTaken: true,
  paymentReference: "paymentref"
}
    """

    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"party.organisation.numberOfBadges",reason:"`A string` is not a valid Integer.",message:"InvalidFormat.party.organisation.numberOfBadges",location:null,locationType:null}

  Scenario: Invalid query param (from date)
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'applications'
    And param name = 'Delete'
    And param postcode = 'zz111zz'
    And param from = 'Not a date'
    And param to = '2050-01-01T12:30:45.123Z'
    And param applicationTypeCode = 'NEW'
    When method GET
    Then status 400
    And match $.error.errors contains {field:"from",reason:"Failed to convert value:`Not a date`",message:"typeMismatch.findApplicationQueryParams.from",location:null,locationType:null}

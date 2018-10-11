@application-create-org-invalid-enum
Feature: Verify Create application

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-citizen-app.feature')
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify valid create organisation
    * def application =
    """
    {
  applicationId: '',
  applicationTypeCode: 'NEW',
  localAuthorityCode: 'BIRM',
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
      postCode: 'zz11 1zz',
      primaryPhoneNumber: 175154771,
      secondaryPhoneNumber: '07970777111',
      emailAddress: 'nobody@blancmange.com'
    },
    organisation: {
      badgeHolderName: 'TestDeleteMe',
      isCharity: true,
      charityNumber: '12345',
      vehicles: [
        {
          registrationNumber: 'VK61VZZ',
          typeCode: 'I AM INVALID',
          usageFrequency: 'Daily'
        }
      ],
      numberOfBadges: 1
    }
  }
}
    """

    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.message contains "InvalidFormat.VehicleTypeCodeField"

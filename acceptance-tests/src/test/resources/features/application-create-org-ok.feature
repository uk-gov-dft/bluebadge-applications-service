@application-create-org-ok
Feature: Verify Create org ok

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-citizen-app.feature')
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

  Scenario: Verify valid create organisation
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
      postCode: 'SW1P 4DR',
      primaryPhoneNumber: 1751 54771,
      secondaryPhoneNumber: ' 0 7 9 707 7 7111 ',
      emailAddress: 'nobody@blancmange.com'
    },
    organisation: {
      badgeHolderName: 'TestDeleteMe',
      isCharity: true,
      charityNumber: '12345',
      vehicles: [
        {
          registrationNumber: 'VK61VZZ',
          typeCode: 'CAR',
          usageFrequency: 'Daily'
        },
        {
          registrationNumber: 'VK62VZZ',
          typeCode: 'CAR',
          usageFrequency: 'Daily'
        }
      ],
      numberOfBadges: 1
    }
  },
  paymentTaken: true,
  paymentReference: "paymentref"
}
    """

    Given path 'applications'
    And request application
    When method POST
    Then status 200
    And match $.data contains "#notnull"
    * def applicationId = $.data

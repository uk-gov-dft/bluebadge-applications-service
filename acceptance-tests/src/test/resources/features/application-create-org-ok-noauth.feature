@application-create-org-ok-noauth
Feature: Verify Create application fails auth

  Background:
    * url baseUrl


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
      postCode: 'SK6 8GH',
      primaryPhoneNumber: 175154771,
      secondaryPhoneNumber: '07970777111',
      emailAddress: 'nobody@blancmange.com'
    },
    organisation: {
      badgeHolderName: 'Trotters Independant Traders',
      isCharity: true,
      charityNumber: '12345',
      vehicles: [
        {
          registrationNumber: 'VK61VZZ',
          typeCode: 'CAR',
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
    Then status 401


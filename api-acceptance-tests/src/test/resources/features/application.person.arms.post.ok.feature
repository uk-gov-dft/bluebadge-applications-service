@application-person-arms-post-ok
Feature: Verify Create application Person with arms problems

  Background:
    * url baseUrl

  Scenario: Verify valid create for person with arms problems
    * def application =
    """
    {
  applicationId: '',
  applicationTypeCode: 'NEW',
  localAuthorityCode: 'BIRM',
  isPaymentTaken: false,
  submissionDate: '2018-12-25T12:30:45Z',
  existingBadgeNumber: 'KKKKKK',
  party: {
    typeCode: 'PERSON',
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
    person: {
      badgeHolderName: 'John Smith',
      nino: 'NS123456A',
      dob: '1970-05-29',
      nameAtBirth: 'John Smith',
      genderCode: 'MALE'
    },
  },
  eligibility: {
    typeCode: 'ARMS',
    descriptionOfConditions: 'description of conditions',
    disabilityArms: {
      drivingFrequency: 'driving frequency',
      isAdaptedVehicle: true,
      adaptedVehicleDescription: 'adapted vehicle description'
    }
  },
  artifacts: {
    proofOfEligibilityUrl: 'string',
    proofOfAddressUrl: 'string',
    proofOfIdentityUrl: 'string',
    badgePhotoUrl: 'string',
    proofOfEligibility: 'string',
    proofOfAddress: 'string',
    proofOfIdentity: 'string',
    badgePhoto: 'string'
  }
}
    """

    Given path 'applications'
    And request application
    When method POST
    Then status 200
    And match $.data contains "#notnull"

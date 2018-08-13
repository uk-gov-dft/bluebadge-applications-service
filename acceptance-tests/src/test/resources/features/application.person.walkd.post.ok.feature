@application-person-walkd-post-ok
Feature: Verify Create application for person with walking difficulty

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify valid create application for person with walking difficulty
    * def application =
    """
    {
  applicationId: '',
  applicationTypeCode: 'NEW',
  localAuthorityCode: 'BIRM',
  paymentTaken: true,
  submissionDate: '2018-12-25T12:30:45Z',
  existingBadgeNumber: ,
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
    }
  },
  eligibility: {
    typeCode: 'WALKD',
    descriptionOfConditions: 'Freetext',
    walkingDifficulty: {
      typeCodes: [
        'BALANCE', 'SOMELSE'
      ],
      otherDescription: 'other description',
      walkingAids: [
        {
          description: 'walk aid description',
          usage: 'walk aid usage',
          howProvidedCode: 'PRIVATE'
        }
      ],
      walkingLengthOfTimeCode: 'LESSMIN',
      walkingSpeedCode: 'SLOW',
      treatments: [
        {
          description: 'treatment desc',
          time: 'treatment time'
        }
      ],
      medications: [
        {
          name: 'medication name',
          isPrescribed: true,
          frequency: 'medication frequency',
          quantity: 'medication quantity'
        }
      ]
    },
    healthcareProfessionals: [
      {
        name: 'pro name',
        location: 'pro location'
      },
      {
        name: 'Doctor Bob',
        location: 'My lovely hospital'
      }
    ]
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

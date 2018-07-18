@badge-post
Feature: Verify Create application

  Background:
    * url baseUrl

  Scenario: Verify valid create
    * def application =
    """
    {
  applicationId: '',
  applicationTypeCode: 'NEW',
  localAuthorityCode: 211,
  isPaymentTaken: true,
  submissionDate: '2018-12-25T12:30:45Z',
  existingBadgeNumber: 'string',
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
    organisation: {
      badgeHolderName: 'Trotters Independant Traders',
      isCharity: false,
      charityNumber: '12345',
      vehicles: [
        {
          registrationNumber: 'VK61VZZ',
          typeCode: 'TBC',
          usageFrequency: 'Daily'
        }
      ],
      numberOfBadges: 1
    }
  },
  eligibility: {
    typeCode: 'string',
    descriptionOfConditions: 'Freetext',
    benefit: {
      isIndefinite: true,
      expiryDate: '2022-03-30'
    },
    walkingDifficulty: {
      typeCodes: [
        'BALANCE'
      ],
      otherDescription: 'string',
      walkingAids: [
        {
          description: 'string',
          usage: 'string',
          howProvidedCode: 'string'
        }
      ],
      walkingLengthOfTimeCode: 'string',
      walkingSpeedCode: 'string',
      treatments: [
        {
          description: 'string',
          time: 'string'
        }
      ],
      medications: [
        {
          name: 'string',
          isPrescribed: true,
          frequency: 'string',
          quantity: 'string'
        }
      ]
    },
    disabilityArms: {
      drivingFrequency: 'string',
      isAdaptedVehicle: true,
      adaptedVehicleDescription: 'string'
    },
    healthcareProfessionals: [
      {
        name: 'Freetext',
        location: 'Freetext'
      },
      {
        name: 'Doctor Bob',
        location: 'My lovely hospital'
      }
    ],
    blind: {
      registeredAtLaId: 7
    },
    childUnder3: {
      bulkyMedicalEquipmentTypeCode: 'string'
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

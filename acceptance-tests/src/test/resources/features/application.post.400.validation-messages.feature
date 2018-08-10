@application-post-400
Feature: Verify Create application validation exceptions

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify invalid create person
    * def application =
    """
    {
  applicationId: '',
  applicationTypeCode: 'NEW',
  localAuthorityCode: '',
  paymentTaken: ,
  submissionDate: '2018-12-25T12:30:45Z',
  existingBadgeNumber: '',
  party: {
    typeCode: 'PERSON',
    contact: {
      fullName: 'Mabel Jones',
      buildingStreet: '65 Basil Chambers',
      line2: 'Northern Quarter',
      townCity: 'Manchester',
      postCode: 'SK6 8GH',
      primaryPhoneNumber: '01234123123somewhattoolong',
      secondaryPhoneNumber: '07970777111',
      emailAddress: 'nobody.thiswillmaketheemaillongerthanahundredcharachters.hjjhjhjkmkmkmkmkmkjkjkjkjkjsfsfsdfsffsffdfd@blancmange.com'
    },
    person: {
      badgeHolderName: 'John Smith',
      nino: 'NS123456A-1234',
      dob: '1970-05-29',
      nameAtBirth: 'John Smith',
      genderCode: 'FEMALE'
    },
    organisation: {
      badgeHolderName: 'Trotters Independant Traders',
      isCharity: false,
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
  },
  eligibility: {
    typeCode: 'PIP',
    descriptionOfConditions: 'Freetext',
    benefit: {
      isIndefinite: true,
      expiryDate: '2022-03-30'
    },
    walkingDifficulty: {
      typeCodes: [
        'PAIN'
      ],
      otherDescription: 'string',
      walkingAids: [
        {
          description: 'string',
          usage: 'string',
          howProvidedCode: 'PRIVATE'
        }
      ],
      walkingLengthOfTimeCode: 'CANTWALK',
      walkingSpeedCode: 'SLOW',
      treatments: [
        {
          description: 'string',
          time: '6 months'
        }
      ],
      medications: [
        {
          name: 'Paracetamol',
          isPrescribed: true,
          frequency: 'Twice daily.',
          quantity: '1 tablet, 20mg'
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
      }
    ],
    blind: {
      registeredAtLaId: 'BIRM'
    },
    childUnder3: {
      bulkyMedicalEquipmentTypeCode: 'CAST'
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
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotNull.application.localAuthorityCode", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotValid.application.party.organisation", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotValid.application.eligibility.benefit.expiryDate", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotValid.application.eligibility.blind", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotValid.application.eligibility.disabilityArms", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotValid.application.eligibility.walkingDifficulty", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotValid.application.eligibility.childUnder3", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotNull.application.paymentTaken", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"Pattern.application.party.person.nino", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"Size.application.party.contact.primaryPhoneNumber", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"Size.application.party.contact.emailAddress", location:"#null", locationType:"#null"}
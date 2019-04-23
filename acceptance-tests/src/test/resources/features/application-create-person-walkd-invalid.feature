@application-create-person-walkd-invalid
Feature: Verify Create person walkd invalid

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-citizen-app.feature')
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

  Scenario: Verify invalid create application for person with walking difficulty when walking difficulty is not breath but breathlessness type is provided
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
      postCode: 'SW1P 4DR',
      primaryPhoneNumber: 175154771,
      secondaryPhoneNumber: '07970777111',
      emailAddress: 'nobody@blancmange.com'
    },
    person: {
      badgeHolderName: 'PersonDeleteMe',
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
      ],
      breathlessness: {
        typeCodes: [
          'OWNPACE', 'OTHER'
        ],
        otherDescription: 'other description'
      }
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
  artifacts: [
  ]
}
    """

    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"eligibility.walkingDifficulty.breathlessness.typeCodes", reason:"For BREATHLESSNESS you must select BREATH as on of the Walking difficulty types", message:"NotValid.application.eligibility.walkingDifficulty.breathlessness.typeCodes", location:"#null", locationType:"#null"}



  Scenario: Verify invalid create application for person with walking difficulty when walking difficulty is breath but breathlessness type is not provided
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
      postCode: 'SW1P 4DR',
      primaryPhoneNumber: 175154771,
      secondaryPhoneNumber: '07970777111',
      emailAddress: 'nobody@blancmange.com'
    },
    person: {
      badgeHolderName: 'PersonDeleteMe',
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
        'BALANCE', 'SOMELSE', 'BREATH'
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
  artifacts: [
  ]
}
    """

    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"eligibility.walkingDifficulty.breathlessness.typeCodes", reason:"Must have at least 1 BREATHLESSNESS type code if eligibility is WALKDIFF and BREATHLESSNESS is selected.", message:"NotValid.application.eligibility.walkingDifficulty.breathlessness.typeCodes", location:"#null", locationType:"#null"}

  Scenario: Verify invalid create application for person with walking difficulty when walking difficulty is breath but breathlessness type provided is null
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
      postCode: 'SW1P 4DR',
      primaryPhoneNumber: 175154771,
      secondaryPhoneNumber: '07970777111',
      emailAddress: 'nobody@blancmange.com'
    },
    person: {
      badgeHolderName: 'PersonDeleteMe',
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
        'BALANCE', 'SOMELSE', 'BREATH'
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
      ],
      breathlessness: {
        typeCodes: [ null, null ]
      }
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
  artifacts: [
  ]
}
    """

    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"eligibility.walkingDifficulty.breathlessness.typeCodes", reason:"Must have at least 1 BREATHLESSNESS type code if eligibility is WALKDIFF and BREATHLESSNESS is selected.", message:"NotValid.application.eligibility.walkingDifficulty.breathlessness.typeCodes", location:"#null", locationType:"#null"}



  Scenario: Verify invalid create application for person with walking difficulty when walking difficulty is breath, breathlessness is OTHER but other description is not provided
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
      postCode: 'SW1P 4DR',
      primaryPhoneNumber: 175154771,
      secondaryPhoneNumber: '07970777111',
      emailAddress: 'nobody@blancmange.com'
    },
    person: {
      badgeHolderName: 'PersonDeleteMe',
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
        'BALANCE', 'SOMELSE', 'BREATH'
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
      ],
      breathlessness: {
        typeCodes: [
          'OWNPACE', 'OTHER'
        ],
        otherDescription: ''
      }
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
  artifacts: [
  ]
}
    """

    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"eligibility.walkingDifficulty.breathlessness.otherDescription", reason:"eligibility.walkingDifficulty.breathlessness.otherDescription must be present if OTHER selected as a type.", message:"NotValid.application.eligibility.walkingDifficulty.breathlessness.otherDescription", location:"#null", locationType:"#null"}



  Scenario: Verify invalid create application for person with breath walking difficulty, breathlessness is OTHER and other description is longer than 255 characters
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
      postCode: 'SW1P 4DR',
      primaryPhoneNumber: 175154771,
      secondaryPhoneNumber: '07970777111',
      emailAddress: 'nobody@blancmange.com'
    },
    person: {
      badgeHolderName: 'PersonDeleteMe',
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
        'BALANCE', 'SOMELSE', 'BREATH'
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
      ],
      breathlessness: {
        typeCodes: [
          'OWNPACE', 'OTHER'
        ],
        otherDescription: '01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456'
      }
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
  artifacts: [
  ]
}
    """

    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"eligibility.walkingDifficulty.breathlessness.otherDescription", reason:"size must be between 0 and 255", message:"Size.application.eligibility.walkingDifficulty.breathlessness.otherDescription", location:"#null", locationType:"#null"}




  Scenario: Verify invalid create application for person with walking difficulty when walking difficulty is breath, breathlessness is set but not OTHER but other description is provided
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
      postCode: 'SW1P 4DR',
      primaryPhoneNumber: 175154771,
      secondaryPhoneNumber: '07970777111',
      emailAddress: 'nobody@blancmange.com'
    },
    person: {
      badgeHolderName: 'PersonDeleteMe',
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
        'BALANCE', 'SOMELSE', 'BREATH'
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
      ],
      breathlessness: {
        typeCodes: [
          'OWNPACE'
        ],
        otherDescription: 'other description'
      }
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
  artifacts: [
  ]
}
    """

    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"eligibility.walkingDifficulty.breathlessness.otherDescription", reason:"eligibility.walkingDifficulty.breathlessness.otherDescription can only be present if OTHER selected as a type.", message:"NotValid.application.eligibility.walkingDifficulty.breathlessness.otherDescription", location:"#null", locationType:"#null"}



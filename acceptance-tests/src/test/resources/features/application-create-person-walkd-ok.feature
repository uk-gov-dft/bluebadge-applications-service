@application-create-person-walkd-ok
Feature: Verify Create person walkd ok

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-citizen-app.feature')
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

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
    Then status 200
    And match $.data contains "#notnull"

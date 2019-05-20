@application-create-person-walkd
Feature: Verify Create person walkd

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-citizen-app.feature')
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
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
          balanceDescription: 'balance description',
          healthProfessionsForFalls: true,
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

  Scenario: Verify valid create application for person with walking difficulty
    Given path 'applications'
    And request application
    When method POST
    Then status 200
    And match $.data contains "#notnull"

  Scenario: Verify invalid create application for person with walking difficulty when walking difficulty is not breath but breathlessness type is provided
    * set application $.eligibility.walkingDifficulty.typeCodes = ['BALANCE', 'SOMELSE']
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"eligibility.walkingDifficulty.breathlessness.typeCodes", reason:"For BREATHLESSNESS you must select BREATH as on of the Walking difficulty types", message:"NotValid.application.eligibility.walkingDifficulty.breathlessness.typeCodes", location:"#null", locationType:"#null"}

  Scenario: Verify invalid create application for person with walking difficulty when walking difficulty is breath but breathlessness type is not provided
    * set application $.eligibility.walkingDifficulty.breathlessness = {}
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"eligibility.walkingDifficulty.breathlessness.typeCodes", reason:"Must have at least 1 BREATHLESSNESS type code if eligibility is WALKDIFF and BREATHLESSNESS is selected.", message:"NotValid.application.eligibility.walkingDifficulty.breathlessness.typeCodes", location:"#null", locationType:"#null"}

  Scenario: Verify invalid create application for person with walking difficulty when walking difficulty is breath but breathlessness type provided is null
    * set application $.eligibility.walkingDifficulty.breathlessness.typeCodes = breathlessness: [null,null]
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"eligibility.walkingDifficulty.breathlessness.typeCodes", reason:"Must have at least 1 BREATHLESSNESS type code if eligibility is WALKDIFF and BREATHLESSNESS is selected.", message:"NotValid.application.eligibility.walkingDifficulty.breathlessness.typeCodes", location:"#null", locationType:"#null"}

  Scenario: Verify invalid create application for person with walking difficulty when walking difficulty is breath, breathlessness is OTHER but other description is not provided
    * set application $.eligibility.walkingDifficulty.breathlessness = {typeCodes: ['OWNPACE', 'OTHER'],otherDescription: ''}
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"eligibility.walkingDifficulty.breathlessness.otherDescription", reason:"eligibility.walkingDifficulty.breathlessness.otherDescription must be present if OTHER selected as a type.", message:"NotValid.application.eligibility.walkingDifficulty.breathlessness.otherDescription", location:"#null", locationType:"#null"}

  Scenario: Verify invalid create application for person with breath walking difficulty, breathlessness is OTHER and other description is longer than 255 characters
    * set application $.eligibility.walkingDifficulty.breathlessness = {typeCodes: ['OWNPACE', 'OTHER'],otherDescription: '01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456'}
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"eligibility.walkingDifficulty.breathlessness.otherDescription", reason:"size must be between 0 and 255", message:"Size.application.eligibility.walkingDifficulty.breathlessness.otherDescription", location:"#null", locationType:"#null"}

  Scenario: Verify invalid create application for person with walking difficulty when walking difficulty is breath, breathlessness is set but not OTHER but other description is provided
    * set application $.eligibility.walkingDifficulty.breathlessness = {typeCodes: ['OWNPACE'],otherDescription: 'other description'}
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:'eligibility.walkingDifficulty.breathlessness.otherDescription', reason:'#notnull', message:'NotValid.application.eligibility.walkingDifficulty.breathlessness.otherDescription', location:'#null', locationType:'#null'}

  Scenario: Verify invalid create application for person with walking difficulty when walking length of time is null for a NEW application
    * set application $.eligibility.walkingDifficulty.walkingLengthOfTimeCode = null
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:'#notnull', reason:'#notnull', message:'NotNull.application.eligibility.walkingDifficulty.walkingLengthOfTimeCode', location:null, locationType:null}

  Scenario: Verify valid create application for person with walking difficulty when walking length of time is null for a RENEW application
    * set application $.eligibility.walkingDifficulty.walkingLengthOfTimeCode = null
    * set application $.applicationTypeCode = 'RENEW'
    Given path 'applications'
    And request application
    When method POST
    Then status 200

  Scenario: Verify invalid create application for person with walking difficulty no walking difficulty set for NEW application
    * remove application $.eligibility.walkingDifficulty
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:'#notnull', reason:'#notnull', message:'NotNull.application.eligibility.walkingDifficulty', location:null, locationType:null}

  Scenario: Verify valid create application for person with walking difficulty no walking difficulty set for a RENEW application
    * remove application $.eligibility.walkingDifficulty
    * set application $.applicationTypeCode = 'RENEW'
    Given path 'applications'
    And request application
    When method POST
    Then status 200
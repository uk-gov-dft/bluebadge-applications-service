@application-create-person-child-under3
Feature: Verify Create person childU3

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
      paymentTaken: false,
      submissionDate: '2018-12-25T12:30:45Z',
      existingBadgeNumber: 'KKKKKK',
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
        },
      },
      eligibility: {
        typeCode: 'CHILDBULK'
      },
      artifacts: []
    }
    """

  Scenario: Verify valid create for person with child under 3 with OTHER equipment and description
    * set application $.eligibility.childUnder3 = {bulkyMedicalEquipmentTypeCodes:['OTHER', 'VENT'], otherMedicalEquipment: 'Some really big equipment'}
    Given path 'applications'
    And request application
    When method POST
    Then status 200
    And match $.data contains "#notnull"

  Scenario: Verify valid create for person with child under 3 without OTHER equipment and description
    * set application $.eligibility.childUnder3 = {bulkyMedicalEquipmentTypeCodes:['VENT'], otherMedicalEquipment: ''}
    Given path 'applications'
    And request application
    When method POST
    Then status 200
    And match $.data contains "#notnull"

  Scenario: Verify bulky equipment type codes required for new app
    * set application $.eligibility.childUnder3 = {bulkyMedicalEquipmentTypeCodes:[], otherMedicalEquipment: ''}
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotNull.application.eligibility.childUnder3.bulkyMedicalEquipmentTypeCodes", location:"#null", locationType:"#null"}

  Scenario: Verify bulky equipment type codes NOT required for RENEW app
    * set application $.eligibility.childUnder3 = {bulkyMedicalEquipmentTypeCodes:[], otherMedicalEquipment: ''}
    * set application $.applicationTypeCode = 'RENEW'
    Given path 'applications'
    And request application
    When method POST
    Then status 200

  Scenario: Verify 400 create for person with child under 3 with OTHER equipment and NO description
    * set application $.eligibility.childUnder3 = {bulkyMedicalEquipmentTypeCodes:['OTHER', 'VENT'], otherMedicalEquipment: ''}
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotNull.application.eligibility.childUnder3.otherMedicalEquipment", location:"#null", locationType:"#null"}

  Scenario: Verify 400 create for person with child under 3 with OTHER equipment and NO description
    * set application $.eligibility.childUnder3 = {bulkyMedicalEquipmentTypeCodes:['VENT'], otherMedicalEquipment: 'Should not be here'}
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotValid.application.eligibility.childUnder3.otherMedicalEquipment", location:"#null", locationType:"#null"}
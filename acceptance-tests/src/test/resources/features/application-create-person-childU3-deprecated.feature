@application-create-person-child-under3-deprecated
Feature: Verify Create person childU3 deprecated api version

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
        typeCode: 'CHILDBULK',
      },
      artifacts: []
    }
    """

  Scenario: Verify invalid create for person with child under 3 with OTHER equipment and no additional description
    * set application $.eligibility.childUnder3 = {bulkyMedicalEquipmentTypeCode: 'OTHER', otherMedicalEquipment: ''}
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotNull.application.eligibility.childUnder3.otherMedicalEquipment", location:"#null", locationType:"#null"}

  Scenario: Verify 200 create for person with child under 3 with OTHER equipment and additional description
    * set application $.eligibility.childUnder3 = {bulkyMedicalEquipmentTypeCode: 'OTHER', otherMedicalEquipment: 'entered'}
    Given path 'applications'
    And request application
    When method POST
    Then status 200
    And match $.data contains "#notnull"

  Scenario: Verify invalid create for person with child under 3 without OTHER equipment and with additional description
    * set application $.eligibility.childUnder3 = {bulkyMedicalEquipmentTypeCode: 'OXYADMIN', otherMedicalEquipment: 'entered'}
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotValid.application.eligibility.childUnder3.otherMedicalEquipment", location:"#null", locationType:"#null"}

  Scenario: Verify 200 create for person with child under 3 without OTHER equipment and no additional description
    * set application $.eligibility.childUnder3 = {bulkyMedicalEquipmentTypeCode: 'VENT', otherMedicalEquipment: ''}
    Given path 'applications'
    And request application
    When method POST
    Then status 200
    And match $.data contains "#notnull"

  Scenario: Verify 200 for renewal with empty childUnder3 object
    * set application $.applicationTypeCode = 'RENEW'
    Given path 'applications'
    And request application
    When method POST
    Then status 200
    And match $.data contains "#notnull"

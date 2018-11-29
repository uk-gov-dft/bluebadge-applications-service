@application-create-person-child-under3-ok
Feature: Verify Create person childU3 OXYADMIN deprecated invalid

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-citizen-app.feature')
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify invalid create for person with child under 3 with OXYADMIN equipment and otherEquipmentDesc supplied
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
      postCode: 'zz11 1zz',
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
    childUnder3: {
      bulkyMedicalEquipmentTypeCode: 'OXYADMIN',
      otherMedicalEquipment: 'This should not be here'
    }
  },
  artifacts: []
}
    """

    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"eligibility.childUnder3.otherMedicalEquipment", reason:"Can only be supplied if a bulky equipment type of OTHER is present", message:"NotValid.application.eligibility.childUnder3.otherMedicalEquipment", location:"#null", locationType:"#null"}


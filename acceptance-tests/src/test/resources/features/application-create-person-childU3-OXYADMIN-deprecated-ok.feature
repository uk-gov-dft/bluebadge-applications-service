@application-create-person-child-under3-ok
Feature: Verify Create person childU3 OXYADMIN deprecated ok

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-citizen-app.feature')
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

  Scenario: Verify valid create for person with child under 3 with OXYADMIN equipment
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
    childUnder3: {
      bulkyMedicalEquipmentTypeCode: 'OXYADMIN',
      otherMedicalEquipment: ''
    }
  },
  artifacts: []
}
    """

    Given path 'applications'
    And request application
    When method POST
    Then status 200
    And match $.data contains "#notnull"

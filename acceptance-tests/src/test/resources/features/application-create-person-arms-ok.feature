@application-create-person-arms-ok
Feature: Verify Create person arms ok

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
        typeCode: 'ARMS',
        disabilityArms: {
          drivingFrequency: 'driving frequency',
          isAdaptedVehicle: true,
          adaptedVehicleDescription: 'adapted vehicle description'
        }
      },
      artifacts: [
      ]
    }
    """

  Scenario: Verify arms 400 when arms object missing for NEW application
    * set application $.eligibility = {typeCode: 'ARMS'}
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotNull.application.eligibility.disabilityArms", location:"#null", locationType:"#null"}

  Scenario: Verify arms 200 when arms object missing for RENEW application
    * set application $.eligibility = {typeCode: 'ARMS'}
    * set application $.applicationTypeCode = 'RENEW'
    Given path 'applications'
    And request application
    When method POST
    Then status 200

  Scenario: Verify arms 400 when mandatory fields missing for NEW application
    * set application $.eligibility = {typeCode: 'ARMS', disabilityArms: {}}
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotNull.application.eligibility.disabilityArms.isAdaptedVehicle", location:"#null", locationType:"#null"}

  Scenario: Verify arms 400 when isAdaptedVehicle false, but description present
    * set application $.eligibility = {typeCode: 'ARMS', disabilityArms: {isAdaptedVehicle: false, adaptedVehicleDescription: 'Stuff'}}
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotValid.application.eligibility.disabilityArms.adaptedVehicleDescription", location:"#null", locationType:"#null"}

  Scenario: Verify arms 200 when mandatory fields missing if a renewal
    * set application $.applicationTypeCode = 'RENEW'
    * set application $.eligibility = {typeCode: 'ARMS', disabilityArms: {}}
    Given path 'applications'
    And request application
    When method POST
    Then status 200

  Scenario: Verify arms create ok
    Given path 'applications'
    And request application
    When method POST
    Then status 200
    And match $.data contains "#notnull"
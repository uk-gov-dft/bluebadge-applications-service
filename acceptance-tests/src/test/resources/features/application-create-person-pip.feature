@application-create-person-pip-ok
Feature: Verify Create person pip ok

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-citizen-app.feature')
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
        typeCode: 'PIP',
        benefit: {
        }
      },
      artifacts: [
      ]
    }
    """

  Scenario: Verify pip 400 when benefit object missing for NEW application
    * set application $.eligibility = {typeCode: 'PIP'}
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotNull.application.eligibility.benefit", location:"#null", locationType:"#null"}

  Scenario: Verify pip 200 when benefit object missing for RENEW application
    * set application $.eligibility = {typeCode: 'PIP'}
    * set application $.applicationTypeCode = 'RENEW'
    Given path 'applications'
    And request application
    When method POST
    Then status 200

  Scenario: Verify pip 400 when benefit mandatory fields missing for NEW application
    Given path 'applications'
    And request application
    When method POST
    Then status 400
    And match $.error.errors contains {field:"#notnull", reason:"#notnull", message:"NotNull.application.eligibility.benefit.isIndefinite", location:"#null", locationType:"#null"}

  Scenario: Verify pip 200 when benefit mandatory fields missing if a renewal
    * set application $.applicationTypeCode = 'RENEW'
    Given path 'applications'
    And request application
    When method POST
    Then status 200

  Scenario: Verify pip create ok
    * set application $.eligibility.benefit.isIndefinite = true
    Given path 'applications'
    And request application
    When method POST
    Then status 200
    And match $.data contains "#notnull"

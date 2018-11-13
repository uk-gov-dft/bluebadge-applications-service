package uk.gov.dft.bluebadge.service.applicationmanagement.service.validation;

class FieldKeys {
  private FieldKeys() {}

  static final String KEY_PAYMENT_TAKEN = "paymentTaken";
  static final String KEY_PARTY = "party";
  static final String KEY_PARTY_TYPE = "party.typeCode";
  static final String KEY_LA = "localAuthorityCode";
  static final String KEY_ORGANISATION = "party.organisation";
  static final String KEY_ORG_CHARITY_NO = "party.organisation.charityNumber";
  static final String KEY_PERSON = "party.person";
  static final String KEY_PERSON_DOB = "party.person.dob";
  static final String KEY_ELIGIBILITY = "eligibility";
  static final String KEY_ELI_TYPE = KEY_ELIGIBILITY + ".typeCode";
  static final String KEY_ELI_BENEFIT = KEY_ELIGIBILITY + ".benefit";
  static final String KEY_ELI_BENE_EXPIRY_DT = KEY_ELI_BENEFIT + ".expiryDate";
  static final String KEY_ELI_ARMS = KEY_ELIGIBILITY + ".disabilityArms";
  static final String KEY_ELI_ARMS_VEH_ADAPTION = KEY_ELI_ARMS + ".adaptedVehicleDescription";
  static final String KEY_ELI_ARMS_VEH_IS_ADAPTED = KEY_ELI_ARMS + ".isAdaptedVehicle";
  static final String KEY_ELI_WALKING = KEY_ELIGIBILITY + ".walkingDifficulty";
  static final String KEY_ELI_WALK_TYPES = KEY_ELI_WALKING + ".typeCodes";
  static final String KEY_ELI_WALK_OTHER_DESC = KEY_ELI_WALKING + ".otherDescription";
  static final String KEY_ELI_WALK_SPEED = KEY_ELI_WALKING + ".walkingSpeedCode";
  static final String KEY_ELI_CHILD3 = KEY_ELIGIBILITY + ".childUnder3";
  static final String KEY_ELI_CHILD3_OTHER_DESC = KEY_ELI_CHILD3 + ".otherMedicalEquipment";
  static final String KEY_ELI_HEALTH_PROS = KEY_ELIGIBILITY + ".healthcareProfessionals";
  static final String KEY_ELI_CONDITIONS_DESC = KEY_ELIGIBILITY + ".descriptionOfConditions";
  static final String KEY_ELI_BLIND = KEY_ELIGIBILITY + ".blind";
  static final String KEY_ELI_BLIND_REG_AT_LA = KEY_ELI_BLIND + ".registeredAtLaId";
  static final String KEY_ARTIFACTS = "artifacts";
}

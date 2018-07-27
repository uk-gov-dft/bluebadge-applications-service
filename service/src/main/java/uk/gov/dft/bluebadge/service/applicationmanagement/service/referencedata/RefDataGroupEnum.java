package uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata;

import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.BulkyMedicalEquipmentTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.GenderCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.HowProvidedCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.PartyTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.VehicleTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.WalkingSpeedCodeField;

@SuppressWarnings("SpellCheckingInspection")
public enum RefDataGroupEnum {
  APP_TYPE("APPTYPE", ApplicationTypeCodeField.class),
  ELIGIBILITY("ELIGIBILIT", EligibilityCodeField.class),
  BULKY_EQUIPMENT("BULKEQUIP", BulkyMedicalEquipmentTypeCodeField.class),
  GENDER("GENDER", GenderCodeField.class),
  HOW_PROVIDED("WALKMOB", HowProvidedCodeField.class),
  PARTY_TYPE("PARTY", PartyTypeCodeField.class),
  VEHICLE_TYPE("VEHICLETYP", VehicleTypeCodeField.class),
  WALK_DIFF_TYPE("WALKDIFF", WalkingDifficultyTypeCodeField.class),
  WALK_LENGTH("WALKLEN", WalkingLengthOfTimeCodeField.class),
  WALK_SPEED("WALKSPEED", WalkingSpeedCodeField.class),
  LOCAL_AUTHORITY("LA", null);

  public String getGroupKey() {
    return groupKey;
  }

  public Class<? extends Enum<?>> getEnumClass() {
    return enumClass;
  }

  private final String groupKey;
  private final Class<? extends Enum<?>> enumClass;

  RefDataGroupEnum(String groupKey, Class<? extends Enum<?>> enumClass) {

    this.groupKey = groupKey;
    this.enumClass = enumClass;
  }
}

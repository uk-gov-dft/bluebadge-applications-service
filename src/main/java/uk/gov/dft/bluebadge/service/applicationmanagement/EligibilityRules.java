package uk.gov.dft.bluebadge.service.applicationmanagement;

import static uk.gov.dft.bluebadge.common.util.Matchers.enumValues;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.DLA;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.PIP;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.WPMS;

import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;

public class EligibilityRules {

  private EligibilityRules() {}

  public static boolean requiresChildUnder3Object(EligibilityCodeField eligibilityCodeField) {
    return EligibilityCodeField.CHILDBULK == eligibilityCodeField;
  }

  public static boolean requiresChildUnder3Object(String eligibilityCode) {
    return requiresChildUnder3Object(EligibilityCodeField.fromValue(eligibilityCode));
  }

  public static boolean requiresBlind(EligibilityCodeField eligibilityCodeField) {
    return EligibilityCodeField.BLIND == eligibilityCodeField;
  }

  public static boolean requiresBlind(String eligibilityCode) {
    return requiresBlind(EligibilityCodeField.fromValue(eligibilityCode));
  }

  public static boolean requiresDisabilityArms(EligibilityCodeField eligibilityCodeField) {
    return EligibilityCodeField.ARMS == eligibilityCodeField;
  }

  public static boolean requiresDisabilityArms(String eligibilityCode) {
    return requiresDisabilityArms(EligibilityCodeField.fromValue(eligibilityCode));
  }

  public static boolean requiresWalkingDifficulty(EligibilityCodeField eligibilityCodeField) {
    return EligibilityCodeField.WALKD == eligibilityCodeField;
  }

  public static boolean requiresWalkingDifficulty(String eligibilityCode) {
    return requiresWalkingDifficulty(EligibilityCodeField.fromValue(eligibilityCode));
  }

  public static boolean requiresBenefit(EligibilityCodeField eligibilityCodeField) {
    return enumValues(PIP, DLA, WPMS).contains(eligibilityCodeField);
  }

  public static boolean requiresBenefit(String eligibilityCode) {
    return requiresBenefit(EligibilityCodeField.fromValue(eligibilityCode));
  }
}

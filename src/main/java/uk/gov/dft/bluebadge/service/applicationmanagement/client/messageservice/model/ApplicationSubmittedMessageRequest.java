package uk.gov.dft.bluebadge.service.applicationmanagement.client.messageservice.model;

import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.Artifact.TypeEnum.PHOTO;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.Artifact.TypeEnum.PROOF_ADD;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.Artifact.TypeEnum.PROOF_ELIG;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.Artifact.TypeEnum.PROOF_ID;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.Artifact.TypeEnum.SUPPORT_DOCS;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.AFRFCS;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.ARMS;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.BLIND;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.CHILDBULK;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.CHILDVEHIC;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.DLA;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.PIP;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.WALKD;
import static uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField.WPMS;
import static uk.gov.dft.bluebadge.service.applicationmanagement.EligibilityRules.DISCRETIONARY_ELIGIBILITIES;

import com.google.common.collect.ImmutableMap;
import java.time.LocalDate;
import java.util.Set;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Artifact;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;

@Getter
@EqualsAndHashCode(callSuper = false)
public class ApplicationSubmittedMessageRequest extends GenericMessageRequest {
  /** This is the name of the template within the Message service */
  public static final String TEMPLATE = "APPLICATION_SUBMITTED";

  public static final long YEARS_WHEN_ADULT = 16L;
  private static final String YES = "yes";
  private static final String NO = "no";

  private final String fullName;
  private final String localAuthority;
  private final EligibilityCodeField eligibilityCodeField;
  private final Set<Artifact.TypeEnum> providedArtifacts;
  private final LocalDate dob;
  private final String eligibilityCriteria;
  private final Boolean paymentTaken;

  @Builder
  private ApplicationSubmittedMessageRequest(
      @NonNull String emailAddress,
      @NonNull String laShortCode,
      @NonNull String fullName,
      @NonNull String localAuthority,
      @NonNull EligibilityCodeField eligibility,
      @NonNull Set<Artifact.TypeEnum> providedArtifacts,
      @NonNull LocalDate dob,
      @NonNull String eligibilityCriteria,
      @NonNull Boolean paymentTaken) {
    super(
        TEMPLATE,
        emailAddress,
        laShortCode,
        attributeBuilder(eligibility, providedArtifacts, dob)
            .put("local authority", localAuthority)
            .put("full name", fullName)
            .put("eligibility criteria", eligibilityCriteria)
            .put("payment not taken", paymentTaken ? NO : YES)
            .build());
    this.fullName = fullName;
    this.localAuthority = localAuthority;
    this.eligibilityCodeField = eligibility;
    this.providedArtifacts = providedArtifacts;
    this.dob = dob;
    this.eligibilityCriteria = eligibilityCriteria;
    this.paymentTaken = paymentTaken;
  }

  private static ImmutableMap.Builder<String, String> attributeBuilder(
      EligibilityCodeField eligibility, Set<Artifact.TypeEnum> providedArtifacts, LocalDate dob) {
    ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
    builder
        .put("eligibility is dla", eligibility == DLA ? YES : NO)
        .put("eligibility is pip", eligibility == PIP ? YES : NO)
        .put("eligibility is blind", eligibility == BLIND ? YES : NO)
        .put("eligibility is afrfcs", eligibility == AFRFCS ? YES : NO)
        .put("eligibility is wpms", eligibility == WPMS ? YES : NO)
        .put("eligibility is walkd", eligibility == WALKD ? YES : NO)
        .put("eligibility is arms", eligibility == ARMS ? YES : NO)
        .put("eligibility is childbulk", eligibility == CHILDBULK ? YES : NO)
        .put("eligibility is childvehic", eligibility == CHILDVEHIC ? YES : NO);

    boolean photoNotProvided = !providedArtifacts.contains(PHOTO);
    boolean idNotProvided = !providedArtifacts.contains(PROOF_ID);
    boolean pipNotProvided = eligibility == PIP && !providedArtifacts.contains(PROOF_ELIG);
    boolean dlaNotProvided = eligibility == DLA && !providedArtifacts.contains(PROOF_ELIG);
    builder
        .put("badge photo not provided", photoNotProvided ? YES : NO)
        .put("proof of identity not provided", idNotProvided ? YES : NO)
        .put("proof of PIP not provided", pipNotProvided ? YES : NO)
        .put("proof of DLA not provided", dlaNotProvided ? YES : NO);

    boolean isAdult = LocalDate.now().minusYears(YEARS_WHEN_ADULT).isAfter(dob);
    boolean addressNotProvided = isAdult && !providedArtifacts.contains(PROOF_ADD);
    builder.put("proof of address not provided", addressNotProvided ? YES : NO);

    boolean supportDocsNotProvided =
        DISCRETIONARY_ELIGIBILITIES.contains(eligibility)
            && !providedArtifacts.contains(SUPPORT_DOCS);
    builder.put("supporting documents not provided", supportDocsNotProvided ? YES : NO);

    builder.put(
        "at least one upload not provided",
        photoNotProvided
                || idNotProvided
                || pipNotProvided
                || dlaNotProvided
                || addressNotProvided
                || supportDocsNotProvided
            ? YES
            : NO);

    return builder;
  }
}

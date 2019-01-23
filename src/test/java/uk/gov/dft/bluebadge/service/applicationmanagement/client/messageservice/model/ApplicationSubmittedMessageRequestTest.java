package uk.gov.dft.bluebadge.service.applicationmanagement.client.messageservice.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Artifact;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;

class ApplicationSubmittedMessageRequestTest {

  private ApplicationSubmittedMessageRequest.ApplicationSubmittedMessageRequestBuilder
      fullRequestBuilder;

  @BeforeEach
  void setup() {
    fullRequestBuilder =
        ApplicationSubmittedMessageRequest.builder()
            .laShortCode("BIRM")
            .fullName("bob jones")
            .dob(LocalDate.now().minusYears(25L))
            .eligibility(EligibilityCodeField.DLA)
            .eligibilityCriteria("Some description")
            .emailAddress("bob.jones@gmail.com")
            .localAuthority("Name of an LA")
            .paymentTaken(true)
            .providedArtifacts(EnumSet.allOf(Artifact.TypeEnum.class));
  }

  @Test
  void allFields() {
    GenericMessageRequest messageRequest = fullRequestBuilder.build();
    assertThat(messageRequest.getEmailAddress()).isEqualTo("bob.jones@gmail.com");
    assertThat(messageRequest.getTemplate()).isEqualTo("APPLICATION_SUBMITTED");

    Map<String, ?> attributes = messageRequest.getAttributes();
    assertThat(attributes.get("local authority")).isEqualTo("Name of an LA");
    assertThat(attributes.get("full name")).isEqualTo("bob jones");
    assertThat(attributes.get("eligibility criteria")).isEqualTo("Some description");
    assertThat(attributes.get("eligibility is dla")).isEqualTo("yes");
    assertThat(attributes.get("eligibility is pip")).isEqualTo("no");
    assertThat(attributes.get("eligibility is blind")).isEqualTo("no");
    assertThat(attributes.get("eligibility is afrfcs")).isEqualTo("no");
    assertThat(attributes.get("eligibility is wpms")).isEqualTo("no");
    assertThat(attributes.get("eligibility is walkd")).isEqualTo("no");
    assertThat(attributes.get("eligibility is arms")).isEqualTo("no");
    assertThat(attributes.get("eligibility is childbulk")).isEqualTo("no");
    assertThat(attributes.get("eligibility is childvehic")).isEqualTo("no");
    assertThat(attributes.get("at least one upload not provided")).isEqualTo("no");
    assertThat(attributes.get("badge photo not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of address not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of identity not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of PIP not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of DLA not provided")).isEqualTo("no");
    assertThat(attributes.get("supporting documents not provided")).isEqualTo("no");
    assertThat(attributes.get("payment not taken")).isEqualTo("no");
  }

  @ParameterizedTest
  @EnumSource(EligibilityCodeField.class)
  @DisplayName("Correct attribute eligibility message set")
  void eligibility(EligibilityCodeField eligibility) {
    ApplicationSubmittedMessageRequest messageRequest =
        fullRequestBuilder.eligibility(eligibility).build();
    assertThat(
            messageRequest
                .getAttributes()
                .get("eligibility is " + eligibility.name().toLowerCase()))
        .isEqualTo("yes");

    EnumSet<EligibilityCodeField> allOthers = EnumSet.complementOf(EnumSet.of(eligibility));
    for (EligibilityCodeField notTheElig : allOthers) {
      assertThat(
              messageRequest
                  .getAttributes()
                  .get("eligibility is " + notTheElig.name().toLowerCase()))
          .isEqualTo("no");
    }
  }

  @Test
  void photo() {
    Set<Artifact.TypeEnum> artifactTypes =
        EnumSet.complementOf(EnumSet.of(Artifact.TypeEnum.PHOTO));
    ApplicationSubmittedMessageRequest messageRequest =
        fullRequestBuilder.providedArtifacts(artifactTypes).build();
    Map<String, ?> attributes = messageRequest.getAttributes();
    assertThat(attributes.get("badge photo not provided")).isEqualTo("yes");
    assertThat(attributes.get("at least one upload not provided")).isEqualTo("yes");

    assertThat(attributes.get("proof of address not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of identity not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of PIP not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of DLA not provided")).isEqualTo("no");
    assertThat(attributes.get("supporting documents not provided")).isEqualTo("no");
  }

  @Test
  void proofOfPIP() {
    Set<Artifact.TypeEnum> artifactTypes =
        EnumSet.complementOf(EnumSet.of(Artifact.TypeEnum.PROOF_ELIG));
    ApplicationSubmittedMessageRequest messageRequest =
        fullRequestBuilder
            .eligibility(EligibilityCodeField.PIP)
            .providedArtifacts(artifactTypes)
            .build();
    Map<String, ?> attributes = messageRequest.getAttributes();
    assertThat(attributes.get("proof of PIP not provided")).isEqualTo("yes");
    assertThat(attributes.get("at least one upload not provided")).isEqualTo("yes");

    assertThat(attributes.get("badge photo not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of address not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of identity not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of DLA not provided")).isEqualTo("no");
    assertThat(attributes.get("supporting documents not provided")).isEqualTo("no");
  }

  @Test
  void proofOfDLA() {
    Set<Artifact.TypeEnum> artifactTypes =
        EnumSet.complementOf(EnumSet.of(Artifact.TypeEnum.PROOF_ELIG));
    ApplicationSubmittedMessageRequest messageRequest =
        fullRequestBuilder
            .eligibility(EligibilityCodeField.DLA)
            .providedArtifacts(artifactTypes)
            .build();
    Map<String, ?> attributes = messageRequest.getAttributes();
    assertThat(attributes.get("proof of DLA not provided")).isEqualTo("yes");
    assertThat(attributes.get("at least one upload not provided")).isEqualTo("yes");

    assertThat(attributes.get("badge photo not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of address not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of identity not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of PIP not provided")).isEqualTo("no");
    assertThat(attributes.get("supporting documents not provided")).isEqualTo("no");
  }

  @Test
  void proofOfID() {
    Set<Artifact.TypeEnum> artifactTypes =
        EnumSet.complementOf(EnumSet.of(Artifact.TypeEnum.PROOF_ID));
    ApplicationSubmittedMessageRequest messageRequest =
        fullRequestBuilder.providedArtifacts(artifactTypes).build();
    Map<String, ?> attributes = messageRequest.getAttributes();
    assertThat(attributes.get("proof of identity not provided")).isEqualTo("yes");
    assertThat(attributes.get("at least one upload not provided")).isEqualTo("yes");

    assertThat(attributes.get("badge photo not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of address not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of PIP not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of DLA not provided")).isEqualTo("no");
    assertThat(attributes.get("supporting documents not provided")).isEqualTo("no");
  }

  @Test
  void proofOfAddress() {
    Set<Artifact.TypeEnum> artifactTypes =
        EnumSet.complementOf(EnumSet.of(Artifact.TypeEnum.PROOF_ADD));
    ApplicationSubmittedMessageRequest messageRequest =
        fullRequestBuilder.providedArtifacts(artifactTypes).build();
    Map<String, ?> attributes = messageRequest.getAttributes();
    assertThat(attributes.get("proof of address not provided")).isEqualTo("yes");
    assertThat(attributes.get("at least one upload not provided")).isEqualTo("yes");

    assertThat(attributes.get("proof of identity not provided")).isEqualTo("no");
    assertThat(attributes.get("badge photo not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of PIP not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of DLA not provided")).isEqualTo("no");
    assertThat(attributes.get("supporting documents not provided")).isEqualTo("no");
  }

  @Test
  void proofOfAddress_child() {
    Set<Artifact.TypeEnum> artifactTypes =
        EnumSet.complementOf(EnumSet.of(Artifact.TypeEnum.PROOF_ADD));
    ApplicationSubmittedMessageRequest messageRequest =
        fullRequestBuilder.dob(LocalDate.now()).providedArtifacts(artifactTypes).build();
    Map<String, ?> attributes = messageRequest.getAttributes();
    assertThat(attributes.get("proof of address not provided")).isEqualTo("no");
    assertThat(attributes.get("at least one upload not provided")).isEqualTo("no");

    assertThat(attributes.get("proof of identity not provided")).isEqualTo("no");
    assertThat(attributes.get("badge photo not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of PIP not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of DLA not provided")).isEqualTo("no");
    assertThat(attributes.get("supporting documents not provided")).isEqualTo("no");
  }

  @ParameterizedTest
  @ValueSource(strings = {"PIP", "DLA", "AFRFCS", "WPMS", "BLIND"})
  void supportingDocs(String eligibility) {
    Set<Artifact.TypeEnum> artifactTypes =
        EnumSet.complementOf(EnumSet.of(Artifact.TypeEnum.SUPPORT_DOCS));
    ApplicationSubmittedMessageRequest messageRequest =
        fullRequestBuilder
            .eligibility(EligibilityCodeField.fromValue(eligibility))
            .providedArtifacts(artifactTypes)
            .build();
    Map<String, ?> attributes = messageRequest.getAttributes();
    assertThat(attributes.get("supporting documents not provided")).isEqualTo("no");
    assertThat(attributes.get("at least one upload not provided")).isEqualTo("no");

    assertThat(attributes.get("proof of identity not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of address not provided")).isEqualTo("no");
    assertThat(attributes.get("badge photo not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of PIP not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of DLA not provided")).isEqualTo("no");
  }

  @ParameterizedTest
  @ValueSource(strings = {"WALKD", "ARMS", "CHILDVEHIC", "CHILDBULK"})
  void supportingDocs_discretionaryEligibilities(String eligibility) {
    Set<Artifact.TypeEnum> artifactTypes =
        EnumSet.complementOf(EnumSet.of(Artifact.TypeEnum.SUPPORT_DOCS));
    ApplicationSubmittedMessageRequest messageRequest =
        fullRequestBuilder
            .eligibility(EligibilityCodeField.fromValue(eligibility))
            .providedArtifacts(artifactTypes)
            .build();
    Map<String, ?> attributes = messageRequest.getAttributes();
    assertThat(attributes.get("supporting documents not provided")).isEqualTo("yes");
    assertThat(attributes.get("at least one upload not provided")).isEqualTo("yes");

    assertThat(attributes.get("proof of identity not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of address not provided")).isEqualTo("no");
    assertThat(attributes.get("badge photo not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of PIP not provided")).isEqualTo("no");
    assertThat(attributes.get("proof of DLA not provided")).isEqualTo("no");
  }

  @Test
  void paymentTaken() {
    ApplicationSubmittedMessageRequest messageRequest =
        fullRequestBuilder.paymentTaken(false).build();
    Map<String, ?> attributes = messageRequest.getAttributes();
    assertThat(attributes.get("payment not taken")).isEqualTo("yes");
  }
}

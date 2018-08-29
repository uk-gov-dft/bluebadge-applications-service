package uk.gov.dft.bluebadge.service.applicationmanagement.converter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationSummary;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.EligibilityCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.PartyTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationSummaryEntity;

public class ApplicationSummaryConverterTest {

  @Test
  public void convertToModel() {
    ApplicationSummaryConverter converter = new ApplicationSummaryConverter();
    UUID uuid = UUID.randomUUID();
    Instant submissionDate = Instant.now();

    ApplicationSummaryEntity entity =
        ApplicationSummaryEntity.builder()
            .applicationId(uuid)
            .applicationTypeCode("NEW")
            .eligibilityCode("PIP")
            .holderName("Holder")
            .nino("NINO")
            .partyTypeCode("PERSON")
            .postcode("Postcode")
            .submissionDate(submissionDate)
            .build();
    List<ApplicationSummaryEntity> entities = new ArrayList<>();
    entities.add(entity);

    List<ApplicationSummary> modelList = converter.convertToModelList(entities);
    ApplicationSummary model = modelList.get(0);
    Assert.assertEquals(1, modelList.size());
    Assert.assertEquals("Holder", model.getName());
    Assert.assertEquals("NINO", model.getNino());
    Assert.assertEquals(PartyTypeCodeField.PERSON, model.getPartyTypeCode());
    Assert.assertEquals(ApplicationTypeCodeField.NEW, model.getApplicationTypeCode());
    Assert.assertEquals(EligibilityCodeField.PIP, model.getEligibilityCode());
    Assert.assertEquals(uuid.toString(), model.getApplicationId());
    Assert.assertEquals(submissionDate.toString(), model.getSubmissionDate().toString());
  }
}

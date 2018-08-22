package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationSummary;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.ApplicationRepository;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationSummaryEntity;

public class ApplicationServiceTest extends ApplicationFixture {

  @Mock private ApplicationRepository repository;
  @Mock private ApplicationConverter converter;
  @Mock SecurityUtils securityUtils;
  private ApplicationService service;

  @Before
  public void setUp() {
    service = new ApplicationService(repository, converter, securityUtils);
  }

  @Test
  public void createApplication() {
    Application application =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityBlind().build();
    ApplicationEntity entity = ApplicationEntity.builder().build();
    application.setSubmissionDate(null);
    application.setApplicationId(null);

    entity.setId(UUID.randomUUID());
    when(converter.convertToEntity(application)).thenReturn(entity);

    UUID result = service.createApplication(application);

    verify(converter, times(1)).convertToEntity(application);
    verify(repository, times(1)).createApplication(entity);
    verify(repository, times(1)).createWalkingDifficultyTypes(any());
    Assert.assertNotNull("Should get id back", result);

    Assert.assertNotNull("Submission date set as part of create", application.getSubmissionDate());
    Assert.assertNotNull("Id set as part of create", application.getApplicationId());
  }

  @Test(expected = BadRequestException.class)
  public void find_nullAuthorityCode() {
    // Given user with no local authority.
    when(securityUtils.getCurrentLocalAuthorityShortCode()).thenReturn(null);

    // When find.
    service.find(null, null, null, null, null);

    // Then Bad Request thrown.
  }

  @Test
  public void find_withNullsValid() {
    // Given a search for applications with valid LA and no other criteria.
    when(securityUtils.getCurrentLocalAuthorityShortCode()).thenReturn("ABERD");
    when(repository.findApplications(any())).thenReturn(new ArrayList<ApplicationSummaryEntity>());

    // When searching
    service.find(null, null, null, null, null);

    // No exceptions
  }

  @Test
  public void find_validAndResults() {
    // Given a search for applications with all criteria specified
    when(securityUtils.getCurrentLocalAuthorityShortCode()).thenReturn("ABERD");
    List<ApplicationSummaryEntity> entities = new ArrayList<>();
    entities.add(ApplicationSummaryEntity.builder().applicationId(UUID.randomUUID()).build());
    when(repository.findApplications(any())).thenReturn(entities);

    // When searching
    List<ApplicationSummary> results =
        service.find("name", "postcode", OffsetDateTime.now(), OffsetDateTime.now(), "NEW");

    // Then valid converted model object returned.
    Assert.assertEquals(1, results.size());
    Assert.assertNotNull(results.get(0).getApplicationId());
  }
}

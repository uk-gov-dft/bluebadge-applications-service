package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.github.pagehelper.Page;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.common.api.model.PagedResult;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationStatusField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationSummary;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationTypeCodeField;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationUpdate;
import uk.gov.dft.bluebadge.service.applicationmanagement.ApplicationFixture;
import uk.gov.dft.bluebadge.service.applicationmanagement.controller.PagingParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.ApplicationRepository;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationSummaryEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.FindApplicationQueryParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.RetrieveApplicationQueryParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.audit.ApplicationAuditLogger;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.ReferenceDataService;

public class ApplicationServiceTest extends ApplicationFixture {

  @Mock private ApplicationRepository repository;
  @Mock private ApplicationConverter converter;
  @Mock SecurityUtils securityUtils;
  @Mock ApplicationAuditLogger applicationAuditLogger;
  @Mock ArtifactService artifactService;
  @Mock MessageService messageService;
  @Mock ReferenceDataService referenceDataService;
  private ApplicationService service;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    service =
        new ApplicationService(
            repository,
            converter,
            securityUtils,
            applicationAuditLogger,
            artifactService,
            messageService,
                referenceDataService);
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
    verify(repository, times(1)).createBulkyEquipment(any());
    assertNotNull("Should get id back", result);

    assertNotNull("Submission date set as part of create", application.getSubmissionDate());
    assertNotNull("Id set as part of create", application.getApplicationId());
    assertNotNull("applicationStatus should be set to TODO", application.getApplicationStatus());

    verify(messageService).sendApplicationSubmittedMessage(application);
  }

  @Test
  public void createApplication_whenNoEmailAddress_thenNoMessageSent() {
    Application application =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityBlind().build();
    ApplicationEntity entity = ApplicationEntity.builder().build();
    application.setSubmissionDate(null);
    application.setApplicationId(null);
    application.getParty().getContact().setEmailAddress(null);

    entity.setId(UUID.randomUUID());
    when(converter.convertToEntity(application)).thenReturn(entity);

    UUID result = service.createApplication(application);

    verify(converter, times(1)).convertToEntity(application);
    verify(repository, times(1)).createApplication(entity);
    verify(repository, times(1)).createWalkingDifficultyTypes(any());
    verify(repository, times(1)).createBulkyEquipment(any());
    assertNotNull("Should get id back", result);

    assertNotNull("Submission date set as part of create", application.getSubmissionDate());
    assertNotNull("Id set as part of create", application.getApplicationId());

    verifyZeroInteractions(messageService);
  }

  @Test(expected = BadRequestException.class)
  public void findApplication_givenNullAuthorityCode() {
    // Given user with no local authority.
    when(securityUtils.getCurrentLocalAuthorityShortCode()).thenReturn(null);

    // When find.
    service.find(FindApplicationQueryParams.builder().build(), new PagingParams());

    // Then Bad Request thrown.
  }

  @Test(expected = BadRequestException.class)
  public void findApplication_givenInvalidApplicationTypeCode() {
    // Given user with no local authority.
    when(securityUtils.getCurrentLocalAuthorityShortCode()).thenReturn("ABERD");

    // When find.
    FindApplicationQueryParams queryParams =
        FindApplicationQueryParams.builder()
            .applicationTypeCode("INVALID_APPLICATION_TYPE_CODE")
            .build();
    PagingParams pagingParams = new PagingParams();
    service.find(queryParams, pagingParams);

    // Then Bad Request thrown.
  }

  @Test
  public void findApplication_givenWNullsValid() {
    // Given a search for applications with valid LA and no other criteria.
    when(securityUtils.getCurrentLocalAuthorityShortCode()).thenReturn("ABERD");
    when(repository.findApplications(any(), any(), any())).thenReturn(new Page<>());

    // When searching
    service.find(FindApplicationQueryParams.builder().build(), new PagingParams());
    // No exceptions

    verify(repository).findApplications(any(), eq(1), eq(50));
  }

  @Test
  public void findApplication_givenValidRequestAndResults() {
    // Given a search for applications with all criteria specified
    when(securityUtils.getCurrentLocalAuthorityShortCode()).thenReturn("ABERD");
    Page<ApplicationSummaryEntity> entities = new Page<>();
    entities.add(ApplicationSummaryEntity.builder().applicationId(UUID.randomUUID()).build());
    when(repository.findApplications(any(), any(), any())).thenReturn(entities);

    // When searching
    OffsetDateTime from = OffsetDateTime.now();
    OffsetDateTime to = OffsetDateTime.now();
    FindApplicationQueryParams queryParams =
        FindApplicationQueryParams.builder()
            .name("name")
            .postcode("postcode")
            .from(from.toInstant())
            .to(to.toInstant())
            .applicationTypeCode("NEW")
            .build();
    PagingParams pagingParams = new PagingParams();
    PagedResult<ApplicationSummary> results = service.find(queryParams, pagingParams);

    // Then valid converted model object returned.
    assertEquals(1, results.getData().size());
    assertNotNull(results.getData().get(0).getApplicationId());

    ArgumentCaptor<FindApplicationQueryParams> captor =
        ArgumentCaptor.forClass(FindApplicationQueryParams.class);
    verify(repository).findApplications(captor.capture(), eq(1), eq(50));

    assertThat(captor).isNotNull();
    assertThat(captor.getValue()).isNotNull();
    assertThat(captor.getValue().getName()).isEqualTo("name");
    assertThat(captor.getValue().getPostcode()).isEqualTo("postcode");
    assertThat(captor.getValue().getSubmissionFrom()).isEqualTo(from.toInstant());
    assertThat(captor.getValue().getSubmissionTo()).isEqualTo(to.toInstant());
    assertThat(captor.getValue().getAuthorityCode()).isEqualTo("ABERD");
    assertThat(captor.getValue().getApplicationTypeCode()).isEqualTo(ApplicationTypeCodeField.NEW);
  }

  @Test
  public void retrieveApplication() {
    String uuid = UUID.randomUUID().toString();
    ApplicationEntity entity = getFullyPopulatedApplicationEntity();
    Application model =
        getApplicationBuilder().addBaseApplication().setPerson().setEligibilityWpms().build();

    when(repository.retrieveApplication(any())).thenReturn(entity);
    when(converter.convertToModel(entity)).thenReturn(model);

    Application a = service.retrieve(uuid);

    assertEquals(model, a);

    verify(artifactService, times(1)).createAccessibleLinks(entity.getArtifacts());
  }

  @Test(expected = BadRequestException.class)
  public void retrieveApplication_givenInvalidUuid() {
    service.retrieve("ABC");
  }

  @Test(expected = NotFoundException.class)
  public void retrieveApplication_givenNoResult() {
    when(repository.retrieveApplication(any())).thenReturn(null);
    service.retrieve(UUID.randomUUID().toString());
  }

  @Test
  public void deleteApplication_givenValidRequest() {
    UUID uuid = UUID.randomUUID();
    String uuidStr = uuid.toString();

    service.delete(uuidStr);

    ArgumentCaptor<RetrieveApplicationQueryParams> captor =
        ArgumentCaptor.forClass(RetrieveApplicationQueryParams.class);
    verify(repository, times(1)).deleteApplication(captor.capture());
    assertThat(captor).isNotNull();
    assertThat(captor.getValue()).isNotNull();
    assertThat(captor.getValue().getUuid()).isEqualTo(uuid);

    verify(repository, times(1)).deleteHealthcareProfessionals(uuidStr);
    verify(repository, times(1)).deleteMedications(uuidStr);
    verify(repository, times(1)).deleteTreatments(uuidStr);
    verify(repository, times(1)).deleteVehicles(uuidStr);
    verify(repository, times(1)).deleteWalkingAids(uuidStr);
    verify(repository, times(1)).deleteWalkingDifficultyTypes(uuidStr);
    verify(repository, times(1)).deleteArtifacts(uuidStr);
  }

  @Test(expected = BadRequestException.class)
  public void updateApplication_givenBadApplicationUuid() {
    ApplicationUpdate applicationUpdate =
        ApplicationFixture.ApplicationUpdateBuilder.anApplicationUpdate()
            .withApplicationStatus(ApplicationStatusField.COMPLETED)
            .build();

    service.update("rubbbish", applicationUpdate);
  }

  @Test(expected = BadRequestException.class)
  public void updateApplication_givenNullApplicationStatus() {

    // given
    ApplicationUpdate applicationUpdate =
        ApplicationFixture.ApplicationUpdateBuilder.anApplicationUpdate()
            .withApplicationStatus(null)
            .build();

    // when
    service.update(UUID.randomUUID().toString(), applicationUpdate);
  }

  @Test
  public void updateApplication_givenValidRequest() {

    // given
    ApplicationUpdate applicationUpdate =
        ApplicationFixture.ApplicationUpdateBuilder.anApplicationUpdate()
            .withApplicationStatus(ApplicationStatusField.COMPLETED)
            .build();

    UUID applicationUuid = UUID.randomUUID();
    when(repository.updateApplication(any())).thenReturn(1);

    // when
    service.update(applicationUuid.toString(), applicationUpdate);

    // then
    ArgumentCaptor<ApplicationUpdate> captor = ArgumentCaptor.forClass(ApplicationUpdate.class);
    verify(repository, times(1)).updateApplication(captor.capture());
    assertThat(captor).isNotNull();
    assertThat(captor.getValue()).isNotNull();
    assertThat(captor.getValue().getApplicationId()).isEqualTo(applicationUuid);
    assertThat(captor.getValue().getApplicationStatusField())
        .isEqualTo(ApplicationStatusField.COMPLETED);
  }

  @Test(expected = BadRequestException.class)
  public void updateApplication_givenNonexistentApplicationId() {

    // given
    ApplicationUpdate applicationUpdate =
        ApplicationFixture.ApplicationUpdateBuilder.anApplicationUpdate()
            .withApplicationStatus(ApplicationStatusField.COMPLETED)
            .build();

    UUID applicationUuid = UUID.randomUUID();
    when(repository.updateApplication(any())).thenReturn(0);

    // when
    service.update(applicationUuid.toString(), applicationUpdate);
  }
}

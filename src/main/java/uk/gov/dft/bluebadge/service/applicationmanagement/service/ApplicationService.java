package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.api.model.PagedResult;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationSummary;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationTransferRequest;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.ApplicationUpdate;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Artifact;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Party;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.PartyTypeCodeField;
import uk.gov.dft.bluebadge.service.applicationmanagement.controller.PagingParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationSummaryConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.ApplicationRepository;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ArtifactEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.FindApplicationQueryParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.RetrieveApplicationQueryParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.TransferApplicationParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.audit.ApplicationAuditLogger;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.referencedata.ReferenceDataService;

@Slf4j
@Service
@Transactional
public class ApplicationService {

  private final ApplicationRepository repository;
  private final ApplicationConverter converter;
  private final SecurityUtils securityUtils;
  private ApplicationAuditLogger applicationAuditLogger;
  private final ArtifactService artifactService;
  private final MessageService messageService;
  private ReferenceDataService referenceDataService;

  @Autowired
  ApplicationService(
      ApplicationRepository repository,
      ApplicationConverter converter,
      SecurityUtils securityUtils,
      ApplicationAuditLogger applicationAuditLogger,
      ArtifactService artifactService,
      MessageService messageService,
      ReferenceDataService referenceDataService) {
    this.repository = repository;
    this.converter = converter;
    this.securityUtils = securityUtils;
    this.applicationAuditLogger = applicationAuditLogger;
    this.artifactService = artifactService;
    this.messageService = messageService;
    this.referenceDataService = referenceDataService;
  }

  /**
   * Creates an Application given a VALIDATED Application.
   *
   * @param applicationModel A validated application.
   * @return A UUID for the newly created Application.
   */
  public UUID createApplication(Application applicationModel) {

    addUuid(applicationModel);

    // For create set submission date to now
    applicationModel.setSubmissionDate(OffsetDateTime.now(Clock.systemUTC()));
    log.debug("Submission date set to:{}", applicationModel.getSubmissionDate());

    ApplicationEntity application = converter.convertToEntity(applicationModel);

    int insertCount;
    log.info("Creating application: {}", application.getId());
    insertCount = repository.createApplication(application);
    log.debug("{} applications created", insertCount);

    List<ArtifactEntity> artifactEntities =
        artifactService.saveArtifacts(applicationModel.getArtifacts(), application.getId());
    try {
      repository.createHealthcareProfessionals(application.getHealthcareProfessionals());
      repository.createMedications(application.getMedications());
      repository.createTreatments(application.getTreatments());
      repository.createVehicles(application.getVehicles());
      repository.createWalkingAids(application.getWalkingAids());
      repository.createWalkingDifficultyTypes(application.getWalkingDifficultyTypes());
      repository.createBreathlessnessTypes(application.getBreathlessnessTypes());
      repository.createBulkyEquipment(application.getBulkyEquipment());
      repository.createArtifacts(artifactEntities);
      applicationAuditLogger.logCreateAuditEvent(applicationModel, log);
    } catch (Exception e) {
      // Remove any S3 objects created before transaction rolled back.
      artifactService.backOutArtifacts(artifactEntities);
      throw e;
    }

    Party party = applicationModel.getParty();
    if (party.getTypeCode() == PartyTypeCodeField.PERSON
        && StringUtils.isNotBlank(party.getContact().getEmailAddress())) {
      messageService.sendApplicationSubmittedMessage(applicationModel);
    }

    return application.getId();
  }

  private void addUuid(Application applicationModel) {
    // Set a UUID if it doesn't have one
    if (null == applicationModel.getApplicationId()) {
      applicationModel.setApplicationId(UUID.randomUUID().toString());
    }
  }

  public PagedResult<ApplicationSummary> find(
      FindApplicationQueryParams searchParams, PagingParams pagingParams) {

    String userAuthorityCode = securityUtils.getCurrentLocalAuthorityShortCode();

    if (null == userAuthorityCode) {
      Error error = new Error();
      error.setMessage("Expected logged in user to have an LA code for find application.");
      error.setReason("localAuthorityShortCode");
      throw new BadRequestException(error);
    }
    searchParams.setAuthorityCode(userAuthorityCode);

    return new ApplicationSummaryConverter()
        .convertToModelList(
            repository.findApplications(
                searchParams, pagingParams.getPageNum(), pagingParams.getPageSize()));
  }

  public Application retrieve(String applicationId) {
    UUID uuid = getUuid(applicationId);

    ApplicationEntity entity =
        repository.retrieveApplication(
            RetrieveApplicationQueryParams.builder().uuid(uuid).deleted(Boolean.FALSE).build());
    if (null == entity) {
      throw new NotFoundException("application", NotFoundException.Operation.RETRIEVE);
    }
    Application application = converter.convertToModel(entity);
    List<Artifact> artifacts = artifactService.createAccessibleLinks(entity.getArtifacts());
    application.setArtifacts(artifacts);
    return application;
  }

  public void delete(String applicationId) {
    log.debug("Deleting application: '{}'", applicationId);
    UUID uuid = getUuid(applicationId);

    RetrieveApplicationQueryParams params =
        RetrieveApplicationQueryParams.builder().uuid(uuid).deleted(Boolean.FALSE).build();

    artifactService.backOutArtifacts(repository.retrieveArtifacts(applicationId));

    repository.deleteApplication(params);
    repository.deleteHealthcareProfessionals(applicationId);
    repository.deleteMedications(applicationId);
    repository.deleteTreatments(applicationId);
    repository.deleteVehicles(applicationId);
    repository.deleteWalkingAids(applicationId);
    repository.deleteWalkingDifficultyTypes(applicationId);
    repository.deleteBreathlessnessTypes(applicationId);
    repository.deleteBulkyEquipmentTypes(applicationId);
    repository.deleteArtifacts(applicationId);
    log.debug("Application: '{}' has been deleted", applicationId);
  }

  public void update(String applicationId, ApplicationUpdate applicationUpdate) {
    Assert.notNull(applicationId, "applicationId must not be null");
    Assert.notNull(applicationUpdate, "applicationUpdate must not be null");

    applicationUpdate.setApplicationId(getUuid(applicationId));
    if (repository.updateApplication(applicationUpdate) == 0) {
      Error error = new Error();
      error.setReason("Nonexistent applicationId specified");
      throw new BadRequestException(error);
    }
  }

  private UUID getUuid(String applicationId) {
    UUID uuid;
    try {
      uuid = UUID.fromString(applicationId);
    } catch (IllegalArgumentException e) {
      Error error = new Error();
      error.setReason("Invalid uuid in query param applicationId");
      throw new BadRequestException(error);
    }

    return uuid;
  }

  public void transferApplication(
      String applicationId, ApplicationTransferRequest applicationTransfer) {
    Assert.notNull(applicationId, "applicationId must not be null");
    Assert.notNull(applicationTransfer, "applicationTransfer must not be null");

    String currentLaShortCode = securityUtils.getCurrentLocalAuthorityShortCode();
    String newLaShortCode = applicationTransfer.getTransferToLaShortCode();

    if (newLaShortCode.equals(currentLaShortCode)) {
      Error error =
          new Error()
              .message("Target local authority code has to differ from current one.")
              .reason("transferToLaShortCode");
      throw new BadRequestException(error);
    }

    validateLocalAuthority(newLaShortCode);

    int updates =
        repository.transferApplication(
            TransferApplicationParams.builder()
                .applicationId(UUID.fromString(applicationId))
                .transferToLaShortCode(applicationTransfer.getTransferToLaShortCode())
                .transferFromLaShortCode(currentLaShortCode)
                .build());
    if (updates == 0) {
      // UUID did not match any application
      throw new NotFoundException("Application", NotFoundException.Operation.UPDATE);
    }
  }

  void validateLocalAuthority(String shortCode) {
    if (null != StringUtils.stripToNull(shortCode)
        && !referenceDataService.isAuthorityCodeValid(shortCode)) {
      throw new BadRequestException(
          "transferToLaShortCode", "NotEmpty", "Invalid local authority code.");
    }
  }
}

package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.ApplicationRepository;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

@Slf4j
@Service
@Transactional
public class ApplicationService {

  private final ApplicationRepository repository;
  private final ApplicationConverter converter;

  @Autowired
  ApplicationService(ApplicationRepository repository, ApplicationConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * Creates an Application given a VALIDATED Application.
   *
   * @param applicationModel A validated application.
   * @return A UUID for the newly created Application.
   */
  // TODO: Needs to validate the incoming Application here (at the service)
  public UUID createApplication(Application applicationModel) {

    addUuid(applicationModel);

    ApplicationEntity application = converter.convertToEntity(applicationModel);

    int insertCount;
    log.info("Creating application: {}", application.getId());
    insertCount = repository.createApplication(application);
    log.debug("{} applications created", insertCount);

    repository.createHealthcareProfessionals(application.getHealthcareProfessionals());
    repository.createMedications(application.getMedications());
    repository.createTreatments(application.getTreatments());
    repository.createVehicles(application.getVehicles());
    repository.createWalkingAids(application.getWalkingAids());
    repository.createWalkingDifficultyTypes(application.getWalkingDifficultyTypes());
    return application.getId();
  }

  private void addUuid(Application applicationModel) {
    // Set a UUID if it doesn't have one
    if (null == applicationModel.getApplicationId()) {
      applicationModel.setApplicationId(UUID.randomUUID().toString());
    }
  }
}

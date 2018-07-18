package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.ApplicationRepository;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;

@Slf4j
@Service
@Transactional
public class ApplicationService {

  private final ApplicationRepository repository;

  @Autowired
  ApplicationService(ApplicationRepository repository) {
    this.repository = repository;
  }

  public void createApplication(ApplicationEntity application) {
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
  }
}

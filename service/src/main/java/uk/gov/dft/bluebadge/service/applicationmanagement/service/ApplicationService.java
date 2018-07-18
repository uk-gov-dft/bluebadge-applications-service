package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Application;
import uk.gov.dft.bluebadge.service.applicationmanagement.converter.ApplicationConverter;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.ApplicationRepository;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ValidatedApplication;
import uk.gov.dft.bluebadge.service.applicationmanagement.service.validation.ApplicationValidator;

@Slf4j
@Service
@Transactional
public class ApplicationService {

  private final ApplicationRepository repository;
  private ApplicationConverter converter;
  private ApplicationValidator validator;

  @Autowired
  ApplicationService(
      ApplicationRepository repository,
      ApplicationConverter converter,
      ApplicationValidator validator) {
    this.repository = repository;
    this.converter = converter;
    this.validator = validator;
  }

  public String createApplication(Application applicationModel) {

    ValidatedApplication validatedApplication = validator.validateForCreate(applicationModel);
    ApplicationEntity application = converter.convertToEntityOnCreate(validatedApplication);
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
    return application.getId().toString();
  }
}

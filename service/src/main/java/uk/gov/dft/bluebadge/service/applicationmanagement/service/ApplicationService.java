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
    repository.createApplication(application);
  }
}

package uk.gov.dft.bluebadge.service.applicationmanagement.repository.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.HealthcareProfessionalEntity;

@Mapper
public interface ApplicationMapper {
  /**
   * Create an application.
   *
   * @param applicationEntity application to create.
   */
  void createApplication(ApplicationEntity applicationEntity);

  int createHealthcareProfessionals(List<HealthcareProfessionalEntity> professionals);
}

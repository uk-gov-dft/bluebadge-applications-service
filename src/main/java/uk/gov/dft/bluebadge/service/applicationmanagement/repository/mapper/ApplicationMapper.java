package uk.gov.dft.bluebadge.service.applicationmanagement.repository.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ApplicationSummaryEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ArtifactEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.FindApplicationQueryParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.HealthcareProfessionalEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.MedicationEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.RetrieveApplicationQueryParams;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.TreatmentEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.VehicleEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingAidEntity;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.WalkingDifficultyTypeEntity;

@Mapper
public interface ApplicationMapper {

  /**
   * Create an application.
   *
   * @param applicationEntity application to create.
   * @return Created count.
   */
  int createApplication(ApplicationEntity applicationEntity);

  /**
   * Create multiple healthcare professionals for an application.
   *
   * @param professionals List of HealthcareProfessionalEntity to create.
   * @return Created count.
   */
  int createHealthcareProfessionals(List<HealthcareProfessionalEntity> professionals);

  /**
   * Create multiple medications for an application.
   *
   * @param medications List of MedicationEntity to create.
   * @return Created count.
   */
  int createMedications(List<MedicationEntity> medications);

  /**
   * Create multiple treatments for an application.
   *
   * @param treatments List of TreatmentEntity to create.
   * @return Created count.
   */
  int createTreatments(List<TreatmentEntity> treatments);

  /**
   * Create multiple vehicles for an application.
   *
   * @param vehicles List of VehicleEntity to create.
   * @return Created count.
   */
  int createVehicles(List<VehicleEntity> vehicles);

  /**
   * Create multiple walking aids for an application.
   *
   * @param walkingAids List of WalkingAidEntity to create.
   * @return Created count.
   */
  int createWalkingAids(List<WalkingAidEntity> walkingAids);

  /**
   * Create multiple walking difficulties for an application.
   *
   * @param walkingDifficultyTypes List of WalkingDifficultyTypeEntity to create.
   * @return Created count.
   */
  int createWalkingDifficultyTypes(List<WalkingDifficultyTypeEntity> walkingDifficultyTypes);

  int createArtifacts(List<ArtifactEntity> artifactEntities);

  /**
   * Search for applications.
   *
   * @param findApplicationQueryParams Search criteria.
   * @return List of application summary records. Could contain 0 items.
   */
  List<ApplicationSummaryEntity> findApplications(
      FindApplicationQueryParams findApplicationQueryParams);

  /**
   * Retrieve full details of a single application.
   *
   * @param params PK
   * @return The application or null.
   */
  ApplicationEntity retrieveApplication(RetrieveApplicationQueryParams params);

  int deleteApplication(RetrieveApplicationQueryParams params);

  int deleteHealthcareProfessionals(String applicationId);

  int deleteMedications(String applicationId);

  int deleteTreatments(String applicationId);

  int deleteVehicles(String applicationId);

  int deleteWalkingAids(String applicationId);

  int deleteWalkingDifficultyTypes(String applicationId);
}

package uk.gov.dft.bluebadge.service.applicationmanagement.repository.mapper;

public enum Statements {
  CREATE("createApplication"),
  CREATE_HEALTHCARE_PROFESSIONALS("createHealthcareProfessionals"),
  CREATE_MEDICATIONS("createMedications"),
  CREATE_TREATMENTS("createTreatments"),
  CREATE_VEHICLES("createVehicles"),
  CREATE_WALKING_AIDS("createWalkingAids"),
  CREATE_WALKING_DIFFICULTY_TYPES("createWalkingDifficultyTypes"),
  CREATE_ARTIFACTS("createArtifacts"),
  FIND("findApplications"),
  RETRIEVE("retrieveApplication"),
  UPDATE("updateToDeleteApplication"),
  DELETE_HEALTHCARE_PROFESSIONALS("deleteHealthcareProfessionals"),
  DELETE_MEDICATIONS("deleteMedications"),
  DELETE_TREATMENTS("deleteTreatments"),
  DELETE_VEHICLES("deleteVehicles"),
  DELETE_WALKING_AIDS("deleteWalkingAids"),
  DELETE_WALKING_DIFFICULTY_TYPES("deleteWalkingDifficultyTypes"),
  DELETE_ARTIFACTS("deleteArtifacts");

  private final String name;

  Statements(String name) {

    this.name = name;
  }

  public String getName() {
    return name;
  }
}

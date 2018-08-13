package uk.gov.dft.bluebadge.service.applicationmanagement.repository.mapper;

public enum Statements {
  CREATE("createApplication"),
  CREATE_HEALTHCARE_PROFESSIONALS("createHealthcareProfessionals"),
  CREATE_MEDICATIONS("createMedications"),
  CREATE_TREATMENTS("createTreatments"),
  CREATE_VEHICLES("createVehicles"),
  CREATE_WALKING_AIDS("createWalkingAids"),
  CREATE_WALKING_DIFFICULTY_TYPES("createWalkingDifficultyTypes");

  private String name;

  Statements(String name) {

    this.name = name;
  }

  public String getName() {
    return name;
  }
}

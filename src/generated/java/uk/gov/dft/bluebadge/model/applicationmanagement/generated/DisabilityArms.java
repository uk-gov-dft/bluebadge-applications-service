package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode
public class DisabilityArms {

  @Size(max = 100)
  private String drivingFrequency;

  private Boolean isAdaptedVehicle;

  @Size(max = 255)
  private String adaptedVehicleDescription;
}

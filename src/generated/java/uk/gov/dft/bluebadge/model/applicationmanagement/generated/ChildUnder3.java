package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ChildUnder3 {
  @Deprecated private BulkyMedicalEquipmentTypeCodeField bulkyMedicalEquipmentTypeCode;
  @Valid private List<BulkyMedicalEquipmentTypeCodeField> bulkyMedicalEquipmentTypeCodes;

  @Size(max = 100)
  private String otherMedicalEquipment = null;
}

package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ChildUnder3 {
  @Deprecated private BulkyMedicalEquipmentTypeCodeField bulkyMedicalEquipmentTypeCode;
  @Valid private List<BulkyMedicalEquipmentTypeCodeField> bulkyMedicalEquipmentTypeCodes;
  @Size(max = 100)
  private String otherMedicalEquipment = null;
}

package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

/** WalkingDifficulty */
@Validated
@Data
public class WalkingDifficulty {

  @Valid private List<WalkingDifficultyTypeCodeField> typeCodes;

  @Size(max = 2000)
  private String painDescription;

  @Size(max = 2000)
  private String balanceDescription;

  private Boolean healthProfessionsForFalls;

  @Size(max = 2000)
  private String dangerousDescription;

  private Boolean chestLungHeartEpilepsy;

  @Size(max = 2000)
  private String otherDescription;

  @Valid private List<WalkingAid> walkingAids;

  private WalkingLengthOfTimeCodeField walkingLengthOfTimeCode;

  @Valid private WalkingSpeedCodeField walkingSpeedCode;

  @Valid private List<Treatment> treatments;

  @Valid private List<Medication> medications;
  @Valid private Breathlessness breathlessness;
}

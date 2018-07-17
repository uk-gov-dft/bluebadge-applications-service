package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** Blind */
@Validated
public class Blind {
  @JsonProperty("registeredAtLaId")
  private String registeredAtLaId = null;

  public Blind registeredAtLaId(String registeredAtLaId) {
    this.registeredAtLaId = registeredAtLaId;
    return this;
  }

  /**
   * Local Authority registered blind at
   *
   * @return registeredAtLaId
   */
  @ApiModelProperty(example = "7", value = "Local Authority registered blind at")
  public String getRegisteredAtLaId() {
    return registeredAtLaId;
  }

  public void setRegisteredAtLaId(String registeredAtLaId) {
    this.registeredAtLaId = registeredAtLaId;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Blind blind = (Blind) o;
    return Objects.equals(this.registeredAtLaId, blind.registeredAtLaId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(registeredAtLaId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Blind {\n");

    sb.append("    registeredAtLaId: ").append(toIndentedString(registeredAtLaId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

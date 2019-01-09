package uk.gov.dft.bluebadge.model.applicationmanagement.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/** Provided files/evidence */
@ApiModel(description = "Provided files/evidence")
@Validated
public class Artifact {
  /** The artifact type */
  public enum TypeEnum {
    PROOF_ELIG("PROOF_ELIG"),

    SUPPORT_DOCS("SUPPORT_DOCS"),

    PROOF_ADD("PROOF_ADD"),

    PROOF_ID("PROOF_ID"),

    PHOTO("PHOTO");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("type")
  private TypeEnum type = null;

  @JsonProperty("link")
  private String link = null;

  public Artifact type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * The artifact type
   *
   * @return type
   */
  @ApiModelProperty(example = "PROOF_ADD", required = true, value = "The artifact type")
  @NotNull
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public Artifact link(String link) {
    this.link = link;
    return this;
  }

  /**
   * The link to the artifact
   *
   * @return link
   */
  @ApiModelProperty(required = true, value = "The link to the artifact")
  @NotNull
  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Artifact artifact = (Artifact) o;
    return Objects.equals(this.type, artifact.type) && Objects.equals(this.link, artifact.link);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, link);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Artifact {\n");

    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
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

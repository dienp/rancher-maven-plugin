package rancher.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Label {
  
  @JsonProperty(value = "io.rancher.container.pull_image", defaultValue ="always")
  private String pullImage = "always";
}

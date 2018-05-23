package rancher.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LaunchConfig {
  boolean tty;
  int vcpu;
  String imageUuid;
  List<String> ports;
  Label labels;

  @Data
  public static class Label {
    @JsonProperty(value = "io.rancher.container.pull_image")
    String pullImage = "always";
  }
}

package rancher.models;

import java.util.List;
import lombok.Data;

@Data
public class LaunchConfig {
  boolean tty;
  int vcpu;
  String imageUuid;
  List<String> ports;
  Label labels;
}

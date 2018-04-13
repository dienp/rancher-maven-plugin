package rancher.models;

import lombok.Data;

@Data
public class LaunchConfig {
	boolean tty;
	int vcpu;
	String imageUuid;
}

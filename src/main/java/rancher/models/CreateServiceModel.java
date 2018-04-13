package rancher.models;

import lombok.Data;

@Data
public class CreateServiceModel {
	String name;
	String stackId;
	Integer scale;
	LaunchConfig launchConfig;
	boolean assignServiceIpAddress;
	boolean startOnCreate;
}

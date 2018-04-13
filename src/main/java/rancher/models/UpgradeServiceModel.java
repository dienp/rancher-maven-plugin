package rancher.models;

import lombok.Data;

@Data
public class UpgradeServiceModel {
	InServiceStrategy inServiceStrategy;
	String toServiceStrategy;
}

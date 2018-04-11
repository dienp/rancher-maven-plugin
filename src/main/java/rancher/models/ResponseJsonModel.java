package rancher.models;

import java.util.List;

import lombok.Data;

@Data
public class ResponseJsonModel {
	List<DataJsonModel> data;
}

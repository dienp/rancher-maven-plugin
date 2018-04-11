package rancher;

import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.codehaus.plexus.util.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import rancher.models.DataJsonModel;
import rancher.models.ResponseJsonModel;
import rancher.util.Constant;
import rancher.util.Util;

public class RancherAPI {

	private static final Logger LOGGER = Logger.getLogger(RancherAPI.class);

	public String findIdByName(String name, String requestURL, String authToken) {
		ResponseJsonModel reponse = getDataFromAPI(requestURL, authToken);
		if (reponse == null || reponse.getData().isEmpty()) {
			LOGGER.error("Couldn't find id with name = " + name);
			return null;
		}
		DataJsonModel data = reponse.getData().iterator().next();
		LOGGER.debug("Found id = " + data.getId() + " for " + name);
		return data.getId();
	}

	private ResponseJsonModel getDataFromAPI(String requestURL, String authToken) {
		String jsonString = Util.connectToWebService(requestURL, Constant.METHOD_GET, null, authToken);
		if (StringUtils.isEmpty(jsonString)) {
			LOGGER.error("Failed to GET data from Rancher. URL = " + requestURL);
			return null;
		}
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			JsonNode node = mapper.readTree(jsonString);
			return mapper.treeToValue(node, ResponseJsonModel.class);
		} catch (IOException e) {
			LOGGER.error("IOException: " + e);
		}
		return null;
	}

}

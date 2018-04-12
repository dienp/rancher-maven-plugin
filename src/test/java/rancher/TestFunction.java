package rancher;

import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.StringJoiner;

import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.StringUtils;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import rancher.models.DataJsonModel;
import rancher.models.ResponseJsonModel;

public class TestFunction {

	private static String rancherHost = "10.3.65.122";
	private static int rancherPort = 8080;
	private static String rancherProjectName = "Default";
	private static String rancherStackName = "DevTMA";
	private static String rancherServiceName = "testHub101";
	private static String authToken = "Basic RTIzQzNFNDhFMzBBNEQ0MkVCREM6TGhYc2JRa0dhQkh0aGlINjJDTHhxVnJRUmR0R0g4dFdVUnBubkw5OA==";

	@Test
	public void testFindServiceId() {
		RancherAPI rancherApi = new RancherAPI();
		String projectId = rancherApi.findProjectIdByName(rancherHost, rancherPort, rancherProjectName, authToken);
		String stackId = rancherApi.findStackIdByName(rancherHost, rancherPort, projectId, rancherStackName, authToken);
		String serviceId = rancherApi.findServiceIdByName(rancherHost, rancherPort, projectId, stackId, rancherServiceName, authToken);
		assertNotEquals(true, StringUtils.isEmpty(serviceId));
	}

}

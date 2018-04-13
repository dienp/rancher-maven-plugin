package rancher;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.codehaus.plexus.util.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import rancher.common.Constant;
import rancher.common.Util;
import rancher.models.CreateServiceModel;


public class TestFunction {

	private static String rancherHost = "10.3.65.122";
	private static int rancherPort = 8080;
	private static String rancherProjectName = "Default";
	private static String rancherStackName = "DevTMA";
	private static String rancherServiceName = "testHub101";
	private static String authToken = "Basic RTIzQzNFNDhFMzBBNEQ0MkVCREM6TGhYc2JRa0dhQkh0aGlINjJDTHhxVnJRUmR0R0g4dFdVUnBubkw5OA==";
	private static String postData = "{ \"name\":\"newServiceFromPlugin\", \"stackId\":\"1st20\", \"scale\":1, \"launchConfig\":{ \"tty\":true, \"vcpu\":1, \"imageUuid\":\"docker:10.3.65.122:5000/testhub:1.0.1\" }, \"assignServiceIpAddress\":false, \"startOnCreate\":true }";
	
	@Ignore
	@Test
	public void testFindServiceId() {
		RancherAPI rancherApi = new RancherAPI();
		String projectId = rancherApi.findProjectIdByName(rancherHost, rancherPort, rancherProjectName, authToken);
		String stackId = rancherApi.findStackIdByName(rancherHost, rancherPort, projectId, rancherStackName, authToken);
		String serviceId = rancherApi.findServiceIdByName(rancherHost, rancherPort, projectId, stackId, rancherServiceName, authToken);
		assertNotEquals(true, StringUtils.isEmpty(serviceId));
	}
	
	@Ignore
	@Test
	public void testCreateService() {
		CreateServiceModel data = (CreateServiceModel) Util.parseJsonFromString(CreateServiceModel.class, postData);
		String createServiceURL = "http://10.3.65.122:8080/v2-beta/projects/1a5/service";
		String json = Util.parseStringFromJson(data);
		String upgradeServiceData = Util.makeUpgradeServicePostData("10.3.65.122:5000/testhub:1.0.1");
		System.out.println("");
		
		//String result = Util.connectToWebService(createServiceURL, Constant.METHOD_POST, postData, authToken);
		//assertNotNull(result);
	}
}

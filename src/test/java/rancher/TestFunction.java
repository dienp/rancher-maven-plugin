package rancher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.StringJoiner;

import org.codehaus.plexus.util.StringUtils;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import rancher.models.DataJsonModel;
import rancher.models.ResponseJsonModel;

public class TestFunction {

	@Test
	public void testJsonParse() {
		String projects = "{  \r\n" + "   \"type\":\"collection\",\r\n" + "   \"resourceType\":\"project\",\r\n"
				+ "   \"links\":{  \r\n" + "      \"self\":\"…/v2-beta/projects\"\r\n" + "   },\r\n"
				+ "   \"createTypes\":{  \r\n" + "\r\n" + "   },\r\n" + "   \"actions\":{  \r\n" + "\r\n" + "   },\r\n"
				+ "   \"data\":[  \r\n" + "      {  \r\n" + "         \"id\":\"1a5\",\r\n"
				+ "         \"type\":\"project\",\r\n" + "         \"links\":{  \r\n"
				+ "            \"self\":\"…/v2-beta/projects/1a5\",\r\n"
				+ "            \"agents\":\"…/v2-beta/projects/1a5/agents\",\r\n"
				+ "            \"auditLogs\":\"…/v2-beta/projects/1a5/auditlogs\",\r\n"
				+ "            \"backupTargets\":\"…/v2-beta/projects/1a5/backuptargets\",\r\n"
				+ "            \"backups\":\"…/v2-beta/projects/1a5/backups\",\r\n"
				+ "            \"certificates\":\"…/v2-beta/projects/1a5/certificates\",\r\n"
				+ "            \"configItemStatuses\":\"…/v2-beta/projects/1a5/configitemstatuses\",\r\n"
				+ "            \"containerEvents\":\"…/v2-beta/projects/1a5/containerevents\",\r\n"
				+ "            \"credentials\":\"…/v2-beta/projects/1a5/credentials\",\r\n"
				+ "            \"externalEvents\":\"…/v2-beta/projects/1a5/externalevents\",\r\n"
				+ "            \"genericObjects\":\"…/v2-beta/projects/1a5/genericobjects\",\r\n"
				+ "            \"healthcheckInstanceHostMaps\":\"…/v2-beta/projects/1a5/healthcheckinstancehostmaps\",\r\n"
				+ "            \"hostTemplates\":\"…/v2-beta/projects/1a5/hosttemplates\",\r\n"
				+ "            \"hosts\":\"…/v2-beta/projects/1a5/hosts\",\r\n"
				+ "            \"images\":\"…/v2-beta/projects/1a5/images\",\r\n"
				+ "            \"instanceLinks\":\"…/v2-beta/projects/1a5/instancelinks\",\r\n"
				+ "            \"instances\":\"…/v2-beta/projects/1a5/instances\",\r\n"
				+ "            \"ipAddresses\":\"…/v2-beta/projects/1a5/ipaddresses\",\r\n"
				+ "            \"labels\":\"…/v2-beta/projects/1a5/labels\",\r\n"
				+ "            \"mounts\":\"…/v2-beta/projects/1a5/mounts\",\r\n"
				+ "            \"networkDrivers\":\"…/v2-beta/projects/1a5/networkdrivers\",\r\n"
				+ "            \"networks\":\"…/v2-beta/projects/1a5/networks\",\r\n"
				+ "            \"physicalHosts\":\"…/v2-beta/projects/1a5/physicalhosts\",\r\n"
				+ "            \"ports\":\"…/v2-beta/projects/1a5/ports\",\r\n"
				+ "            \"processInstances\":\"…/v2-beta/projects/1a5/processinstances\",\r\n"
				+ "            \"projectMembers\":\"…/v2-beta/projects/1a5/projectmembers\",\r\n"
				+ "            \"projectTemplate\":\"…/v2-beta/projects/1a5/projecttemplate\",\r\n"
				+ "            \"projectTemplates\":\"…/v2-beta/projects/1a5/projecttemplates\",\r\n"
				+ "            \"scheduledUpgrades\":\"…/v2-beta/projects/1a5/scheduledupgrades\",\r\n"
				+ "            \"secrets\":\"…/v2-beta/projects/1a5/secrets\",\r\n"
				+ "            \"serviceConsumeMaps\":\"…/v2-beta/projects/1a5/serviceconsumemaps\",\r\n"
				+ "            \"serviceEvents\":\"…/v2-beta/projects/1a5/serviceevents\",\r\n"
				+ "            \"serviceExposeMaps\":\"…/v2-beta/projects/1a5/serviceexposemaps\",\r\n"
				+ "            \"serviceLogs\":\"…/v2-beta/projects/1a5/servicelogs\",\r\n"
				+ "            \"services\":\"…/v2-beta/projects/1a5/services\",\r\n"
				+ "            \"snapshots\":\"…/v2-beta/projects/1a5/snapshots\",\r\n"
				+ "            \"stacks\":\"…/v2-beta/projects/1a5/stacks\",\r\n"
				+ "            \"storageDrivers\":\"…/v2-beta/projects/1a5/storagedrivers\",\r\n"
				+ "            \"storagePools\":\"…/v2-beta/projects/1a5/storagepools\",\r\n"
				+ "            \"subnets\":\"…/v2-beta/projects/1a5/subnets\",\r\n"
				+ "            \"userPreferences\":\"…/v2-beta/projects/1a5/userpreferences\",\r\n"
				+ "            \"volumeTemplates\":\"…/v2-beta/projects/1a5/volumetemplates\",\r\n"
				+ "            \"volumes\":\"…/v2-beta/projects/1a5/volumes\",\r\n"
				+ "            \"accounts\":\"…/v2-beta/projects/1a5/accounts\",\r\n"
				+ "            \"apiKeys\":\"…/v2-beta/projects/1a5/apikeys\",\r\n"
				+ "            \"azureadconfigs\":\"…/v2-beta/projects/1a5/azureadconfigs\",\r\n"
				+ "            \"clusterMemberships\":\"…/v2-beta/projects/1a5/clustermemberships\",\r\n"
				+ "            \"composeProjects\":\"…/v2-beta/projects/1a5/composeprojects\",\r\n"
				+ "            \"composeServices\":\"…/v2-beta/projects/1a5/composeservices\",\r\n"
				+ "            \"configItems\":\"…/v2-beta/projects/1a5/configitems\",\r\n"
				+ "            \"containers\":\"…/v2-beta/projects/1a5/containers\",\r\n"
				+ "            \"databasechangeloglocks\":\"…/v2-beta/projects/1a5/databasechangeloglocks\",\r\n"
				+ "            \"databasechangelogs\":\"…/v2-beta/projects/1a5/databasechangelogs\",\r\n"
				+ "            \"dnsServices\":\"…/v2-beta/projects/1a5/dnsservices\",\r\n"
				+ "            \"extensionPoints\":\"…/v2-beta/projects/1a5/extensionpoints\",\r\n"
				+ "            \"externalDnsEvents\":\"…/v2-beta/projects/1a5/externaldnsevents\",\r\n"
				+ "            \"externalHandlerExternalHandlerProcessMaps\":\"…/v2-beta/projects/1a5/externalhandlerexternalhandlerprocessmaps\",\r\n"
				+ "            \"externalHandlerProcesses\":\"…/v2-beta/projects/1a5/externalhandlerprocesses\",\r\n"
				+ "            \"externalHandlers\":\"…/v2-beta/projects/1a5/externalhandlers\",\r\n"
				+ "            \"externalHostEvents\":\"…/v2-beta/projects/1a5/externalhostevents\",\r\n"
				+ "            \"externalServiceEvents\":\"…/v2-beta/projects/1a5/externalserviceevents\",\r\n"
				+ "            \"externalServices\":\"…/v2-beta/projects/1a5/externalservices\",\r\n"
				+ "            \"externalStoragePoolEvents\":\"…/v2-beta/projects/1a5/externalstoragepoolevents\",\r\n"
				+ "            \"externalVolumeEvents\":\"…/v2-beta/projects/1a5/externalvolumeevents\",\r\n"
				+ "            \"haConfigInputs\":\"…/v2-beta/projects/1a5/haconfiginputs\",\r\n"
				+ "            \"haConfigs\":\"…/v2-beta/projects/1a5/haconfigs\",\r\n"
				+ "            \"hostApiProxyTokens\":\"…/v2-beta/projects/1a5/hostapiproxytokens\",\r\n"
				+ "            \"identities\":\"…/v2-beta/projects/1a5/identities\",\r\n"
				+ "            \"kubernetesServices\":\"…/v2-beta/projects/1a5/kubernetesservices\",\r\n"
				+ "            \"kubernetesStacks\":\"…/v2-beta/projects/1a5/kubernetesstacks\",\r\n"
				+ "            \"loadBalancerServices\":\"…/v2-beta/projects/1a5/loadbalancerservices\",\r\n"
				+ "            \"localAuthConfigs\":\"…/v2-beta/projects/1a5/localauthconfigs\",\r\n"
				+ "            \"machineDrivers\":\"…/v2-beta/projects/1a5/machinedrivers\",\r\n"
				+ "            \"machines\":\"…/v2-beta/projects/1a5/machines\",\r\n"
				+ "            \"networkDriverServices\":\"…/v2-beta/projects/1a5/networkdriverservices\",\r\n"
				+ "            \"networkPolicyRuleWithins\":\"…/v2-beta/projects/1a5/networkpolicyrulewithins\",\r\n"
				+ "            \"openldapconfigs\":\"…/v2-beta/projects/1a5/openldapconfigs\",\r\n"
				+ "            \"passwords\":\"…/v2-beta/projects/1a5/passwords\",\r\n"
				+ "            \"processDefinitions\":\"…/v2-beta/projects/1a5/processdefinitions\",\r\n"
				+ "            \"processExecutions\":\"…/v2-beta/projects/1a5/processexecutions\",\r\n"
				+ "            \"processPools\":\"…/v2-beta/projects/1a5/processpools\",\r\n"
				+ "            \"processSummary\":\"…/v2-beta/projects/1a5/processsummary\",\r\n"
				+ "            \"projects\":\"…/v2-beta/projects/1a5/projects\",\r\n"
				+ "            \"pullTasks\":\"…/v2-beta/projects/1a5/pulltasks\",\r\n"
				+ "            \"register\":\"…/v2-beta/projects/1a5/register\",\r\n"
				+ "            \"registrationTokens\":\"…/v2-beta/projects/1a5/registrationtokens\",\r\n"
				+ "            \"registries\":\"…/v2-beta/projects/1a5/registries\",\r\n"
				+ "            \"registryCredentials\":\"…/v2-beta/projects/1a5/registrycredentials\",\r\n"
				+ "            \"resourceDefinitions\":\"…/v2-beta/projects/1a5/resourcedefinitions\",\r\n"
				+ "            \"schemas\":\"…/v2-beta/projects/1a5/schemas\",\r\n"
				+ "            \"serviceProxies\":\"…/v2-beta/projects/1a5/serviceproxies\",\r\n"
				+ "            \"settings\":\"…/v2-beta/projects/1a5/settings\",\r\n"
				+ "            \"storageDriverServices\":\"…/v2-beta/projects/1a5/storagedriverservices\",\r\n"
				+ "            \"taskInstances\":\"…/v2-beta/projects/1a5/taskinstances\",\r\n"
				+ "            \"tasks\":\"…/v2-beta/projects/1a5/tasks\",\r\n"
				+ "            \"typeDocumentations\":\"…/v2-beta/projects/1a5/typedocumentations\",\r\n"
				+ "            \"virtualMachines\":\"…/v2-beta/projects/1a5/virtualmachines\",\r\n"
				+ "            \"hostStats\":\"…/v2-beta/projects/1a5/projects/1a5/hoststats\"\r\n" + "         },\r\n"
				+ "         \"actions\":{  \r\n"
				+ "            \"upgrade\":\"…/v2-beta/projects/1a5/?action=upgrade\",\r\n"
				+ "            \"update\":\"…/v2-beta/projects/1a5/?action=update\",\r\n"
				+ "            \"deactivate\":\"…/v2-beta/projects/1a5/?action=deactivate\",\r\n"
				+ "            \"setmembers\":\"…/v2-beta/projects/1a5/?action=setmembers\",\r\n"
				+ "            \"delete\":\"…/v2-beta/projects/1a5/?action=delete\",\r\n"
				+ "            \"defaultNetworkId\":\"…/v2-beta/projects/1a5/?action=defaultnetworkid\"\r\n"
				+ "         },\r\n" + "         \"baseType\":\"account\",\r\n" + "         \"name\":\"Default\",\r\n"
				+ "         \"state\":\"active\",\r\n" + "         \"allowSystemRole\":false,\r\n"
				+ "         \"created\":\"2017-10-16T03:43:16Z\",\r\n" + "         \"createdTS\":1508125396000,\r\n"
				+ "         \"data\":{  \r\n" + "            \"fields\":{  \r\n"
				+ "               \"orchestration\":\"cattle\",\r\n" + "               \"servicesPortRange\":{  \r\n"
				+ "                  \"endPort\":65535,\r\n" + "                  \"startPort\":49153\r\n"
				+ "               },\r\n" + "               \"createdStackIds\":[  \r\n" + "                  1,\r\n"
				+ "                  2,\r\n" + "                  3,\r\n" + "                  4\r\n"
				+ "               ],\r\n" + "               \"startedStackIds\":[  \r\n" + "                  1,\r\n"
				+ "                  2,\r\n" + "                  3,\r\n" + "                  4\r\n"
				+ "               ]\r\n" + "            }\r\n" + "         },\r\n"
				+ "         \"defaultNetworkId\":\"1n5\",\r\n" + "         \"description\":null,\r\n"
				+ "         \"healthState\":\"degraded\",\r\n" + "         \"hostRemoveDelaySeconds\":null,\r\n"
				+ "         \"kind\":\"project\",\r\n" + "         \"members\":null,\r\n"
				+ "         \"orchestration\":\"cattle\",\r\n" + "         \"projectLinks\":null,\r\n"
				+ "         \"projectTemplateId\":\"1pt5\",\r\n" + "         \"removeTime\":null,\r\n"
				+ "         \"removed\":null,\r\n" + "         \"servicesPortRange\":{  \r\n"
				+ "            \"type\":\"servicesPortRange\",\r\n" + "            \"endPort\":65535,\r\n"
				+ "            \"startPort\":49153\r\n" + "         },\r\n" + "         \"transitioning\":\"no\",\r\n"
				+ "         \"transitioningMessage\":null,\r\n" + "         \"transitioningProgress\":null,\r\n"
				+ "         \"uuid\":\"adminProject\",\r\n" + "         \"version\":\"2\",\r\n"
				+ "         \"virtualMachine\":false\r\n" + "      }\r\n" + "   ],\r\n" + "   \"sortLinks\":{  \r\n"
				+ "\r\n" + "   },\r\n" + "   \"pagination\":null,\r\n" + "   \"sort\":null,\r\n"
				+ "   \"filters\":{  \r\n" + "\r\n" + "   },\r\n" + "   \"createDefaults\":{  \r\n" + "\r\n"
				+ "   }\r\n" + "}";

		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			JsonNode node = mapper.readTree(projects);
			// JsonNode dataNode = node.at("/data").get(0);
			ResponseJsonModel data = mapper.treeToValue(node, ResponseJsonModel.class);
			System.out.println(data.getData().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUrlTemplate() {
		try {
			String projectId ="1a5";
			String stackId = "1st20";
			StringJoiner sj = new StringJoiner("/");
			sj.add("");
			sj.add("v2-beta");
			if(!StringUtils.isEmpty(projectId)) {
				sj.add("projects");
				sj.add(projectId);
			}
			
			if(!StringUtils.isEmpty(stackId)) {
				sj.add("stacks");
				sj.add(stackId);
			}
			
			String scheme = "http";
			String host = "10.3.65.122";
			int port = 8080;
			String path = "/v2-beta/projects/1a5/stacks/1st20";
				String query = "query=33&query2=55";
			URI uri = new URI(scheme, null, host, port, path, query, null);
			System.out.println(uri.toURL().toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}

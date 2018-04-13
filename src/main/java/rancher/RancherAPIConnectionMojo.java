package rancher;

import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import rancher.common.Constant;
import rancher.common.Util;
import rancher.models.URIBuilder;

@Mojo(name = "run")
public class RancherAPIConnectionMojo extends AbstractMojo {

	@Parameter(property = "rancher.host")
	private String rancherHost;

	@Parameter(property = "rancher.port")
	private int rancherPort;

	@Parameter(property = "rancher.project.name")
	private String rancherProjectName;

	@Parameter(property = "rancher.stack.name")
	private String rancherStackName;

	@Parameter(property = "rancher.service.name")
	private String rancherServiceName;

	@Parameter(property = "rancher.username")
	private String username;

	@Parameter(property = "rancher.password")
	private String password;

	@Parameter(property = "service.action.timeout")
	private Long actionTimeout;

	@Parameter(property = "docker.image.name")
	private String dockerImageName;

	private static final Logger LOGGER = Logger.getLogger(RancherAPIConnectionMojo.class);

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		validateParameters(this);

		RancherAPI rancherApi = new RancherAPI();
		String authToken = Util.getBasicAuthToken(username, password);
		String projectId = rancherApi.findProjectIdByName(rancherHost, rancherPort, rancherProjectName, authToken);
		String stackId = rancherApi.findStackIdByName(rancherHost, rancherPort, projectId, rancherStackName, authToken);
		String serviceId = rancherApi.findServiceIdByName(rancherHost, rancherPort, projectId, stackId,
				rancherServiceName, authToken);

		if (serviceId.equals(Constant.SERVICE_ID_NOT_EXISTED)) {
			String createServiceURL = new URIBuilder(rancherHost, rancherPort, projectId, null).buildCreateServiceURI()
					.toString();
			createService(createServiceURL, rancherServiceName, stackId, dockerImageName, authToken);
		} else {
			URIBuilder uriBuilder = new URIBuilder(rancherHost, rancherPort, projectId, serviceId, null);
			String serviceWithIdURL = uriBuilder.buildServiceWithIdURL().toString();
			LOGGER.debug("serviceWithIdURL: " + serviceWithIdURL);
			uriBuilder.setQuery(Constant.QueryParam.ACTION + Constant.Action.UPGRAGE);
			String upgradeURL = uriBuilder.buildServiceWithIdURL().toString();

			uriBuilder.setQuery(Constant.QueryParam.ACTION + Constant.Action.FINISH_UPGRAGE);
			String finishUpgradeURL = uriBuilder.buildServiceWithIdURL().toString();

			uriBuilder.setQuery(Constant.QueryParam.ACTION + Constant.Action.ROLLBACK);
			String rollbackURL = uriBuilder.buildServiceWithIdURL().toString();

			String state = Util.findKeyValue(Util.fetchServiceInfoFromRancher(serviceWithIdURL, authToken),
					Constant.KEYWORD_STATE);

			switch (state) {
			case Constant.State.ACTIVATING:
			case Constant.State.ACTIVE:
				upgradeService(upgradeURL, rollbackURL, finishUpgradeURL, serviceWithIdURL, dockerImageName,
						actionTimeout, authToken);
				break;
			case Constant.State.UPGRADED:
				finishUpgradeService(finishUpgradeURL, authToken);
				if (Util.pollingForState(serviceWithIdURL, authToken, actionTimeout, Constant.State.ACTIVE)) {
					upgradeService(upgradeURL, rollbackURL, finishUpgradeURL, serviceWithIdURL, dockerImageName,
							actionTimeout, authToken);
				} else {
					rollbackService(rollbackURL, authToken);
				}
				break;
			case Constant.State.UPGRADING:
				rollbackService(rollbackURL, authToken);
				if (Util.pollingForState(serviceWithIdURL, authToken, actionTimeout, Constant.State.ACTIVE)) {
					upgradeService(upgradeURL, rollbackURL, finishUpgradeURL, serviceWithIdURL, dockerImageName,
							actionTimeout, authToken);
				} else {
					rollbackService(rollbackURL, authToken);
				}
				break;
			default:
				LOGGER.error("Failed to fetch service status");
				break;
			}
		}
	}

	private void upgradeService(String upgradeURL, String rollbackURL, String finishUpgradeURL, String serviceWithIdURL,
			String dockerImage, Long actionTimeout, String authToken) {
		LOGGER.debug("Upgrading service...");
		Util.postToRancher(upgradeURL, Util.makeUpgradeServicePostData(dockerImage), authToken);
		if (Util.pollingForState(serviceWithIdURL, authToken, actionTimeout, Constant.State.UPGRADED)) {
			finishUpgradeService(finishUpgradeURL, authToken);
		} else {
			rollbackService(rollbackURL, authToken);
		}
	}

	private void rollbackService(String rollbackURL, String authToken) {
		LOGGER.debug("Rolling-back service...");
		Util.postToRancher(rollbackURL, Constant.EMPTY_POST_DATA, authToken);
	}

	private void finishUpgradeService(String finishUpgradeURL, String authToken) {
		LOGGER.debug("Finishing upgrade service...");
		Util.postToRancher(finishUpgradeURL, Constant.EMPTY_POST_DATA, authToken);
	}

	private void createService(String createServiceURL, String name, String stackId, String dockerImage,
			String authToken) {
		LOGGER.debug("Creating service...");
		Util.postToRancher(createServiceURL, Util.makeCreateServicePostData(name, stackId, dockerImage), authToken);
	}

	private void validateParameters(Object obj) {
		Class<?> clazz = obj.getClass();
		LOGGER.debug("Validating parameters...");
		Field[] fields = clazz.getDeclaredFields();
		LOGGER.debug("Found: " + fields.length + " parameters");
		for (Field field : fields) {
			try {
				LOGGER.debug("Validating parameter " + field.getName());
				if (field.get(obj) == null) {
					throw new IOException(field.getName() + " cannot be null");
				}
				LOGGER.debug(field.getName() + ". PASSED!");
			} catch (IllegalArgumentException e) {
				LOGGER.error("IllegalArgumentException: ", e);
			} catch (IllegalAccessException e) {
				LOGGER.error("IllegalAccessException: ", e);
			} catch (IOException e) {
				LOGGER.error("IOException: ", e);
			}
		}
	}

}

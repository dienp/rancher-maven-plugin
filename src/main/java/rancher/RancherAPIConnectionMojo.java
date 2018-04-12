package rancher;

import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import rancher.models.URIBuilder;
import rancher.util.Constant;
import rancher.util.Util;

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
		RancherAPIConnectionMojo obj = this;
		validateParameters(obj);
		
		RancherAPI rancherApi = new RancherAPI();
		String authToken = Util.getBasicAuthToken(username, password);
		String projectId = rancherApi.findProjectIdByName(rancherHost, rancherPort, rancherProjectName, authToken);
		String stackId = rancherApi.findStackIdByName(rancherHost, rancherPort, projectId, rancherStackName, authToken);
		String serviceId = rancherApi.findServiceIdByName(rancherHost, rancherPort, projectId, stackId,
				rancherServiceName, authToken);
		
		URIBuilder uriBuilder = new URIBuilder(rancherHost, rancherPort, projectId, serviceId, null);
		String url = uriBuilder.buildSerivceURI().toString();

		uriBuilder.setQuery(Constant.QueryParam.ACTION + Constant.Action.UPGRAGE);
		String upgradeURL = uriBuilder.buildSerivceURI().toString();

		uriBuilder.setQuery(Constant.QueryParam.ACTION + Constant.Action.FINISH_UPGRAGE);
		String finishUpgradeURL = uriBuilder.buildSerivceURI().toString();

		uriBuilder.setQuery(Constant.QueryParam.ACTION + Constant.Action.ROLLBACK);
		String rollbackURL = uriBuilder.buildSerivceURI().toString();

		String state = Util.findKeyValue(Util.fetchServiceInfoFromRancher(url, authToken), Constant.KEYWORD_STATE);

		switch (state) {
		case Constant.State.ACTIVE:
			upgradeService(upgradeURL, rollbackURL, finishUpgradeURL, rollbackURL, authToken);
			break;
		case Constant.State.UPGRADED:
			finishUpgrade(finishUpgradeURL, authToken);
			if (Util.pollingForState(url, authToken, actionTimeout, Constant.State.ACTIVE)) {
				upgradeService(upgradeURL, rollbackURL, finishUpgradeURL, rollbackURL, authToken);
			} else {
				rollbackService(rollbackURL, authToken);
			}
			break;
		case Constant.State.UPGRADING:
			rollbackService(rollbackURL, authToken);
			if (Util.pollingForState(url, authToken, actionTimeout, Constant.State.ACTIVE)) {
				upgradeService(upgradeURL, rollbackURL, finishUpgradeURL, url, authToken);
			} else {
				rollbackService(rollbackURL, authToken);
			}
			break;
		default:
			LOGGER.error("Failed to fetch service status");
			break;
		}

	}

	private void upgradeService(String upgradeURL, String rollbackURL, String finishUpgradeURL, String url,
			String authToken) {
		LOGGER.debug("Upgrading service...");
		Util.postToRancher(upgradeURL, Util.makePostData(dockerImageName), authToken);
		if (Util.pollingForState(url, authToken, actionTimeout, Constant.State.UPGRADED)) {
			finishUpgrade(finishUpgradeURL, authToken);
		} else {
			rollbackService(rollbackURL, authToken);
		}
	}

	private void rollbackService(String rollbackURL, String authToken) {
		LOGGER.debug("Rolling-back service...");
		Util.postToRancher(rollbackURL, Constant.EMPTY_POST_DATA, authToken);
	}

	private void finishUpgrade(String finishUpgradeURL, String authToken) {
		LOGGER.debug("Finishing upgrade service...");
		Util.postToRancher(finishUpgradeURL, Constant.EMPTY_POST_DATA, authToken);
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

package rancher;

import org.apache.log4j.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import rancher.util.Constant;
import rancher.util.Util;

@Mojo(name = "run")
public class RancherAPIConnectionMojo extends AbstractMojo {

	@Parameter(property = "rancher.host")
	private String rancherHost;
	
	@Parameter(property= "rancher.port")
	private String rancherPort;

	@Parameter(property = "rancher.username")
	private String username;

	@Parameter(property = "rancher.password")
	private String password;

	@Parameter(property = "service.upgrade.timeout")
	private Long upgradeTimeout;

	@Parameter(property = "docker.image")
	private String dockerImage;

	private static final Logger LOGGER = Logger.getLogger(RancherAPIConnectionMojo.class);

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		String url = Constant.PROTOCOL + rancherHost.replace("\\", "/");
		LOGGER.debug("RANCHER URL: " + url);

		String upgradeURL = url + Constant.Action.UPGRAGE;
		String finishUpgradeURL = url + Constant.Action.FINISH_UPGRAGE;
		String rollbackURL = url + Constant.Action.ROLLBACK;
		String authToken = Util.getBasicAuthToken(username, password);
		String state = Util.findKeyValue(Util.fetchServiceInfoFromRancher(url, authToken), Constant.KEYWORD_STATE);

		LOGGER.debug("Initial state = " + state);
		switch (state) {
		case Constant.State.ACTIVE:
			upgradeService(upgradeURL, rollbackURL, finishUpgradeURL, rollbackURL, authToken);
			break;
		case Constant.State.UPGRADED:
			finishUpgrade(finishUpgradeURL, authToken);
			if (Util.pollingForState(url, authToken, upgradeTimeout, Constant.State.ACTIVE)) {
				upgradeService(upgradeURL, rollbackURL, finishUpgradeURL, rollbackURL, authToken);
			} else {
				rollbackService(rollbackURL, authToken);
			}
			break;
		case Constant.State.UPGRADING:
			rollbackService(rollbackURL, authToken);
			if (Util.pollingForState(url, authToken, upgradeTimeout, Constant.State.ACTIVE)) {
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
		Util.postToRancher(upgradeURL, Util.makePostData(dockerImage), authToken);
		if (Util.pollingForState(url, authToken, upgradeTimeout, Constant.State.UPGRADED)) {
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
}

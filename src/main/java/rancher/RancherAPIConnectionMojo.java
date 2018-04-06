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

	@Parameter(property = "rancher.root")
	private String rancherUrl;

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
		rancherUrl = rancherUrl.replace("\\", "/");
		String url = Constant.PROTOCOL + rancherUrl;
		String upgradeURL = url + Constant.Action.UPGRAGE;
		String finishUpgradeURL = url + Constant.Action.FINISH_UPGRAGE;
		String rollbackURL = url + Constant.Action.ROLLBACK;
		String authToken = Util.getBasicAuthToken(username, password);
		String state = Util.findKeyValue(Util.fetchServiceInfoFromRancher(url, authToken), Constant.KEYWORD_STATE);
		LOGGER.debug("Initial state = " + state);
		switch (state) {
		case Constant.State.ACTIVE:
			Util.postToRancher(upgradeURL, Util.makePostData(dockerImage), authToken);
			if (Util.pollingForState(url, authToken, upgradeTimeout, Constant.State.UPGRADED)) {
				Util.postToRancher(finishUpgradeURL, "{}", authToken);
			} else {
				Util.postToRancher(rollbackURL, "{}", authToken);
			}
			break;
		case Constant.State.UPGRADED:
			Util.postToRancher(finishUpgradeURL, "{}", authToken);
			if (Util.pollingForState(url, authToken, upgradeTimeout, Constant.State.ACTIVE)) {
				Util.postToRancher(upgradeURL, Util.makePostData(dockerImage), authToken);
				if (Util.pollingForState(url, authToken, upgradeTimeout, Constant.State.UPGRADED)) {
					Util.postToRancher(finishUpgradeURL, "{}", authToken);
				} else {
					Util.postToRancher(rollbackURL, "{}", authToken);
				}
			} else {
				Util.postToRancher(rollbackURL, "{}", authToken);
			}

			break;
		case Constant.State.UPGRADING:
			Util.postToRancher(rollbackURL, "{}", authToken);
			if (Util.pollingForState(url, authToken, upgradeTimeout, Constant.State.ACTIVE)) {
				Util.postToRancher(upgradeURL, Util.makePostData(dockerImage), authToken);
			} else {
				Util.postToRancher(rollbackURL, "{}", authToken);
			}
			break;
		default:
			LOGGER.error("Failed to fetch service status");
			break;
		}
	}
}

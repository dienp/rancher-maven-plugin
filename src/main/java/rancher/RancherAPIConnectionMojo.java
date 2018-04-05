package rancher;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import rancher.util.Constant;
import rancher.util.Util;

@Mojo(name = "run")
public class RancherAPIConnectionMojo extends AbstractMojo {

	@Parameter(property = "rancher.root")
	String rancherRoot;

	@Parameter(property = "rancher.username")
	String username;

	@Parameter(property = "rancher.password")
	String password;

	@Parameter(property = "service.upgrade.timeout")
	Integer serviceTimeout;
	
	@Parameter(property = "service.upgrade.data")
	String upgradePostData;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		rancherRoot = Util.escapeSlash(rancherRoot);
		upgradePostData = Util.escapeSlash(upgradePostData);
		String upgradeURL = Util.constructURL(rancherRoot, Constant.Action.UPGRAGE);
		String finishUpgradeURL = Util.constructURL(rancherRoot, Constant.Action.FINISH_UPGRAGE);
		String getStateURL = Util.constructURL(rancherRoot, null);
		String authToken = Util.getBasicAuthToken(username, password);
		
		try {
			String state = Util.findKeyValue(fetchServiceInfoFromRancher(getStateURL, authToken), "state");
			switch (state) {
			case Constant.State.ACTIVE:
				postToRancher(upgradeURL, upgradePostData, authToken);
				break;
			case Constant.State.UPGRADED:
				postToRancher(finishUpgradeURL, "{}", authToken);
				while (state != Constant.State.UPGRADED) {
					state = Util.findKeyValue(fetchServiceInfoFromRancher(getStateURL, authToken), "state");
					Thread.sleep(3000);
				}
				postToRancher(upgradeURL, upgradePostData, authToken);
				break;
			default:
				getLog().error("Failed to fetch service status");
				break;
			}
		} catch (InterruptedException e1) {
			getLog().error("InterruptedException: ", e1);
			Thread.currentThread().interrupt();
		}
	}
	
	public static void main(String[] args) throws MojoExecutionException, MojoFailureException {
		new RancherAPIConnectionMojo().execute();
	}

	private String postToRancher(String url, String postData, String authToken) {
		getLog().info("IN - doSendRequest()");
		getLog().info("Send POST request to " + url);
		getLog().info("Post data = " + postData);
		String result = Util.connectToWebService(url, Constant.METHOD_POST, postData, authToken);
		getLog().info("Result : " + result);
		getLog().info("OUT - doSendRequest()");
		return result;
	}
	
	private String fetchServiceInfoFromRancher(String url, String authToken) {
		getLog().info("IN - getServiceInfo()");
		getLog().info("Send GET request to " + url);
		String result = Util.connectToWebService(url, Constant.METHOD_GET, null, authToken);
		getLog().info("Result : " + result);
		getLog().info("OUT - getServiceInfo()");
		return result;
	}

}

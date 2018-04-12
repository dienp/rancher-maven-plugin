package rancher.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.codehaus.plexus.util.StringUtils;

public class Util {

	private Util() {
		throw new IllegalArgumentException("Utility class");
	}

	private static final Logger LOGGER = Logger.getLogger(Util.class);

	public static String postToRancher(String url, String postData, String authToken) {
		LOGGER.debug("Send POST request to URL: " + url);
		LOGGER.debug("Post data: " + postData);
		return Util.connectToWebService(url, Constant.METHOD_POST, postData, authToken);
	}

	public static String fetchServiceInfoFromRancher(String url, String authToken) {
		LOGGER.debug("Send GET request to URL: " + url);
		return Util.connectToWebService(url, Constant.METHOD_GET, null, authToken);
	}

	public static String connectToWebService(String urlString, String method, String postBody, String authToken) {
		String result = null;
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(urlString).openConnection();
			con.setRequestProperty(Constant.AUTHORIZATION, authToken);
			con.setRequestProperty(Constant.CONTENT_TYPE, "application/json");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod(method);
			if (method.equalsIgnoreCase(Constant.METHOD_POST)) {
				con.getOutputStream().write(postBody.getBytes());
				con.getOutputStream().flush();
			}
			con.connect();
			if (con.getResponseCode() == 200 || con.getResponseCode() == 202) {
				result = readContentOfStream(con.getInputStream());
			} else {
				result = readContentOfStream(con.getErrorStream());
			}
		} catch (IOException e) {
			LOGGER.error("IOException: ", e);
		}
		return result;
	}

	public static String makePostData(String dockerImage) {
		LOGGER.debug("Docker image name = " + dockerImage);
		dockerImage = dockerImage.replace("\\", "/");
		String template = "{  \r\n" + "   \"inServiceStrategy\":{  \r\n" + "      \"launchConfig\":{  \r\n"
				+ "         \"imageUuid\":\"docker:" + dockerImage + "\"\r\n" + "      }\r\n" + "   },\r\n"
				+ "   \"toServiceStrategy\":null\r\n" + "}";
		return template;
	}

	public static boolean pollingForState(String url, String authToken, Long upgradeTimeout, String desiredState) {
		long startTimestamp = System.currentTimeMillis();
		LOGGER.debug("Polling...");
		while (startTimestamp + upgradeTimeout > (new Date()).getTime()) {
			try {
				Thread.sleep(Constant.REQUEST_INTERVAL);
			} catch (InterruptedException e) {
				LOGGER.error("InterruptedException: " + e);
				Thread.currentThread().interrupt();
			}

			String state = Util.findKeyValue(Util.fetchServiceInfoFromRancher(url, authToken), Constant.KEYWORD_STATE);
			if (StringUtils.isEmpty(state)) {
				LOGGER.error("Failed to fetch service state");
				return false;
			}

			LOGGER.debug("Service state = " + state);
			if (state.equalsIgnoreCase(desiredState)) {
				LOGGER.debug("Found desired state!");
				return true;
			}
		}
		LOGGER.debug("Upgrading service timeout!");
		return false;
	}

	public static String findKeyValue(String json, String key) {
		String value = null;
		Pattern pattern = Pattern.compile("\"" + key + "\":\"(\\w+)\"");
		Matcher m = pattern.matcher(json);
		if (m.find()) {
			value = m.group(1);
			LOGGER.debug("Found: " + key + " = " + value);
		} else {
			LOGGER.error("Couldn't find any instances of key =" + key);
		}
		return value;
	}

	public static String getBasicAuthToken(String username, String password) {
		// Basic + base64(username:password) = Basic Auth Token
		String encoding = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
		return "Basic " + encoding;
	}

	private static String readContentOfStream(InputStream inputStream) {
		StringBuilder stringBuilder = new StringBuilder();
		byte[] readBuffer = new byte[4096];
		int read;
		try {
			while ((read = inputStream.read(readBuffer)) != -1) {
				stringBuilder.append(new String(readBuffer, 0, read));
			}
		} catch (IOException e) {
			LOGGER.error("IOException: ", e);
		}
		return stringBuilder.toString();
	}
}

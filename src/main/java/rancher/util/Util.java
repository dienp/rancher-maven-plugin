package rancher.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class Util {

	private Util() {
		throw new IllegalStateException("Utility class");
	}

	private static final Logger logger = Logger.getLogger(Util.class);

	public static String connectToWebService(String urlString, String method, String postBody, String authToken) {
		logger.debug("IN - connectToWebService()");
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
			logger.error("IOException: ", e);
		}
		logger.debug("OUT - connectToWebService()");
		return result;
	}

	public static String constructURL(String rootPath, String action) {
		logger.debug("IN - constructURL()");
		StringBuilder sb = new StringBuilder();
		sb.append(rootPath);
		;
		if (action != null) {
			sb.append("?action=" + action);
		}
		logger.debug("Constructed URL = " + sb.toString());
		logger.debug("OUT - constructURL()");
		return sb.toString();
	}

	private static String readContentOfStream(InputStream inputStream) {
		logger.debug("IN - readContentOfStream()");
		StringBuilder stringBuilder = new StringBuilder();
		byte[] readBuffer = new byte[4096];
		int read;
		try {
			while ((read = inputStream.read(readBuffer)) != -1) {
				stringBuilder.append(new String(readBuffer, 0, read));
			}
		} catch (IOException e) {
			logger.error("IOException: ", e);
		}
		logger.debug("OUT - readContentOfStream()");
		return stringBuilder.toString();
	}

	public static String getBasicAuthToken(String username, String password) {
		String encoding = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
		return "Basic " + encoding;
	}

	public static String escapeSlash(String s) {
		logger.debug("IN - escapeSlash()");
		logger.debug("Input string = " + s);
		String result = s.replaceAll("\\", "/");
		logger.debug("Result string = " + result);
		logger.debug("OUT - escapeSlash()");
		return result;
	}

	public static String findKeyValue(String json, String key) {
		logger.debug("IN - findKeyValue()");
		String value = null;
		Pattern pattern = Pattern.compile("\"" + key + "\":\"(\\w+)\"");
		Matcher m = pattern.matcher(json);
		if (m.find()) {
			value = m.group(1);
			logger.debug("Found: " + key + " = " + value);
		} else {
			logger.error("Couldn't find any instances of key =" + key);
		}
		logger.debug("OUT - findKeyValue()");
		return value;
	}
}

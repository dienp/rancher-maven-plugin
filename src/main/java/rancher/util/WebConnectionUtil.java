package rancher.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class WebConnectionUtil {
	
	private static final int URL_TIMEOUT = 60000;
	public static final String connectToWebService(String urlString, String method, String postBody, List<String> headers) throws IOException
	{
		
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestMethod(method);
		con.setConnectTimeout(URL_TIMEOUT);
		for (String header: headers)
		{
			String[] headerSplit = header.split(":");
			con.setRequestProperty(headerSplit[0], headerSplit[1]);			
		}
		if (method.equalsIgnoreCase("post"))
		{
			//con.setRequestProperty("Content-length", "" + postBody.getBytes().length);
			//System.out.println(">>>" + postBody);
			con.getOutputStream().write(postBody.getBytes());
			con.getOutputStream().flush();
		}
		con.connect();
		
		System.out.println(con.getResponseCode());
		if (con.getResponseCode() == 200 || con.getResponseCode() == 202)
		{
			return readContentOfStream(con.getInputStream());
		}
		else
		{
			return readContentOfStream(con.getErrorStream());
		}
	}
	private static String readContentOfStream(InputStream inputStream) throws IOException {
		
		StringBuilder stringBuilder = new StringBuilder();
		byte[] readBuffer = new byte[4096];
		int read;
		while ((read = inputStream.read(readBuffer)) != -1)
		{
			stringBuilder.append(new String(readBuffer, 0 , read));
		}
		return stringBuilder.toString();
	}
}

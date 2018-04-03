package rancher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rancher.util.WebConnectionUtil;

public class RancherAPIConnection {
	
	
	public RancherAPIConnection()
	{
		
	}
	
	public static void main(String ... args) throws IOException
	{
		RancherAPIConnection rancherAPIConnection = new RancherAPIConnection();
		rancherAPIConnection.upgradeService();
	}

	private void upgradeService() throws IOException {
		List<String> headers = new ArrayList<>();
		headers.add("Authorization:Basic RTIzQzNFNDhFMzBBNEQ0MkVCREM6TGhYc2JRa0dhQkh0aGlINjJDTHhxVnJRUmR0R0g4dFdVUnBubkw5OA==");
		headers.add("Content-Type:application/json");
		String postData = "{\r\n" + 
				"	\"inServiceStrategy\":{\r\n" + 
				"		\"launchConfig\":{\r\n" + 
				"			\"tty\":true,\r\n" + 
				"			\"vcpu\":1,\r\n" + 
				"			\"imageUuid\":\"docker:10.3.65.122:5000/testhub:1.0.1\"\r\n" + 
				"		}\r\n" + 
				"	}, \"toServiceStrategy\":null\r\n" + 
				"}";
		System.out.println(WebConnectionUtil.connectToWebService("http://10.3.65.122:8080/v2-beta/projects/1a5/services/1s63/?action=upgrade", "POST", postData, headers));
	}
}

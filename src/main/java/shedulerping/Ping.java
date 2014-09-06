package shedulerping;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Ping implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 URL siteURL;
		try {
			siteURL = new URL("http://hugo-redon.rhcloud.com/");
			HttpURLConnection connection = (HttpURLConnection) siteURL
	                 .openConnection();
	         connection.setRequestMethod("GET");
	         connection.connect();
	         int code = connection.getResponseCode();
	         System.out.println("ping to server hugo-redon with code: " + code);
	         
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
	}

}

package watchcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Mail {
	public void send(String to, String body) throws IOException {
		URL url = new URL("https://play.devras.info/line/hook.php");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.addRequestProperty("User-Agent", "BukkitMailAPI");
		
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;");
		
		OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
		out.write("TO=" + URLEncoder.encode(to, "UTF-8") + "&BODY=" + URLEncoder.encode(body, "UTF-8"));
		out.close();
		
		
		con.connect();

		if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
			BufferedReader buf = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String str;
			while ((str = buf.readLine()) != null) {
				System.out.println(str);
			}
			//readLine();
			//System.out.println(buf);
			con = null;
		}else {
			System.out.println("Error " + con.getResponseCode());
		}
	}
}

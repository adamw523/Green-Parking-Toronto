package ca.tbcn.greenp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

public class Util {
	public static boolean fileExists(Context context, String f) {

		String[] filenames = context.fileList();
		for (String name : filenames) {
			if (name.equals(f)) {
				return true;
			}
		}

		return false;
	}
	
	public static String inputStreamToString(InputStream is) throws IOException {
		byte[] reader = new byte[is.available()];
		while (is.read(reader) != -1) {
		}
		
		return new String(reader);
	}
	
	public static String httpGet(String url) throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse res = httpclient.execute(httpGet);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        StringBuffer sb = new StringBuffer("");
        String line = "";
        String NL = System.getProperty("line.separator");
        while ((line = in.readLine()) != null) {
            sb.append(line + NL);
        }
        in.close();
        return sb.toString();
	}
}

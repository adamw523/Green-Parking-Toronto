package ca.tbcn.greenp;

import java.io.IOException;
import java.io.InputStream;

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
}

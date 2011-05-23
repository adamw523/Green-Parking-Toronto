/*
Copyright (c) 2011 Adam Wisniewski (http://adamw523.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

package ca.tbcn.greenp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class GreenParkingApp {
	public static final String TAG = "GreenParkingApp";

	private static ArrayList<Carpark> carparks = null;

	private static String JSON_FILE_NAME = "carparks.json";

	public static ArrayList<Carpark> getCarparks(Context context) {
		if (carparks == null) {
			loadCarparks(context);
		}

		return carparks;
	}
	
	/**
	 * Load carparks by reading JSON, then populating carparks
	 * 
	 * @param context
	 * @return
	 */
	private static void loadCarparks(Context context) {
		Log.i(TAG, "Loading JSON");
		// Get the JSON
		JSONObject json = GreenParkingApp.getCarparksJson(context);
		
		Log.i(TAG, "Populating carparksArray");

		// Parse the JSON
		carparks = new ArrayList<Carpark>();
		if (json != null) {
			try {
				JSONArray carparksJson = json.getJSONArray("carparks");
				for (int i = 0; i < carparksJson.length(); i++) {
					JSONObject carpark = carparksJson.getJSONObject(i);
					carparks.add(Carpark.fromJSON(carpark));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		Log.i(TAG, "Done populating carparks");
	}

	/**
	 * Load JSONObject from json file
	 * 
	 * @param context
	 * @return
	 */
	public static JSONObject getCarparksJson(Context context) {
		InputStream is = null;
		String jsonString = null;
		JSONObject json = null;

		try {
			
			jsonString = "{\"carparks\": " + getJsonFileContents(context) + "}";
			json = new JSONObject(jsonString);

		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
			}
		}

		return json;
	}
	
	/**
	 * Load json from stored file, create file if it's not there
	 * 
	 * @param context
	 * @return
	 * @throws IOException 
	 */
	public static String getJsonFileContents(Context context) throws IOException {
		if (!Util.fileExists(context, JSON_FILE_NAME)) {
			seedJsonFile(context);
		}
		
		FileInputStream fis = context.openFileInput(JSON_FILE_NAME);
		
		String contents = Util.inputStreamToString(fis);
		fis.close();
		
		return contents;
	}

	public static void seedJsonFile(Context context) throws IOException {
		// read contents of seed file
		InputStream is = context.getResources().openRawResource(R.raw.carparks);
		String seedContents = Util.inputStreamToString(is);
		
		updateJsonFile(context, seedContents);
	}

	public static void updateJsonFile(Context context, String contents) throws IOException {
		// write out to local file
		FileOutputStream fos = context.openFileOutput(JSON_FILE_NAME,
				Context.MODE_PRIVATE);
		fos.write(contents.getBytes());
		fos.close();
	}

}

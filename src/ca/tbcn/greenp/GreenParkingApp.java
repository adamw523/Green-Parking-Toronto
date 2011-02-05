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
	
	private static ArrayList<Carpark> carparks = new ArrayList<Carpark>();
	private static JSONObject json;
	
	/**
	 * Return list of carparks.. load if hasn't been populated yet
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<Carpark> cachedCarparks(Context context) {
		if(carparks.size() > 0) {
			return carparks;
		} else {
			return GreenParkingApp.loadCarparks(context);
		}
	}

	/**
	 * Load carparks by reading JSON, then populating carparks
	 * @param context
	 * @return
	 */
	private static ArrayList<Carpark> loadCarparks(Context context) {
		Log.i(TAG, "Loading JSON");
		JSONObject json = GreenParkingApp.readJson(context);
		Log.i(TAG, "Populating carparksArray");
	
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
		
		return carparks;
	}


	/**
	 * Load JSONObject from javascript file
	 * @param context
	 * @return
	 */
	public static JSONObject readJson(Context context) {
		InputStream is = null;
		String jsonString = null;
		JSONObject json = null;

		Log.i(TAG, "Reading JSON...");

		try {
			is = context.getResources().openRawResource(R.raw.carparks);
			byte[] reader = new byte[is.available()];
			while (is.read(reader) != -1) {
			}
			jsonString = "{\"carparks\": " + new String(reader) + "}";
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
			}
		}

		try {
			json = new JSONObject(jsonString);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}

		return json;
	}

	public static ArrayList<Carpark> getCarparks() {
		return carparks;
	}

	public static JSONObject getJson() {
		return json;
	}
	
}

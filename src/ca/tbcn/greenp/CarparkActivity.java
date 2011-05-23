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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CarparkActivity extends Activity {
	private static final String TAG = "CarparkActivity";
	private static final String CARPARK_INDEX = "carpark_index";
	private Carpark carpark = null;

	TextView tvTitle;
	TextView tvFacilityType;
	TextView tvRate;
	TextView tvCapacity;
	TextView tvStreetAddress;
	TextView tvUrl;
	WebView webView;
	Button loadWebsiteButton;

	// TextView tvRateDetails = (TextView)
	// findViewById(R.id.carpark_rate_details);

	// private GreenParkingItemizedOverlay currentLocationitemizedOverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.carpark);

		loadCarpark();
		initFields();
		updateFields();
		initButtons();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Log.i(TAG, "got a configuration change");
	}

	private void loadCarpark() {
		int carparkIndex = getIntent().getIntExtra(CARPARK_INDEX, -1);
		carpark = GreenParkingApp.getCarparks(this).get(carparkIndex);
	}

	private void initFields() {
		tvTitle = (TextView) findViewById(R.id.carpark_title);
		tvFacilityType = (TextView) findViewById(R.id.carpark_facility_type);
		tvRate = (TextView) findViewById(R.id.carpark_rate);
		tvCapacity = (TextView) findViewById(R.id.carpark_capacity);
		tvStreetAddress = (TextView) findViewById(R.id.carpark_street_address);
		tvUrl = (TextView) findViewById(R.id.carpark_url);
	}

	private void updateFields() {
		tvTitle.setText(carpark.getTitle());
		tvFacilityType.setText(carpark.getFacilityType());
		tvRate.setText(carpark.getRate());
		tvCapacity.setText(carpark.getCapacity());
		tvStreetAddress.setText(carpark.getStreetAddress());
		tvUrl.setText(carpark.getUrl());
		// tvRateDetails.setText(carpark.getRateDetails());
	}

	/**
	 * method to start this activity given a carpark index
	 * 
	 * @param c
	 * @param carparkIndex
	 */
	public static void callMe(Context c, Integer carparkIndex) {
		Intent intent = new Intent(c, CarparkActivity.class);
		intent.putExtra(CARPARK_INDEX, carparkIndex);
		c.startActivity(intent);
	}

	private void goDirections() {
		String url = "http://maps.google.com/maps?saddr=&daddr=" + carpark.directionsAddress();
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
	}

	private void goNavigateDriving() {
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri
				.parse("google.navigation:q=" + carpark.directionsAddress()));
		startActivity(intent);
	}

	private void goNavigateWalking() {
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri
				.parse("google.navigation:q=" + carpark.directionsAddress() + "&mode=w"));
		startActivity(intent);
	}

	/**
	 * commenting out for now, not using WebView
	 * 
	private void loadWebsite() {
		LinearLayout webWrapper = (LinearLayout) tvUrl.getParent(); 
		webWrapper.removeAllViews();
		WebView webView = new WebView(this);
		webWrapper.addView(webView);
		webView.setMinimumHeight(200);
		webView.setInitialScale(60);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.loadUrl(carpark.getUrl());
	}
	 */
	
	private void sendToBrowser() {
		String url = carpark.getUrl();
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
	}
	
	private void goStreetview() {
		// String url = "google.streetview:cbll=46.813812,-71.207378&cbp=1,99.56,,1,-5.27&mz=21";
		String url = "google.streetview:cbll=" + carpark.getLat() + "," + carpark.getLng();
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
	}
	
	private void initButtons() {
		ImageButton directionsButton = (ImageButton) findViewById(R.id.directions_button);
		directionsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDirectionsOptions();
			}
		});
		
		/*
		 * disabling for now
		loadWebsiteButton = (Button) findViewById(R.id.load_website_button);
		loadWebsiteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadWebsite();
			}
		});
		*/
		
		/*
		 * Making text button for now 
		ImageButton sendToBrowserButton = (ImageButton) findViewById(R.id.browser_button);
		sendToBrowserButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendToBrowser();
			}
		});
		*/

		Button websiteButton = (Button) findViewById(R.id.website_button);
		websiteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendToBrowser();
			}
		});

		Button streetviewButton = (Button) findViewById(R.id.streetview_button);
		streetviewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goStreetview();
			}
		});

	}

	private void showDirectionsOptions() {
		final CharSequence[] items = {"Driving Navigation",
				"Walking Navigation", "Get Directions" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Please Select");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) {
					goNavigateDriving();
				} else if (item == 1) {
					goNavigateWalking();
				} else if (item == 2) {
					goDirections();
				} else {
					Toast.makeText(getApplicationContext(), "WTF?",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

}
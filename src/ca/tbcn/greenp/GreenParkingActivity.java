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
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class GreenParkingActivity extends MapActivity {
	private static final String TAG = "MapActivity";
	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;

	private List<Overlay> mapOverlays;
	private GreenParkingItemizedOverlay itemizedOverlay;
	// private GreenParkingItemizedOverlay currentLocationitemizedOverlay;

	LocationManager locationManager;

	public void onResume() {
		super.onResume();
		myLocationOverlay.enableMyLocation();
	}

	@Override
	public void onPause() {
		super.onPause();
		myLocationOverlay.disableMyLocation();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initMap();
		createOverlays();
		initButtons();

		// Log.i(TAG, "onCreate here");

		loadAndDrawMarkers();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Log.i(TAG, "got a configuration change");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "id: " + item.getItemId());
		
		switch (item.getItemId()) {
		case R.id.refresh_carparks:
			try {
				String s = Util.httpGet(GreenParkingApp.JSON_URL);
				Log.d(TAG, "l: " + s.length());
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GreenParkingApp.refreshCarparks(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void loadAndDrawMarkers() {
		ProgressDialog dialog = ProgressDialog.show(this, "",
				"Loading. Please wait...", true);
		drawCarparks();
		dialog.dismiss();
	}

	private void initMap() {
		mapView = (MapView) findViewById(R.id.mapview);

		mapView.setOnTouchListener((new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.i(TAG, "Tapped somewhere");
				return false;
			}
		}));
		mapView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "Got a click on mapView");
			}
		});

		mapView.setBuiltInZoomControls(true);
		mapView.setClickable(true);
		mapView.getController().animateTo(
				new GeoPoint((int) (43.65250 * 1e6), (int) (-79.38167 * 1e6)));
		mapView.getController().setZoom(13);
	}

	private void initButtons() {
		ImageView reticle_iv = (ImageView) findViewById(R.id.center_reticle);
		reticle_iv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				centerOnMyLocation();
			}
		});
		// TODO: check for setting background
		// http://groups.google.com/group/android-developers/msg/0714e077e25d63a6
	}

	private void centerOnMyLocation() {
		if (myLocationOverlay.getMyLocation() == null) {
			myLocationOverlay.runOnFirstFix(new Runnable() {
				public void run() {
					centerOnMyLocation();
				}
			});
			Toast.makeText(this, "Getting Location", Toast.LENGTH_SHORT).show();
		} else {
			mapView.getController()
					.animateTo(myLocationOverlay.getMyLocation());
		}

		// loadCarparks();
	}

	/***
	 * Draw carparks on the carpark overlay
	 */
	private void drawCarparks() {
		// Log.i(TAG, "Drawing carparks");
		itemizedOverlay.clearOverlays();

		for (Carpark c : GreenParkingApp.getCarparks(this)) {
			GeoPoint point = new GeoPoint((int) (c.getLat() * 1e6), (int) (c
					.getLng() * 1e6));
			OverlayItem overlayitem = new OverlayItem(point, c.getRate(), c
					.getCapacity());
			itemizedOverlay.addOverlay(overlayitem);
		}
		itemizedOverlay.runPopulate();
		// Log.i(TAG, "Finished drawing carparks");
	}

	/***
	 * Create overlays for carparks and for current location
	 */
	private void createOverlays() {
		mapOverlays = mapView.getOverlays();
		Drawable map_marker = this.getResources().getDrawable(
				R.drawable.map_marker);
		itemizedOverlay = new GreenParkingItemizedOverlay(map_marker, mapView,
				this);
		mapOverlays.add(itemizedOverlay);

		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapOverlays.add(myLocationOverlay);
		myLocationOverlay.enableMyLocation();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
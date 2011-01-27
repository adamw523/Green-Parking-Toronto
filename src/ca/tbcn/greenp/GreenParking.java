package ca.tbcn.greenp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class GreenParking extends MapActivity {
	private static final String TAG = "MapActivity";
	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;

	private List<Overlay> mapOverlays;
	private GreenParkingItemizedOverlay itemizedOverlay;
	// private GreenParkingItemizedOverlay currentLocationitemizedOverlay;

	LocationManager locationManager;
	private LocationListener locationListener;
	
	private ArrayList<Carpark> carparksArray = new ArrayList<Carpark>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initMap();
		createOverlays();
		initButtons();

		loadAndDrawMarkers();
	}
	
	private void loadAndDrawMarkers() {
		ProgressDialog dialog = ProgressDialog.show(this, "", 
                "Loading. Please wait...", true);
		loadCarparks();
		drawCarparks();
		dialog.dismiss();
	}
	
	private void initMap() {
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setClickable(true);
		mapView.getController().animateTo(new GeoPoint(
				(int) (43.73 * 1e6),
				(int) (-79.381667 * 1e6)));
		mapView.getController().setZoom(13);
	}

	private void initButtons() {
		ImageView reticle_iv = (ImageView) findViewById(R.id.center_reticle);
		reticle_iv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				centerOnMyLocation();
			}
		});
		// TODO: check for setting background http://groups.google.com/group/android-developers/msg/0714e077e25d63a6
		
		/*
		Button draw_but = (Button) findViewById(R.id.Button01);
		Button json_but = (Button) findViewById(R.id.Button02);
		draw_but.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				drawCarparks();
			}
		});

		json_but.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadCarparks();
			}
		});
		*/
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

	private void createLocationManager() {
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location
				// provider.
				Log.i(TAG, "new location :)");
				// updateCurrentLocationMarker(location);
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}

	private void drawCarparks() {
		Log.i(TAG, "Drawing carparks");
		itemizedOverlay.clearOverlays();

		for(Carpark c : carparksArray) {
			GeoPoint point = new GeoPoint(
					(int) (c.getLat() * 1e6),
					(int) (c.getLng() * 1e6));
			OverlayItem overlayitem = new OverlayItem(point, "", "");
			itemizedOverlay.addOverlay(overlayitem);
		}
		itemizedOverlay.runPopulate();
		Log.i(TAG, "Finished drawing carparks");
	}
	
	private void loadCarparks() {
		Log.i(TAG, "Loading JSON");
		JSONObject json = readJson();
		Log.i(TAG, "Populating carparksArray");
	
		if (json != null) {
			try {
				JSONArray carparks = json.getJSONArray("carparks");
				for (int i = 0; i < carparks.length(); i++) {
					JSONObject carpark = carparks.getJSONObject(i);
					carparksArray.add(Carpark.fromJSON(carpark));

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.i(TAG, "Done populating carparksArray");
	}

	private JSONObject readJson() {
		InputStream is = null;
		String jsonString = null;
		JSONObject json = null;

		Log.i(TAG, "Reading JSON...");

		try {
			is = getResources().openRawResource(R.raw.carparks);
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

	private void createOverlays() {
		mapOverlays = mapView.getOverlays();
		Drawable map_marker = this.getResources().getDrawable(
				R.drawable.map_marker);
		itemizedOverlay = new GreenParkingItemizedOverlay(map_marker, this);
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
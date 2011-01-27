package ca.tbcn.greenp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class CarparkActivity extends Activity {
	private static final String TAG = "MapActivity";
	private static final String CARPARK_INDEX = "carpark_index";
	

	// private GreenParkingItemizedOverlay currentLocationitemizedOverlay;

	private ArrayList<Carpark> carparksArray = new ArrayList<Carpark>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.carpark);

		// loadCarpark...
		initButtons();
	}
	
	/**
	 * method to start this activity given a carpark index
	 * @param c
	 * @param carparkIndex
	 */
	public static void callMe(Context c, Integer carparkIndex) {
		Intent intent = new Intent(c, CarparkActivity.class);
		intent.putExtra(CARPARK_INDEX, carparkIndex);
		c.startActivity(intent);
	}

	private void initButtons() {
		ImageView reticle_iv = (ImageView) findViewById(R.id.center_reticle);
		reticle_iv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//centerOnMyLocation();
			}
		});
	}


}
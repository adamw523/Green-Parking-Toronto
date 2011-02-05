package ca.tbcn.greenp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import ca.tbcn.greenp.balloon.BalloonItemizedOverlay;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class GreenParkingItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {

	public static String TAG = "GreenParkingItemizedOverlay";
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context context;
	
	public GreenParkingItemizedOverlay(Drawable defaultMarker, MapView mapView, Context context) {
		super(boundCenterBottom(defaultMarker), mapView);
		// super(boundCenterBottom(defaultMarker));
		setBalloonBottomOffset(defaultMarker.getIntrinsicHeight());
		this.context = context;
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	}

	public void clearOverlays() {
	    mOverlays = new ArrayList<OverlayItem>();
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}
	
	@Override
	public int size() {
		return mOverlays.size();
	}
	
	@Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, false);
    }

	@Override
	protected boolean onBalloonTap(int index) {
		// OverlayItem item = mOverlays.get(index);
		Log.i(TAG, "Got into onBallonTap");
		
		CarparkActivity.callMe(context, index);
		return true;
	}
	
	public void runPopulate() {
		populate();
	}

}

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

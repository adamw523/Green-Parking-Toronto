package ca.tbcn.greenp;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class UpdateService extends IntentService {
	private static final String TAG = "UpdateService";
	private static final int NOTIFICATION_ID = 1;

	public UpdateService() {
		super("UpdateService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			String carparks = GreenParkingApp.fetchCarparksFromHWeb(this);
			GreenParkingApp.updateJsonFile(this, carparks);
			String ns = Context.NOTIFICATION_SERVICE;
			
			String msg = "Fetched carparks from web";
			
			// notification - what appears
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
			int icon = R.drawable.icon;
			Notification notification = new Notification(icon, msg, System.currentTimeMillis());
			
			// expanded message and Intent
			Context context = getApplicationContext();
			CharSequence contentTitle = "Toronto Green Parking";
			CharSequence contentText = "Updated Carparks, length: " + carparks.length();
			Intent notificationIntent = new Intent(this, GreenParkingActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
						
			mNotificationManager.notify(NOTIFICATION_ID, notification);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}

}

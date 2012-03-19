package dev.ronlemire.monitorservice;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class MonitorService extends IntentService {
	private static final String TAG = "MonitorService";
	private static int saveSettingsTableCount;
//	private ContentResolver cr;
//	private Resources res;
//	private Uri get_all_settings_uri;

	private NotificationManager notificationManager; 
	private Notification notification; 

	public MonitorService() {
		super(TAG);
		//Log.d(TAG, "MonitorService constructed");
	}

	@Override
	protected void onHandleIntent(Intent inIntent) {
//		cr = this.getContentResolver();
//		res = getResources();
//		get_all_settings_uri = Uri.parse(res
//				.getString(R.string.content_provider));
		
		this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		this.notification = new Notification(
				android.R.drawable.stat_notify_chat, "", 0);

		// Use ConfigProvider to get settings table record count
		Log.d(TAG, "Checking SettingsTableCount");
		int currentSettingsTableCount = getSettingsTableCount();

		if (MonitorService.saveSettingsTableCount != currentSettingsTableCount) {
			Log.d(TAG, "ConfigDB has changes");
			MonitorService.saveSettingsTableCount = currentSettingsTableCount;

			// Send user a notification that settings table count has changed
			sendMonitorServiceNotification(currentSettingsTableCount); //
		}
	}
	
	//******************************************************************************
	// Helper Methods
	//******************************************************************************
	private void sendMonitorServiceNotification(long dbRecCount) {
		// Create PI to start ConfigAdmin when user clicks on notification
		PendingIntent pendingIntent = PendingIntent.getActivity(this, -1,
				new Intent("dev.ronlemire.configadmin.ConfigAdminActivity"),
				PendingIntent.FLAG_UPDATE_CURRENT);

		this.notification.when = System.currentTimeMillis();
		this.notification.flags |= Notification.FLAG_AUTO_CANCEL;
		CharSequence notificationTitle = this
				.getText(R.string.msgNotificationTitle);
		CharSequence notificationSummary = this.getString(
				R.string.msgNotificationMessage, dbRecCount);

		// Attach PI to notification
		this.notification.setLatestEventInfo(this, notificationTitle,
				notificationSummary, pendingIntent); // <10>

		// Register notification with NotificationManager
		this.notificationManager.notify(0, this.notification);
		Log.d(TAG, "sendMonitorServiceNotification");
	}

	private int getSettingsTableCount() {
		int settingsTableCount = 0;
		
		try {
			// Setup config provider stuff
			ContentResolver cr = this.getContentResolver();
			Resources res = getResources();
			Uri get_all_settings_uri = Uri.parse(res
					.getString(R.string.content_provider));
			
			// Get all records from settings table
			Cursor c = cr.query(get_all_settings_uri, null, null, null, null);
			if (c == null) {
				settingsTableCount = -1;
			} else if (c.moveToFirst()) {
				settingsTableCount = c.getCount();
			} else {
				settingsTableCount = 0;
			}
		} catch (Exception ex) {
		}

		return settingsTableCount;
	}
}

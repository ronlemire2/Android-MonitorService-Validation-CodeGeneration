package dev.ronlemire.monitorservice;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MonitorServiceActivity extends Activity {
	private static final String TAG = "MonitorServiceActivity";
	private static final int DEFAULT_INTERVAL = 60000;
	private Context context;
	private Spinner intervalSpinner;
	private int intervalInMilliseconds = DEFAULT_INTERVAL; 
	private Intent monitorServiceIntent;
	private PendingIntent pendingMonitorServiceIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = this;

		// MonitorInterval
		intervalSpinner = (Spinner) findViewById(R.id.intervalSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.intervals, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		intervalSpinner.setAdapter(adapter);

		// StartService button
		Button btnStartMonitor = (Button) findViewById(R.id.btnStartMonitor);
		btnStartMonitor.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				intervalInMilliseconds = getIntervalInMilliseconds();
			    Log.d(TAG, "StartMonitor");

				// Create PI to start MonitorService
				monitorServiceIntent = new Intent(context, dev.ronlemire.monitorservice.MonitorService.class); 
				pendingMonitorServiceIntent = PendingIntent.getService(context,
						-1, monitorServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT); 
	
				// Setup alarm service to wake up and start service periodically
				AlarmManager alarmManager = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE); 
				alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
						System.currentTimeMillis(), intervalInMilliseconds, pendingMonitorServiceIntent); 
			}
		});

		// StopService button
		Button btnStopMonitor = (Button) findViewById(R.id.btnStopMonitor);
		btnStopMonitor.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			    Log.d(TAG, "StopMonitor");
			    
			    // Stop Monitor
				AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE); 
				alarmManager.cancel(pendingMonitorServiceIntent);
			}
		});
	}

	// *******************************************************************
	// Helper Methods
	// *******************************************************************
	private int getIntervalInMilliseconds() {
		int milliseconds = DEFAULT_INTERVAL;

		String[] intervalArray = getResources().getStringArray(
				R.array.intervals);
		String intervalString = intervalSpinner.getSelectedItem().toString();
		if (intervalString.equals(intervalArray[0])) {
			return milliseconds * 1;
		} else if (intervalString.equals(intervalArray[1])) {
			return milliseconds * 5;
		} else if (intervalString.equals(intervalArray[2])) {
			return milliseconds * 10;
		}

		return milliseconds;
	}
}
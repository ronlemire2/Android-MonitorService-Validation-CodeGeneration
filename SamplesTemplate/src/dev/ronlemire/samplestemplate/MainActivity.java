/*
 * Copyright 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.ronlemire.samplestemplate;

import dev.ronlemire.samplestemplate.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends ActionBarFragmentActivity {
	public static final String TAG = "MainActivity";
	private FragmentManager fm = getSupportFragmentManager();
	private Fragment sampleListFragment;
	private String sampleSelected;
	public static View sampleListView;
	private TextView messageTextView;

	private SampleListReceiver sampleListReceiver;
	private IntentFilter sampleListFilter;
 	private MessageFromSampleReceiver messageFromSampleReceiver;
 	private IntentFilter messageFromSampleFilter;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Test whether if enough display space for 2 panes. Only if tablet and
		// landscape.
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
				&& isTablet()) {
			// 2 panes - list on left - sample on right
			setContentView(R.layout.main_land);
			StartEmptyFragment(); // clear sample pane
		} else {
			// 1 pane - list and sample will overlay each other
			setContentView(R.layout.main);
		}

		// Save reference to List in order to replace later
		sampleListView = this.findViewById(R.id.list_replacer);
		messageTextView = (TextView) this.findViewById(R.id.etMessages);
		messageTextView.setEnabled(false);
		messageTextView.setTextColor(Color.parseColor("#000000")); // black

		// Receiver to catch message sent from List after a sample has been
		// picked
		sampleListReceiver = new SampleListReceiver();
		sampleListFilter = new IntentFilter(
				SampleListFragment.SAMPLE_LIST_FRAGMENT_BROADCAST_INTENT);

		// Messages from controls
 		messageFromSampleReceiver = new MessageFromSampleReceiver();
		messageFromSampleFilter = new IntentFilter(
				SampleFragment.MESSAGE_FROM_SAMPLE_INTENT);


		if (savedInstanceState == null) {
			StartSampleListFragment();
		}
      else {
			messageTextView.setText(savedInstanceState.getString("message"));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("message", messageTextView.getText().toString());
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(sampleListReceiver, sampleListFilter);
 		registerReceiver(messageFromSampleReceiver, messageFromSampleFilter);

	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(sampleListReceiver);
 		unregisterReceiver(messageFromSampleReceiver);

	}

	@Override
	public void onBackPressed() {
		ClearMessageBox();
		super.onBackPressed();
	}

	// *****************************************************************************
	// BroadcastReceivers
	// *****************************************************************************
	class SampleListReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Sample selected from list will be in Intent's extra data
			sampleSelected = intent
					.getStringExtra(SampleListFragment.SAMPLE_SELECTED);

			// Start sample fragment depending on which sample was selected
			ClearMessageBox();
  			PopFragmentBackStack();
			if (sampleSelected.equals("SampleFragment")) {
				StartSampleFragment();

			}
		}
	}
	
 	class MessageFromSampleReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String message = (String) intent
					.getSerializableExtra(SampleFragment.OUT_MESSAGE_KEY);
			messageTextView.setText(message);
		}
	}


	// *****************************************************************************
	// Start Fragments
	// *****************************************************************************
	public void StartSampleListFragment() {
		sampleListFragment = (Fragment) getSupportFragmentManager()
				.findFragmentById(R.id.list_replacer);
		sampleListFragment = SampleListFragment
				.newInstance("inMessageGoesHere");
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.list_replacer,
				sampleListFragment);
		fragmentTransaction.commit();
	}

	public void StartEmptyFragment() {
		Fragment sampleEmptyFragment = (Fragment) fm
				.findFragmentById(R.id.detail_replacer);
		sampleEmptyFragment = EmptyFragment.newInstance();

		fm.beginTransaction()
				.replace(R.id.detail_replacer, sampleEmptyFragment).commit();
	}
	
    public void StartSampleFragment() {
      if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
			   && isTablet()) {
		   Fragment sampleFragment = (Fragment) fm
				   .findFragmentById(R.id.detail_replacer);
		   sampleFragment = SampleFragment.newInstance("Sample");

		   FragmentTransaction transaction = fm.beginTransaction()
			   .replace(R.id.detail_replacer, sampleFragment);
			transaction.addToBackStack(null);
			transaction.commit();
	   } else {
		   SampleFragment sampleFragment = SampleFragment
				   .newInstance("Sample");
		   FragmentTransaction transaction = getSupportFragmentManager()
            .beginTransaction()
				.replace(MainActivity.sampleListView.getId(), sampleFragment);
			transaction.addToBackStack(null);
			transaction.commit();
	   }
   }	


	// *****************************************************************************
	// Action Bar
	// *****************************************************************************
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);

		// Calling super after populating the menu is necessary here to ensure
		// that the action bar helpers have a chance to handle this event.
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
			ReturnToSampleList();
			break;

		case R.id.menu_refresh:
			// Toast.makeText(this, "Fake refreshing...",
			// Toast.LENGTH_SHORT).show();
			ReturnToSampleList();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// *****************************************************************************
	// Helper methods
	// *****************************************************************************
	private boolean isTablet() {
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);

		int width = displayMetrics.widthPixels / displayMetrics.densityDpi;
		int height = displayMetrics.heightPixels / displayMetrics.densityDpi;

		double screenDiagonal = Math.sqrt(width * width + height * height);
		return (screenDiagonal >= 8.5);
	}

	private void ReturnToSampleList() {
		ClearMessageBox();
		PopFragmentBackStack();

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
				&& isTablet()) {
			StartSampleListFragment();
			StartEmptyFragment();
		} else {
			SampleListFragment sampleListFragment = SampleListFragment
					.newInstance("List");
			getSupportFragmentManager()
					.beginTransaction()
					.replace(MainActivity.sampleListView.getId(),
							sampleListFragment).commit();
		}
	}
	
	private void PopFragmentBackStack() {
		// http://stackoverflow.com/questions/5802141/is-this-the-right-way-to-clean-up-fragment-back-stack-when-leaving-a-deeply-nest
		fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	private void ClearMessageBox() {
		messageTextView.setText("");
	}
}

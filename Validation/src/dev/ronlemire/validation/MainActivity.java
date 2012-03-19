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

package dev.ronlemire.validation;

import dev.ronlemire.validation.R;

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
	private Fragment validationListFragment;
	private String validationSelected;
	public static View validationListView;
	private TextView messageTextView;

	private ValidationListReceiver validationListReceiver;
	private IntentFilter validationListFilter;
	private MessageFromHintsReceiver messageFromHintsReceiver;
	private MessageFromInputTypeReceiver messageFromInputTypeReceiver;
	private MessageFromRegexReceiver messageFromRegexReceiver;
	private IntentFilter messageFromHintsFilter;
	private IntentFilter messageFromInputTypeFilter;
	private IntentFilter messageFromRegexFilter;

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
		validationListView = this.findViewById(R.id.list_replacer);
		messageTextView = (TextView) this.findViewById(R.id.etMessages);
		messageTextView.setEnabled(false);
		messageTextView.setTextColor(Color.parseColor("#000000")); // black

		// Receiver to catch message sent from List after a sample has been
		// picked
		validationListReceiver = new ValidationListReceiver();
		validationListFilter = new IntentFilter(
				ValidationListFragment.VALIDATION_LIST_FRAGMENT_BROADCAST_INTENT);

		// Messages from controls
		messageFromHintsReceiver = new MessageFromHintsReceiver();
		messageFromHintsFilter = new IntentFilter(
				HintsFragment.MESSAGE_FROM_HINTS_INTENT);

		messageFromInputTypeReceiver = new MessageFromInputTypeReceiver();
		messageFromInputTypeFilter = new IntentFilter(
				InputTypeFragment.MESSAGE_FROM_INPUT_TYPE_INTENT);

		messageFromRegexReceiver = new MessageFromRegexReceiver();
		messageFromRegexFilter = new IntentFilter(
				RegexFragment.MESSAGE_FROM_REGEX_INTENT);

		if (savedInstanceState == null) {
			StartValidationListFragment();
		} else {
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
		registerReceiver(validationListReceiver, validationListFilter);
		registerReceiver(messageFromHintsReceiver, messageFromHintsFilter);
		registerReceiver(messageFromInputTypeReceiver,
				messageFromInputTypeFilter);
		registerReceiver(messageFromRegexReceiver, messageFromRegexFilter);

	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(validationListReceiver);
		unregisterReceiver(messageFromHintsReceiver);
		unregisterReceiver(messageFromInputTypeReceiver);
		unregisterReceiver(messageFromRegexReceiver);

	}

	@Override
	public void onBackPressed() {
		ClearMessageBox();
		super.onBackPressed();
	}

	// *****************************************************************************
	// BroadcastReceivers
	// *****************************************************************************
	class ValidationListReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Sample selected from list will be in Intent's extra data
			validationSelected = intent
					.getStringExtra(ValidationListFragment.VALIDATION_SELECTED);

			// Start sample fragment depending on which sample was selected
			ClearMessageBox();
			PopFragmentBackStack();
			if (validationSelected.equals("HintsFragment")) {
				StartHintsFragment();
			} else if (validationSelected.equals("InputTypeFragment")) {
				StartInputTypeFragment();
			} else if (validationSelected.equals("RegexFragment")) {
				StartRegexFragment();

			}
		}
	}

	class MessageFromHintsReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String message = (String) intent
					.getSerializableExtra(HintsFragment.OUT_MESSAGE_KEY);
			messageTextView.setText(message);
		}
	}

	class MessageFromInputTypeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String message = (String) intent
					.getSerializableExtra(InputTypeFragment.OUT_MESSAGE_KEY);
			messageTextView.setText(message);
		}
	}

	class MessageFromRegexReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String message = (String) intent
					.getSerializableExtra(RegexFragment.OUT_MESSAGE_KEY);
			messageTextView.setText(message);
		}
	}

	// *****************************************************************************
	// Start Fragments
	// *****************************************************************************
	public void StartValidationListFragment() {
		validationListFragment = (Fragment) getSupportFragmentManager()
				.findFragmentById(R.id.list_replacer);
		validationListFragment = ValidationListFragment
				.newInstance("inMessageGoesHere");
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.list_replacer, validationListFragment);
		fragmentTransaction.commit();
	}

	public void StartEmptyFragment() {
		Fragment validationEmptyFragment = (Fragment) fm
				.findFragmentById(R.id.detail_replacer);
		validationEmptyFragment = EmptyFragment.newInstance();

		fm.beginTransaction()
				.replace(R.id.detail_replacer, validationEmptyFragment)
				.commit();
	}

	public void StartHintsFragment() {
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
				&& isTablet()) {
			Fragment hintsFragment = (Fragment) fm
					.findFragmentById(R.id.detail_replacer);
			hintsFragment = HintsFragment.newInstance("Hints");

			FragmentTransaction transaction = fm.beginTransaction().replace(
					R.id.detail_replacer, hintsFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		} else {
			HintsFragment hintsFragment = HintsFragment.newInstance("Hints");
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction().replace(
							MainActivity.validationListView.getId(),
							hintsFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}

	public void StartInputTypeFragment() {
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
				&& isTablet()) {
			Fragment inputTypeFragment = (Fragment) fm
					.findFragmentById(R.id.detail_replacer);
			inputTypeFragment = InputTypeFragment.newInstance("InputType");

			FragmentTransaction transaction = fm.beginTransaction().replace(
					R.id.detail_replacer, inputTypeFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		} else {
			InputTypeFragment inputTypeFragment = InputTypeFragment
					.newInstance("InputType");
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction().replace(
							MainActivity.validationListView.getId(),
							inputTypeFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}

	public void StartRegexFragment() {
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
				&& isTablet()) {
			Fragment regexFragment = (Fragment) fm
					.findFragmentById(R.id.detail_replacer);
			regexFragment = RegexFragment.newInstance("Regex");

			FragmentTransaction transaction = fm.beginTransaction().replace(
					R.id.detail_replacer, regexFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		} else {
			RegexFragment regexFragment = RegexFragment.newInstance("Regex");
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction().replace(
							MainActivity.validationListView.getId(),
							regexFragment);
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
			ReturnToValidationList();
			break;

		case R.id.menu_refresh:
			// Toast.makeText(this, "Fake refreshing...",
			// Toast.LENGTH_SHORT).show();
			ReturnToValidationList();
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

	private void ReturnToValidationList() {
		ClearMessageBox();
		PopFragmentBackStack();

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
				&& isTablet()) {
			StartValidationListFragment();
			StartEmptyFragment();
		} else {
			ValidationListFragment validationListFragment = ValidationListFragment
					.newInstance("List");
			getSupportFragmentManager()
					.beginTransaction()
					.replace(MainActivity.validationListView.getId(),
							validationListFragment).commit();
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

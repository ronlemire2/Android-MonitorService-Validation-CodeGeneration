


===========================================================================================================================
3/15/2012
This template needs to be updated for List
   string listName = "Sample";
   string listNameCaps = "SAMPLE";
   string listNameLower = "sample";
===========================================================================================================================




0=============================================================================================================================

	private SampleListReceiver sampleListReceiver;
	private IntentFilter sampleListFilter;
  	private MessageFromSampleReceiver messageFromSampleReceiver;
 	private IntentFilter messageFromSampleFilter;

1=============================================================================================================================

		// Receiver to catch message sent from List after a sample has been picked
		sampleListReceiver = new SampleListReceiver();
		sampleListFilter = new IntentFilter(
				SampleListFragment.SAMPLE_LIST_FRAGMENT_BROADCAST_INTENT);

		// Messages from controls
 		messageFromSampleReceiver = new MessageFromSampleReceiver();
		messageFromSampleFilter = new IntentFilter(
				SampleFragment.MESSAGE_FROM_SAMPLE_INTENT);

	
2=============================================================================================================================

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

3=============================================================================================================================

 	class MessageFromSampleReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String message = (String) intent
					.getSerializableExtra(SampleFragment.OUT_MESSAGE_KEY);
			messagesEditText.setText(message);
		}
	}


4=============================================================================================================================


public void StartSampleFragment() {
	if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
			&& isTablet()) {
		Fragment sampleFragment = (Fragment) fm
				.findFragmentById(R.id.sample_replacer);
		sampleFragment = SampleFragment.newInstance("Sample");

		fm.beginTransaction()
			.replace(R.id.sample_replacer, sampleFragment)
			.commit();
	} else {
		SampleFragment sampleFragment = SampleFragment
				.newInstance("Sample");
		getSupportFragmentManager().beginTransaction()
				.replace(MainActivity.sampleListView.getId(), sampleFragment)
				.commit();
	}
}	

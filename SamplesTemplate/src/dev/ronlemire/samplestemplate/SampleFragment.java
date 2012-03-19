package dev.ronlemire.samplestemplate;

import dev.ronlemire.samplestemplate.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SampleFragment extends Fragment {
	public static final String TAG = "SampleFragment";
	public static final String MESSAGE_FROM_SAMPLE_INTENT = "dev.ronlemire.commoncontrols.SAMPLE_BROADCAST";
   public static final String OUT_MESSAGE_KEY = "OutMessage";
	public static final String IN_MESSAGE_KEY = "InMessage";
	private View rootView;
	private Intent intent;
	private String inMessage;
  	private boolean saveCBstate;
	
	// *****************************************************************************
	// Singleton method used to pass variables to a new Fragment instance.
	// *****************************************************************************
	public static SampleFragment newInstance(String inMessage) {
		SampleFragment sampleFragment = new SampleFragment();
		Bundle argumentsBundle = new Bundle(); // create a new Bundle
		argumentsBundle.putString(IN_MESSAGE_KEY, inMessage);
		sampleFragment.setArguments(argumentsBundle);
		return sampleFragment;
	}	

	// *****************************************************************************
	// Fragment Life Cycle events
	// *****************************************************************************
	@Override
	public void onCreate(Bundle argumentsBundle) {
		super.onCreate(argumentsBundle);
		intent = new Intent(MESSAGE_FROM_SAMPLE_INTENT);
		this.inMessage = getArguments().getString(IN_MESSAGE_KEY);
      Log.d(TAG, inMessage);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.sample_fragment, null);

		CheckBox sampleCB = (CheckBox) rootView.findViewById(R.id.sampleCB);
		saveCBstate = sampleCB.isChecked();

		sampleCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				saveCBstate = isChecked;
				SendMessage("SampleCB is "
						+ (isChecked ? "checked" : "unchecked"));
			}
		});

		return rootView;
	}		
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			// Restore last state for checked position.
         //http://developer.android.com/guide/topics/fundamentals/fragments.html
			saveCBstate = savedInstanceState.getBoolean("sampleCB");
			SendMessage("Sample checkbox ***saved state*** "
					+ (saveCBstate ? "checked" : "not checked"));
		}
    }
	
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save state here.
        //http://developer.android.com/guide/topics/fundamentals/fragments.html
        outState.putBoolean("sampleCB", saveCBstate);
    }   	

	// *****************************************************************************
	// Helper Methods
	// *****************************************************************************
	private void SendMessage(String message) {
		intent.putExtra(OUT_MESSAGE_KEY, message);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getActivity().sendBroadcast(intent);	
	}
}

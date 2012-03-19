package dev.ronlemire.validation;

import dev.ronlemire.validation.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InputTypeFragment extends Fragment {
	public static final String TAG = "InputTypeFragment";
	public static final String MESSAGE_FROM_INPUT_TYPE_INTENT = "dev.ronlemire.validation.INPUT_TYPE_BROADCAST";
   public static final String OUT_MESSAGE_KEY = "OutMessage";
	public static final String IN_MESSAGE_KEY = "InMessage";
	private View rootView;
	private Intent intent;
	private String inMessage;
	
	// *****************************************************************************
	// Singleton method used to pass variables to a new Fragment instance.
	// *****************************************************************************
	public static InputTypeFragment newInstance(String inMessage) {
		InputTypeFragment inputTypeFragment = new InputTypeFragment();
		Bundle argumentsBundle = new Bundle(); // create a new Bundle
		argumentsBundle.putString(IN_MESSAGE_KEY, inMessage);
		inputTypeFragment.setArguments(argumentsBundle);
		return inputTypeFragment;
	}	

	// *****************************************************************************
	// Fragment Life Cycle events
	// *****************************************************************************
	@Override
	public void onCreate(Bundle argumentsBundle) {
		super.onCreate(argumentsBundle);
		intent = new Intent(MESSAGE_FROM_INPUT_TYPE_INTENT);
		this.inMessage = getArguments().getString(IN_MESSAGE_KEY);
      Log.d(TAG, inMessage);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.input_type_fragment, null);
		return rootView;
	}		
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
           // Restore last state here.
           //http://developer.android.com/guide/topics/fundamentals/fragments.html

        }
    }
	
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save state here.
        //http://developer.android.com/guide/topics/fundamentals/fragments.html

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

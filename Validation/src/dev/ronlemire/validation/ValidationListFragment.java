package dev.ronlemire.validation;

import dev.ronlemire.validation.R;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ValidationListFragment extends Fragment {
	public static final String VALIDATION_LIST_FRAGMENT_BROADCAST_INTENT = "dev.ronlemire.validation.VALIDATION_LIST_FRAGMENT_BROADCAST";
	public static final String VALIDATION_SELECTED = "validationSelected";
	public static final String IN_MESSAGE_KEY = "InMessage";
	public static final String OUT_MESSAGE_KEY = "ReturnMessage";
	private View rootView;
//	private String inMessage;
	private String fragments[] = null;
	
	// *****************************************************************************
	// Singleton method used to pass variables to a new Fragment instance.
	// *****************************************************************************
	public static ValidationListFragment newInstance(String inMessage) {
		ValidationListFragment listFragment = new ValidationListFragment();
		Bundle argumentsBundle = new Bundle(); // create a new Bundle
		argumentsBundle.putString(IN_MESSAGE_KEY, inMessage);
		listFragment.setArguments(argumentsBundle);
		return listFragment;
	}

	// *****************************************************************************
	// Fragment Life Cycle events
	// *****************************************************************************
	@Override
	public void onCreate(Bundle argumentsBundle) {
		super.onCreate(argumentsBundle);
//		this.inMessage = getArguments().getString(IN_MESSAGE_KEY);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.validation_list_fragment, null);
		// context = rootView.getContext();

	   Resources res = getResources();
      fragments = res.getStringArray(R.array.list_fragments);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(), 
         R.array.list_titles,	android.R.layout.simple_list_item_single_choice);

    	ListView lvLeftPanel = (ListView) rootView.findViewById(R.id.lvValidationList);
    	lvLeftPanel.setAdapter(adapter);
      lvLeftPanel.setItemsCanFocus(false);
		lvLeftPanel.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    	lvLeftPanel.setOnItemClickListener(new OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view,
    			int position, long id) {
    			Intent intent = new Intent(VALIDATION_LIST_FRAGMENT_BROADCAST_INTENT);
    			intent.putExtra(VALIDATION_SELECTED, fragments[position]);
    			getActivity().sendBroadcast(intent);
    		}
    	});		
		
		return rootView;
	}
}
package dev.ronlemire.validation;

import dev.ronlemire.validation.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegexFragment extends Fragment {
	public static final String TAG = "RegexFragment";
	public static final String MESSAGE_FROM_REGEX_INTENT = "dev.ronlemire.validation.REGEX_BROADCAST";
	public static final String OUT_MESSAGE_KEY = "OutMessage";
	public static final String IN_MESSAGE_KEY = "InMessage";
	public static String newline = System.getProperty("line.separator");
	private View rootView;
	private Intent intent;
	private String inMessage;
	private String errorMessage;
	private EditText etValidationConsole;
	private EditText etFirstName;
	private EditText etEmail;
	private EditText etPhone;
	private EditText etZipCode;
	private EditText etDate;
	private String asterisk;
	private String errRequired;
	private String errFormat;
	private String lblFirstName;
	private String lblEmail;
	private String lblPhone;
	private String lblZipCode;
	private String lblDate;

	// *****************************************************************************
	// Singleton method used to pass variables to a new Fragment instance.
	// *****************************************************************************
	public static RegexFragment newInstance(String inMessage) {
		RegexFragment regexFragment = new RegexFragment();
		Bundle argumentsBundle = new Bundle(); // create a new Bundle
		argumentsBundle.putString(IN_MESSAGE_KEY, inMessage);
		regexFragment.setArguments(argumentsBundle);
		return regexFragment;
	}

	// *****************************************************************************
	// Fragment Life Cycle events
	// *****************************************************************************
	@Override
	public void onCreate(Bundle argumentsBundle) {
		super.onCreate(argumentsBundle);
		intent = new Intent(MESSAGE_FROM_REGEX_INTENT);
		this.inMessage = getArguments().getString(IN_MESSAGE_KEY);
		Log.d(TAG, inMessage);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.regex_fragment, null);

		errorMessage = "";
		etValidationConsole = (EditText) rootView
				.findViewById(R.id.etErrorMessage);
		etValidationConsole.setFocusable(false);
		etValidationConsole.setFocusableInTouchMode(false);
		etFirstName = (EditText) rootView.findViewById(R.id.etFirstName);
		etEmail = (EditText) rootView.findViewById(R.id.etEmail);
		etPhone = (EditText) rootView.findViewById(R.id.etPhone);
		etZipCode = (EditText) rootView.findViewById(R.id.etZipCode);
		etDate = (EditText) rootView.findViewById(R.id.etDate);
		asterisk = rootView.getResources().getString(R.string.asterisk);
		errRequired = rootView.getResources().getString(R.string.err_required);
		errFormat = rootView.getResources().getString(R.string.err_format);
		lblFirstName = rootView.getResources().getString(R.string.label_firstname);
		lblEmail = rootView.getResources().getString(R.string.label_email);
		lblPhone = rootView.getResources().getString(R.string.label_phone);
		lblZipCode = rootView.getResources().getString(R.string.label_zipcode);
		lblDate = rootView.getResources().getString(R.string.label_date);

		// Save button
		Button btnSave = (Button) rootView.findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "SaveButton");
				if (save()) {
					Toast.makeText(getActivity(), "Form valid",
							Toast.LENGTH_SHORT).show();
					// setResult(RESULT_OK, null);
					// exitActivity();
				} else {
					etValidationConsole.setText(errorMessage);
				}
			}
		});

		// Cancel button
		Button btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "CancelButton");
				ClearForm();
			}
		});

		return rootView;
	}

	protected boolean save() {
		Log.d(TAG, "save() method");

		boolean saved = true;

		if (validated()) {
			// TODO: use ContentProvider to save data
		} else {
			saved = false;
		}

		return saved;
	}

	protected boolean validated() {
		Log.d(TAG, "validated() method");

		boolean validated = true;

		errorMessage = "";

		// FirstName
		if (!RegexValidate.hasText(etFirstName)) {
			errorMessage += String.format("%1$s %2$s %3$s", lblFirstName, errRequired, newline);
			validated = false;
		}

		// Email
		if (!RegexValidate.hasText(etEmail)) {
			errorMessage += String.format("%1$s %2$s %3$s", lblEmail, errRequired, newline);
			validated = false;
		} else if (!RegexValidate.isEmailAddress(etEmail, true)) {
			errorMessage += String.format("%1$s %2$s %3$s", lblEmail, errFormat, newline);
			validated = false;
		}

		// Phone
		if (!RegexValidate.hasText(etPhone)) {
			errorMessage += String.format("%1$s %2$s %3$s", lblPhone, errRequired, newline);
			validated = false;
		} else if (!RegexValidate.isPhoneNumber(etPhone, true)) {
			errorMessage += String.format("%1$s %2$s %3$s", lblPhone, errFormat, newline);
			validated = false;
		}

		// ZipCode
		if (!RegexValidate.hasText(etZipCode)) {
			errorMessage += String.format("%1$s %2$s %3$s", lblZipCode,	errRequired, newline);
			validated = false;
		} else if (!RegexValidate.isPostalCode(etZipCode, true)) {
			errorMessage += String.format("%1$s %2$s %3$s", lblZipCode,	errFormat, newline);
			validated = false;
		}

		// Date
		if (!RegexValidate.hasText(etDate)) {
			errorMessage += String.format("%1$s %2$s %3$s", lblDate, errRequired, newline);
			validated = false;
		} else if (!RegexValidate.isDate(etDate)) {
			errorMessage += String.format("%1$s %2$s %3$s", lblDate, errFormat, newline);
			validated = false;
		}

		return validated;
	}

	private void ClearForm() {
		errorMessage = "";
		etValidationConsole.setText("");
		etFirstName.setText("");
		etEmail.setText("");
		etPhone.setText("");
		etZipCode.setText("");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			// Restore last state here.
			// http://developer.android.com/guide/topics/fundamentals/fragments.html

		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save state here.
		// http://developer.android.com/guide/topics/fundamentals/fragments.html

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

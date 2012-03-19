package dev.ronlemire.validation;

import java.util.regex.Pattern;

import android.graphics.Color;
import android.util.Log;
import android.widget.EditText;

// ****************************************************************************
// Most code from: http://www.zunisoft.com/2009/08/android-input-validation.html
// Added: isDate
//*****************************************************************************
public class RegexValidate {
	private static final String CLASS_TAG = "Validate";

	public static final int VALID_TEXT_COLOR = Color.BLACK;
	public static final int INVALID_TEXT_COLOR = Color.RED;

	public static boolean isEmailAddress(EditText editText, boolean required) {
		Log.d(CLASS_TAG, "isEmailAddress()");

		String regex = editText.getResources().getString(R.string.regex_email);

		return isValid(editText, regex);
	}

	public static boolean isPhoneNumber(EditText editText, boolean required) {
		Log.d(CLASS_TAG, "isPhoneNumber()");

		String regex = editText.getResources().getString(R.string.regex_phone);

		return isValid(editText, regex);
	}

	public static boolean isPostalCode(EditText editText, boolean required) {
		Log.d(CLASS_TAG, "isPostalCode()");

		String regex = editText.getResources().getString(
				R.string.regex_postal_code);

		return isValid(editText, regex);
	}

	public static boolean isDate(EditText editText) {
		Log.d(CLASS_TAG, "isDate()");

		String regex = editText.getResources().getString(
				R.string.regex_date);

		return isValid(editText, regex);
	}

	public static boolean isValid(EditText editText, String regex) {
		Log.d(CLASS_TAG, "isValid()");

		boolean validated = true;
		String text = editText.getText().toString().trim();

		editText.setTextColor(VALID_TEXT_COLOR);

		if (validated) {
			if (!Pattern.matches(regex, text)) {
				editText.setTextColor(INVALID_TEXT_COLOR);
				validated = false;
			}
		}

		return validated;
	}

	public static boolean hasText(EditText editText) {
		Log.d(CLASS_TAG, "hasText()");

		boolean validated = true;

		String text = editText.getText().toString().trim();

		if (text.length() == 0) {
			editText.setText(text);
			validated = false;
		}

		return validated;
	}
}
package dev.ronlemire.personprovider;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class TesterActivity extends Activity implements ITesterReportBack {
	public static final String tag = "Tester";
	private Tester tester = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		tester = new Tester(this, this);
		tester.showPerson();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(tag, item.getTitle().toString());
		if (item.getItemId() == R.id.menu_display_table) {
			tester.showPerson();
			return true;
		}
		if (item.getItemId() == R.id.menu_display_one) {
			tester.showOnePerson();
			return true;
		}
		if (item.getItemId() == R.id.menu_add) {
			tester.addPerson();
			return true;
		}
		if (item.getItemId() == R.id.menu_update) {
			tester.updatePerson();
			return true;
		}
		if (item.getItemId() == R.id.menu_delete) {
			tester.removePerson();
			return true;
		}
		if (item.getItemId() == R.id.menu_da_clear) {
			TextView tv = (TextView) this.findViewById(R.id.text2);
			tv.setText("");
			return true;
		}
		return true;
	}

	@Override
	public void reportBack(String tag, String message) {
		this.appendText(tag + ": " + message);
		Log.d(tag, message);
	}

	private void appendText(String s) {
		TextView tv = (TextView) this.findViewById(R.id.text2);
		tv.setText(tv.getText() + "\n" + s);
		Log.d(tag, s);
	}

	@Override
	public void clear() {
		TextView tv = (TextView) this.findViewById(R.id.text2);
		tv.setText("");
	}

}

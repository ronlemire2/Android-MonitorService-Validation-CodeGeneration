package dev.ronlemire.personprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class Tester extends TesterBase {
	private static String tag = "Tester";
	private Context testerContext;
	private static String currentPersonId;
	private ContentResolver cr;
	private ITesterReportBack rb;
	private Resources res;

	public Tester(Context ctx, ITesterReportBack rb) {
		super(ctx);
		this.cr = ctx.getContentResolver();
		testerContext = ctx;
		this.rb = rb;
		res = testerContext.getResources();
	}

	// *****************************************************************************
	// Show All Person on a separate thread
	// *****************************************************************************
	public void showPerson() {
		reportClear();
		new ShowAllPersonAsyncTask("", testerContext,
				new AllPersonLoadedListener()).execute();
	}

	public interface AllPersonLoadedListenerInterface {
		public void onAllPersonLoaded(Cursor cursor);
	}

	public class ShowAllPersonAsyncTask extends
			AsyncTask<Object, Object, String> {
		String idParameterString;
		private Cursor cursor;

		// listener for retrieved PersonList
		private AllPersonLoadedListener allPersonLoadedListener;

		public ShowAllPersonAsyncTask(
				String idParameterString,
				Context context,
				dev.ronlemire.personprovider.Tester.AllPersonLoadedListenerInterface allPersonLoadedListener) {
			this.idParameterString = idParameterString;
			this.allPersonLoadedListener = (AllPersonLoadedListener) allPersonLoadedListener;
		}

		// Call PersonService to get PersonList
		@Override
		protected String doInBackground(Object... params) {
			try {
				Uri get_all_person_uri = Uri.parse(res
						.getString(R.string.person_provider));

				cursor = cr.query(get_all_person_uri, null, null, null, null);

			} catch (Exception e) {
				Log.e("log_tag", "Error querying DB " + e.toString());
			}

			return "";
		}

		// executed back on the UI thread after the city name loads
		protected void onPostExecute(String domainString) {
			allPersonLoadedListener.onAllPersonLoaded(cursor);
		}
	}

	// listens for person loaded in background task
	public class AllPersonLoadedListener implements
			AllPersonLoadedListenerInterface {

		public AllPersonLoadedListener() {
		}

		@Override
		public void onAllPersonLoaded(Cursor cursor) {

			int numberOfRecords = cursor.getCount();
			reportString("Num of Person: " + numberOfRecords);

		 
			int iId = cursor
					.getColumnIndex(PersonProviderMetaData.PersonTableMetaData.PERSON_ID);
		 
			int iFirstName = cursor
					.getColumnIndex(PersonProviderMetaData.PersonTableMetaData.PERSON_FIRSTNAME);
		 
			int iLastName = cursor
					.getColumnIndex(PersonProviderMetaData.PersonTableMetaData.PERSON_LASTNAME);
		 
			int iEmail = cursor
					.getColumnIndex(PersonProviderMetaData.PersonTableMetaData.PERSON_EMAIL);

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				// Gather values
				String Id = cursor.getString(iId);
				currentPersonId = Id;
		 
	         String FirstName = cursor.getString(iFirstName);
		 
	         String LastName = cursor.getString(iLastName);
		 
	         String Email = cursor.getString(iEmail);
				

				// Report or log the row
				StringBuffer cbuf = new StringBuffer(Id);
			 
		      cbuf.append(", ").append(Id);
			 
		      cbuf.append(", ").append(FirstName);
			 
		      cbuf.append(", ").append(LastName);
			 
		      cbuf.append(", ").append(Email);
				reportString(cbuf.toString());
			}

			// Close the cursor
			// ideally this should be done in
			// a finally block.
			cursor.close();
		}
	}

	// *****************************************************************************
	// Show One Person on a separate thread
	// *****************************************************************************
	public void showOnePerson() {
		reportClear();
		new ShowOnePersonAsyncTask("", testerContext,
				new OnePersonLoadedListener()).execute();
	}

	public interface OnePersonLoadedListenerInterface {
		public void onOnePersonLoaded(Cursor cursor);
	}

	public class ShowOnePersonAsyncTask extends
			AsyncTask<Object, Object, String> {
		String idParameterString;
		private Cursor cursor;

		// listener for retrieved PersonList
		private OnePersonLoadedListener onePersonLoadedListener;

		public ShowOnePersonAsyncTask(
				String idParameterString,
				Context context,
				dev.ronlemire.personprovider.Tester.OnePersonLoadedListenerInterface onePersonLoadedListener) {
			this.idParameterString = idParameterString;
			this.onePersonLoadedListener = (OnePersonLoadedListener) onePersonLoadedListener;
		}

		// Call PersonService to get PersonList
		@Override
		protected String doInBackground(Object... params) {
			Uri get_all_person_uri = Uri.parse(res
					.getString(R.string.person_provider));
			Uri showOneUri = Uri.withAppendedPath(get_all_person_uri,
					currentPersonId);
			cursor = cr.query(showOneUri, null, null, null, null);

			return "";
		}

		// executed back on the UI thread after the city name loads
		protected void onPostExecute(String domainString) {
			onePersonLoadedListener.onOnePersonLoaded(cursor);
		}
	}

	// listens for person loaded in background task
	public class OnePersonLoadedListener implements
			OnePersonLoadedListenerInterface {

		public OnePersonLoadedListener() {
		}

		@Override
		public void onOnePersonLoaded(Cursor cursor) {

			// Report how many rows have been read
			int numberOfRecords = cursor.getCount();

			// Display each Person
		 
			int iId = cursor
					.getColumnIndex(PersonProviderMetaData.PersonTableMetaData.PERSON_ID);
		 
			int iFirstName = cursor
					.getColumnIndex(PersonProviderMetaData.PersonTableMetaData.PERSON_FIRSTNAME);
		 
			int iLastName = cursor
					.getColumnIndex(PersonProviderMetaData.PersonTableMetaData.PERSON_LASTNAME);
		 
			int iEmail = cursor
					.getColumnIndex(PersonProviderMetaData.PersonTableMetaData.PERSON_EMAIL);

			if (numberOfRecords == 1) {
				cursor.moveToFirst();
				String Id = cursor.getString(iId);
				currentPersonId = Id;
		 
	         String FirstName = cursor.getString(iFirstName);
		 
	         String LastName = cursor.getString(iLastName);
		 
	         String Email = cursor.getString(iEmail);

				// Report or log the row
				StringBuffer cbuf = new StringBuffer(Id);
			 
		      cbuf.append(", ").append(Id);
			 
		      cbuf.append(", ").append(FirstName);
			 
		      cbuf.append(", ").append(LastName);
			 
		      cbuf.append(", ").append(Email);
				reportString(cbuf.toString());
			} else {
				reportString("Error: Exactly 1 Person not returned from showOnePerson() method.");
			}

			// Close the cursor
			// ideally this should be done in
			// a finally block.
			cursor.close();

		}
	}

	// *****************************************************************************
	// Add Person on a separate thread
	// *****************************************************************************
	public void addPerson() {
		reportClear();
		new AddPersonAsyncTask("", testerContext, new PersonAddedListener())
				.execute();
	}

	public interface PersonAddedListenerInterface {
		public void onPersonAdded(String currentPersonId);
	}

	public class AddPersonAsyncTask extends AsyncTask<Object, Object, String> {
		String idParameterString;

		// listener for retrieved PersonList
		private PersonAddedListener contactAddedListener;

		public AddPersonAsyncTask(
				String idParameterString,
				Context context,
				dev.ronlemire.personprovider.Tester.PersonAddedListenerInterface contactAddedListener) {
			this.idParameterString = idParameterString;
			this.contactAddedListener = (PersonAddedListener) contactAddedListener;
		}

		// Call PersonService to get PersonList
		@Override
		protected String doInBackground(Object... params) {

			Log.d(tag, "Adding a book");
			ContentValues cv = new ContentValues();
			cv.put(PersonProviderMetaData.PersonTableMetaData.PERSON_ID, "0");
			cv.put(PersonProviderMetaData.PersonTableMetaData.PERSON_FIRSTNAME,"TRKCC");
			cv.put(PersonProviderMetaData.PersonTableMetaData.PERSON_LASTNAME,"KNGGO");
			cv.put(PersonProviderMetaData.PersonTableMetaData.PERSON_EMAIL,"UNXWQ");
			Uri get_all_person_uri = Uri.parse(res
					.getString(R.string.person_provider));
			Uri insertUri = Uri.withAppendedPath(get_all_person_uri, "0");
			Uri insertedUri = cr.insert(insertUri, cv);
			currentPersonId = insertedUri.getPathSegments().get(1);

			return "";
		}

		// executed back on the UI thread after the city name loads
		protected void onPostExecute(String domainString) {
			contactAddedListener.onPersonAdded(currentPersonId);
		}
	}

	// listens for person loaded in background task
	public class PersonAddedListener implements PersonAddedListenerInterface {

		public PersonAddedListener() {
		}

		@Override
		public void onPersonAdded(String Id) {
			reportString("Person Added: " + currentPersonId);
		}
	}

	// *****************************************************************************
	// Update Person on a separate thread
	// *****************************************************************************
	public void updatePerson() {
		reportClear();
		new UpdatePersonAsyncTask("", testerContext,
				new PersonUpdatedListener()).execute();
	}

	public interface PersonUpdatedListenerInterface {
		public void onPersonUpdated(String currentPersonId);
	}

	public class UpdatePersonAsyncTask extends
			AsyncTask<Object, Object, String> {
		String idParameterString;

		// listener for retrieved PersonList
		private PersonUpdatedListener contactUpdatedListener;

		public UpdatePersonAsyncTask(
				String idParameterString,
				Context context,
				dev.ronlemire.personprovider.Tester.PersonUpdatedListenerInterface contactUpdatedListener) {
			this.idParameterString = idParameterString;
			this.contactUpdatedListener = (PersonUpdatedListener) contactUpdatedListener;
		}

		// Call PersonService to get PersonList
		@Override
		protected String doInBackground(Object... params) {

			Log.d(tag, "Updating a Person");
			ContentValues cv = new ContentValues();
			cv.put(PersonProviderMetaData.PersonTableMetaData.PERSON_ID, currentPersonId);
			cv.put(PersonProviderMetaData.PersonTableMetaData.PERSON_FIRSTNAME,"CCDLZ");
			cv.put(PersonProviderMetaData.PersonTableMetaData.PERSON_LASTNAME,"WSPPL");
			cv.put(PersonProviderMetaData.PersonTableMetaData.PERSON_EMAIL,"PMLLB");
			Uri get_all_person_uri = Uri.parse(res
					.getString(R.string.person_provider));
			Uri updateUri = Uri.withAppendedPath(get_all_person_uri,
					currentPersonId);
			String[] selectionArgs = new String[1];
			selectionArgs[0] = "";
			cr.update(updateUri, cv, "", selectionArgs);

			return "";
		}

		// executed back on the UI thread after the city name loads
		protected void onPostExecute(String domainString) {
			contactUpdatedListener.onPersonUpdated(currentPersonId);
		}
	}

	// listens for person loaded in background task
	public class PersonUpdatedListener implements
			PersonUpdatedListenerInterface {

		public PersonUpdatedListener() {
		}

		@Override
		public void onPersonUpdated(String Id) {
			reportString("Person Updated: " + currentPersonId);
		}
	}

	// *****************************************************************************
	// Remove Person on a separate thread
	// *****************************************************************************
	public void removePerson() {
		reportClear();
		new DeletePersonAsyncTask("", testerContext,
				new PersonDeletedListener()).execute();
	}

	public interface PersonDeletedListenerInterface {
		public void onPersonDeleted(Cursor cursor);
	}

	public class DeletePersonAsyncTask extends
			AsyncTask<Object, Object, String> {
		String idParameterString;
		private Cursor cursor;

		// listener for retrieved PersonList
		private PersonDeletedListener contactDeletedListener;

		public DeletePersonAsyncTask(
				String idParameterString,
				Context context,
				dev.ronlemire.personprovider.Tester.PersonDeletedListenerInterface contactDeletedListener) {
			this.idParameterString = idParameterString;
			this.contactDeletedListener = (PersonDeletedListener) contactDeletedListener;
		}

		// Call PersonService to get PersonList
		@Override
		protected String doInBackground(Object... params) {

			Uri get_all_person_uri = Uri.parse(res
					.getString(R.string.person_provider));
			Uri delUri = Uri.withAppendedPath(get_all_person_uri,
					currentPersonId);
			cr.delete(delUri, null, null);

			return "";
		}

		// executed back on the UI thread after the city name loads
		protected void onPostExecute(String domainString) {
			contactDeletedListener.onPersonDeleted(cursor);
		}
	}

	// listens for person loaded in background task
	public class PersonDeletedListener implements
			PersonDeletedListenerInterface {

		public PersonDeletedListener() {
		}

		@Override
		public void onPersonDeleted(Cursor cursor) {
			reportString("Person Deleted: " + currentPersonId);
		}
	}

	// *****************************************************************************
	// Helpers
	// *****************************************************************************

	private void reportString(String s) {
		rb.reportBack(tag, s);
	}

	private void reportClear() {
		rb.clear();
	}
}


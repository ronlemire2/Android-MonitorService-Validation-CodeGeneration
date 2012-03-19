package dev.ronlemire.personprovider;

import dev.ronlemire.personprovider.PersonProviderMetaData.PersonTableMetaData;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

public class PersonProvider extends ContentProvider {
	private static final String TAG = "PersonProvider";

	// Provide a mechanism to identify
	// all the incoming uri patterns.
	private static final UriMatcher sUriMatcher;
	private static final int PERSON_All = 1;
	private static final int PERSON_ONE = 2;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(PersonProviderMetaData.AUTHORITY, "person",
				PERSON_All);
		sUriMatcher.addURI(PersonProviderMetaData.AUTHORITY, "person/#",
				PERSON_ONE);

	}

	@Override
	public boolean onCreate() {
		Log.d(TAG, "main onCreate called");
		return true;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Person[] personArray = null;

		PersonHttpClient personHttpClient = new PersonHttpClient(
				getContext());

		switch (sUriMatcher.match(uri)) {
		case PERSON_All: {
			personArray = personHttpClient.GetPersonArray("0");
			break;
		}
		case PERSON_ONE: {
			personArray = personHttpClient.GetPersonArray(uri
					.getPathSegments().get(1));
			break;
		}
		default:
			break;

		}

		// Column Names
		String[] colNames = new String[4];
		colNames[0] = "Id";
		colNames[1] = "FirstName";
		colNames[2] = "LastName";
		colNames[3] = "Email";
		// Column Values
		String Id;
		String FirstName;
		String LastName;
		String Email;

		MatrixCursor cursor = new MatrixCursor(colNames);
		for (Person person : personArray) {
			Id = person.getId();
			FirstName = person.getFirstName();
			LastName = person.getLastName();
			Email = person.getEmail();
			cursor.addRow(new Object[] { 
				Id,
				FirstName,
				LastName,
				Email
         });
		}

		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// Validate the requested uri
		if (sUriMatcher.match(uri) != PERSON_ONE) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		// Make sure that the fields are all set
	   
			if (values.containsKey(PersonTableMetaData.PERSON_ID) == false) {
				throw new SQLException(
						"Failed to insert row because Id is needed " + uri);
			}
	   
			if (values.containsKey(PersonTableMetaData.PERSON_FIRSTNAME) == false) {
				throw new SQLException(
						"Failed to insert row because Id is needed " + uri);
			}
	   
			if (values.containsKey(PersonTableMetaData.PERSON_LASTNAME) == false) {
				throw new SQLException(
						"Failed to insert row because Id is needed " + uri);
			}
	   
			if (values.containsKey(PersonTableMetaData.PERSON_EMAIL) == false) {
				throw new SQLException(
						"Failed to insert row because Id is needed " + uri);
			}

		String Id = values.getAsString(PersonTableMetaData.PERSON_ID);
		String FirstName = values.getAsString(PersonTableMetaData.PERSON_FIRSTNAME);
		String LastName = values.getAsString(PersonTableMetaData.PERSON_LASTNAME);
		String Email = values.getAsString(PersonTableMetaData.PERSON_EMAIL);

		Person addPerson = new Person(
			Id,
			FirstName,
			LastName,
			Email
         );
		PersonHttpClient personHttpClient = new PersonHttpClient(
				getContext());
		Integer newPersonId = personHttpClient.SavePerson(addPerson);

		if (newPersonId > 0) {
			Uri insertedPersonUri = ContentUris.withAppendedId(
					PersonTableMetaData.CONTENT_URI, newPersonId);
			getContext().getContentResolver().notifyChange(insertedPersonUri,
					null);

			return insertedPersonUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int update(Uri uri, ContentValues updateValues, String selection,
			String[] selectionArgs) {

		switch (sUriMatcher.match(uri)) {
		case PERSON_All:
			// count = db.update(BookTableMetaData.TABLE_NAME, values, where,
			// whereArgs);
			break;

		case PERSON_ONE:
			ContentValues values;
			if (updateValues != null) {
				values = new ContentValues(updateValues);
			} else {
				values = new ContentValues();
			}

			// Make sure that the fields are all set
	   
			if (values.containsKey(PersonTableMetaData.PERSON_ID) == false) {
				throw new SQLException(
						"Failed to insert row because Id is needed " + uri);
			}
	   
			if (values.containsKey(PersonTableMetaData.PERSON_FIRSTNAME) == false) {
				throw new SQLException(
						"Failed to insert row because Id is needed " + uri);
			}
	   
			if (values.containsKey(PersonTableMetaData.PERSON_LASTNAME) == false) {
				throw new SQLException(
						"Failed to insert row because Id is needed " + uri);
			}
	   
			if (values.containsKey(PersonTableMetaData.PERSON_EMAIL) == false) {
				throw new SQLException(
						"Failed to insert row because Id is needed " + uri);
			}

			String Id = values.getAsString(PersonTableMetaData.PERSON_ID);
			String FirstName = values.getAsString(PersonTableMetaData.PERSON_FIRSTNAME);
			String LastName = values.getAsString(PersonTableMetaData.PERSON_LASTNAME);
			String Email = values.getAsString(PersonTableMetaData.PERSON_EMAIL);

			Person updatePerson = new Person(
				Id,
				FirstName,
				LastName,
				Email
         );
			PersonHttpClient personHttpClient = new PersonHttpClient(
					getContext());
			int numRowsUpdated = personHttpClient.SavePerson(updatePerson);

			if (numRowsUpdated > 0) {
				Uri updatedPersonUri = ContentUris.withAppendedId(
						PersonTableMetaData.CONTENT_URI, Long.parseLong(Id));
				getContext().getContentResolver().notifyChange(updatedPersonUri,
						null);

				return numRowsUpdated;
			}

			throw new SQLException("Failed to insert row into " + uri);
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return 0;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int count = 0;
		switch (sUriMatcher.match(uri)) {
		case PERSON_All:
			// count = db.delete(BookTableMetaData.TABLE_NAME, where,
			// whereArgs);
			break;

		case PERSON_ONE:
			String Id = uri.getPathSegments().get(1);
			Person deletePerson = new Person(Id, "","","");
			PersonHttpClient personHttpClient = new PersonHttpClient(
					getContext());
			count = personHttpClient.DeletePerson(deletePerson);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}

package dev.ronlemire.personprovider;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class PersonHttpClient {
	private static final String TAG = PersonHttpClient.class.getSimpleName();
	private String multiplePersonUrl = "";
	private String singlePersonUrl = "";
	private Uri content_uri;
	private ContentResolver cr;
	private static final int COL_VALUE_IX = 3;
	
	// *****************************************************************************
	// Constructor - get URLs from Shared Preferences
	// *****************************************************************************
	// TODO: pass in URLs
	public PersonHttpClient(Context context) {
		Resources res = context.getResources();
		content_uri = Uri.parse(res.getString(R.string.config_provider));
		cr = context.getContentResolver();
		
		multiplePersonUrl = GetSetting("MULTIPLE_PERSON_URL");
		singlePersonUrl = GetSetting("SINGLE_PERSON_URL");
		Log.i(TAG, "PersonHttpClient constructor");
	}

	private String GetSetting(String name) {
		String settingName = name;
		String msg = "";
		try {
			Cursor c = cr.query(content_uri, null, "name=?",
					new String[] { settingName }, null);
			Log.v("CHECK 1", "CHECK 1");
			if (c == null) {
				msg += "Null cursor";
			} else if (c.moveToFirst()) {
				String vl = c.getString(COL_VALUE_IX);
				msg += vl;
			} else {
				msg += "No records in cursor";
			}
		} catch (Exception ex) {
			msg += "Database Exception";
		}
		
		return msg;
	}

	// *****************************************************************************
	// If Id is zero(0) get ContactList using HTTP GET method with
	// multipleContactUrl.
	// If Id is non-zero get Contact using HTTP GET method with
	// singleContactUrl.
	// ContactService will return a Contact[] in either case.
	// If getting a specific Contact, the Contact[] will contain only 1 Contact.
	// *****************************************************************************
	public Person[] GetPersonArray(String Id) {
		List<Person> personList = new ArrayList<Person>();
		Person[] personArray = null;
		String IdString = "";
		String FirstNameString = "";
		String LastNameString = "";
		String EmailString = "";
		String urlString = "";

		if (Id == "0") {
			urlString = multiplePersonUrl;
		} else {
			urlString = singlePersonUrl + Id;
		}

		try {
			HttpGet request = new HttpGet(urlString);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);
			HttpEntity responseEntity = response.getEntity();

			// Read response data into buffer
			char[] buffer = new char[(int) responseEntity.getContentLength()];
			InputStream stream = responseEntity.getContent();
			InputStreamReader reader = new InputStreamReader(stream);
			reader.read(buffer);
			stream.close();

			JSONArray jsonArray = new JSONArray(new String(buffer));
			try {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject e = jsonArray.getJSONObject(i);
					IdString = e.getString("Id");
					FirstNameString = e.getString("FirstName");
					LastNameString = e.getString("LastName");
					EmailString = e.getString("Email");
					personList.add(new Person(
						IdString,
						FirstNameString,
						LastNameString,
						EmailString
               ));
				}
				int personListSize = personList.size();
				personArray = new Person[personListSize];
				for (int i = 0; i < personListSize; i++) {
					personArray[i] = (Person) personList.get(i);
				}
			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return personArray;
	}

	// *****************************************************************************
	// Save Person using HTTP PUT method with singleContactUrl.
	// If Id is zero(0) a new Person will be added.
	// If Id is non-zero an existing Person will be updated.
	// HTTP POST could be used to add a new Person but the PersonService knows
	// an Id of zero means a new Person so in this case the HTTP PUT is used.
	// *****************************************************************************
	public Integer SavePerson(Person savePerson) {
		Integer statusCode = 0;
		HttpResponse response;

		try {
			boolean isValid = true;

			// Data validation goes here

			if (isValid) {

				// POST request to <service>/SaveVehicle
				HttpPut request = new HttpPut(singlePersonUrl
						+ savePerson.getId());
				request.setHeader("User-Agent", "dev.ronlemire.contactClient");
				request.setHeader("Accept", "application/json");
				request.setHeader("Content-type", "application/json");

				// Build JSON string
				JSONStringer person = new JSONStringer().object()
					.key("Id").value(Integer.parseInt(savePerson.getId()))
					.key("FirstName").value(savePerson.getFirstName())
					.key("LastName").value(savePerson.getLastName())
					.key("Email").value(savePerson.getEmail())
					.endObject();
				StringEntity entity = new StringEntity(person.toString());

				request.setEntity(entity);

				// Send request to WCF service
				DefaultHttpClient httpClient = new DefaultHttpClient();
				response = httpClient.execute(request);

				Log.d("WebInvoke", "Saving : "
						+ response.getStatusLine().getStatusCode());

				// statusCode =
				// Integer.toString(response.getStatusLine().getStatusCode());
				statusCode = response.getStatusLine().getStatusCode();

				if (savePerson.getId().equals("0")) {
					// New Person.Id is in buffer
					HttpEntity responseEntity = response.getEntity();
					char[] buffer = new char[(int) responseEntity
							.getContentLength()];
					InputStream stream = responseEntity.getContent();
					InputStreamReader reader = new InputStreamReader(stream);
					reader.read(buffer);
					stream.close();
					statusCode = Integer.parseInt(new String(buffer));
				} else {
					statusCode = response.getStatusLine().getStatusCode();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return statusCode;
	}

	// *****************************************************************************
	// Delete Person using HTTP DELETE method with singlePersonUrl.
	// *****************************************************************************
	public Integer DeletePerson(Person deletePerson) {
		Integer statusCode = 0;
		HttpResponse response;

		try {
			boolean isValid = true;

			// Data validation goes here

			if (isValid) {
				HttpDelete request = new HttpDelete(singlePersonUrl
						+ deletePerson.getId());
				request.setHeader("User-Agent", "dev.ronlemire.personprovider");
				request.setHeader("Accept", "application/json");
				request.setHeader("Content-type", "application/json");

				// Send request to WCF service
				DefaultHttpClient httpClient = new DefaultHttpClient();
				response = httpClient.execute(request);

				Log.d("WebInvoke", "Saving : "
						+ response.getStatusLine().getStatusCode());

				statusCode = response.getStatusLine().getStatusCode();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return statusCode;
	}
	
}

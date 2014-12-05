package com.BARcode.mycarpooling;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import static utilities.Constants.SERVER_URL;
import static utilities.Utilities.fieldsValid;
import android.opengl.ETC1;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddCar extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_car);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_car, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void addCarIntoDB(View view) {
		EditText licensePlateET = (EditText) findViewById(R.id.carnumber);
		EditText brandET = (EditText) findViewById(R.id.brand);
		EditText colorET = (EditText) findViewById(R.id.color);
		EditText seatsNoET = (EditText) findViewById(R.id.seatsNo);
		EditText yearET = (EditText) findViewById(R.id.year);

		String licensePlate = licensePlateET.getText().toString();
		String brand = brandET.getText().toString();
		String color = colorET.getText().toString();
		String seatsNo = seatsNoET.getText().toString();
		String year = yearET.getText().toString();

		TextView notAllFieldsCompleted = (TextView) findViewById(R.id.notAllFieldsCompletedAddCar);
		if (!fieldsValid(licensePlate, brand, color, seatsNo, year)) {
			notAllFieldsCompleted.setVisibility(View.VISIBLE);
		} else {
			notAllFieldsCompleted.setVisibility(View.INVISIBLE);

			String[] params = new String[5];
			params[0] = licensePlate;
			params[1] = brand;
			params[2] = color;
			params[3] = seatsNo;
			params[4] = year;

			new AddCarIntoDB().execute(params);
		}
	}

	class AddCarIntoDB extends AsyncTask<String, String, Void> {

		private ProgressDialog progressMessage;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressMessage = new ProgressDialog(AddCar.this);
			progressMessage.setMessage("Loading ...");
			progressMessage.setIndeterminate(false);
			progressMessage.setCancelable(false);
			progressMessage.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			String result = "";
			String url = SERVER_URL + "/insert_into_cars.php";
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse;
			InputStream is = null;

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						10);
				nameValuePairs.add(new BasicNameValuePair("username",
						MainActivity.userLoggedIn.getUsername()));
				nameValuePairs.add(new BasicNameValuePair("carnumber",
						(String) params[0]));
				nameValuePairs.add(new BasicNameValuePair("brand",
						(String) params[1]));
				nameValuePairs.add(new BasicNameValuePair("colour",
						(String) params[2]));
				nameValuePairs.add(new BasicNameValuePair("seats",
						(String) params[3]));
				nameValuePairs.add(new BasicNameValuePair("year",
						(String) params[4]));

				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				httpResponse = httpClient.execute(httpPost);

				// check response
				if (httpResponse != null) {
					is = httpResponse.getEntity().getContent();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is));
					StringBuilder sb = new StringBuilder();
					String line = "";

					while ((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}

					is.close();
					result = sb.toString();

					if (result.startsWith("success")) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								if (!isFinishing()) {
									new AlertDialog.Builder(AddCar.this)
											.setTitle("Add Car")
											.setMessage(
													"Car added successfully!")
											.setCancelable(false)
											.setPositiveButton("OK",
													new OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															Intent intent = new Intent(
																	AddCar.this,
																	AddEditCar.class);
															startActivity(intent);
														}
													}).create().show();
								}
							}
						});
					} else {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								if (!isFinishing()) {
									new AlertDialog.Builder(AddCar.this)
											.setTitle("Car")
											.setMessage(
													"Failed adding car. Please try once again")
											.setCancelable(false)
											.setPositiveButton("OK",
													new OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															Intent intent = new Intent(
																	AddCar.this,
																	AddCar.class);
															startActivity(intent);
														}
													}).create().show();
								}
							}
						});
					}
				} else {
					Log.e("POST:", "HTTP RESPONSE IS NULL");
				}

			} catch (Exception e) {
				Log.e("ERROR:", e.getMessage());
			}

			return null;
		}

	}
}

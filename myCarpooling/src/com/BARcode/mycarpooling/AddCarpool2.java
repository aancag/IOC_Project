
package com.BARcode.mycarpooling;

import static com.BARcode.utilities.Constants.ADD_CARPOOL;
import static com.BARcode.utilities.Constants.SERVER_URL;
import static com.BARcode.utilities.Utilities.fieldsValid;

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
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class AddCarpool2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_carpool2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_carpool2, menu);
		return true;
	}

	public void finishAddCarpool(View view) {
		EditText seatsNumberET = (EditText) findViewById(R.id.seatsNumber);
		RadioButton onlyOnce = (RadioButton) findViewById(R.id.once);
		RadioButton daily = (RadioButton) findViewById(R.id.daily);
		RadioButton weekly = (RadioButton) findViewById(R.id.weekly);

		String seatsNumber = seatsNumberET.getText().toString();
		boolean onlyOnceChecked = onlyOnce.isChecked();
		boolean dailyChecked = daily.isChecked();
		boolean weeklyChecked = weekly.isChecked();
		int commutePeriod = 0;
		// TODO: integrate GoogleMaps

		if (onlyOnceChecked) {
			commutePeriod = 1;
		} else if (dailyChecked) {
			commutePeriod = 2;
		} else if (weeklyChecked) {
			commutePeriod = 3;
		}

		TextView notAllFieldsCompleted = (TextView) findViewById(R.id.notAllFieldsCompletedAC2);
		if (!fieldsValid(seatsNumber)) {
			notAllFieldsCompleted.setVisibility(View.VISIBLE);
		} else {
			notAllFieldsCompleted.setVisibility(View.INVISIBLE);
			String[] params_from_parent = this.getIntent().getStringArrayExtra(
					ADD_CARPOOL);
			int length = params_from_parent.length;
			String[] params = new String[length + 3];
			for (int i = 0; i < params_from_parent.length; i++) {
				params[i] = params_from_parent[i];
			}
			params[length] = seatsNumber;
			params[length + 1] = String.valueOf(commutePeriod);
			// TODO: params[length + 3] - itinerary

			new AddCarpoolInDB().execute(params);
		}
	}

	class AddCarpoolInDB extends AsyncTask<String, String, String> {

		private ProgressDialog progressMessage;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressMessage = new ProgressDialog(AddCarpool2.this);
			progressMessage.setMessage("Loading ...");
			progressMessage.setIndeterminate(false);
			progressMessage.setCancelable(false);
			progressMessage.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String url = SERVER_URL + "/insert_into_carpools.php";
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse;
			InputStream is = null;

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						10);
				nameValuePairs.add(new BasicNameValuePair("username",
						MainActivity.userLoggedIn.getUsername()));
				nameValuePairs.add(new BasicNameValuePair("source",
						(String) params[0]));
				nameValuePairs.add(new BasicNameValuePair("destination",
						(String) params[1]));
				nameValuePairs.add(new BasicNameValuePair("date",
						(String) params[2]));
				nameValuePairs.add(new BasicNameValuePair("time",
						(String) params[3]));
				nameValuePairs.add(new BasicNameValuePair("price",
						(String) params[4]));
				nameValuePairs.add(new BasicNameValuePair("duration",
						(String) params[5]));
				nameValuePairs.add(new BasicNameValuePair("avseats",
						(String) params[6]));
				nameValuePairs.add(new BasicNameValuePair("commper",
						(String) params[7]));

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
									new AlertDialog.Builder(
											AddCarpool2.this)
											.setTitle("Add Carpool")
											.setMessage(
													"Carpool added successfully!")
											.setCancelable(false)
											.setPositiveButton("OK",
													new OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															Intent intent = new Intent(
																	AddCarpool2.this,
																	Driver.class);
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
									new AlertDialog.Builder(
											AddCarpool2.this)
											.setTitle("Carpool")
											.setMessage(
													"Failed adding carpool. Please try once again")
											.setCancelable(false)
											.setPositiveButton("OK",
													new OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															Intent intent = new Intent(
																	AddCarpool2.this,
																	AddCarpool.class);
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

package com.BARcode.mycarpooling;

import static utilities.Constants.SERVER_URL;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import databaseModels.Car;

public class AddEditCar extends Activity {

	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_edit_car);

		username = MainActivity.userLoggedIn.getUsername();
		new AddEditCarConnectDB().execute(username);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_edit_car, menu);
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

	public void addNewCar(View view) {
		Intent addCarIntent = new Intent(this, AddCar.class);
		startActivity(addCarIntent);
	}
	
	private ProgressDialog progressMessage;

	class AddEditCarConnectDB extends AsyncTask<String, String, String> {

		InputStream is = null;
		String cars = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressMessage = new ProgressDialog(AddEditCar.this);
			progressMessage.setMessage("Loading ...");
			progressMessage.setIndeterminate(false);
			progressMessage.setCancelable(false);
			progressMessage.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String username = (String) params[0];
			String url = SERVER_URL + "get_cars.php?username=" + username;

			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet();
				request.setURI(new URI(url));

				HttpResponse response = client.execute(request);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));

				StringBuilder sb = new StringBuilder();
				String line = "";

				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}

				String[] lines = sb.toString().split("\n");
				for (int i = 0; i < lines.length - 3; i++) {
					cars += lines[i] + "\n";
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (!cars.equals("null\n")) {
							List<Car> carsList = new ArrayList<Car>();

							JSONArray ja = null;

							try {
								ja = new JSONArray(cars);
								for (int i = 0; i < ja.length(); i++) {
									Car c = new Car(ja.getJSONObject(i));
									carsList.add(c);
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						progressMessage.dismiss();
//						
//						// add button for adding new car
//						Button addNewCar = new Button(AddEditCar.this);
//						addNewCar.setText("Add new car");
//						addNewCar.setOnClickListener(new OnClickListener() {
//							@Override
//							public void onClick(View arg0) {
//								Intent addCarIntent = new Intent(
//										AddEditCar.this, AddCar.class);
//								startActivity(addCarIntent);
//							}
//						});
//						
//						LinearLayout layout = (LinearLayout) findViewById(R.id.add_edit_car_layout);
//						layout.addView(addNewCar);
						
					}
				});

			} catch (Exception e) {
				Log.e("ERROR:", e.getMessage());
			}

			return null;
		}

	}
}

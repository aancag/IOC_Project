package com.BARcode.mycarpooling;

import static com.BARcode.utilities.Constants.SERVER_URL;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import com.BARcode.databaseModels.Car;

public class AddEditCar extends Activity {

	private String username;

	private LinearLayout lm;

	private List<Button> deleteButtons;
	private List<Button> editButtons;
	private List<Car> carsList = new ArrayList<Car>();
	private Map<Integer, String> buttonCar = new HashMap<Integer, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_edit_car);

		lm = (LinearLayout) findViewById(R.id.addEditCar);

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

	class DeleteFromCars extends AsyncTask<String, String, Void> {

		private ProgressDialog progressMessage;

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
		protected Void doInBackground(String... params) {
			String url = SERVER_URL + "delete_from_cars.php";
			String result = "";

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				nameValuePairs.add(new BasicNameValuePair("carnumber", (String) params[0]));

				HttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = client.execute(httpPost);
				BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				StringBuilder sb = new StringBuilder();
				String line = "";

				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}

				result = sb.toString();

				if (result.startsWith("success")) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Intent intent = new Intent(AddEditCar.this, AddEditCar.class);
							startActivity(intent);
						}
					});
				}

			} catch (Exception e) {
				Log.e("ERROR:", e.toString());
			}

			return null;
		}

	}

	class AddEditCarConnectDB extends AsyncTask<String, String, String> {

		InputStream is = null;
		String cars = "";

		View.OnClickListener myhandler = new View.OnClickListener() {
			public void onClick(View v) {
				// find clicked button
				int id = v.getId();
				// find car to delete
				final String licensePlate = buttonCar.get(id);

				new AlertDialog.Builder(AddEditCar.this).setTitle("Delete car").setMessage("Are you sure you want to delete car " + licensePlate + "?")
						.setNegativeButton("Cancel", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).setPositiveButton("OK", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// delete from DB
								new DeleteFromCars().execute(licensePlate);
							}
						}).create().show();
			}
		};

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
				BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

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

							final float scale = getResources().getDisplayMetrics().density;
							int padding_5dp = (int) (5 * scale + 0.5f);
							int padding_10dp = (int) (10 * scale + 0.5f);
							int padding_20dp = (int) (20 * scale + 0.5f);
							
							for (int i = 0; i < carsList.size(); i++) {
								Car car = carsList.get(i);

								LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
								layoutParams.setMargins(0, 0, 5, 10);

								Space space = new Space(AddEditCar.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								// Create LinearLayout VERTICAL
								LinearLayout llv = new LinearLayout(AddEditCar.this);
								llv.setOrientation(LinearLayout.VERTICAL);
								llv.setPadding(padding_20dp, padding_5dp, padding_5dp, padding_5dp);

								// Create licensePlate
								TextView licensePlate = new TextView(AddEditCar.this);
								licensePlate.setText("License Plate: " + car.getCarNumber());
								licensePlate.setTextAppearance(AddEditCar.this, android.R.style.TextAppearance_Medium);

								llv.addView(licensePlate);

								// Create LinearLayout HORIZONTAL
								LinearLayout ll1 = new LinearLayout(AddEditCar.this);
								ll1.setOrientation(LinearLayout.HORIZONTAL);
								ll1.setLayoutParams(layoutParams);
								ll1.setPadding(padding_20dp, padding_5dp, padding_5dp, padding_5dp);

								// Create brand
								TextView brandTV = new TextView(AddEditCar.this);
								brandTV.setText("Brand: ");
								brandTV.setTypeface(null, Typeface.BOLD);

								TextView brand = new TextView(AddEditCar.this);
								brand.setText(car.getBrand());

								ll1.addView(brandTV);
								ll1.addView(brand);
								ll1.addView(space);

								// Create color
								TextView colourTV = new TextView(AddEditCar.this);
								colourTV.setText("Color: ");
								colourTV.setTypeface(null, Typeface.BOLD);

								TextView colour = new TextView(AddEditCar.this);
								colour.setText(car.getColour());

								space = new Space(AddEditCar.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								ll1.addView(colourTV);
								ll1.addView(colour);
								ll1.addView(space);

								// Create LinearLayout HORIZONTAL
								LinearLayout ll2 = new LinearLayout(AddEditCar.this);
								ll2.setOrientation(LinearLayout.HORIZONTAL);
								ll2.setLayoutParams(layoutParams);
								ll2.setPadding(padding_20dp, padding_5dp, padding_5dp, padding_5dp);

								// Create seatsnumber
								TextView seatsNoTV = new TextView(AddEditCar.this);
								seatsNoTV.setText("Seats number: ");
								seatsNoTV.setTypeface(null, Typeface.BOLD);

								TextView seatsNo = new TextView(AddEditCar.this);
								seatsNo.setText(String.valueOf(car.getSeats()));

								space = new Space(AddEditCar.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								ll2.addView(seatsNoTV);
								ll2.addView(seatsNo);
								ll2.addView(space);

								// Create acquisition year
								TextView yearTV = new TextView(AddEditCar.this);
								yearTV.setText("Year: ");
								yearTV.setTypeface(null, Typeface.BOLD);

								TextView year = new TextView(AddEditCar.this);
								year.setText(String.valueOf(car.getYear()));

								ll2.addView(yearTV);
								ll2.addView(year);

								llv.addView(ll1);
								llv.addView(ll2);

								lm.addView(llv);

								// add button to edit car
								Button editButton = new Button(AddEditCar.this);
								editButton.setText("Edit Car");
								editButton.setId(i);
								// TODO: add handler for edit
								// editButton.setOnClickListener();
								buttonCar.put(i, car.getCarNumber());
								// editButtons.add(editButton);

								lm.addView(editButton);

								// add button delete car
								Button deleteButton = new Button(AddEditCar.this);
								deleteButton.setText("Delete this car");
								deleteButton.setId(i * 99999);
								deleteButton.setOnClickListener(myhandler);
								buttonCar.put(i * 99999, car.getCarNumber());
								// deleteButtons.add(deleteButton);

								// deleteButton.setOnClickListener(new
								// OnClickListener() {
								//
								// @Override
								// public void onClick(View v) {
								// String url = SERVER_URL +
								// "delete_from_cars.php?carnumber=" +
								// car.getCarNumber();
								//
								// try {
								// HttpClient client = new DefaultHttpClient();
								// HttpGet request = new HttpGet();
								// request.setURI(new URI(url));
								//
								// HttpResponse response =
								// client.execute(request);
								// BufferedReader br = new BufferedReader(new
								// InputStreamReader(response.getEntity().getContent()));
								//
								// StringBuilder sb = new StringBuilder();
								// String line = "";
								//
								// while ((line = br.readLine()) != null) {
								// sb.append(line + "\n");
								// }
								// } catch (Exception e) {
								// Log.e("ERROR:", e.getMessage());
								// }
								// }
								// });

								lm.addView(deleteButton);
							}
						}

						progressMessage.dismiss();

					}
				});

			} catch (Exception e) {
				Log.e("ERROR:", e.getMessage());
			}

			return null;
		}

	}
}

package com.BARcode.mycarpooling;

import static com.BARcode.utilities.Constants.SERVER_URL;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.BARcode.databaseModels.Carpool;
import com.BARcode.databaseModels.User;

public class UserInformation2 extends Activity {

	public static User user = UserInformation1.user;

	private LinearLayout joinedCarpoolsLayout;

	private List<Carpool> carpoolsList = new ArrayList<Carpool>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_information_2);

		((EditText) findViewById(R.id.emailUI)).setText(user.getEmail());
		((EditText) findViewById(R.id.phoneUI)).setText(user.getPhone());
		((EditText) findViewById(R.id.cityUI)).setText(user.getCity());
		((CheckBox) findViewById(R.id.smokerUI)).setChecked(user.getSmoker() == 1);

		joinedCarpoolsLayout = (LinearLayout) findViewById(R.id.joinedCarpoolsLayout);

		// get joinedCarpools from DB
		new GetJoinedCarpoolsDB().execute(user.getUsername());
	}

	// save changes in DB
	public void saveChangesIntoDB(View view) {
		// take changes
		user.setEmail(((EditText) findViewById(R.id.emailUI)).getText().toString());
		user.setPhone(((EditText) findViewById(R.id.phoneUI)).getText().toString());
		user.setCity(((EditText) findViewById(R.id.cityUI)).getText().toString());
		user.setSmoker(((CheckBox) findViewById(R.id.smokerUI)).isChecked() ? 1 : 0);
		
		MainActivity.userLoggedIn = user;
		
		new UpdateUsersDB().execute();
	}

	/************ get joined carpools ************/
	private ProgressDialog progressMessage;

	@Override
	public void onStop() {
		super.onStop();
		progressMessage.dismiss(); // try this
	}

	class GetJoinedCarpoolsDB extends AsyncTask<String, String, String> {

		String carpools = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressMessage = new ProgressDialog(UserInformation2.this);
			progressMessage.setMessage("Loading ...");
			progressMessage.setIndeterminate(false);
			progressMessage.setCancelable(false);
			if (progressMessage != null && progressMessage.isShowing()) {
				progressMessage.cancel();
			}
			progressMessage.show();
		}

		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			progressMessage.dismiss();
		}

		@Override
		protected String doInBackground(String... params) {
			String url = SERVER_URL + "get_joined_carpools.php?carpooled_user=" + params[0];

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
					carpools += lines[i] + "\n";
				}

				//
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (!carpools.equals("null\n")) {
							JSONArray ja = null;

							try {
								ja = new JSONArray(carpools);
								for (int i = 0; i < ja.length(); i++) {
									Carpool c = new Carpool(ja.getJSONObject(i));
									carpoolsList.add(c);
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}

							// sort by date
							Collections.sort(carpoolsList, new Comparator<Carpool>() {

								@Override
								public int compare(Carpool lhs, Carpool rhs) {
									return rhs.getDate().compareTo(lhs.getDate());
								}

							});

							// create GUI
							final float scale = getResources().getDisplayMetrics().density;
							int padding_5dp = (int) (5 * scale + 0.5f);
							int padding_10dp = (int) (10 * scale + 0.5f);
							int padding_20dp = (int) (20 * scale + 0.5f);

							for (int i = 0; i < carpoolsList.size(); i++) {
								Carpool carpool = carpoolsList.get(i);

								LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
								layoutParams.setMargins(0, 0, 5, 10);

								Space space = new Space(UserInformation2.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								// LL horizontal
								LinearLayout llh1 = new LinearLayout(UserInformation2.this);
								llh1.setOrientation(LinearLayout.HORIZONTAL);
								llh1.setLayoutParams(layoutParams);
								llh1.setPadding(padding_20dp, padding_5dp, padding_5dp, padding_5dp);

								// Driver
								TextView driverText = new TextView(UserInformation2.this);
								driverText.setText("Driver: ");
								driverText.setTypeface(null, Typeface.BOLD);
								driverText.setTextAppearance(UserInformation2.this, android.R.style.TextAppearance_Medium);

								TextView driver = new TextView(UserInformation2.this);
								driver.setText(carpool.getUsername());
								driver.setTextAppearance(UserInformation2.this, android.R.style.TextAppearance_Medium);

								llh1.addView(driverText);
								llh1.addView(driver);

								// LL horizontal
								LinearLayout llh2 = new LinearLayout(UserInformation2.this);
								llh2.setOrientation(LinearLayout.HORIZONTAL);
								llh2.setLayoutParams(layoutParams);
								llh2.setPadding(padding_20dp, padding_5dp, padding_5dp, padding_5dp);

								// date
								TextView date = new TextView(UserInformation2.this);
								date.setText("Date: ");
								date.setTypeface(null, Typeface.BOLD);

								TextView dateC = new TextView(UserInformation2.this);
								dateC.setText(carpool.getDate());

								space = new Space(UserInformation2.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								llh2.addView(date);
								llh2.addView(dateC);
								llh2.addView(space);

								// time
								TextView time = new TextView(UserInformation2.this);
								time.setText("Time: ");
								time.setTypeface(null, Typeface.BOLD);

								TextView timeC = new TextView(UserInformation2.this);
								timeC.setText(carpool.getTime());

								space = new Space(UserInformation2.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								llh2.addView(time);
								llh2.addView(timeC);
								llh2.addView(space);

								// LL horizontal
								LinearLayout llh3 = new LinearLayout(UserInformation2.this);
								llh3.setOrientation(LinearLayout.HORIZONTAL);
								llh3.setLayoutParams(layoutParams);
								llh3.setPadding(padding_20dp, padding_5dp, padding_5dp, padding_5dp);

								// avSeats
								TextView avSeats = new TextView(UserInformation2.this);
								avSeats.setText("Available seats: ");
								avSeats.setTypeface(null, Typeface.BOLD);

								TextView avSeatsC = new TextView(UserInformation2.this);
								avSeatsC.setText("" + carpool.getAvailableSeats());

								space = new Space(UserInformation2.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								llh3.addView(avSeats);
								llh3.addView(avSeatsC);
								llh3.addView(space);

								// price
								TextView price = new TextView(UserInformation2.this);
								price.setText("Price: ");
								price.setTypeface(null, Typeface.BOLD);

								TextView priceC = new TextView(UserInformation2.this);
								priceC.setText("" + carpool.getPrice());

								space = new Space(UserInformation2.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								llh3.addView(price);
								llh3.addView(priceC);
								llh3.addView(space);

								// LL horizontal
								LinearLayout llh4 = new LinearLayout(UserInformation2.this);
								llh4.setOrientation(LinearLayout.HORIZONTAL);
								llh4.setLayoutParams(layoutParams);
								llh4.setPadding(padding_20dp, padding_5dp, padding_5dp, padding_5dp);

								// duration
								TextView duration = new TextView(UserInformation2.this);
								duration.setText("Duration: ");
								duration.setTypeface(null, Typeface.BOLD);

								TextView durationC = new TextView(UserInformation2.this);
								durationC.setText("" + carpool.getDuration());

								space = new Space(UserInformation2.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								llh4.addView(duration);
								llh4.addView(durationC);
								llh4.addView(space);

								// commPeriod
								TextView commutePeriod = new TextView(UserInformation2.this);
								commutePeriod.setText("Commute Period: ");
								commutePeriod.setTypeface(null, Typeface.BOLD);

								TextView commutePeriodC = new TextView(UserInformation2.this);
								switch (carpool.getCommutePeriod()) {
								case 1:
									commutePeriodC.setText("only once");
									break;
								case 2:
									commutePeriodC.setText("daily");
									break;
								default:
									commutePeriodC.setText("weekly");
									break;
								}

								space = new Space(UserInformation2.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								llh4.addView(commutePeriod);
								llh4.addView(commutePeriodC);
								llh4.addView(space);

								// add layouts
								joinedCarpoolsLayout.addView(llh1);
								joinedCarpoolsLayout.addView(llh2);
								joinedCarpoolsLayout.addView(llh3);
								joinedCarpoolsLayout.addView(llh4);
							}
						}
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

	}

	class UpdateUsersDB extends AsyncTask<String, String, String> {		
				
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressMessage = new ProgressDialog(UserInformation2.this);
			progressMessage.setMessage("Loading ...");
			progressMessage.setIndeterminate(false);
			progressMessage.setCancelable(false);
			if (progressMessage != null && progressMessage.isShowing()) {
				progressMessage.cancel();
			}
			progressMessage.show();
		}

		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			progressMessage.dismiss();
		}
		
		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String url = SERVER_URL + "update_users.php";
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse;
			InputStream is = null;
			
			try {			
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
		        nameValuePairs.add(new BasicNameValuePair("username", user.getUsername()));
		        nameValuePairs.add(new BasicNameValuePair("password", user.getPassword()));
		        nameValuePairs.add(new BasicNameValuePair("email", user.getEmail()));
		        nameValuePairs.add(new BasicNameValuePair("phone", user.getPhone()));
		        nameValuePairs.add(new BasicNameValuePair("firstName", user.getFirstName()));
		        nameValuePairs.add(new BasicNameValuePair("lastName", user.getLastName()));
		        nameValuePairs.add(new BasicNameValuePair("city", user.getCity()));
		        nameValuePairs.add(new BasicNameValuePair("birthDate", user.getBirthDate()));
		        nameValuePairs.add(new BasicNameValuePair("smoker", "" + user.getSmoker()));
		        nameValuePairs.add(new BasicNameValuePair("bio", user.getBio()));

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
											UserInformation2.this)
											.setTitle("User Information")
											.setMessage("User information updated successfully!")
											.setCancelable(false)
											.setPositiveButton("OK",
													new OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															Class<?> goUpClass = MainActivity.isDriver ? Driver.class : Passenger.class;
															Intent intent = new Intent(UserInformation2.this, goUpClass);
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
											UserInformation2.this)
											.setTitle("User Information")
											.setMessage("Failed updating user information. Please try once again")
											.setCancelable(false)
											.setPositiveButton("OK",
													new OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															Intent intent = new Intent(UserInformation2.this, UserInformation1.class);
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
				e.printStackTrace();
			}
			
			
			return null;
		}
		
	}
}

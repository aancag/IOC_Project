package com.BARcode.mycarpooling;

import static com.BARcode.utilities.Constants.SERVER_URL;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Space;
import android.widget.TextView;

import com.BARcode.databaseModels.Carpool;

public class SearchCarpools extends Activity {

	private LinearLayout lm;

	private Map<Integer, Integer> joinButtons = new HashMap<Integer, Integer>();
	private List<Carpool> carpoolsList = new ArrayList<Carpool>();

	public static final String SHOW_DRIVER_INFO = "SearchCarpools.username";
	
	public static final String CARPOOL_SOURCE = "SearchCarpools.source";
	
	public static final String CARPOOL_DEST = "SearchCarpools.destination";

	private String sourceSearch;

	private String destinationSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_carpools);

		lm = (LinearLayout) findViewById(R.id.searchCarpools);

		sourceSearch = getIntent().getExtras().getString("source");

		destinationSearch = getIntent().getExtras().getString("destination");

		new SearchCarpoolsConnectDB().execute();
	}

	private ProgressDialog progressMessage;

	@Override
	public void onStop() {
		super.onStop();
		progressMessage.dismiss(); // try this
	}

	class SearchCarpoolsConnectDB extends AsyncTask<String, String, String> {

		String carpools = "";

		// listeners
		View.OnClickListener showDriverInfoHandler = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// find clicked button
				Carpool carpool = carpoolsList.get(v.getId());

				Intent intent = new Intent(SearchCarpools.this, ShowDriverInformation.class);
				intent.putExtra(SHOW_DRIVER_INFO, carpool.getUsername());
				intent.putExtra(CARPOOL_SOURCE, carpool.getSource());
				intent.putExtra(CARPOOL_DEST, carpool.getDestination());
				
				startActivity(intent);
			}
		};

		View.OnClickListener joinCarpoolHandler = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// find clicked button
				int id = v.getId();

			}
		};

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressMessage = new ProgressDialog(SearchCarpools.this);
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
			String url = SERVER_URL + String.format("get_carpools_src_dst.php?source=%s&destination=%s", sourceSearch, destinationSearch);
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

							// sort by number of av seats
							Collections.sort(carpoolsList, new Comparator<Carpool>() {

								@Override
								public int compare(Carpool lhs, Carpool rhs) {
									return rhs.getAvailableSeats() - lhs.getAvailableSeats();
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

								Space space = new Space(SearchCarpools.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								// LL vertical
								LinearLayout llv = new LinearLayout(SearchCarpools.this);
								llv.setOrientation(LinearLayout.VERTICAL);
								llv.setPadding(padding_20dp, padding_5dp, padding_5dp, padding_5dp);

								// LL horizontal
								LinearLayout llh1 = new LinearLayout(SearchCarpools.this);
								llh1.setOrientation(LinearLayout.HORIZONTAL);
								llh1.setLayoutParams(layoutParams);
								llh1.setPadding(padding_20dp, padding_5dp, padding_5dp, padding_5dp);

								// Driver
								TextView driverText = new TextView(SearchCarpools.this);
								driverText.setText("Driver: ");
								driverText.setTypeface(null, Typeface.BOLD);

								TextView driver = new TextView(SearchCarpools.this);
								driver.setText(carpool.getUsername());

								// create show driver info
								Button driverInfo = new Button(SearchCarpools.this);
								driverInfo.setText("Show driver information");
								driverInfo.setId(i);
								driverInfo.setOnClickListener(showDriverInfoHandler);

								llh1.addView(driverText);
								llh1.addView(driver);
								llh1.addView(space);
								llh1.addView(driverInfo);

								// LL horizontal
								LinearLayout llh2 = new LinearLayout(SearchCarpools.this);
								llh2.setOrientation(LinearLayout.HORIZONTAL);
								llh2.setLayoutParams(layoutParams);
								llh2.setPadding(padding_20dp, padding_5dp, padding_5dp, padding_5dp);

								// date
								TextView date = new TextView(SearchCarpools.this);
								date.setText("Date: ");
								date.setTypeface(null, Typeface.BOLD);

								TextView dateC = new TextView(SearchCarpools.this);
								dateC.setText(carpool.getDate());

								space = new Space(SearchCarpools.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								llh2.addView(date);
								llh2.addView(dateC);
								llh2.addView(space);

								// time
								TextView time = new TextView(SearchCarpools.this);
								time.setText("Time: ");
								time.setTypeface(null, Typeface.BOLD);

								TextView timeC = new TextView(SearchCarpools.this);
								timeC.setText(carpool.getTime());

								space = new Space(SearchCarpools.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								llh2.addView(time);
								llh2.addView(timeC);
								llh2.addView(space);

								// LL horizontal
								LinearLayout llh3 = new LinearLayout(SearchCarpools.this);
								llh3.setOrientation(LinearLayout.HORIZONTAL);
								llh3.setLayoutParams(layoutParams);
								llh3.setPadding(padding_20dp, padding_5dp, padding_5dp, padding_5dp);

								// avSeats
								TextView avSeats = new TextView(SearchCarpools.this);
								avSeats.setText("Available seats: ");
								avSeats.setTypeface(null, Typeface.BOLD);

								TextView avSeatsC = new TextView(SearchCarpools.this);
								avSeatsC.setText("" + carpool.getAvailableSeats());

								space = new Space(SearchCarpools.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								llh3.addView(avSeats);
								llh3.addView(avSeatsC);
								llh3.addView(space);

								// price
								TextView price = new TextView(SearchCarpools.this);
								price.setText("Price: ");
								price.setTypeface(null, Typeface.BOLD);

								TextView priceC = new TextView(SearchCarpools.this);
								priceC.setText("" + carpool.getPrice());

								space = new Space(SearchCarpools.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								llh3.addView(price);
								llh3.addView(priceC);
								llh3.addView(space);

								// LL horizontal
								LinearLayout llh4 = new LinearLayout(SearchCarpools.this);
								llh4.setOrientation(LinearLayout.HORIZONTAL);
								llh4.setLayoutParams(layoutParams);
								llh4.setPadding(padding_20dp, padding_5dp, padding_5dp, padding_5dp);

								// duration
								TextView duration = new TextView(SearchCarpools.this);
								duration.setText("Duration: ");
								duration.setTypeface(null, Typeface.BOLD);

								TextView durationC = new TextView(SearchCarpools.this);
								durationC.setText("" + carpool.getDuration());

								space = new Space(SearchCarpools.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								llh4.addView(duration);
								llh4.addView(durationC);
								llh4.addView(space);

								// commPeriod
								TextView commutePeriod = new TextView(SearchCarpools.this);
								commutePeriod.setText("Commute Period: ");
								commutePeriod.setTypeface(null, Typeface.BOLD);

								TextView commutePeriodC = new TextView(SearchCarpools.this);
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

								space = new Space(SearchCarpools.this);
								space.setLayoutParams(layoutParams);
								space.setPaddingRelative(padding_20dp, padding_10dp, padding_10dp, padding_10dp);

								llh4.addView(commutePeriod);
								llh4.addView(commutePeriodC);
								llh4.addView(space);

								// button
								Button joinCarpoolButton = new Button(SearchCarpools.this);
								joinCarpoolButton.setText("Join Carpool");
								joinCarpoolButton.setId((i + 1) * 999);
								joinCarpoolButton.setOnClickListener(joinCarpoolHandler);
								joinButtons.put((i + 1) * 999, i);
								if (carpool.getAvailableSeats() == 0) {
									joinCarpoolButton.setClickable(false);
									joinCarpoolButton.setEnabled(false);
								}

								// add all layouts
								llv.addView(llh1);
								llv.addView(llh2);
								llv.addView(llh3);
								llv.addView(llh4);

								lm.addView(llv);
								lm.addView(joinCarpoolButton);
							}

							// progressMessage.dismiss();
						}
					}
				});
			} catch (Exception e) {
				Log.e("ERROR:", e.getMessage());
			}

			return null;
		}

	}

}

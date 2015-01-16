package com.BARcode.mycarpooling;

import static com.BARcode.utilities.Constants.SERVER_URL;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;

import com.BARcode.databaseModels.User;

public class ShowDriverInformation extends Activity {

	private User driver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_driver_information);

		String driver = getIntent().getExtras().getString(SearchCarpools.SHOW_DRIVER_INFO);
		new GetUserInfoFromDB().execute(driver);
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

	/*************** DB ***************/
	class GetUserInfoFromDB extends AsyncTask<String, String, String> {

		private ProgressDialog progressMessage;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressMessage = new ProgressDialog(ShowDriverInformation.this);
			progressMessage.setMessage("Loading ...");
			progressMessage.setIndeterminate(false);
			progressMessage.setCancelable(false);
			progressMessage.show();
		}

		@Override
		protected void onPostExecute(String s) {
		     // dismiss the dialog once done
			progressMessage.dismiss();
		}
		
		String result = "";
		
		@Override
		protected String doInBackground(String... params) {
			String url = SERVER_URL + "list_user_username.php?username=" + params[0];

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
					result += lines[i] + "\n";
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try {
							driver = new User(new JSONArray(result).getJSONObject(0));
							
							// populate GUI
							((EditText) findViewById(R.id.dName)).setText(driver.getFirstName() + " " + driver.getLastName());
							((EditText) findViewById(R.id.dMail)).setText(driver.getEmail());
							((EditText) findViewById(R.id.dPhone)).setText(driver.getPhone());
							((EditText) findViewById(R.id.dMail)).setText(driver.getEmail());
							((RatingBar) findViewById(R.id.dRating)).setRating(driver.getRankingDriver());
							((EditText) findViewById(R.id.dBio)).setText(driver.getBio());
							((EditText) findViewById(R.id.dMail)).setText(driver.getEmail());
							((EditText) findViewById(R.id.dBirthDate)).setText(driver.getBirthDate());
							((EditText) findViewById(R.id.dMail)).setText(driver.getEmail());
							if (driver.getSmoker() == 0) {
								((CheckBox) findViewById(R.id.dSmoker)).setChecked(false);
							} else {
								((CheckBox) findViewById(R.id.dSmoker)).setChecked(true);
							}
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						progressMessage.dismiss();
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

	}
}

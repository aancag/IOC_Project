package com.BARcode.mycarpooling;

import static utilities.Constants.SERVER_URL;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity2 extends Activity {

	String[] params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		params = this.getIntent().getStringArrayExtra(
				SignUpActivity1.signUpActivity1);
		setContentView(R.layout.fragment_sign_up_activity2);
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

	// butonul de next te redirecteaza spre pagina de signup3
	public void finishRegistration(View view) {
		EditText firstNameET = (EditText) findViewById(R.id.firstName);
		EditText lastNameET = (EditText) findViewById(R.id.lastName);
		EditText cityET = (EditText) findViewById(R.id.city);
		CheckBox smokerCB = (CheckBox) findViewById(R.id.smokerCB);
		EditText birthDateET = (EditText) findViewById(R.id.birthDate);
		EditText bioET = (EditText) findViewById(R.id.bio);

		String firstName = firstNameET.getText().toString();
		String lastName = lastNameET.getText().toString();
		String city = cityET.getText().toString();
		int smokerCBValue = smokerCB.isChecked() ? 1 : 0;
		String birthDate = birthDateET.getText().toString();
		String bio = bioET.getText().toString();

		TextView notAllFieldsCompleted = (TextView) findViewById(R.id.notAllFieldsCompleted);
		if (firstName.equals("") || lastName.equals("") || city.equals("")
				|| birthDate.equals("")) {
			notAllFieldsCompleted.setVisibility(View.VISIBLE);
		} else {
			new InsertIntoDatabase().execute(params[0], params[1], params[2],
					params[3], firstName, lastName, city, birthDate,
					String.valueOf(smokerCBValue), bio);
		}

	}

	private ProgressDialog progressMessage;

	class InsertIntoDatabase extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressMessage = new ProgressDialog(SignUpActivity2.this);
			progressMessage.setMessage("Loading ...");
			progressMessage.setIndeterminate(false);
			progressMessage.setCancelable(false);
			progressMessage.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			String url = SERVER_URL + "/insert_into_users.php";
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse;
			InputStream is = null;

			try {			
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
		        nameValuePairs.add(new BasicNameValuePair("username", (String) params[0]));
		        nameValuePairs.add(new BasicNameValuePair("password", (String) params[1]));
		        nameValuePairs.add(new BasicNameValuePair("email", (String) params[2]));
		        nameValuePairs.add(new BasicNameValuePair("phone", (String) params[3]));
		        nameValuePairs.add(new BasicNameValuePair("firstName", (String) params[4]));
		        nameValuePairs.add(new BasicNameValuePair("lastName", (String) params[5]));
		        nameValuePairs.add(new BasicNameValuePair("city", (String) params[6]));
		        nameValuePairs.add(new BasicNameValuePair("birthDate", (String) params[7]));
		        nameValuePairs.add(new BasicNameValuePair("smoker", (String) params[8]));
		        nameValuePairs.add(new BasicNameValuePair("bio", (String) params[9]));

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
											SignUpActivity2.this)
											.setTitle("Account")
											.setMessage("Account created successfully!")
											.setCancelable(false)
											.setPositiveButton("OK",
													new OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															Intent intent = new Intent(SignUpActivity2.this, MainActivity.class);
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
											SignUpActivity2.this)
											.setTitle("Account")
											.setMessage("Failed creating account. Please try once again")
											.setCancelable(false)
											.setPositiveButton("OK",
													new OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															Intent intent = new Intent(SignUpActivity2.this, SignUpActivity1.class);
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

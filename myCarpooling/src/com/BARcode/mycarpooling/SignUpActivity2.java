package com.BARcode.mycarpooling;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import static utilities.Constants.SERVER_URL;

public class SignUpActivity2 extends Activity {

	String[] params;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		params = this.getIntent().getStringArrayExtra(SignUpActivity1.signUpActivity1);
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
		CalendarView birthDateCV = (CalendarView) findViewById(R.id.birthDateCalendar);
		EditText bioET = (EditText) findViewById(R.id.bio);
		
		String firstName = firstNameET.getText().toString();
		String lastName = lastNameET.getText().toString();
		String city = cityET.getText().toString();
		int smokerCBValue = smokerCB.isChecked() ? 1 : 0;
		String birthDate = new Date(birthDateCV.getDate() * 1000).toString(); 
		String bio = bioET.getText().toString();
		
		TextView notAllFieldsCompleted = (TextView) findViewById(R.id.notAllFieldsCompleted);
		if (firstName.equals("") || lastName.equals("") || city.equals("") || birthDate.equals("")) {
			notAllFieldsCompleted.setVisibility(View.VISIBLE);
		} else {
			new InsertIntoDatabase().execute(params[0], params[1], params[2], params[3], firstName, lastName, city, birthDate, String.valueOf(smokerCBValue), bio);
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
//			List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
//			urlParams.add(new BasicNameValuePair("username", username));
//			urlParams.add(new BasicNameValuePair("password", password));

			String url = SERVER_URL + "/insert_users.php";
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse;
			JSONObject httpJson = new JSONObject();
			
//			try {
//				HttpPost httpPost = new HttpPost(url);
////				//httpPost.setEntity(new UrlEncodedFormEntity(urlParams));
////
////				HttpResponse httpResponse = httpClient.execute(httpPost);
////				HttpEntity httpEntity = httpResponse.getEntity();
////
////				is = httpEntity.getContent();
//			} catch (Exception e) {
//				// something bad happened
//			}
//
//			try {
//				BufferedReader br = new BufferedReader(
//						new InputStreamReader(is));
//				StringBuilder sb = new StringBuilder();
//				String line = "";
//
//				while ((line = br.readLine()) != null) {
//					sb.append(line + "\n");
//				}
//
//				is.close();
//				result = sb.toString();
//			} catch (Exception e) {
//				// bad
////			}
			return null;
		}
		
	}
}

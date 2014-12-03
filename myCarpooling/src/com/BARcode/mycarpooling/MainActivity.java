package com.BARcode.mycarpooling;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	public final static String LOGIN_INFO = "com.BARcode.mycarpooling.LOGIN_INFO";

	private ProgressDialog progressMessage;
	
	private Intent loginIntent;
	
	private TextView loginFailed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// hide action bar on start page
		ActionBar actionBar = getActionBar();
		actionBar.hide();

        
        setContentView(R.layout.activity_main);
        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	// cand apesi butonul de logIN se intra in metoda asta
	public void logIN(View view) {
		loginIntent = new Intent(this, ChooseRole.class);
		// get username
		EditText userNameET = (EditText) findViewById(R.id.userName);
		// get password
		EditText passwordET = (EditText) findViewById(R.id.password);

		String userName = userNameET.getText().toString();
		String password = passwordET.getText().toString();

		// check login
		loginFailed = (TextView) findViewById(R.id.loginFailed);
		if (userName.equals("") || password.equals("")) {
			loginFailed.setVisibility(View.VISIBLE);
		} else {
			loginFailed.setVisibility(View.INVISIBLE);

			new SignInActivity().execute(userName, password);

		}
	}

	public void signUP(View view) {
		Intent intent = new Intent(this, SignUpActivity1.class);
		startActivity(intent);
	}

	class SignInActivity extends AsyncTask<String, Void, String> {
		InputStream is = null;
		String result = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressMessage = new ProgressDialog(MainActivity.this);
			progressMessage.setMessage("Loading ...");
			progressMessage.setIndeterminate(false);
			progressMessage.setCancelable(false);
			progressMessage.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String username = (String) params[0];
			String password = (String) params[1];
			//String url = "http://simurg.site40.net/list_user_username.php?";
			String link = String.format("http://simurg.site40.net/list_user_username.php?username=%s&password=%s", username, password);

			try {				
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet();
				request.setURI(new URI(link));
				
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
				
				progressMessage.dismiss();
				
				if (result.equals("null\n")) {
					// username not found in DB
					runOnUiThread(new Runnable() {
					     @Override
					     public void run() {
					    	 loginFailed.setVisibility(View.VISIBLE);
					    }
					});
				} else {
					loginIntent.putExtra(LOGIN_INFO, result);
					startActivity(loginIntent);
				}
				
			} catch (Exception e) {
				Log.e("ERROR:", e.getMessage());
			}
						
//			List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
//			urlParams.add(new BasicNameValuePair("username", username));
//			urlParams.add(new BasicNameValuePair("password", password));
//
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpPost httpPost = new HttpPost(url);
//
//			try {
//				httpPost.setEntity(new UrlEncodedFormEntity(urlParams));
//
//				HttpResponse httpResponse = httpClient.execute(httpPost);
//				HttpEntity httpEntity = httpResponse.getEntity();
//
//				is = httpEntity.getContent();
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
//			}

			return null;
		}
		
		@Override
		protected void onPostExecute(String v) {
			try {
				JSONArray Jarray = new JSONArray(result);
				for (int i = 0; i < Jarray.length(); i++) {
					JSONObject Jasonobject = null;
					Jasonobject = Jarray.getJSONObject(i);

					// get an output on the screen
					String name = Jasonobject.getString("username");
					Log.v("from_db", name);
				}
				
				progressMessage.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("log_tag", "Error parsing data " + e.toString());
			}
		}
	}
}

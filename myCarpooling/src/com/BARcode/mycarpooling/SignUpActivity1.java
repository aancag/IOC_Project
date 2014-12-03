package com.BARcode.mycarpooling;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity1 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Intent intent = getIntent();
		setContentView(R.layout.fragment_sign_up_activity1);
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

	public void signUP2(View view){
	
		// check if passwords are equal
		EditText usernameET = (EditText)findViewById(R.id.userNameSignUp);
		EditText passwordET = (EditText)findViewById(R.id.signUpPassword);
		EditText retypedPasswordET = (EditText) findViewById(R.id.signUpRePassword);
		EditText emailET = (EditText) findViewById(R.id.email);
		EditText phoneET = (EditText) findViewById(R.id.phone);
		
		
		String username = usernameET.getText().toString();
		String password = passwordET.getText().toString();
		String retypedPassword = retypedPasswordET.getText().toString(); 
		
		String email = emailET.getText().toString();
		String phone = phoneET.getText().toString();
	
		boolean allFieldsCompleted = username.equals("") || password.equals("")
				|| retypedPassword.equals("") || email.equals("") 
				|| phone.equals("");
		
		TextView passMatchFailed = (TextView) findViewById(R.id.passMatchFailed);

		if (!password.equals(retypedPassword) || allFieldsCompleted) {
			passMatchFailed.setVisibility(View.VISIBLE);
			usernameET.setText("");
			passwordET.setText("");
			retypedPasswordET.setText("");
			emailET.setText("");
		} else {
			passMatchFailed.setVisibility(View.INVISIBLE);
			
			userExists = (TextView) findViewById(R.id.userExists);
			
			signUpActivity2Intent = new Intent(this, SignUpActivity2.class);
			// check if user with username already exists in DB
			new SignUpActivity().execute(username);
		}
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_sign_up_activity1, container, false);
			return rootView;
		}
	}
	
	private ProgressDialog progressMessage;
	
	private TextView userExists;
	
	private Intent signUpActivity2Intent;
	
	// connect to DB
	class SignUpActivity extends AsyncTask<String, Void, String> {
		String result = "";
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressMessage = new ProgressDialog(SignUpActivity1.this);
			progressMessage.setMessage("Loading ...");
			progressMessage.setIndeterminate(false);
			progressMessage.setCancelable(false);
			progressMessage.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			String username = (String) params[0];
			String link = String.format("http://simurg.site40.net/list_user_username.php?username=%s", username);
			
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
				
				if (!result.equals("null\n")) {
					// username found in DB
					runOnUiThread(new Runnable() {
					     @Override
					     public void run() {
					    	 userExists.setVisibility(View.VISIBLE);
					    }
					});
				} else {
					runOnUiThread(new Runnable() {
					     @Override
					     public void run() {
					    	 userExists.setVisibility(View.INVISIBLE);
					    	 startActivity(signUpActivity2Intent);
					    }
					});
				}
			} catch (Exception e) {
				Log.e("ERROR:", e.getMessage());
			}
			
			return null;
		}
		
		
	}
}

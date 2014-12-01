package com.BARcode.mycarpooling;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
		// TODO: check if user with this username already exists in DB
		
		// check if passwords are equal
		EditText passwordET = (EditText) findViewById(R.id.signUpPassword);
		EditText retypedPasswordET = (EditText) findViewById(R.id.signUpRePassword);
				
		String password = passwordET.getText().toString();
		String retypedPassword = retypedPasswordET.getText().toString(); 

		TextView passMatchFailed = (TextView) findViewById(R.id.passMatchFailed);
		if (!password.equals(retypedPassword)) {
			passMatchFailed.setVisibility(View.VISIBLE);
			passwordET.setText("");
			retypedPasswordET.setText("");
		} else {
			passMatchFailed.setVisibility(View.INVISIBLE);
			Intent intent = new Intent(this, SignUpActivity2.class);
			startActivity(intent);
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
}

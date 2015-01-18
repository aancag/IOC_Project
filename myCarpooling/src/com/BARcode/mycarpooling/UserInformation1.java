package com.BARcode.mycarpooling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.BARcode.databaseModels.User;

public class UserInformation1 extends Activity {

	public static User user = MainActivity.userLoggedIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_information_1);
	
		((EditText) findViewById(R.id.usernameUI)).setText(user.getUsername());
		// username cannot be changed - PK in DBs
		((EditText) findViewById(R.id.usernameUI)).setKeyListener(null);;
		
		((EditText) findViewById(R.id.passwordUI)).setText(user.getPassword());
		((EditText) findViewById(R.id.firstNameUI)).setText(user.getFirstName());
		((EditText) findViewById(R.id.lastNameUI)).setText(user.getLastName());
		((EditText) findViewById(R.id.birthDateUI)).setText(user.getBirthDate());
		((EditText) findViewById(R.id.bioUI)).setText(user.getBio());
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == 16908332) {
			Class<?> goUpClass = MainActivity.isDriver ? Driver.class : Passenger.class;
			Intent intent = new Intent(UserInformation1.this, goUpClass);
			startActivity(intent);
			
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	// go to next page which displays joined carpools
	public void nextUserInformation(View view) {
		// take changes		
		user.setPassword(((EditText) findViewById(R.id.passwordUI)).getText().toString());
		user.setFirstName(((EditText) findViewById(R.id.firstNameUI)).getText().toString());
		user.setLastName(((EditText) findViewById(R.id.lastNameUI)).getText().toString());
		user.setBirthDate(((EditText) findViewById(R.id.birthDateUI)).getText().toString());
		user.setBio(((EditText) findViewById(R.id.bioUI)).getText().toString());
			
		// start next activity
		Intent intent = new Intent(UserInformation1.this, UserInformation2.class);
		startActivity(intent);
	}
	
}

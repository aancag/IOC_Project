package com.BARcode.mycarpooling;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.BARcode.databaseModels.User;

public class UserInformation2 extends Activity {

	public static User user = UserInformation1.user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_information_2);
		
		((EditText) findViewById(R.id.emailUI)).setText(user.getEmail());
		((EditText) findViewById(R.id.phoneUI)).setText(user.getPhone());
		((EditText) findViewById(R.id.cityUI)).setText(user.getCity());
		((CheckBox) findViewById(R.id.smokerUI)).setChecked(user.getSmoker() == 1);
		
		// get joinedCarpools from DB
		
	}
	
	// save changes in DB
	public void saveChangesIntoDB(View view) {
		
		
		
		MainActivity.userLoggedIn = user;
	}
	
}

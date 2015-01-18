package com.BARcode.mycarpooling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseRole extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_role);
	}
	
	//cand dai click pe poza driver-ului
	public void LogoDriver(View view){
		MainActivity.isDriver = true;
		Intent intent = new Intent(this, Driver.class);
		startActivity(intent);
	}

	//cand dai click pe poza passenger-ului
	public void LogoPassenger(View view){
		MainActivity.isDriver = false;
		Intent intent = new Intent(this, Passenger.class);
		startActivity(intent);
	}
}

package com.BARcode.mycarpooling;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import databaseModels.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class Driver extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver);
		//show available carpools from databse
		EditText availableCarpool = (EditText)findViewById(R.id.availableCarpool);
		
	
		JSONObject data = null;
		try {
			data = new JSONArray(MainActivity.result).getJSONObject(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		User user = new User(data);
		
		availableCarpool.setText(user.getEmail());
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.driver, menu);
		return true;
	}

	public void addCar(View view){
		Intent intent = new Intent(this, AddEditCar.class);
    	startActivity(intent);
	}
	public void addCarpool(View view){
		Intent intent = new Intent(this, AddCarpool.class);
    	startActivity(intent);
	}
	public void showHistory(View view){
		Intent intent = new Intent(this, ShowHistory.class);
    	startActivity(intent);
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
}

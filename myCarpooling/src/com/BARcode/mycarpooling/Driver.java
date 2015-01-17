package com.BARcode.mycarpooling;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.BARcode.databaseModels.User;

public class Driver extends Activity {

	private RatingBar ratingBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver);
		EditText availableCarpool = (EditText)findViewById(R.id.availableCarpool);
		
		JSONObject data = null;
		try {
			data = new JSONArray(MainActivity.result).getJSONObject(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		User user = new User(data);
		
		ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
		ratingBar.setRating(user.getRankingDriver());
		ratingBar.setIsIndicator(true);
	
		
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
	
}

package com.BARcode.mycarpooling;

import static com.BARcode.utilities.Constants.ADD_CARPOOL;
import static com.BARcode.utilities.Utilities.fieldsValid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddCarpool2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_carpool2);
	}
	
	public void nextOptions(View view) {
		EditText dateET = (EditText) findViewById(R.id.carpoolDate);
		EditText hourET = (EditText) findViewById(R.id.carpoolTime);
		EditText priceET = (EditText) findViewById(R.id.carpoolPrice);
		EditText durationET = (EditText) findViewById(R.id.carpoolDuration);
		
		String date = dateET.getText().toString();
		String hour = hourET.getText().toString();
		String price = priceET.getText().toString();
		String duration = durationET.getText().toString();
		
		TextView notAllFieldsCompleted = (TextView) findViewById(R.id.notAllFieldsCompletedAC2);
		if (!fieldsValid(date, hour, price, duration)) {
			notAllFieldsCompleted.setVisibility(View.VISIBLE);
		} else {
			notAllFieldsCompleted.setVisibility(View.INVISIBLE);
			
			String[] params_from_parent = this.getIntent().getStringArrayExtra(
					ADD_CARPOOL);
			int length = params_from_parent.length;
			String[] params = new String[length + 4];
			for (int i = 0; i < params_from_parent.length; i++) {
				params[i] = params_from_parent[i];
			}
			params[length] = date; 
			params[length + 1] = hour; params[length + 2] = price;
			params[length + 3] = duration;
			
			Intent intent = new Intent(this, AddCarpool3.class);
			intent.putExtra(ADD_CARPOOL, params);
	    	startActivity(intent);
		}
	}
	
}

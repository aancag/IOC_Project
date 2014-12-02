package com.BARcode.mycarpooling;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity {

	 public final static String LOGIN_INFO = "com.BARcode.mycarpooling.LOGIN_INFO";
	
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
    
    //cand apesi butonul de logIN se intra in metoda asta 
    public void logIN(View view){
    	Intent intent = new Intent(this, ChooseRole.class);
    	// get username 
    	EditText userNameET = (EditText) findViewById(R.id.userName);
    	// get password
    	EditText passwordET = (EditText) findViewById(R.id.password);
    	
    	String userName = userNameET.getText().toString();
    	String password = passwordET.getText().toString();
    	
    	// check login
    	TextView loginFailed = (TextView) findViewById(R.id.loginFailed);
    	if (userName.equals("") || password.equals("")) {
    		loginFailed.setVisibility(View.VISIBLE);
    	} else {
    		loginFailed.setVisibility(View.INVISIBLE);
    		
    		// TODO: connect to database	
    		
    	intent.putExtra(LOGIN_INFO, userName);
        startActivity(intent);
    	}
    }
    
    public void signUP(View view){
    	Intent intent = new Intent(this, SignUpActivity1.class);
    	startActivity(intent);
    	
    }
}

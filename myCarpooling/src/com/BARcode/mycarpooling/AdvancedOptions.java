package com.BARcode.mycarpooling;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.BARcode.googleMaps.MapActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AdvancedOptions extends Activity {

	private GoogleMap map;
	private int count = 0;
	private boolean sourceMarker = false;
	private boolean destinationMarker = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced_options);
		
		
		/*mapActivity = new MapActivity();
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.map, mapActivity);
		fragmentTransaction.commit();*/
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
		        .getMap();
		CameraUpdate romanica =
			        CameraUpdateFactory.newLatLng(new LatLng(44.51,24.9));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo((float) 5.6);
		
		map.moveCamera(romanica);
		map.animateCamera(zoom);
		
	
	
		map.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng coord) {
				count++;
				if(count == 1){
					sourceMarker = true;
					map.addMarker(new MarkerOptions()
							.position(coord)
							.draggable(true)
							.title("Source"));
				}
				if(count == 2){
					destinationMarker = true;
					map.addMarker(new MarkerOptions()
							.position(coord)
							.draggable(true)
							.title("Destination"));
				}	
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.advanced_options, menu);
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
}

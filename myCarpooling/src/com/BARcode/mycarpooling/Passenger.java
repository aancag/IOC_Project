package com.BARcode.mycarpooling;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.BARcode.googleMaps.Route;
import com.BARcode.googleMaps.Routing;
import com.BARcode.googleMaps.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class Passenger extends Activity implements RoutingListener {

	private GoogleMap map;
	private int count = 0;
	private LatLng srcCoord;
	private LatLng dstCoord;
	private Routing routing;
	
	private String source;
	private String destination;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_passenger);
		
		final MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
		
		map = mapFragment.getMap();
		
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(44.51,24.9));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo((float) 5.6);

        map.moveCamera(center);
        map.animateCamera(zoom);
        routing = new Routing(Routing.TravelMode.DRIVING);
        routing.registerListener(this);
        
        map.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng coord) {
				count++;
				if(count == 1){
					map.addMarker(new MarkerOptions()
							.position(coord)
							.draggable(true)
							.title("Source"));
					srcCoord = coord;
				}
				if(count == 2){
					map.addMarker(new MarkerOptions()
							.position(coord)
							.draggable(true)
							.title("Destination"));
					dstCoord = coord;
					routing.execute(srcCoord, dstCoord);
					
//					System.out.println("uiteeeSRC: " + getAddress(srcCoord.latitude, srcCoord.longitude));
//					System.out.println("uiteeeDST: " + getAddress(dstCoord.latitude, dstCoord.longitude));
					EditText src = (EditText) findViewById(R.id.sourcePassenger);
					EditText dst = (EditText) findViewById(R.id.destinationPassenger);
					
					source = getAddress(srcCoord.latitude, srcCoord.longitude)
							.replace("ă", "a")
							.replace("ț", "t")
							.replace("ș", "s")
							.replace("î", "i")
							.replace("â", "a");
					destination = getAddress(dstCoord.latitude, dstCoord.longitude)
							.replace("ă", "a")
							.replace("ț", "t")
							.replace("ș", "s")
							.replace("î", "i")
							.replace("â", "a");
										
					src.setText(source);
					dst.setText(destination);
				}
				
				
			}
		}); 
	}
	
	/*
	 * Intoarce numele orasului in functie de coordonate
	 * Daca nu te afli intr-un oras, intoarce numele tarii
	 */
	private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality());
               // result.append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag--mai si pierzi", e.getMessage());
        }

        return result.toString();
	}

	@Override
	public void onRoutingFailure() {
		
	}

	@Override
	public void onRoutingStart() {
		
	}

	@Override
	public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route) {
		 PolylineOptions polyoptions = new PolylineOptions();
	      polyoptions.color(Color.BLUE);
	      polyoptions.width(10);
	      polyoptions.addAll(mPolyOptions.getPoints());
	      map.addPolyline(polyoptions);

	      // Start marker
	      MarkerOptions options = new MarkerOptions();
	      options.position(srcCoord);
	      //options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue));
	      map.addMarker(options);

	      // End marker
	      options = new MarkerOptions();
	      options.position(dstCoord);
	      //options.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green));  
	      map.addMarker(options);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_edit_car, menu);
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
	
	/************************** END INTERFACE ***************************/
	public void searchResults(View view){
	/*	EditText source = (EditText) findViewById(R.id.sourcePassenger);
		EditText destination = (EditText) findViewById(R.id.destinationPassenger);
		
		Intent intent = new Intent(this, SearchResults.class);
		intent.putExtra("source", source.getText().toString());
		intent.putExtra("destination", destination.getText().toString());
		
		startActivity(intent);*/
		
		Intent intent = new Intent(this, SearchCarpools.class);
		intent.putExtra("source", ((EditText) findViewById(R.id.sourcePassenger)).getText().toString());
		intent.putExtra("destination", ((EditText) findViewById(R.id.destinationPassenger)).getText().toString());
		startActivity(intent);
	}
	
	public void advancedOptions(View view){
		//Intent intent = new Intent(this, AdvancedOptions.class);
		//startActivity(intent);
	}
}

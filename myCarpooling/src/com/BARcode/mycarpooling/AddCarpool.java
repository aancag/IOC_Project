package com.BARcode.mycarpooling;

import static com.BARcode.utilities.Constants.ADD_CARPOOL;
import static com.BARcode.utilities.Utilities.fieldsValid;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.BARcode.googleMaps.Route;
import com.BARcode.googleMaps.Routing;
import com.BARcode.googleMaps.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddCarpool extends Activity implements RoutingListener {

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
		setContentView(R.layout.activity_add_carpool);

		integrateMaps();
	}

	public void integrateMaps() {
		final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

		map = mapFragment.getMap();

		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(46.10, 25.19));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo((float) 5.6);

		map.moveCamera(center);
		map.animateCamera(zoom);
		routing = new Routing(Routing.TravelMode.DRIVING);
		routing.registerListener(this);

		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng coord) {
				count++;
				if (count == 1) {
					map.addMarker(new MarkerOptions().position(coord).draggable(true).title("Source"));
					srcCoord = coord;
				}
				if (count == 2) {
					map.addMarker(new MarkerOptions().position(coord).draggable(true).title("Destination"));
					dstCoord = coord;
					routing.execute(srcCoord, dstCoord);

					// System.out.println("uiteeeSRC: " +
					// getAddress(srcCoord.latitude, srcCoord.longitude));
					// System.out.println("uiteeeDST: " +
					// getAddress(dstCoord.latitude, dstCoord.longitude));
					EditText src = (EditText) findViewById(R.id.addSource);
					EditText dst = (EditText) findViewById(R.id.addDestination);

					source = getAddress(srcCoord.latitude, srcCoord.longitude).replace("ă", "a").replace("ț", "t").replace("ș", "s").replace("î", "i").replace("â", "a");
					destination = getAddress(dstCoord.latitude, dstCoord.longitude).replace("ă", "a").replace("ț", "t").replace("ș", "s").replace("î", "i").replace("â", "a");

					src.setText(source);
					dst.setText(destination);
				}

			}
		});
	}

	/*
	 * Intoarce numele orasului in functie de coordonate Daca nu te afli intr-un
	 * oras, intoarce numele tarii
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

	// cand se intra pe butonul de next
	public void nextOptions(View view) {
		EditText sourceET = (EditText) findViewById(R.id.addSource);
		EditText destinationET = (EditText) findViewById(R.id.addDestination);

		String source = sourceET.getText().toString();
		String destination = destinationET.getText().toString();

		TextView notAllFieldsCompleted = (TextView) findViewById(R.id.notAllFieldsCompletedAC);
		if (!fieldsValid(source, destination)) {
			notAllFieldsCompleted.setVisibility(View.VISIBLE);
		} else {
			notAllFieldsCompleted.setVisibility(View.INVISIBLE);

			String[] params = new String[2];
			params[0] = source;
			params[1] = destination;

			Intent intent = new Intent(this, AddCarpool2.class);
			intent.putExtra(ADD_CARPOOL, params);
			startActivity(intent);
		}
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

}

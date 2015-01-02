package com.BARcode.googleMaps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends MapFragment 
		implements OnMapReadyCallback {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapReady(GoogleMap map) {
		// TODO Auto-generated method stub
		map.addMarker(new MarkerOptions()
			.position(new LatLng(44.26,26))
			.title("Marker"));
	}
}

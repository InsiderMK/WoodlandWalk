package com.insidermk.woodlandwalk;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


public class GpsService {
	private Context context;
    private Location loc;
	private LocationManager locManager;
	private LocationListener view;
	
	private LocationListener locListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			view.onStatusChanged(provider, status, extras);
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			view.onProviderEnabled(provider);
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			view.onProviderDisabled(provider);
		}
		
		@Override
		public void onLocationChanged(Location location) {
			loc = location;
			view.onLocationChanged(location);
		}
	};
	
	public GpsService(Context c, LocationListener v) {
		context = c;
		view = v;
		
		init();
	}

	private void init() {
		locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locListener);
		loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
	
	public Location getLocation() {
		return loc;
	}
	
	public void Destroy() {
		locManager.removeUpdates(locListener);
	}
}

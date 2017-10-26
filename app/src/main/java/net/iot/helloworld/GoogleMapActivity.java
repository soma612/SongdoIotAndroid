package net.iot.helloworld;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.SupportActionModeWrapper;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class GoogleMapActivity extends AppCompatActivity {

    SupportMapFragment mapFragment;
    GoogleMap map;

    class Loc {
        String title;
        double latitude, longitude;

        Loc(String title, double latitude, double longitude) {
            this.title = title;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    ArrayList<Loc> locList = new ArrayList<Loc>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        locList.add(new Loc("내집", 37.454618, 126.688903));
        locList.add(new Loc("여의나루역", 37.527094, 126.932851));
        locList.add(new Loc("신촌역", 37.559798, 126.942325));
        locList.add(new Loc("용산역", 37.529881, 126.964818));
        locList.add(new Loc("고잔역", 37.317051, 126.823105));

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                for (int i = 0; i < locList.size(); i++) {
                    MarkerOptions marker = new MarkerOptions();
                    marker.position(new LatLng(locList.get(i).latitude, locList.get(i).longitude));
                    marker.title(locList.get(i).title);
                    map.addMarker(marker);
                }
            }
        });
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                String message = "Last known location - lat:" + location.getLatitude() + ", long:" + location.getLongitude();
                Toast.makeText(GoogleMapActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        long time = 10000;
        float dist = 0;
        GPSListener gpsListener = new GPSListener();
        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time, dist, gpsListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private class GPSListener implements android.location.LocationListener {
        public void onLocationChanged(Location location) {
            String message = "Current location - lat:" + location.getLatitude() + ", long:" + location.getLongitude();
            Toast.makeText(GoogleMapActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}

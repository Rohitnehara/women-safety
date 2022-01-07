package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.womensafety.model.Mylocation;
import com.example.womensafety.model.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.womensafety.databinding.ActivityMapsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
//host
    private TextView textView;
    private Mylocation mylocation;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference ref,hostRef;
    private LocationManager manager;
    Location location;
    LocationListener locationListener;
    private final int MIN_TIME=1000;
    private final int MIN_DISTANCE =1;
    Marker myMarker;
FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();


       textView=findViewById(R.id.textView2);
        Random r = new Random();
        int randomNumber = r.nextInt(100000);
       textView.setText(String.valueOf(randomNumber));
       auth=FirebaseAuth.getInstance();
       String userKaId=auth.getUid();

        assert userKaId != null;
        auth= FirebaseAuth.getInstance();
        String usersId=auth.getCurrentUser().getUid();
        ref= database.getReference().child("Users").child(usersId);

        SharedPreferences sharedPreferences=getSharedPreferences("shared_Pref",MODE_PRIVATE);
        String userFeromshare=sharedPreferences.getString("userName","nahi chala");
                hostRef=database.getReference().child("Hosts").child(userFeromshare);
                manager=(LocationManager)getSystemService(LOCATION_SERVICE);
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                assert mapFragment != null;
                mapFragment.getMapAsync( MapsActivity.this);
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                getLocationUpdates();

//        if(!TextUtils.isEmpty(ds))
//        {
//            hostRef=database.getReference().child("Hosts").child(ds).child(String.valueOf(randomNumber));
//            manager=(LocationManager)getSystemService(LOCATION_SERVICE);
//            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                    .findFragmentById(R.id.map);
//            assert mapFragment != null;
//            mapFragment.getMapAsync(this);
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
//            getLocationUpdates();
//        }
//        else {
//            ref = database.getReference().child("Hosts").child(userKaId).child(String.valueOf(randomNumber));
//            manager = (LocationManager) getSystemService(LOCATION_SERVICE);
//            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                    .findFragmentById(R.id.map);
//            assert mapFragment != null;
//            mapFragment.getMapAsync(this);
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
//            getLocationUpdates();
//        }
  //  readChanges();
    }

//    private void readChanges() {
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()) {
//                    try {
//                        Mylocation location = snapshot.getValue(Mylocation.class);
//                        if (location != null) {
//                              myMarker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
//                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),10));
//                        }
//                    } catch (Exception e) {
//                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void getLocationUpdates() {
        if(manager!=null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                    
                } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                } else {
                    Toast.makeText(getApplicationContext(), "No provider enabled", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==101)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLocationUpdates();
            }else {
                Toast.makeText(getApplicationContext(), "Permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//database=FirebaseDatabase.getInstance();
//ref=database.getReference().child("User-6969");
//ref.addValueEventListener(new ValueEventListener() {
//    @Override
//    public void onDataChange(@NonNull DataSnapshot snapshot) {  if(snapshot.exists()) {
//                    try {
//                        Mylocation location = snapshot.getValue(Mylocation.class);
//                        if (location != null) {
//                            mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("Marker in Sydney"));
////mMap.setMinZoomPreference(12);
////mMap.getUiSettings().setAllGesturesEnabled(true);
//                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),10));   }
//                    } catch (Exception e) {
//                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError error) {
//
//    }
//});


        // Add a marker in Sydney and move the camera
       // LatLng ltt=new LatLng(mylocation.getLatitude(),mylocation.getLongitude());
//       LatLng sydney = new LatLng(39, -121);
//        //LatLng myLatln=new LatLng(mylocation.getLatitude(),mylocation.getLongitude());
//      mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
////mMap.setMinZoomPreference(12);
////mMap.getUiSettings().setAllGesturesEnabled(true);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,10));
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
         if(location !=null){
             saveLocation(location);
             drawMarker(location, getText(R.string.i_am_here).toString());
             //manager.removeUpdates(locationListener);
         }
         else
         {
             Toast.makeText(getApplicationContext(), "Location nahi mill rahi", Toast.LENGTH_SHORT).show();
         }
    }

    private void saveLocation(Location location) {
        hostRef.setValue(location);
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {

    }

    @Override
    public void onFlushComplete(int requestCode) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
    private void drawMarker(Location location, String title) {
        if (this.mMap != null) {
            mMap.clear();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(title);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }
}
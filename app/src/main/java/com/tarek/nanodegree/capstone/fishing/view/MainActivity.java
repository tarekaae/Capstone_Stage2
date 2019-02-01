package com.tarek.nanodegree.capstone.fishing.view;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.tarek.nanodegree.capstone.fishing.R;
import com.tarek.nanodegree.capstone.fishing.model.FirebaseHandler;
import com.tarek.nanodegree.capstone.fishing.model.pojo.Spot;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {


    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, NewSpotActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        new WidgetTask().execute();
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestRuntimePermission();
        } else {
            enableLocation();
        }
        ValueEventListener spotsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Spot> spotArrayList = new ArrayList<>();

                for (DataSnapshot spotSnapshot : dataSnapshot.getChildren()) {
                    Spot spot = spotSnapshot.getValue(Spot.class);
                    spotArrayList.add(spot);
                }

                drawMarkers(spotArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Spot failed, log a message
                Log.w("mainAct", "loadPost:onCancelled", databaseError.toException());

            }
        };
        FirebaseHandler.getInstance(MainActivity.this).getDatabaseReference().addValueEventListener(spotsListener);
    }

    private void drawMarkers(ArrayList<Spot> spotArrayList) {

        mMap.clear();
        for (int i = 0; i < spotArrayList.size(); i++) {

            Spot spot = spotArrayList.get(i);

            LatLng newLatLng = new LatLng(spot.getLat(), spot.getLng());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(newLatLng);
            markerOptions.title(spot.getDepth() + " M");
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(spot);

        }

    }

    private void requestRuntimePermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            enableLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    enableLocation();
                }

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void enableLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//
        LatLng hurghada = new LatLng(27.2427078, 33.8486703);
        mMap.addMarker(new MarkerOptions().position(hurghada).title("Hurghada"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hurghada, 9f));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Spot selectedSpot = (Spot) marker.getTag();
        Intent intent = new Intent(MainActivity.this, SpotDetailsActivity.class);
        intent.putExtra("spot", new Gson().toJson(selectedSpot));
        startActivity(intent);

        return false;
    }


    class WidgetTask extends AsyncTask<Void, Void, DatabaseReference> {
        @Override
        protected DatabaseReference doInBackground(Void... voids) {

            return FirebaseHandler.getInstance(MainActivity.this).getDatabaseReference();
        }

        @Override
        protected void onPostExecute(DatabaseReference result) {
            super.onPostExecute(result);
            ValueEventListener spotsListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Spot> spotArrayList = new ArrayList<>();

                    String fishSpecies = "";
                    for (DataSnapshot spotSnapshot : dataSnapshot.getChildren()) {
                        Spot spot = spotSnapshot.getValue(Spot.class);
                        spotArrayList.add(spot);
                        List<String> all = spot.getSpecies();

                        if(all!=null){
                            for (int j = 0; j < all.size(); j++) {
                                fishSpecies = fishSpecies + "-" + all.get(j) ;
                            }
                        }

                    }

                    SharedPreferences preferences = getSharedPreferences("pref", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(SpotsWidget.TOTAL_SPOTS, spotArrayList.size()+"");
                    editor.putString(SpotsWidget.FISH_SPECIES, fishSpecies);
                    editor.apply();

                    int[] ids = AppWidgetManager.getInstance(MainActivity.this).getAppWidgetIds(new ComponentName(MainActivity.this, SpotsWidget.class));
                    SpotsWidget mWidget = new SpotsWidget();
                    mWidget.onUpdate(MainActivity.this, AppWidgetManager.getInstance(MainActivity.this), ids);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Spot failed, log a message
                    Log.w("mainAct", "loadPost:onCancelled", databaseError.toException());

                }
            };

            result.addValueEventListener(spotsListener);

        }
    }

}

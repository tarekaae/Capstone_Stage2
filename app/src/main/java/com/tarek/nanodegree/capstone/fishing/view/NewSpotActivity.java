package com.tarek.nanodegree.capstone.fishing.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;

import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tarek.nanodegree.capstone.fishing.R;
import com.tarek.nanodegree.capstone.fishing.model.FirebaseHandler;
import com.tarek.nanodegree.capstone.fishing.model.pojo.Spot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NewSpotActivity extends AppCompatActivity implements LocationListener {

    @BindView(R.id.water_depth)
    EditText waterDepth;
    @BindView(R.id.species)
    EditText species;
    @BindView(R.id.date)
    TextView date;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private double lat, lng;
    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_spot);
        ButterKnife.bind(this);

        // pre populate date
        String pattern = "dd-MMM-yyyy     HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateStr = simpleDateFormat.format(new Date());
        date.setText(dateStr);


        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    }

    @Override
    protected void onPause() {

        mLocationManager.removeUpdates(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkVersionAndStart();
    }

    private void checkVersionAndStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestRuntimePermission();
        } else {
            requestLocationUpdate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_spot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:

                if (!(waterDepth.getText().toString().isEmpty()) &&
                        !(species.getText().toString().isEmpty())) {
                    handleDoneBtn();
                } else {

                    Toast.makeText(this, getResources().getString(R.string.please_fill_fields), Toast.LENGTH_SHORT).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleDoneBtn() {
        Spot spot = new Spot();
        spot.setId(System.currentTimeMillis());
        spot.setDateTime(date.getText().toString());
        String depthInput = waterDepth.getText().toString();
        if (!depthInput.isEmpty()) {
            spot.setDepth(Integer.parseInt(depthInput));
        }
        String speciesInput = species.getText().toString();
        if (!speciesInput.isEmpty()) {
            String[] species = speciesInput.split(",");
            List<String> list = new ArrayList<>();
            for (int i = 0; i < species.length; i++) {
                list.add(species[i]);
            }
            spot.setSpecies(list);
        }

        spot.setLat(lat);
        spot.setLng(lng);

        saveSpot(spot);
        finish();
    }


    private void saveSpot(Spot spot) {
        FirebaseHandler.getInstance(NewSpotActivity.this).saveTOFirebase(spot);
    }

    private void requestRuntimePermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        } else {
            requestLocationUpdate();
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

                    requestLocationUpdate();
                }

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }


    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        lng = location.getLongitude();

     }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

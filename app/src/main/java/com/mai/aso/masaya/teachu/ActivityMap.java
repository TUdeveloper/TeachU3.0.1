package com.mai.aso.masaya.teachu;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class ActivityMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private LatLng latLng;
    private String locationName,locationAddress;
    private Button buttonHere;
    private static final String TAG = ActivityMap.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        buttonHere = (Button)findViewById(R.id.act_map_here);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //阿蘓　グーグルのPlace Autocompleteを使って候補を挙げてくれるフラグメントを用意
        //https://developers.google.com/places/android-api/autocomplete
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                latLng = place.getLatLng();
                locationName = place.getName().toString();
                locationAddress = place.getAddress().toString();
                Log.i(TAG, "Location:latitude: " + place.getLatLng().latitude);
                Log.i(TAG, "Location:Address: " + place.getAddress());
                Log.i(TAG, "Location:Web: " + place.getWebsiteUri());
                Log.i(TAG, "Location:Place: " + place.getName());
                mMap.addMarker(new MarkerOptions().position(latLng).title(locationName).snippet(locationAddress)).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        //阿蘓　intentに乗せて選んだ場所の名前とアドレスをActivityMeetingCardに戻す
        buttonHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_map = new Intent();
                intent_map.putExtra("locationName",locationName);
                intent_map.putExtra("locationAddress",locationAddress);
                intent_map.putExtra("locationLat",latLng.latitude);
                intent_map.putExtra("locationLng",latLng.longitude);
                setResult(1, intent_map);
                finish();
            }
        });
    }

    //Place Autocompleteで必要（今はつかわれてないかも）
    //検索結果が出た時の操作
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }



    public void onSearch(View view) {
        EditText location_tf = (EditText) findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            //String addressLine = address.getAddressLine(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            Log.d(TAG, "LocationInfo:AddressLine:" + address.getAddressLine(0));
            Log.d(TAG, "LocationInfo:FeatureName:" + address.getFeatureName());
            Log.d(TAG, "LocationInfo:AdminArea:" + address.getAdminArea());
            Log.d(TAG, "LocationInfo:Locality:" + address.getLocality());
            Log.d(TAG, "LocationInfo:Url:" + address.getUrl());
            Log.d(TAG, "LocationInfo:Latitude:" + address.getLatitude());
            Log.d(TAG, "LocationInfo:Longitude:" + address.getLongitude());
        }
    }


    public void onZoom(View view) {
        if (view.getId() == R.id.Bzoomin) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if (view.getId() == R.id.Bzoomout) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    public void changeview(View view) {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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

        CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(35.68, 139.76), 12);
        //mMap.addMarker(new MarkerOptions().position(new LatLng(35.68, 139.76)).title("Marker in Tokyo"));
        mMap.moveCamera(cUpdate);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        mMap.setTrafficEnabled(true);
    }
}

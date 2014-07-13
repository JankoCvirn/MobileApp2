package com.cvirn.mobileapp2.survey.poi;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.datasources.PoiDS;
import com.cvirn.mobileapp2.survey.model.POI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class NewPoiActivity extends FragmentActivity implements GooglePlayServicesClient
.ConnectionCallbacks ,GooglePlayServicesClient.OnConnectionFailedListener,LocationListener {

    private static final String TAG ="MainMap" ;
    PoiDS poids;
    ArrayList<POI> poi_list;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    LocationClient mLocation;
    private static final float def_zoom=15;
    private static final float zoom_to=18;
    Marker poi;
    Button btnDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poi);

        //getPoiData();

        if(setUpMapIfNeeded()){


            mMap.setMyLocationEnabled(true);
            mLocation=new LocationClient(this,this,this);
            mLocation.connect();

        }

        final ActionBar actionBar = getActionBar();

        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setDisplayUseLogoEnabled(false);

        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        setTitle("");

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {


                if(poi!=null){
                    poi.remove();
                }

                    MarkerOptions mOptions = new MarkerOptions().position(latLng).flat(false)
                            .title("New POI!");

                    poi = mMap.addMarker(mOptions);


            }
        });

        btnDetails=(Button)findViewById(R.id.buttonDetails);
        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (poi!=null){
                    Intent iExp=new Intent(NewPoiActivity.this,NewPoiDetailsActivity.class);
                    iExp.putExtra("lat",poi.getPosition().latitude);
                    iExp.putExtra("lng",poi.getPosition().longitude);
                    startActivity(iExp);
                }
                else{
                    Toast.makeText(NewPoiActivity.this,"No POI added to map!Tap on map to add a " +
                            "new POI location!",Toast.LENGTH_LONG);
                }

            }
        });

    }

    private void getPoiData(){

        poids=new PoiDS(this);
        poids.open();
        poi_list=poids.findAll();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private boolean setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }

        return (mMap!=null) ;
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        /*for (int i=0;i<poi_list.size();i++){

            double lat=Double.parseDouble(poi_list.get(i).getLat());
            double lng=Double.parseDouble(poi_list.get(i).getLng());
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat,
                    lng)).title(poi_list.get(i).getName()));
            LatLng ll=new LatLng(lat,lng);
            CameraUpdate update= CameraUpdateFactory.newLatLngZoom(ll, 16);
            mMap.moveCamera(update);
        }*/


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");
        LocationRequest request=LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        request.setInterval(60000);
        request.setFastestInterval(30000);
        mLocation.requestLocationUpdates(request,this);
    }

    @Override
    public void onDisconnected() {
        Log.d(TAG,"onDisconnected");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG,"Failed connection");
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(ll, zoom_to);
        mMap.animateCamera(update);
        /*mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),
                location.getLongitude())).title("I am here"));*/
    }
}

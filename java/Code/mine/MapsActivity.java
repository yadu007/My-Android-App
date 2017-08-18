package yzcop





import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.DialogPreference;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStreamWriter;
import java.net.HttpRetryException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class MapsActivity extends  FragmentActivity implements  OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
   public LocationListener locli;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    StringBuilder f;
    LocationManager fd;
    StringBuilder ls;
    Handler yzhandler;
    String Scan;
    private GoogleApiClient client;
    private Handler handler;
    static final int PICK_CONTACT_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.INTERNET},99
               );
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);


        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);

        }


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {


                Geocoder gc = new Geocoder(MapsActivity.this);
                LatLng ll = mCurrLocationMarker.getPosition();

                if(f.length()!=0){f.setLength(0);}
                f.append("Latitude: " + ll.latitude + " ");
                f.append("Longitude: " + ll.longitude);


                marker.setTitle(f.toString());
                marker.showInfoWindow();

            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //jj=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void mapfinder(View view) {
        try {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);


            builder.setTitle(f.toString());
            builder.setMessage("Enter Dcu ID");


            final EditText input = new EditText(this);



            builder.setView(input);

builder.setNeutralButton("Scan", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {



try {

    IntentIntegrator integrator=new IntentIntegrator(MapsActivity.this);
    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
    integrator.setPrompt("Focus on the Barcode");
    integrator.setCameraId(0);
    integrator.initiateScan();


}
catch (Exception ee){

    Toast.makeText(MapsActivity.this, "222"+ee.toString(), Toast.LENGTH_SHORT).show();
}

    }
});
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LatLng ll = mCurrLocationMarker.getPosition();
                    double lat=ll.latitude;
                    double lng=ll.longitude;
                    String latt=Double.toString(lat);
                    String lngg=Double.toString(lng);
                    String ID=input.getText().toString();
                    String url="URL to post the data="+ID+"&longitude="+lngg+"&lattitude="+latt;


                    //StringBuilder d=new StringBuilder();
                    //d.append(input.getText().toString());
                 //   Toast.makeText(MapsActivity.this, d.toString(), Toast.LENGTH_SHORT).show();

                    RequestQueue queue = Volley.newRequestQueue(getBaseContext());

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(MapsActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MapsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(stringRequest);

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        }
       

        catch (Exception r){
            Toast.makeText(this, "Location Not Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        try {
     
                    mapfinder_v2(result.getContents().toString());
               
        }
        catch (Exception rr){
            Toast.makeText(this, "No Result Found", Toast.LENGTH_SHORT).show();
        }


    }
    public void mapfinder_v2(String value){
        try {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);


            builder.setTitle(f.toString());
            builder.setMessage("Enter Dcu ID");


            final EditText input = new EditText(this);

            input.setText(value);

            builder.setView(input);

            builder.setNeutralButton("Scan", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {



                    try {

                        IntentIntegrator integrator=new IntentIntegrator(MapsActivity.this);
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                        integrator.setPrompt("Focus on the Barcode");
                        integrator.setCameraId(0);

                        integrator.initiateScan();

                    }
                    catch (Exception ee){

                        Toast.makeText(MapsActivity.this, "222"+ee.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LatLng ll = mCurrLocationMarker.getPosition();
                    double lat=ll.latitude;
                    double lng=ll.longitude;
                    String latt=Double.toString(lat);
                    String lngg=Double.toString(lng);
                    String ID=input.getText().toString();
                    String url="URL to post the data"+ID+"&longitude="+lngg+"&lattitude="+latt;


                    //StringBuilder d=new StringBuilder();
                    //d.append(input.getText().toString());
                    //   Toast.makeText(MapsActivity.this, d.toString(), Toast.LENGTH_SHORT).show();

                    RequestQueue queue = Volley.newRequestQueue(getBaseContext());

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(MapsActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MapsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(stringRequest);

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        }


        catch (Exception r){
            Toast.makeText(this, r.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLocationChanged(final Location location) {


        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                f = new StringBuilder();
                f.append("Latitude: " + location.getLatitude() + " ");
                f.append("Longitude: " + location.getLongitude());


                markerOptions.title(f.toString());
                markerOptions.draggable(true);


                mCurrLocationMarker = mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(120));

                return false;
            }
        });

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                try{
                       CameraPosition position = mMap.getCameraPosition();
                       if (position != null) {

                           mCurrLocationMarker.remove();


                           mCurrLocationMarker = mMap.addMarker(new MarkerOptions().position(position.target));
                           LatLng ll = mCurrLocationMarker.getPosition();

                           if (f.length() != 0) {
                               f.setLength(0);
                           }
                           f.append("Latitude: " + ll.latitude + " ");
                           f.append("Longitude: " + ll.longitude);
                           mCurrLocationMarker.setTitle(f.toString());
                       }

                       }
                catch (Exception except){}

                   }







        });

    }





    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET},
                        1);


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {


                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    }





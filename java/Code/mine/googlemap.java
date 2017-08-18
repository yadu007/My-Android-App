package yzcop
import android.app.DownloadManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class googlemap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

     
try {
    RequestQueue queue = Volley.newRequestQueue(this);
  JsonArrayRequest json=new JsonArrayRequest(Request.Method.GET, "URL to get the JSON file", null, new Response.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray response) {
          try{
              for(int i=0;i<response.length();i++) {
                  JSONObject obj = response.getJSONObject(i);
                  String Device_ID = obj.getString("DeviceId");
                  double Device_lat= Double.parseDouble(obj.getString("Lattitude"));
                  double Device_lng= Double.parseDouble(obj.getString("Longitude"));
                 // Toast.makeText(googlemap.this, dd.toString(), Toast.LENGTH_SHORT).show();
                  LatLng Position = new LatLng(Device_lat, Device_lng);
                  mMap.addMarker(new MarkerOptions().position(Position).title(Device_ID).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Position,10));
              }

          }
          catch (Exception e){
              Toast.makeText(googlemap.this, e.toString(), Toast.LENGTH_SHORT).show();
          }



      }
  }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
          Toast.makeText(googlemap.this, error.toString(), Toast.LENGTH_SHORT).show();

      }
  });
    queue.add(json);
}catch (Exception rr){}

    }



}

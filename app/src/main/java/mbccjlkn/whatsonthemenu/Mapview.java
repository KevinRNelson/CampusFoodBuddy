package mbccjlkn.whatsonthemenu;

import android.Manifest;
import android.app.ActionBar;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.util.ArrayList;
import java.util.List;

import java.net.URL;
import java.net.URLConnection;


public class Mapview extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    private GoogleMap mMap;
    DBAccess db = MainActivity.dba;
    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    GeoApiContext mGeoApiContext = null;
    Location user_location;
    boolean checkOncompelte = false;
    private URLConnection urlConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        Log.d("test", "666666666666666");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if(mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_map_api_key)).build();

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getLastKnownLocation() {
        Log.d("check","LastKnow Called");
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("check","permission denied");
            return;
        }
        Log.d("check"," get to task");
        mFusedLocationClient.getLastLocation().addOnCompleteListener(task ->  {
            //@Override
            //public void onComplete(@NonNull Task<Location> task) {
                Log.d("check", "on Complete called");
                if (task.isSuccessful()) {
                    user_location = task.getResult();
                    Log.d("check", "v: " + user_location.getLatitude());
                    Log.d("check", "v1: " +  user_location.getLongitude());
                    final Bundle extras = getIntent().getExtras();
                    int current = extras.getInt("id");
                    ArrayList<String> list = db.viewEatery(current);

                    String coordinate = db.getLocation(current);
                    String[] split_string = coordinate.split("_", 0);
                    float v = Float.parseFloat(split_string[0]);
                    float v1 = Float.parseFloat(split_string[1]);
                    //GeoPoint geoPoint = new GeoPoint(location.getLatitude())


                    double latitude = user_location.getLatitude();
                    double longitude = user_location.getLongitude();

                    LatLng current_pos = new LatLng(latitude, longitude);
                    LatLng des_point = new LatLng(v, v1);

                    Marker destinations = mMap.addMarker(new MarkerOptions().position(des_point).title(list.get(0)));
                    Marker origin = mMap.addMarker(new MarkerOptions().position(current_pos).title("Your Location"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(des_point, 15));
                    Direction(destinations, origin);
                    origin.remove();
                }else{
                    Log.d("check", "fail ----------");
                }
           // }
        });
    }


    public void Direction(Marker marker, Marker origin){


        Log.d("check","Direction Called");
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng
                (marker.getPosition().latitude, marker.getPosition().longitude) ;

        DirectionsApiRequest direction = new DirectionsApiRequest(mGeoApiContext);
        direction.alternatives(true);
        direction.origin(new com.google.maps.model.LatLng(origin.getPosition().latitude, origin.getPosition().longitude));

        direction.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>(){
            @Override
            public void onResult(DirectionsResult result) {
                Log.d("Directions", "calculateDirections: routes: " + result.routes[0].toString());
                Log.d("Directions", "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d("Directions", "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d("Directions", "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());
                addDirectionPath(result);
            }


           @Override
            public void onFailure(Throwable e) {
               Log.e("Directions", "calculateDirections: Failed to get directions: " + e.getMessage() );

            }
        });


    }


    public void addDirectionPath(final DirectionsResult result) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for(DirectionsRoute route: result.routes) {
                    List<com.google.maps.model.LatLng> decodedPath =
                            PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());
                    List<LatLng> newPath = new ArrayList<>();
                    for(com.google.maps.model.LatLng latLng: decodedPath) {
                        newPath.add(new LatLng(latLng.lat, latLng.lng));
                    }
                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newPath));
                    polyline.setColor(0xFF87ceff);
                    polyline.setClickable(true);
                }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        getLastKnownLocation();
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mLocationRequest = new LocationRequest();
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);

    }
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Your Location", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
        Log.d("check", "Latitude" + location.getLatitude());
        Log.d("check", "Longitude" +  location.getLongitude());
    }
}

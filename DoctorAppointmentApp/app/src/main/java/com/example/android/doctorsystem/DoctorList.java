package com.example.android.doctorsystem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import android.content.pm.PackageManager;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DoctorList extends AppCompatActivity {

    // initializing
    // FusedLocationProviderClient
    // object
    FusedLocationProviderClient mFusedLocationClient;

    // Initializing other items
    // from layout file
    //TextView latitudeTextView, longitTextView;
    private static final int PERMISSION_ID = 1;
    ArrayList<Double> arr=new ArrayList<>();

    ArrayList<Double> arr1=new ArrayList<>();
    HashMap<String, Double> hmap=new HashMap<>();
    double longitude;
    double latitude;
    FirebaseFirestore fStore;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu1:
                FirebaseAuth.getInstance().signOut();//logout
                Log.v("HELLO I am","hey bro");
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.doctor_list);
        fStore = FirebaseFirestore.getInstance();
       
//        latitudeTextView = findViewById(R.id.lat);
//        longitTextView = findViewById(R.id.lon);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // method to get the location
        getLastLocation();
        //Log.v("the array is",""+arr);



     // DocumentReference documentReference = fStore.collection("users").document();
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        
        if (checkPermissions()) {
            // check if location is enabled
            if (isLocationEnabled()) {
                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) { Location location = task.getResult();
                                        if (location == null)
                                            requestNewLocationData();
                                        } else {
                                            longitude = location.getLongitude();
                                            latitude = location.getLatitude();
                                            Log.v("the long1", "" + longitude);
                                            Log.v("the lat1", "" + latitude+"and long is"+longitude);
                                            fStore.collection("users")
                                                    .whereEqualTo("role", "Doctor")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                double longitude1 = Math.toRadians(longitude);
                                                                double latitude1 = Math.toRadians(latitude);
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    if(document.exists()){
                                                                    double doctorLatitude = document.getDouble("latitude");
                                                                    double doctorLongitude = document.getDouble("longitude");
                                                                    arr1.add(doctorLongitude);
                                                                    String myId = document.getId();
                                                                    //Log.v("doctor lati is ",""+doctorLatitude +"and longi is"+doctorLongitude);
                                                                    doctorLongitude = Math.toRadians(doctorLongitude);

                                                                    doctorLatitude = Math.toRadians(doctorLatitude);

                                                                    // Haversine formula
                                                                    double dlon = doctorLongitude - longitude1;
                                                                    double dlat = doctorLatitude - latitude1;
                                                                    double a = Math.pow(Math.sin(dlat / 2), 2)
                                                                            + Math.cos(latitude1) * Math.cos(doctorLatitude)
                                                                            * Math.pow(Math.sin(dlon / 2), 2);

                                                                    double c = 2 * Math.asin(Math.sqrt(a));

                                                                    // Radius of earth in kilometers. Use 3956
                                                                    // for miles
                                                                    double r = 6371;
                                                                    double result = c * r;
                                                                    arr.add(result);
                                                                    hmap.put(myId, result);


                                                                    Log.v("the result is", "" + result);

                                                                    //Log.v("doctor longi is ",""+doctorLongitude);
                                                                    // Log.d(TAG, document.getId() + " => " + document.getData());
                                                                }
                                                            }
                                                                //Map<String, Double> hm1 = sortByValue(hmap);
                                                                Log.v("try", ""+hmap);
                                                                Intent i = new Intent(getApplicationContext(), SupportActivity.class);
                                                                i.putExtra("map", (Serializable) hmap );
                                                                startActivity(i);

                                                                //i.putExtra("newmap", (Serializable) hm1);
//                                                                final ArrayList<Word> words = new ArrayList<Word>();
//                                                                Set<String> s=hm1.keySet();
//


                                                            } else {
                                                                Log.v("fhfhb","fhjhghhf");
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
            } else {
                Toast
                        .makeText(
                                this,
                                "Please turn on"
                                        + " your location...",
                                Toast.LENGTH_LONG)
                        .show();

                Intent intent
                        = new Intent(
                        Settings
                                .ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest
                = new LocationRequest();
        mLocationRequest.setPriority(
                LocationRequest
                        .PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient
                = LocationServices
                .getFusedLocationProviderClient(this);

        mFusedLocationClient
                .requestLocationUpdates(
                        mLocationRequest,
                        mLocationCallback,
                        Looper.myLooper());
    }

    private LocationCallback
            mLocationCallback
            = new LocationCallback() {

        @Override
        public void onLocationResult(
                LocationResult locationResult) {
            Location mLastLocation
                    = locationResult
                    .getLastLocation();


            longitude = mLastLocation.getLongitude();
            latitude = mLastLocation.getLatitude();
            Log.v("the long2", "" + longitude);
            Log.v("the lat2", "" + latitude);


        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;


    }

    // method to requestfor permissions
    private void requestPermissions() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);

    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v("request ",""+requestCode);

        if (requestCode == PERMISSION_ID) {
            for(int h=0;h<grantResults.length;h++)
                Log.v("grrant result of",""+h+" "+grantResults[h]);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
    public static HashMap<String, Double> sortByValue(HashMap<String, Double> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double> > list =
                new LinkedList<Map.Entry<String, Double> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double > aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}

package com.example.android.doctorsystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompleteDoctorProfile extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText registeredIdVariable,pincodeVariable,feeVariable,experianceVariable, addressVariable, morningTimeVariable, eveningTimeVariable;
    com.androidbuts.multispinnerfilter.MultiSpinnerSearch genderVariable,doctorTypeVariable,specialityVariable,degreeVariable;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    Serializable use;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_doctor_profile);
        Intent intent=getIntent();
        use=intent.getSerializableExtra("user");
        Log.v("after layout", "in editdoctorprofile");
        registeredIdVariable=(EditText)findViewById(R.id.registered_id);
        pincodeVariable=(EditText)findViewById(R.id.pincode);
        feeVariable=(EditText)findViewById(R.id.fee);
        experianceVariable=(EditText)findViewById(R.id.experiance);
        addressVariable=(EditText)findViewById(R.id.address);
        morningTimeVariable=(EditText)findViewById(R.id.morning_timings);
        eveningTimeVariable=(EditText)findViewById(R.id.evening_timings);
        genderVariable=(com.androidbuts.multispinnerfilter.MultiSpinnerSearch)findViewById((R.id.gender));
        doctorTypeVariable=(com.androidbuts.multispinnerfilter.MultiSpinnerSearch)findViewById(R.id.doctor_type);
        specialityVariable=(com.androidbuts.multispinnerfilter.MultiSpinnerSearch)findViewById(R.id.speciality);
        degreeVariable=(com.androidbuts.multispinnerfilter.MultiSpinnerSearch)findViewById(R.id.degree);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        ArrayList<ArrayList<Integer>> arr = new ArrayList<>(4);
        ArrayList<Integer> gender = new ArrayList<>(2);
        gender.add(R.id.gender);
        gender.add(R.array.gender);
        arr.add(gender);
        ArrayList<Integer> doctorType = new ArrayList<>(2);
        doctorType.add(R.id.doctor_type);
        doctorType.add(R.array.doctor_type);
        arr.add(doctorType);
        ArrayList<Integer> speciality = new ArrayList<>(2);
        speciality.add(R.id.speciality);
        speciality.add(R.array.speciality);
        arr.add(speciality);
        ArrayList<Integer> degree = new ArrayList<>(2);
        degree.add(R.id.degree);
        degree.add(R.array.degree);
        arr.add(degree);
        String ar1[]=new String[4];
        ar1[0]="Gender50";
        ar1[1]="Type of Doctor";
        ar1[2]="Speciality";
        ar1[3]="Degree";
        Log.v("the arr size is",""+arr.size());
        Log.v("the gender size is",""+gender.size());
        Log.v("the doctottype size is",""+doctorType.size());
        Log.v("the speciality size is",""+speciality.size());
        Log.v("the degree size is",""+degree.size());
        for (int j = 0; j < arr.size(); j++)
        {
            final List<String> list = Arrays.asList(getResources().getStringArray(arr.get(j).get(1)));
        Log.v("after list", "in editdoctorprofile");
        final List<KeyPairBoolData> listArray1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Log.v("after listarray1", "in editdoctorprofile");
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(list.get(i));
            h.setSelected(i == -1);
            listArray1.add(h);
        }
        MultiSpinnerSearch multiSelectSpinnerWithSearch = findViewById(arr.get
                (j).get(0));
        Log.v("after mss", "in editdoctorprofile");
        // Pass true If you want searchView above the list. Otherwise false. default = true.
        multiSelectSpinnerWithSearch.setSearchEnabled(true);

        // A text that will display in search hint.
        multiSelectSpinnerWithSearch.setSearchHint(ar1[j]);

        // Set text that will display when search result not found...
        multiSelectSpinnerWithSearch.setEmptyTitle("Not Data Found!");

        // If you will set the limit, this button will not display automatically.
        multiSelectSpinnerWithSearch.setShowSelectAllButton(true);

        // Removed second parameter, position. Its not required now..
        // If you want to pass preselected items, you can do it while making listArray,
        // pass true in setSelected of any item that you want to preselect


        multiSelectSpinnerWithSearch.setItems(listArray1, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i(TAG, i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                    }
                }
            }
        });

        /**
         * If you want to set limit as maximum item should be selected is 2.
         * For No limit -1 or do not call this method.
         *
         */
        multiSelectSpinnerWithSearch.setLimit(-1, new MultiSpinnerSearch.LimitExceedListener() {
            @Override
            public void onLimitListener(KeyPairBoolData data) {
                Toast.makeText(getApplicationContext(),
                        "Limit exceed ", Toast.LENGTH_LONG).show();
            }
        });
    }
        Button idOfButton=(Button)findViewById(R.id.submit);
        idOfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String mregisteredId=registeredIdVariable.getText().toString();
                String mpincode=pincodeVariable.getText().toString();
                String mfee=feeVariable.getText().toString();
                String mexperiance=experianceVariable.getText().toString();
                String maddress=addressVariable.getText().toString();
                String mmorningTime=morningTimeVariable.getText().toString();
                String meveningTime=eveningTimeVariable.getText().toString();
                List<KeyPairBoolData> mgender=genderVariable.getSelectedItems();
                List<KeyPairBoolData> mdoctorType=doctorTypeVariable.getSelectedItems();
                List<KeyPairBoolData> mspeciality=specialityVariable.getSelectedItems();
                List<KeyPairBoolData> mdegree=degreeVariable.getSelectedItems();


                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(maddress+" "+mpincode,
                        getApplicationContext(), new GeocoderHandler());



               Log.v("FFFF","GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
                userID = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("users").document(userID);
                HashMap<String,Object> user=(HashMap<String, Object>)intent.getSerializableExtra("user");
                Log.v("hhh","DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
                user.put("registeredId",mregisteredId);
                user.put("gender",mgender);
                user.put("pincode",mpincode);
                user.put("typeOfDoctor",mdoctorType);
                user.put("speciality",mspeciality);
                user.put("degree",mdegree);
                user.put("fee",mfee);
                user.put("experiance",mexperiance);
                user.put("address",maddress);
                user.put("morningTime",mmorningTime);
                user.put("eveningTime",meveningTime);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: user Profile is completed "+ userID);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                }) ;
                startActivity(new Intent(getApplicationContext(),PatientDetails.class));
                finish();
            }
        });

    }



    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            Log.v("ans",""+locationAddress);
            String arr[]=locationAddress.split("Latitude and Longitude :");
            int l=arr.length;
            String d[]=arr[l-1].split("\n");

            Log.v("len",""+d.length);

            double latit = Double.parseDouble(d[1]);
            double longit = Double.parseDouble(d[2]);
            Log.v("latit"," "+latit);
            Log.v("longit",""+longit);
            userID = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("users").document(userID);
            HashMap<String,Object> user=(HashMap<String, Object>)use;
            user.put("latitude",latit);
            user.put("longitude",longit);
            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: user Profile is completed "+ userID);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.toString());
                    progressBar.setVisibility(View.GONE);
                }
            }) ;


        }
    }


}

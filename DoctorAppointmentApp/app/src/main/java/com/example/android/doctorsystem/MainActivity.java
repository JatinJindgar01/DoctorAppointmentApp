package com.example.android.doctorsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    String Role = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        Log.v("Main activity", "log 1...................................................................");


        if (fAuth.getCurrentUser() != null) {
            Log.v("current user check", "in main activity ====================================================");
            userId = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("users").document(userId);

            documentReference.addSnapshotListener(this, (documentSnapshot, e) -> {
                assert documentSnapshot != null;
                if (documentSnapshot.exists()) {

                    Log.v("Main Activity ", "before extracting role " + Role);
                    Role = documentSnapshot.getString("role");
                    Log.v("Main Activity ", "after extracting role " + Role);
                    if (Role.equals("Doctor")) {
                        Log.v("Main Activity", "to patient detail ---------------------------------------------==============================");
                        startActivity(new Intent(getApplicationContext(), PatientDetails.class));
                        finish();
                    }
                    if (Role.equals("Patient")) {
                        Log.v("from register class", "to doctor list-----------------------------------------]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");
                        startActivity(new Intent(getApplicationContext(), DoctorList.class));
                        finish();
                    }

                }
                finish();
            });
        }
        Log.v("Main Activity ", "current user not present----------------------;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
        setContentView(R.layout.activity_main);

        LinearLayout doctor_ll = (LinearLayout) findViewById(R.id.doctor_login);
        doctor_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, Login.class);
                i.putExtra("myRole", "Doctor");
                startActivity(i);
                finish();
            }
        });
        LinearLayout patient_ll = (LinearLayout) findViewById(R.id.patient_login);
        patient_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Login.class);
                i.putExtra("myRole", "Patient");
                startActivity(i);
                finish();
            }
        });


    }

}

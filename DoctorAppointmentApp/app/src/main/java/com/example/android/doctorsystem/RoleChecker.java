package com.example.android.doctorsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class RoleChecker extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId="jatin";
    String Role;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_checker);
        Intent i = getIntent();
        Log.v("working","role");
        String myRole = i.getStringExtra("myRole");
       // String myRole="Doctor";
        fStore = FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();
        Log.v("before user is",""+userId);
        userId = fAuth.getCurrentUser().getUid();
        Log.v("after user is",""+userId);
        Log.v("the role is",""+myRole);

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
               if(e==null) {
                   if (documentSnapshot.exists()) {
                       Role = documentSnapshot.getString("role");
                       Log.v("in role","i am");
                   }
                   else
                       Log.v("hello","this is ");
               }
               else
               {
                   Log.v("in else of ","null");
               }
            }
        });






//        documentReference.addSnapshotListener(this, (documentSnapshot, e) -> {
//           if(documentSnapshot==null)
//           {
//               Log.v("null document","snapshot");
//
//           }
//            if (Objects.requireNonNull(documentSnapshot).exists()) {
//                Role = documentSnapshot.getString("role");
//
//            }
//        });
        if (myRole.equals(Role)) {
            Log.v("in if","");
            if (myRole.equals("Doctor")) {
                startActivity(new Intent(getApplicationContext(), PatientDetails.class));
            } else {
                startActivity(new Intent(getApplicationContext(), DoctorList.class));
            }
        } else {
           // FirebaseAuth.getInstance().signOut();//logout
            Toast.makeText(RoleChecker.this, "You are not registered", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

    }
}

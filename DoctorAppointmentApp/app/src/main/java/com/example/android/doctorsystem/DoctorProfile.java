package com.example.android.doctorsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoctorProfile extends AppCompatActivity {
    private static final int GALLERY_INTENT_CODE = 1023;
    TextView fullName, email, phone, verifyMsg, registeredId, speciality, degree, doctorType, morningTime, fee, experiance, address, eveningTime, pincode, gender;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button resendCode;
    Button resetPassLocal, changeProfileImage, logout1;
    FirebaseUser user;
    ImageView profileImage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile);
        Log.v("in doctor profile 1", "through patient details");

        phone = (TextView) findViewById(R.id.profilePhone);
        fullName = (TextView) findViewById(R.id.profileName);
        email = (TextView) findViewById(R.id.profileEmail);
        resetPassLocal = findViewById(R.id.resetPasswordLocal);
        gender = (TextView) findViewById(R.id.profileGender);
        registeredId = (TextView) findViewById(R.id.profileRegisteredId);
        speciality = (TextView) findViewById(R.id.profileSpeciality);
        degree = (TextView) findViewById(R.id.profileDegree);
        doctorType = (TextView) findViewById(R.id.profileDoctorType);
        morningTime = (TextView) findViewById(R.id.profileMorningTimings);
        eveningTime = (TextView) findViewById(R.id.profileEveningTimings);
        fee = (TextView) findViewById(R.id.profileFee);
        experiance = (TextView) findViewById(R.id.profileExperiance);
        address = (TextView) findViewById(R.id.profileAddress);
        pincode = (TextView) findViewById(R.id.profilePincode);

        profileImage = findViewById(R.id.profileImage);
        changeProfileImage = findViewById(R.id.changeProfile);
        logout1 = findViewById(R.id.button);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        resendCode = findViewById(R.id.resendCode);
        verifyMsg = findViewById(R.id.verifyMsg);


        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        if (!user.isEmailVerified()) {
            verifyMsg.setVisibility(View.VISIBLE);
            resendCode.setVisibility(View.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "onFailure: Email not sent " + e.getMessage());
                        }
                    });
                }
            });
        }


        Log.v("in doctor profile 2", "through patient details");
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, (documentSnapshot, e) -> {

            assert documentSnapshot != null;
            if (documentSnapshot.exists()) {
                String arrayOfKeys[] = new String[4];
                arrayOfKeys[0] = "gender";
                arrayOfKeys[1] = "typeOfDoctor";
                arrayOfKeys[2] = "speciality";
                arrayOfKeys[3] = "degree";
                TextView arrayOfId[] = new TextView[4];
                arrayOfId[0] = gender;
                arrayOfId[1] = doctorType;
                arrayOfId[2] = speciality;
                arrayOfId[3] = degree;
                phone.setText(documentSnapshot.getString("phone"));
                fullName.setText(documentSnapshot.getString("fName"));
                email.setText(documentSnapshot.getString("email"));
                Log.v("pincode is", "" + documentSnapshot.getString("pincode"));
                pincode.setText(documentSnapshot.getString("pincode"));
                address.setText(documentSnapshot.getString("address"));
                experiance.setText(documentSnapshot.getString("experiance"));
                registeredId.setText(documentSnapshot.getString("registeredId"));
                fee.setText(documentSnapshot.getString("fee"));
                morningTime.setText(documentSnapshot.getString("morningTime"));
                eveningTime.setText(documentSnapshot.getString("eveningTime"));
                Map<String, Object> m = documentSnapshot.getData();

                String t2 = "";
                ArrayList<HashMap<String, Object>> ar2 = (ArrayList<HashMap<String, Object>>) m.get("gender");
                for (int y = 0; y < ar2.size(); y++) {
                    t2 += (String) ar2.get(y).get("name") + ", ";
                }
                gender.setText(t2);
                String t3 = "";
                ArrayList<HashMap<String, Object>> ar3 = (ArrayList<HashMap<String, Object>>) m.get("typeOfDoctor");
                for (int y = 0; y < ar3.size(); y++) {
                    t3 += (String) ar3.get(y).get("name") + ", ";
                }
                doctorType.setText(t3);
                String t4 = "";
                ArrayList<HashMap<String, Object>> ar4 = (ArrayList<HashMap<String, Object>>) m.get("speciality");
                for (int y = 0; y < ar4.size(); y++) {
                    t4 += (String) ar4.get(y).get("name") + ", ";
                }
                speciality.setText(t4);
                String t5 = "";
                ArrayList<HashMap<String, Object>> ar5 = (ArrayList<HashMap<String, Object>>) m.get("degree");
                for (int y = 0; y < ar5.size(); y++) {
                    t5 += (String) ar5.get(y).get("name") + ", ";
                }
                degree.setText(t5);


            } else {
                Log.d("tag", "onEvent: Document do not exists");
            }

        });
        fStore.terminate();


        resetPassLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetPassword = new EditText(v.getContext());

                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter New Password > 6 Characters long.");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String newPassword = resetPassword.getText().toString();
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DoctorProfile.this, "Password Reset Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DoctorProfile.this, "Password Reset Failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close
                    }
                });

                passwordResetDialog.create().show();

            }
        });

        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open gallery
                Intent i = new Intent(v.getContext(), EditDoctorProfile.class);
                i.putExtra("fullName", fullName.getText().toString());
                i.putExtra("email", email.getText().toString());
                i.putExtra("phone", phone.getText().toString());
                Log.v("in doctor profile 2", "through patient details");

                i.putExtra("registeredId", registeredId.getText().toString());

                i.putExtra("fee", fee.getText().toString());
                i.putExtra("morningTime", morningTime.getText().toString());
                i.putExtra("eveningTime", eveningTime.getText().toString());
                i.putExtra("experiance", experiance.getText().toString());
                i.putExtra("address", address.getText().toString());
                i.putExtra("pincode", pincode.getText().toString());
                startActivity(i);


            }
        });

        logout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("HELLO I am this logout", "hey broOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
                FirebaseAuth.getInstance().signOut();
                Log.v("HELLO I am this logout", "hey bro");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Log.v("HELLO I am this logout", "hey bro");
                finish();

            }
        });


    }


}

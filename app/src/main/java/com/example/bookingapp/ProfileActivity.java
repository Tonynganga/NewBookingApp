package com.example.bookingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingapp.Models.UserProfile;
import com.example.bookingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 100;
    Toolbar toolbar;
    CircleImageView profileimageview;
    TextView username_tv, useremail_tv, userphone_tv, usercity_tv;
    Button btnprofiledit;

    //Popup dialog
    CircleImageView profilePhoto;
    TextView profilePhotoSelect;
    EditText profileName, profileEmail, profilePhone, profileLocation;
    Button profileSave, profileCancel;

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Uri imageUri;
    StorageReference storageRef;
    ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        profileimageview = findViewById(R.id.circleImageView);
        username_tv = findViewById(R.id.tv_name);
        useremail_tv = findViewById(R.id.tv_email);
        userphone_tv = findViewById(R.id.tv_phone);
        usercity_tv = findViewById(R.id.tv_city);
        btnprofiledit = findViewById(R.id.btn_editprofile);
        mLoadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("UserProfiles");
        storageRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");

        profileimageview.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE);
        });

        btnprofiledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateProfileDialog();
            }
        });

        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String profileimage = snapshot.child("profileimage").getValue().toString();
                    String username = snapshot.child("profilename").getValue().toString();
                    String email = snapshot.child("profileemail").getValue().toString();
                    String phone = snapshot.child("profilephone").getValue().toString();
                    String city = snapshot.child("profilecity").getValue().toString();



                    Picasso.get().load(profileimage).into(profileimageview);
                    username_tv.setText(username);
                    useremail_tv.setText(email);
                    userphone_tv.setText(phone);
                    usercity_tv.setText(city);

                }else {
                    Toast.makeText(ProfileActivity.this, "Data does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CreateProfileDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        View profileView = getLayoutInflater().inflate(R.layout.add_profile, null);

        profileName = profileView.findViewById(R.id.profile_name);
        profileEmail = profileView.findViewById(R.id.profile_email);
        profilePhone = profileView.findViewById(R.id.profile_phone);
        profileLocation = profileView.findViewById(R.id.profile_location);
        profileSave = profileView.findViewById(R.id.btnmem_save);
        profileCancel = profileView.findViewById(R.id.btnmem_cancel);
        profilePhoto = profileView.findViewById(R.id.profile_imageview);
        profilePhotoSelect = profileView.findViewById(R.id.btnselectphoto);

        dialogBuilder.setView(profileView);
        dialog = dialogBuilder.create();
        dialog.show();

        profilePhotoSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        profileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null){
                    SaveProfile(imageUri);
                }else{
                    Toast.makeText(ProfileActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        profileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void SaveProfile(Uri imageUri) {
        String profilename = profileName.getText().toString();
        String profileemail = profileEmail.getText().toString();
        String profilephone = profilePhone.getText().toString();
        String profilecity = profileLocation.getText().toString();

        if (profilename.isEmpty() || profilename.length() < 3) {
            showError(profileName, "Username is not valid");
        } else if (profileemail.isEmpty() || !profileemail.endsWith("@gmail.com")) {
            showError(profileEmail, "Email is not valid");
        } else if (profilephone.isEmpty() || profilephone.length() < 10) {
            showError(profilePhone, "Phone is not valid");
        } else if (profilecity.isEmpty()) {
            showError(profileLocation, "Location is not valid");
        } else if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        } else {
            mLoadingBar.setTitle("Saving profile");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.setMessage("Please wait while saving profile");
            mLoadingBar.show();

            storageRef.child(mUser.getUid()).putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    storageRef.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(uri -> {
                        UserProfile userProfile = new UserProfile(profilename, profileemail, profilephone, profilecity, imageUri.toString());

                        mUserRef.child(mUser.getUid()).setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mLoadingBar.dismiss();
                                Toast.makeText(ProfileActivity.this, "Profile SetUp Successful", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mLoadingBar.dismiss();
                                Toast.makeText(ProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    });
                }
            });

        }
    }

//    private void saveData() {
//        String username = inputusername.getText().toString();
//        String phone = inputphone.getText().toString();
//        String city = inputcity.getText().toString();
//
//        if (username.isEmpty() || username.length()<3){
//            showError(inputusername, "Username is not valid");
//        }else  if (phone.isEmpty() || phone.length()<3){
//            showError(inputphone, "Phone is not valid");
//        }else  if (city.isEmpty() || city.length()<3){
//            showError(inputcity, "City is not valid");
//        }else if (imageUri == null) {
//            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
//        }else {
//
//            mLoadingBar.setTitle("Saving profile");
//            mLoadingBar.setCanceledOnTouchOutside(false);
//            mLoadingBar.setMessage("Please wait while saving profile");
//            mLoadingBar.show();
//            storageRef.child(mUser.getUid()).putFile(imageUri).addOnCompleteListener(task -> {
//                if (task.isSuccessful()){
//                    storageRef.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(uri -> {
//                        UserProfile userProfile = new UserProfile(username, phone, city, imageUri.toString());
//
//                        mUserRef.child(mUser.getUid()).setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                mLoadingBar.dismiss();
//                                Toast.makeText(ProfileActivity.this, "Profile SetUp Successful", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                mLoadingBar.dismiss();
//                                Toast.makeText(ProfileActivity.this, e.toString() , Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                    });
//                }
//            });
//        }
//    }
//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data!=null){
            imageUri = data.getData();
            profilePhoto.setImageURI(imageUri);
        }
    }


    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
    }
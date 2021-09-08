package com.example.bookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    CircleImageView profileimageview;
    EditText inputusername, inputphone, inputcity;
    Button btnupdate;

    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        profileimageview = findViewById(R.id.circleImageView);
        inputusername = findViewById(R.id.inputusernameprofile);
        inputcity = findViewById(R.id.inputcityprofile);
        inputphone = findViewById(R.id.inputphoneprofile);
        btnupdate = findViewById(R.id.buttonupdate);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("UserProfiles");

        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String profileimage = snapshot.child("profileImage").getValue().toString();
                    String username = snapshot.child("username").getValue().toString();
                    String city = snapshot.child("city").getValue().toString();
                    String phone = snapshot.child("phone").getValue().toString();
                    String profession = snapshot.child("profession").getValue().toString();

                    Picasso.get().load(profileimage).into(profileimageview);
                    inputusername.setText(username);
                    inputcity.setText(city);
                    inputphone.setText(phone);
                }else {
                    Toast.makeText(ProfileActivity.this, "Data does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
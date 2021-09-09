package com.example.bookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminLoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button adminLogin;

    EditText Adminemail, Adminpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Adminemail = findViewById(R.id.inputEmail);
        Adminpassword = findViewById(R.id.inputPassword);
        adminLogin = findViewById(R.id.btnadminLogin);

        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               LogInAdmin();
            }
        });
    }

    private void LogInAdmin() {
        String email = Adminemail.getText().toString();
        String password = Adminpassword.getText().toString();

        if (email.isEmpty()){
            showError(Adminemail, "Please Input Email");
        }else if (password.isEmpty()){
            showError(Adminpassword, "Please Input Password");
        }else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        Toast.makeText(AdminLoginActivity.this, "LogIn Successful", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminLoginActivity.this, "LogIn Failed!!", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void showError(EditText field, String text) {
        field.setError(text);
        field.requestFocus();
    }
}
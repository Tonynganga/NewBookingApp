package com.example.bookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookingapp.Models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout inputemailRegister, inputpasswordRegister, inputNameRegister, inputPhoneRegister;
    private Button btnRegister, gotoLogin;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference mRegisterUser;
    ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        inputemailRegister = findViewById(R.id.inputEmailRegister);
        inputpasswordRegister = findViewById(R.id.inputPasswordRegister);
        inputNameRegister = findViewById(R.id.inputNameRegister);
        inputPhoneRegister = findViewById(R.id.inputPhoneRegister);

        btnRegister = findViewById(R.id.btnRegister);
        gotoLogin = findViewById(R.id.gotoLogin);


        mRegisterUser = FirebaseDatabase.getInstance().getReference();
        mLoadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUsers();
            }
        });
    }

    private void RegisterUsers() {
        String name = inputNameRegister.getEditText().getText().toString();
        String phone = inputPhoneRegister.getEditText().getText().toString();
        String email = inputemailRegister.getEditText().getText().toString();
        String password = inputpasswordRegister.getEditText().getText().toString();

        if (name.isEmpty() || name.length() < 5){
            showError(inputNameRegister, "Name Must be 5 Characters and above");
        }else if (phone.isEmpty()){
            showError(inputPhoneRegister, "Enter Valid Phone Number");
        }
        else if(email.isEmpty() || !email.contains("@gmail")){
            showError(inputemailRegister, "Email is not valid");
        }else  if (password.isEmpty() || password.length()<5){
            showError(inputpasswordRegister, "Password must be greater than 5 letters");
        }else {
            mLoadingBar.setTitle("Registration");
            mLoadingBar.setMessage("Please wait while your credentials get verified");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        UserModel newUser = new UserModel(name, phone, email);
                        mRegisterUser.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    mLoadingBar.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Registration is successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(RegisterActivity.this, "Something Went Wrong!Try Again", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

                    }else {
                        mLoadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "Registarion Unsuccessful..Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showError(TextInputLayout field, String text) {
        field.setError(text);
        field.requestFocus();
    }
}
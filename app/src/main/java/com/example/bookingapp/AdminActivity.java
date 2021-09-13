package com.example.bookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingapp.Models.Bus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText travelsName;
    private EditText busNumber;
    private EditText dateBus;
    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDatesetListener;

    private static final int REQUEST_CODE = 100;
    private MaterialCardView addBus, addAdmin, logout;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog, admindialog;

    private Uri imageUri;
    
    EditText bus_Id, bus_Number, busFrom, busTo, travels_Name, travelDate, bus_Condition;
    private Button comMPhotoSelect, comMSave, comMCancel, btnadminsave, btnadmincancel, savereading, cancelreading;
    Button busSave, annImageSelect, annSave, annCancel;
    private EditText adminusername, adminpassword;

    EditText readingday, readingdate, englishreading, kiswahilireading, kikuyureading,
            englishinjili, kiswahiliinjili, kikuyuinjili;

    private DatabaseReference mBusRef;
    private DatabaseReference mReadingRef;

    ProgressDialog LoadingBar;


    private StorageReference mMemRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mAuth = FirebaseAuth.getInstance();

        LoadingBar = new ProgressDialog(this);

        addBus = findViewById(R.id.addMember);
        addAdmin = findViewById(R.id.addAdministrator);
        mBusRef = FirebaseDatabase.getInstance().getReference();

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), DisplayLocationsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateBusDialog();
            }
        });

        addAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAdminDialog();
            }
        });




    }

    private void CreateAdminDialog() {
    }

    private void CreateBusDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        View busView = getLayoutInflater().inflate(R.layout.add_bus, null);

        //From
        Spinner spinner1 = busView.findViewById(R.id.busFrom);
        String[] items1 = new String[]{"From", "Mombasa", "Mariakani", "Mazerus", "Voi", "Mtito Wa Ndei", "Kibwezi", "Emali"
                                        , "AthiRiver", "Nairobi", "Mahimahiu", "Narok", "Bomet", "Sotit", "Sondu", "Ahero", "Kisumu"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        spinner1.setAdapter(adapter1);

        //To
        Spinner spinner2 = busView.findViewById(R.id.busTo);
        String[] items2 = new String[]{"To", "Mombasa", "Mariakani", "Mazerus", "Voi", "Mtito Wa Ndei", "Kibwezi", "Emali"
                , "AthiRiver", "Nairobi", "Mahimahiu", "Narok", "Bomet", "Sotit", "Sondu", "Ahero", "Kisumu"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        spinner2.setAdapter(adapter2);

        //Condition
        Spinner spinner3 = busView.findViewById(R.id.busCondition);
        String[] items3 = new String[]{"Bus Condition", "A/C Sleepers", "A/C Non_Sleepers", "Non_A/C Sleepers", "Non_A/C Non_Sleepers"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items3);
        spinner3.setAdapter(adapter3);

        travelsName = busView.findViewById(R.id.travelsName);
        busNumber = busView.findViewById(R.id.busNumber);
        dateBus = busView.findViewById(R.id.journeyDate);
//        spinner1 = busView.findViewById(R.id.busFrom);
//        spinner2 = busView.findViewById(R.id.busTo);
//        spinner3 = busView.findViewById(R.id.busCondition);
        busSave = busView.findViewById(R.id.btnaddBus);
        dialogBuilder.setView(busView);
        dialog = dialogBuilder.create();
        dialog.show();



        busSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDisplayDate = busView.findViewById(R.id.journeyDate);
                mDisplayDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(AdminActivity.this
                                , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                                , mDatesetListener
                                , year, month, day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                });
                mDatesetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                Log.d(TAG, "OnDateSet:date :" + day + "/" + (month + 1) + "/" + year);
                        String date = day + "-" + (month + 1) + "-" + year;
                        // String status="Journey Date";
                        // mDisplayDate.setText(status+"\n"+date);
                        mDisplayDate.setText(date);
                    }
                };

                String travelsNameI = travelsName.getText().toString().trim();
                String busNumberI = busNumber.getText().toString().trim();
                String date = dateBus.getText().toString().trim();
                String from = spinner1.getSelectedItem().toString().trim();
                String to = spinner2.getSelectedItem().toString().trim();
                String busCondition = spinner3.getSelectedItem().toString().trim();

                String busId = mBusRef.push().getKey();

                if (TextUtils.isEmpty(travelsNameI)) {
                    //email is empty
                    Toast.makeText(getApplicationContext(), "Please Enter Travels Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(busNumberI)) {
                    //password is empty
                    Toast.makeText(getApplicationContext(), "Please Enter Bus Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(date)) {
                    //password is empty
                    Toast.makeText(getApplicationContext(), "Please Enter Journey Date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.equals(from,"From")) {
                    //email is empty
                    Toast.makeText(getApplicationContext(), "Please Select Departure Place", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.equals(to,"To")) {
                    //password is empty
                    Toast.makeText(getApplicationContext(), "Please Select Destination Place", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.equals(busCondition,"Bus Condition")) {
                    //password is empty
                    Toast.makeText(getApplicationContext(), "Please Select Bus Condition", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    LoadingBar.setMessage("Adding Buses Please Wait...");
                    LoadingBar.show();

                    Bus bus = new Bus(busId, travelsNameI, busNumberI, date, from, to, busCondition);

//                    FirebaseUser user = adminAuth.getCurrentUser();
                    mBusRef.child("BusDetails").child(busId).setValue(bus).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AdminActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();
                            }else {
                                Toast.makeText(AdminActivity.this, "error", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();
                            }
                        }
                    });

                }


//                if (busId.isEmpty()) {
//                    showError(bus_Id, "Name is not valid!! Must be more then 3 characters.");
//                } else if (travelsName.isEmpty()) {
//                    showError(travels_Name, "Position can not be null");
//                } else if (busNumber.isEmpty() || bus_Number.length() < 3) {
//                    showError(bus_Number, "Phone number not valid!!");
//                } else if (date.isEmpty()) {
//                    showError(travelDate, "Location cannot be null!!");
//                } else if (from.isEmpty()) {
//                    showError(busFrom, "Location cannot be null!!");
//                } else if (to.isEmpty()) {
//                    showError(busTo, "Location cannot be null!!");
//                } else if (busCondition.isEmpty()) {
//                    showError(bus_Condition, "Location cannot be null!!");
//                } else {
//                    LoadingBar.setTitle("Saving Member");
//                    LoadingBar.setMessage("Please Wait a Moment.");
//                    LoadingBar.setCanceledOnTouchOutside(false);
//                    LoadingBar.show();
//                    Bus newBus = new Bus(busId, travelsName, busNumber, date, from, to, busCondition);
//                    mBusRef.push().setValue(newBus).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()){
//                                LoadingBar.dismiss();
//                                Toast.makeText(AdminActivity.this, "Bus Saved Successfully", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            LoadingBar.dismiss();
//                            Toast.makeText(AdminActivity.this, "Something Went Wrong! Try Agaain", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
            }
        });
    }

    private void showError(EditText input, String s) {
            input.setError(s);
            input.requestFocus();
    }


    @Override
    public void onClick(View view) {
        if (view == addBus) {
            CreateBusDialog();
        }
    }
}
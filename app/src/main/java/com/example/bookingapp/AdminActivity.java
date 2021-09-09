package com.example.bookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bookingapp.Models.Bus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    private MaterialCardView addBus, addAdmin;
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
    private FirebaseAuth adminAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        LoadingBar = new ProgressDialog(this);

        addBus = findViewById(R.id.addMember);
        addAdmin = findViewById(R.id.addAdministrator);
        mBusRef = FirebaseDatabase.getInstance().getReference().child("BusDetails");

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
        bus_Id = busView.findViewById(R.id.bus_id);
        travels_Name = busView.findViewById(R.id.travels_name);
        bus_Number = busView.findViewById(R.id.bus_number);
        travelDate = busView.findViewById(R.id.travel_date);
        busFrom = busView.findViewById(R.id.bus_from);
        busTo = busView.findViewById(R.id.bus_to);
        bus_Condition = busView.findViewById(R.id.bus_condition);
        busSave = busView.findViewById(R.id.bus_save);
        dialogBuilder.setView(busView);
        dialog = dialogBuilder.create();
        dialog.show();

        busSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String busId = bus_Id.getText().toString();
                String travelsName = travels_Name.getText().toString();
                String busNumber = bus_Number.getText().toString();
                String date = travelDate.getText().toString();
                String from = busFrom.getText().toString();
                String to = busTo.getText().toString();
                String busCondition = bus_Condition.getText().toString();

                if (busId.isEmpty()) {
                    showError(bus_Id, "Name is not valid!! Must be more then 3 characters.");
                } else if (travelsName.isEmpty()) {
                    showError(travels_Name, "Position can not be null");
                } else if (busNumber.isEmpty() || bus_Number.length() < 3) {
                    showError(bus_Number, "Phone number not valid!!");
                } else if (date.isEmpty()) {
                    showError(travelDate, "Location cannot be null!!");
                } else if (from.isEmpty()) {
                    showError(busFrom, "Location cannot be null!!");
                } else if (to.isEmpty()) {
                    showError(busTo, "Location cannot be null!!");
                } else if (busCondition.isEmpty()) {
                    showError(bus_Condition, "Location cannot be null!!");
                } else {
                    LoadingBar.setTitle("Saving Member");
                    LoadingBar.setMessage("Please Wait a Moment.");
                    LoadingBar.setCanceledOnTouchOutside(false);
                    LoadingBar.show();
                    Bus newBus = new Bus(busId, travelsName, busNumber, date, from, to, busCondition);
                    mBusRef.push().setValue(newBus).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                LoadingBar.dismiss();
                                Toast.makeText(AdminActivity.this, "Bus Saved Successfully", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            LoadingBar.dismiss();
                            Toast.makeText(AdminActivity.this, "Something Went Wrong! Try Agaain", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void showError(EditText input, String s) {
            input.setError(s);
            input.requestFocus();
    }
}
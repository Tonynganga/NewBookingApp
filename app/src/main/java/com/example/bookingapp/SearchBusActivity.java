package com.example.bookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SearchBusActivity extends AppCompatActivity {
    private static final String TAG = "NavigationActivity";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDatesetListener;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    Spinner spinner1;
    Spinner spinner2;
    TextView tvDate;
    Button angry_btn;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bus);

        Intent intent1 = getIntent();
        Bundle extras = intent1.getExtras();
        String Ssource = extras.getString("Ssource");
        String Sdestination = extras.getString("Sdestination");
//        String distance = extras.getString("Sdistance");



//        String password_string = extras.getString("EXTRA_PASSWORD");
//        Toast.makeText(this, ""+distance, Toast.LENGTH_SHORT).show();


        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);

        tvDate = (TextView) findViewById(R.id.tvDate);
        angry_btn = (Button) findViewById(R.id.angry_btn);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LogInActivity.class));
        }

        Toolbar mToolbar = findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Searching Buses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //From
        Spinner dropdown1 = findViewById(R.id.spinner1);
        String[] items1 = new String[]{Ssource, "Mombasa", "Mariakani", "Mazerus", "Voi", "Mtito Wa Ndei", "Kibwezi", "Emali"
                , "AthiRiver", "Nairobi", "Mahimahiu", "Narok", "Bomet", "Sotit", "Sondu", "Ahero", "Kisumu"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        dropdown1.setAdapter(adapter1);

        //To
        Spinner dropdown2 = findViewById(R.id.spinner2);
        String[] items2 = new String[]{Sdestination, "Mombasa", "Mariakani", "Mazerus", "Voi", "Mtito Wa Ndei", "Kibwezi", "Emali"
                , "AthiRiver", "Nairobi", "Mahimahiu", "Narok", "Bomet", "Sotit", "Sondu", "Ahero", "Kisumu"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        dropdown2.setAdapter(adapter2);

        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(SearchBusActivity.this
                        , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , mDatesetListener
                        , year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        mDatesetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Log.d(TAG, "OnDateSet:date :" + day + "/" + (month + 1) + "/" + year);
                String date = day + "/" + (month + 1) + "/" + year;
                // String status="Journey Date";
                // mDisplayDate.setText(status+"\n"+date);
                mDisplayDate.setText(date);
            }
        };

        angry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchBuses();
            }
        });
    }

    private void SearchBuses() {
        String from = spinner1.getSelectedItem().toString().trim();
        String to = spinner2.getSelectedItem().toString().trim();
        String date = tvDate.getText().toString().trim();
        // String key = databaseReference.push().getKey();

        if (TextUtils.equals(from,"FROM")) {
            //email is empty
            Toast.makeText(this, "Please select departure place", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.equals(to,"TO")) {
            //password is empty
            Toast.makeText(this, "Please select destination place", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(date)) {
            //password is empty
            Toast.makeText(this, "Please select journey date", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Searching Buses Please Wait...");
        progressDialog.show();
        BookingDetail bookingDetail = new BookingDetail(from, to, date);
        databaseReference.child("BookingDetails").push().setValue(bookingDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent1 = getIntent();
                    Bundle extras = intent1.getExtras();
                    String distance = extras.getString("Sdistance");
                    Intent intent=new Intent(SearchBusActivity.this,BusActivity.class);
                    intent.putExtra("FROM_BUS",from);
                    intent.putExtra("TO_BUS",to);
                    intent.putExtra("DATE_BUS",date);
                    intent.putExtra("SDistance", distance);
                    startActivity(intent);
                    progressDialog.dismiss();

                }
            }
        });

//        FirebaseUser user1 = firebaseAuth.getCurrentUser();

        //child("bookings").child(userId);

//        String fromDetail = spinner1.getSelectedItem().toString().trim();

    }
}
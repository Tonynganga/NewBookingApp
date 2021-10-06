package com.example.bookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingapp.Models.PaymentDetails;
import com.example.bookingapp.model.AccessToken;
import com.example.bookingapp.model.STKPush;
import com.example.bookingapp.services.DarajaApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.example.bookingapp.Constants.BUSINESS_SHORT_CODE;
import static com.example.bookingapp.Constants.CALLBACKURL;
import static com.example.bookingapp.Constants.PARTYB;
import static com.example.bookingapp.Constants.PASSKEY;
import static com.example.bookingapp.Constants.TRANSACTION_TYPE;

public class PaybleActivity extends AppCompatActivity implements View.OnClickListener {

    private DarajaApiClient mApiClient;
    private ProgressDialog mProgressDialog;


    TextView mAmount;
    EditText mPhone;
    Button mPay;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReferencePayment;
    TextView a,b,c;
    TextView totalCost;
    TextView totalSeat;
    String total, seats, distance, nameBus, dateBus, conditionBus;
    private ArrayList<Integer> mSelectedSeats;
    private String mBusId;


    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payble);

        mAmount = findViewById(R.id.etAmount);
        mPhone = findViewById(R.id.etPhone);


        firebaseAuth= FirebaseAuth.getInstance();
        databaseReferencePayment = FirebaseDatabase.getInstance().getReference();

//        getSupportActionBar().setTitle("You Can Pay");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        total=getIntent().getStringExtra("TOTALCOST");
        seats=getIntent().getStringExtra("TOTALSEAT");
        distance=getIntent().getStringExtra("DISTANCE");
        nameBus=getIntent().getStringExtra("NAME_BUS");
        dateBus=getIntent().getStringExtra("DATE_BUS");
        conditionBus=getIntent().getStringExtra("CONDITION_BUS");
        mSelectedSeats = getIntent().getIntegerArrayListExtra("SEATSET");
        mBusId = getIntent().getStringExtra("BUS_ID");

        a=(TextView)findViewById(R.id.textView11);
        b=(TextView)findViewById(R.id.textView21);
        c=(TextView)findViewById(R.id.textView31);

        a.setText(nameBus);
        b.setText(dateBus);
        c.setText(conditionBus);
        mPay = findViewById(R.id.btnPay);

        totalCost=(TextView)findViewById(R.id.etAmount);
        totalSeat=(TextView)findViewById(R.id.totalSeatsFinal);
        totalCost.setText(total);
        totalSeat.setText("Number Of Seats : "+seats);


        mProgressDialog = new ProgressDialog(this);
        mApiClient = new DarajaApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

        mPay.setOnClickListener(this);

        getAccessToken();



    }

    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view== mPay){
            String phone_number = mPhone.getText().toString();
            String amount = mAmount.getText().toString();
            performSTKPush(phone_number,amount);
        }
    }

    public void performSTKPush(String phone_number,String amount) {
        mProgressDialog.setMessage("Processing your request");
        mProgressDialog.setTitle("Please Wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                String.valueOf(amount),
                Utils.sanitizePhoneNumber(phone_number),
                PARTYB,
                Utils.sanitizePhoneNumber(phone_number),
                CALLBACKURL,
                "SmartLoan Ltd", //Account reference
                "SmartLoan STK PUSH by TDBSoft"  //Transaction description
        );

        mApiClient.setGetAccessToken(false);

        //Sending the data to the Mpesa API, remember to remove the logging when in production.
        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        Timber.d("post submitted to API. %s", response.body());
                        Date date = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                        final String strDate = formatter.format(date);

                        PaymentDetails newPayment = new PaymentDetails(amount, strDate, seats);
                        FirebaseUser user=firebaseAuth.getCurrentUser();
                        databaseReferencePayment.child(user.getUid()).child("PaymentDetails").push().setValue(newPayment).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    DatabaseReference tempRef2= databaseReferencePayment.child("BusDetails").child(mBusId);
                                    for(int seat :  mSelectedSeats) {
                                        tempRef2.child("BookedSeats").push().setValue(seat);
                                    }

                                    Intent intent=new Intent(PaybleActivity.this,FinishActivity.class);
                                    intent.putExtra("TOTALCOST",total);
                                    intent.putExtra("TOTALSEAT",seats);
                                    intent.putExtra("DISTANCE",distance);
                                    intent.putExtra("NAME_BUS",nameBus);
                                    intent.putExtra("DATE_BUS",dateBus);
                                    intent.putExtra("CONDITION_BUS",conditionBus);
                                    startActivity(intent);

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PaybleActivity.this, "Something Went Wrong!Try Again again.", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(PaybleActivity.this,PaybleActivity.class);
                                startActivity(intent);

                            }
                        });




                    } else {
                        Toast.makeText(PaybleActivity.this, "Something Went Wrong!Try Again.", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(PaybleActivity.this,PaybleActivity.class);
                        startActivity(intent);
                        Timber.e("Response %s", response.errorBody().string());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }





            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();
                Timber.e(t);
            }
        });


    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
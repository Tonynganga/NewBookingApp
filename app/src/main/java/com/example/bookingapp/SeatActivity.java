package com.example.bookingapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingapp.Models.Bus;
import com.example.bookingapp.Models.SeatDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SeatActivity extends AppCompatActivity {

    GridLayout mainGrid;
    float seatPrice;
    int totatCost = 0;
    int totalSeats = 0;
    TextView totalPrice;
    TextView totalBookedSeats;
    private Button buttonBook;
    private HashSet<Integer> selectedSeats;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    DatabaseReference mPriceRef;
    int finalDistance;
    private HashSet<Integer> mCurrentlyBookedSeats;
    private String mBusId;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);
        Intent intent1 = getIntent();
        Bundle extras = intent1.getExtras();
        String distance = extras.getString("SDistance");
        mBusId = extras.getString("BUS_ID");
        finalDistance = Integer.parseInt(distance);
        selectedSeats=new HashSet<>();
        mCurrentlyBookedSeats = new HashSet<>();
//        List<Integer> range = IntStream.rangeClosed(start, end)
//                .boxed().collect(Collectors.toList());


        mainGrid = findViewById(R.id.mainGrid);
        totalBookedSeats = findViewById(R.id.total_seats);
        totalPrice = findViewById(R.id.total_cost);
        buttonBook = findViewById(R.id.btnBook);


        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mPriceRef = FirebaseDatabase.getInstance().getReference().child("Prices");
        mPriceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                seatPrice = Float.parseFloat(snapshot.child("price").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference  mBookedSeatsRef = FirebaseDatabase.getInstance().getReference().child("BusDetails").child(mBusId).child("BookedSeats").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mBookedSeatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Integer no = snap.getValue(Integer.class);
                        mCurrentlyBookedSeats.add(no);
                        Toast.makeText(SeatActivity.this, "Booked Seats"+mCurrentlyBookedSeats, Toast.LENGTH_SHORT).show();
                    }
                }
                setToggleEvent(mainGrid);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //Set Event

        final String nameBus=getIntent().getStringExtra("NAME_BUS");
        final String dateBus=getIntent().getStringExtra("DATE_BUS");
        final String conditionBus=getIntent().getStringExtra("CONDITION_BUS");

        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String totalPriceI=totalPrice.getText().toString().trim();
                String totalBookedSeatsI=totalBookedSeats.getText().toString().trim();

                SeatDetails seatDetails =new SeatDetails(totalPriceI,totalBookedSeatsI,new ArrayList<>(selectedSeats));

                FirebaseUser user=firebaseAuth.getCurrentUser();
//                databaseReference.child(user.getUid()).child("SeatDetails").push().setValue(seatDetails);
//                DatabaseReference tempRef= databaseReference.child(user.getUid()).child("SeatDetails");
//                for(int seat : new ArrayList<>(selectedSeats)) {
//                    tempRef.child("BookedSeats").push().setValue(seat);
//                }

                //store selected seats details
//                DatabaseReference tempRef2= databaseReference.child("BusDetails").child(mBusId);
//                for(int seat : new ArrayList<>(selectedSeats)) {
//                    tempRef2.child("BookedSeats").push().setValue(seat);
//                }

                Intent intent=new Intent(SeatActivity.this,PaybleActivity.class);
                intent.putExtra("TOTALCOST",totalPriceI);
                intent.putExtra("TOTALSEAT",totalBookedSeatsI);
                intent.putExtra("SEATSET", new ArrayList<>(selectedSeats));
                intent.putExtra("DISTANCE", distance);
                intent.putExtra("NAME_BUS",nameBus);
                intent.putExtra("SEAT_DETAILS",seatDetails);
                intent.putExtra("BUS_ID",mBusId);
                intent.putExtra("DATE_BUS",dateBus);
                intent.putExtra("CONDITION_BUS",conditionBus);

                startActivity(intent);

            }
        });

    }

    private  boolean askPermissions(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {

            return true;
        }else {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE},
                    1);
            return false;

        }

    }

    private void setToggleEvent(GridLayout mainGrid) {


        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            TextView lTextView=(TextView) cardView.getChildAt(1);
            final int finalI = i;
            if(mCurrentlyBookedSeats.contains(Integer.parseInt(lTextView.getText().toString()))){
                cardView.setVisibility(View.GONE);
                continue;
            }
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (cardView.getCardBackgroundColor().getDefaultColor() != ContextCompat.getColor(getApplication(), R.color.green)) {
                        //Change background color
                        cardView.setCardBackgroundColor(getResources().getColor(R.color.green));
                        selectedSeats.add(Integer.parseInt(lTextView.getText().toString()));
                        totatCost += (seatPrice * selectedSeats.size());
                        ++totalSeats;
                        Toast.makeText(SeatActivity.this, "You Selected Seat Number :" + (finalI + 1), Toast.LENGTH_SHORT).show();

                    } else {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        selectedSeats.remove(Integer.parseInt(lTextView.getText().toString()));
                        totatCost -= (seatPrice * selectedSeats.size());
                        --totalSeats;
                        Toast.makeText(SeatActivity.this, "You Unselected Seat Number :" + (finalI + 1), Toast.LENGTH_SHORT).show();
                    }
                    totalPrice.setText( "" + totatCost);
                    totalBookedSeats.setText(""+selectedSeats.size());
                }
            });
        }
    }
}
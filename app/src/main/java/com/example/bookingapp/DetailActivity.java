package com.example.bookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingapp.Models.Bus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

     TextView a,b,c,d,e,f,g,h,i,j,k,l,m,n;
     DatabaseReference databaseReference1,databaseReference2,databaseReference3,databaseReference4,
            databaseReference5;
    private FirebaseAuth firebaseAuth;

    Toolbar toolbar;
    private Button cancelBooking;
    private List<Bus> busList;
    private BusAdapter adapter;
    String BusId;

    ArrayList<Integer> mSelectedSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        BusId = getIntent().getStringExtra("BUS_ID");

//        mSelectedSeats = getIntent().getIntegerArrayListExtra("SEATSET");


//        toolbar = findViewById(R.id.app_bar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("History");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        cancelBooking= findViewById(R.id.cancelBooking);

        a= findViewById(R.id.busDetailName1);
        b= findViewById(R.id.busDetailDate1);
        m = findViewById(R.id.busDetailTime1);
        c= findViewById(R.id.busDetailFrom1);
        d= findViewById(R.id.busDetailTo1);
        e= findViewById(R.id.busDetailCondition1);

        f= findViewById(R.id.bookingDetailFrom1);
        g= findViewById(R.id.bookingDetailTo1);

        h= findViewById(R.id.ticketDetailNumber1);
//        n.setText("Seats" + mSelectedSeats.toString());
        i= findViewById(R.id.ticketDetailPrice1);

        j= findViewById(R.id.customerDetailName1);
        k= findViewById(R.id.customerDetailEmail1);
        l= findViewById(R.id.customerDetailPhone1);
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference1= FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("BusBookingDetails");
        databaseReference2= FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("BookingDetails");
//        databaseReference3= FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("SeatDetails").child(BusId);
        databaseReference5= FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("SeatDetails").child("bookedSeats");
        databaseReference4= FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

//        DatabaseReference  mBookedSeatsRef = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("SeatDetails").child(BusId);
//        mBookedSeatsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for (DataSnapshot snap : snapshot.getChildren()) {
//                        String key = snap.getKey();
//                        mBookedSeatsRef.child(key).child("bookedSeats").addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                Integer no = snapshot.getValue(Integer.class);
//                                n.setText(no);
//                                Toast.makeText(DetailActivity.this, ""+no, Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                    }
//                }
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        databaseReference5.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                for (DataSnapshot snapshot: task.getResult().getChildren()){
//                    String seats = snapshot.getChildren().toString();
//                    n.setText(seats);
//                }
//            }
//        });

        databaseReference1.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {

                    String busDetailName=snapshot.child("travelsName").getValue().toString();
                    String busDetailDate=snapshot.child("date").getValue().toString();
                    String busDetailTime=snapshot.child("time").getValue().toString();
                    String busDetailFrom=snapshot.child("from").getValue().toString();
                    String busDetailTo=snapshot.child("to").getValue().toString();
                    String busDetailCondition=snapshot.child("busCondition").getValue().toString();

                    a.setText(""+busDetailName);
                    b.setText(" Date              :  "+busDetailDate);
                    m.setText(" Time              :  "+busDetailTime);
                    c.setText(" From             :  "+busDetailFrom);
                    d.setText(" To                  :  "+busDetailTo);
                    e.setText(" Condition     :  "+busDetailCondition);
                }
            }
        });

        databaseReference2.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String bookingDetailFrom=snapshot.child("from").getValue().toString();
                    String bookingDetailTo=snapshot.child("to").getValue().toString();

                    f.setText(" From            :  "+bookingDetailFrom);
                    g.setText(" To                 :  "+bookingDetailTo);

                }

            }
        });
//        databaseReference3.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                for (DataSnapshot snapshot : task.getResult().getChildren()) {
//                    String ticketDetailNumber=snapshot.child("total_seats").getValue().toString();
//                    String ticketDetailPrice=snapshot.child("total_cost").getValue().toString();
//
//                    h.setText(" Number of Seats    :  "+ticketDetailNumber);
//                    i.setText(" Total Cost                :  "+ticketDetailPrice);
//                }
//
//            }
//        });

        cancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user1 = firebaseAuth.getCurrentUser();
                DatabaseReference databaseReferenceA= FirebaseDatabase.getInstance().getReference().child(user1.getUid());
                databaseReferenceA.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference databaseReferenceB = FirebaseDatabase.getInstance().getReference().child("BusDetails").child(BusId).child("BookedSeats").child(user1.getUid());
                            databaseReferenceB.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(DetailActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            Toast.makeText(DetailActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                startActivity(new Intent(getApplicationContext(),DisplayLocationsActivity.class));
                finish();

            }
        });
        databaseReference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String customerDetailName=dataSnapshot.child("name").getValue().toString();
                String customerDetailEmail1=dataSnapshot.child("email").getValue().toString();
                String customerDetailPhone=dataSnapshot.child("phone").getValue().toString();

                j.setText(customerDetailName);
                k.setText(customerDetailEmail1);
                l.setText(customerDetailPhone);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
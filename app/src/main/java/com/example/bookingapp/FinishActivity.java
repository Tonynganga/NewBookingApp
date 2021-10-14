package com.example.bookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FinishActivity extends AppCompatActivity {

    private Button buttonHome, buttonReceipt;
    private TextView a,b,c;
    DatabaseReference databaseReference, databaseReferencetwo;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Toolbar toolbar;
    String total, seats, distance, nameBus, dateBus, conditionBus;
    String name, phone;
    private  ArrayList<Integer> mSelectedSeats;
    private String mBusId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
//        toolbar = findViewById(R.id.app_bar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Booking  Finished");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        a= findViewById(R.id.textView11);
        b= findViewById(R.id.textView21);
        c= findViewById(R.id.textView31);
        buttonHome= findViewById(R.id.btnHome);
        buttonReceipt = findViewById(R.id.btnPrintReceipt);


        mBusId = getIntent().getStringExtra("BUS_ID");

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        buttonReceipt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                createPdfFile(Common.getAppPath(FinishActivity.this) + "test_pdf.pdf");
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReferencetwo= FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        databaseReference= FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("BusBookingDetails");
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        String busDetailName=snapshot.child("travelsName").getValue().toString();
                        String busDetailDate=snapshot.child("date").getValue().toString();
                        String busDetailCondition=snapshot.child("busCondition").getValue().toString();

                        a.setText(busDetailName);
                        b.setText(busDetailDate);
                        c.setText(busDetailCondition);

                    }
                }
            }
        });


        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String BusId = getIntent().getStringExtra("BUS_ID");
//                String message="Your Ticket Booking Success";
//                NotificationCompat.Builder builder=new NotificationCompat.Builder(FinishActivity.this)
//                        .setSmallIcon(R.drawable.detail)
//                        .setContentTitle("New Notification")
//                        .setContentText(message)
//                        .setAutoCancel(true);
                Intent intent=new Intent(FinishActivity.this,DisplayLocationsActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("message",message);
                intent.putExtra("BUS_ID", mBusId);
//                intent.putExtra("SEATSET", new ArrayList<>(mSelectedSeats));
                startActivity(intent);

//                PendingIntent pendingIntent=PendingIntent.getActivity(FinishActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(pendingIntent);
//                NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify(0,builder.build());
//                startActivity(new Intent(getApplicationContext(), DisplayLocationsActivity.class));
            }
        });
    }

    private void createPdfFile(String path) {
        databaseReferencetwo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue().toString();
                phone = snapshot.child("phone").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        total=getIntent().getStringExtra("TOTALCOST");
        seats=getIntent().getStringExtra("TOTALSEAT");
        distance=getIntent().getStringExtra("DISTANCE");
        nameBus=getIntent().getStringExtra("NAME_BUS");
        dateBus=getIntent().getStringExtra("DATE_BUS");
        conditionBus=getIntent().getStringExtra("CONDITION_BUS");
        mSelectedSeats = getIntent().getIntegerArrayListExtra("SEATSET");

        if (new File(path).exists())
            new File(path).delete();
        try {
            Document document = new Document();

            //save
            PdfWriter.getInstance(document,new FileOutputStream(path));
            //open to write
            document.open();

            //setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Mombasa Raha Co. Ltd");
            document.addCreator("Mombasa Raha");

            //print setting
            BaseColor colorAccent = new BaseColor(0,153,204,255);
            float fontsize = 20.0f;
            float valuefontsize = 26.0f;

            //custom font
            BaseFont fontName = BaseFont.createFont("assets/fonts/BrandonText-Medium.otf", "UTF-8",BaseFont.EMBEDDED);


            //create font
            Font titleFont = new Font(fontName, 46.0f, Font.NORMAL, BaseColor.BLUE);
            Font subtitleFont = new Font(fontName, 36.0f, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "Mombasa Raha Co. Ltd", Element.ALIGN_CENTER, titleFont);
            addNewItem(document, "You Trust We Deliver!", Element.ALIGN_CENTER, subtitleFont);

//            //add more
            Font orderNumberFont = new Font(fontName, fontsize, Font.NORMAL, colorAccent);
//            addNewItem(document, "Travelling Date:", Element.ALIGN_LEFT, orderNumberFont);
//
            Font orderNumberValueFont = new Font(fontName, valuefontsize, Font.NORMAL, BaseColor.BLACK);
//            addNewItem(document, "Bus Condition:" , Element.ALIGN_LEFT, orderNumberValueFont);
//
//            addLineSeperator(document);
//
//            addNewItem(document, "No of Seats:", Element.ALIGN_LEFT, orderNumberFont);
//            addNewItem(document, "Distance:", Element.ALIGN_LEFT, orderNumberValueFont);
//
//            addLineSeperator(document);
//
//
//            addNewItem(document, "Acount Name", Element.ALIGN_LEFT, orderNumberFont);
//            addNewItem(document, "Eddy Lee", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeperator(document);


            //add product order detail
            addLineSpace(document);
            addNewItem(document, "Receipt Detail.", Element.ALIGN_CENTER, titleFont);
            addLineSeperator(document);


            //add product order detail
            addNewItem(document, "Name:"+name, Element.ALIGN_CENTER, subtitleFont);
            addLineSeperator(document);
            addLineSeperator(document);


            //add product order detail
            addLineSpace(document);
            addNewItem(document, "Phone:"+phone, Element.ALIGN_CENTER, subtitleFont);
            addLineSeperator(document);

            //item 1
            addNewItemWithLeftAndRight(document, "Bus Name:", ""+nameBus, subtitleFont, orderNumberValueFont);

            addLineSeperator(document);

            addNewItemWithLeftAndRight(document, "Travelling date:", ""+dateBus, subtitleFont, orderNumberValueFont);

            addLineSeperator(document);

            //item 2
            addNewItemWithLeftAndRight(document, "Bus Condition:", ""+conditionBus, subtitleFont, orderNumberValueFont);
            addLineSeperator(document);

            addNewItemWithLeftAndRight(document, "Number of Seats", ""+seats, subtitleFont, orderNumberValueFont);

            addLineSeperator(document);

            addNewItemWithLeftAndRight(document, "Seats Number(s)", ""+mSelectedSeats.toString(), subtitleFont, orderNumberValueFont);

            addLineSeperator(document);

            addNewItemWithLeftAndRight(document, "Distance", ""+distance, subtitleFont, orderNumberValueFont);


            addLineSeperator(document);
            //Total
            addLineSpace(document);
            addLineSpace(document);

            addNewItemWithLeftAndRight(document, "Total", "Ksh "+total, subtitleFont, orderNumberValueFont);

            document.close();

            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

            printPDF();



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printPDF() {
        PrintManager printManager = (PrintManager)getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printDocumentAdapter = new pdfDocumentAdapter(FinishActivity.this, Common.getAppPath(FinishActivity.this) + "test_pdf.pdf");
            printManager.print("Document",printDocumentAdapter, new PrintAttributes.Builder().build());
        }catch (Exception ex){
            Log.e("EMMMTDev", "" + ex.getMessage());
        }

    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException {
        Chunk chunkTextLeft = new Chunk(textLeft, textLeftFont);
        Chunk chunkTextRight = new Chunk(textRight, textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);
    }

    private void addLineSeperator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,60));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);

    }

}
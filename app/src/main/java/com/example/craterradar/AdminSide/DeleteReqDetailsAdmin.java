package com.example.craterradar.AdminSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.craterradar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.security.spec.ECField;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeleteReqDetailsAdmin extends AppCompatActivity implements View.OnClickListener{
    ActionBar actionBar;
    ProgressBar progressBar;
    CircleImageView oldImage,newImage;
    TextView oldLat,oldLong,oldTimeStamp,oldDescription,newLat,newLong,newTimeStamp,newDescription,UID;
    Button Accept_btn,Reject_btn;
    String Pothole_Delete_Req_ID,
            Pothole_ID,
            Pothole_Old_ImageURL,
            Pothole_New_ImageURL,
            Pothole_Old_Latitude,
            Pothole_New_Latitude,
            Pothole_Old_Longotide,
            Pothole_New_Longitude,
            Pothole_Old_TimeStamp,
            Pothole_New_TimeStamp,
            Pothole_Old_Description,
            Pothole_New_Description,
            Pothole_AddedBy_UID;
    Uri oldImageURI,newImageURI;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_req_details_admin);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.deleteDetails_progress_admin);
        oldImage = findViewById(R.id.old_image_details_admin);
        newImage = findViewById(R.id.new_image_details_admin);
        oldLat = findViewById(R.id.old_lat_details);
        oldLong = findViewById(R.id.old_long_details);
        oldTimeStamp = findViewById(R.id.old_timeStamp_details);
        oldDescription = findViewById(R.id.old_description_details);
        newLat = findViewById(R.id.new_lat_details);
        newLong = findViewById(R.id.new_long_details);
        newTimeStamp = findViewById(R.id.new_timeStamp_details);
        newDescription = findViewById(R.id.new_description_details);
        UID = findViewById(R.id.uid_deleteDetails_admin);
        Accept_btn = findViewById(R.id.accept_details_btn);
        Reject_btn = findViewById(R.id.reject_details_btn);


        getDetails();


        Accept_btn.setOnClickListener(this);
        Reject_btn.setOnClickListener(this);
    }







    private void getDetails() {
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable(){
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);

                Bundle bundle = getIntent().getExtras();
                Pothole_Delete_Req_ID = bundle.getString("Pothole_Delete_Req_ID");
                Pothole_ID = bundle.getString("Pothole_ID");
                Pothole_Old_ImageURL = bundle.getString("Pothole_Old_ImageURL");
                Pothole_New_ImageURL = bundle.getString("Pothole_New_ImageURL");
                Pothole_Old_Latitude = bundle.getString("Pothole_Old_Latitude");
                Pothole_New_Latitude = bundle.getString("Pothole_New_Latitude");
                Pothole_Old_Longotide = bundle.getString("Pothole_Old_Longitude");
                Pothole_New_Longitude = bundle.getString("Pothole_New_Longitude");
                Pothole_Old_TimeStamp = bundle.getString("Pothole_Old_TimeStamp");
                Pothole_New_TimeStamp = bundle.getString("Pothole_New_TimeStamp");
                Pothole_Old_Description = bundle.getString("Pothole_Old_Description");
                Pothole_New_Description = bundle.getString("Pothole_New_Description");
                Pothole_AddedBy_UID = bundle.getString("Pothole_AddedBy_UID");


                actionBar.setTitle(Pothole_Delete_Req_ID);
                oldImageURI = Uri.parse(Pothole_Old_ImageURL);
                newImageURI = Uri.parse(Pothole_New_ImageURL);
                Picasso.get().load(oldImageURI).into(oldImage);
                Picasso.get().load(newImageURI).into(newImage);

                oldLat.setText("Old Lat: "+Pothole_Old_Latitude);
                oldLong.setText("Old Long: "+Pothole_Old_Longotide);
                oldTimeStamp.setText("Old TimeStamp: "+Pothole_Old_TimeStamp);
                oldDescription.setText("Old Description: "+Pothole_Old_Description);

                newLat.setText("New Lat: "+Pothole_New_Latitude);
                newLong.setText("New Long: "+Pothole_New_Longitude);
                newTimeStamp.setText("New TimeStamp: "+Pothole_New_TimeStamp);
                newDescription.setText("New Description: "+Pothole_New_Description);

                UID.setText("Pothole Added By: "+Pothole_AddedBy_UID);

                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {

        if(v == Accept_btn)
        {
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable(){
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(),"Accept Clicked!",Toast.LENGTH_LONG).show();
                    AcceptMethod();
                }});
        }
        else if(v == Reject_btn)
        {
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable(){
                @Override
                public void run() {
                    //Toast.makeText(getApplicationContext(),"Reject Clicked!",Toast.LENGTH_LONG).show();
                    RejectMethod();
                }});
        }
    }

    private void AcceptMethod() {

        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable(){
            @Override
            public void run() {


                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                storage = FirebaseStorage.getInstance();
                final StorageReference storageReference = storage.getReference();

                final StorageReference deleteReq_ref = storageReference.child("Pothole Delete Request").child(Pothole_AddedBy_UID).child(Pothole_Delete_Req_ID).child("PotholeDeleteRequestImage.jpg");
                deleteReq_ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        final StorageReference pothole_ref = storageReference.child("Potholes").child(Pothole_AddedBy_UID).child(Pothole_ID).child("Pothole_Image.jpg");
                        pothole_ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Remove Values from Delete Reqest Table
                                databaseReference.child("Pothole Delete Request").child(Pothole_AddedBy_UID).addValueEventListener(new ValueEventListener()
                                {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                    {
                                        if(dataSnapshot.exists())
                                        {
                                            for(final DataSnapshot ds : dataSnapshot.getChildren())
                                            {
                                                if(ds.getKey().contentEquals(Pothole_Delete_Req_ID))
                                                {

                                                    databaseReference.child("Potholes").addValueEventListener(new ValueEventListener()
                                                    {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                                        {
                                                            if(dataSnapshot.exists())
                                                            {
                                                                for(DataSnapshot ds1 : dataSnapshot.getChildren())
                                                                {
                                                                    if(ds1.getKey().contentEquals(Pothole_ID))
                                                                    {

                                                                        // progressBar.setVisibility(View.GONE);
                                                                        ds.getRef().removeValue();
                                                                        //Toast.makeText(getApplicationContext(),"Delete Request Accepted Successfully",Toast.LENGTH_SHORT).show();
                                                                        progressBar.setVisibility(View.GONE);
                                                                        ds1.getRef().removeValue();
                                                                        Toast.makeText(getApplicationContext(),"Delete Request Accepted Successfully",Toast.LENGTH_SHORT).show();

                                                                        Intent i = new Intent(DeleteReqDetailsAdmin.this,Admin_host.class);
                                                                        startActivity(i);
                                                                        finish();
                                                                    }
                                                                }
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError)
                                                        {
                                                            progressBar.setVisibility(View.GONE);
                                                            Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                                                        }
                                                    });



                                                }
                                                else
                                                {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(getApplicationContext(),"Something is wrong! Please try again",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                        else
                                        {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(),"Something is wrong! Please try again",Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError)
                                    {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(),"Database Error: "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void RejectMethod()
    {


        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable(){
            @Override
            public void run() {


                progressBar.setVisibility(View.VISIBLE);
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Pothole Delete Request");
                storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();



                final StorageReference reference = storageReference.child("Pothole Delete Request").child(Pothole_AddedBy_UID).child(Pothole_Delete_Req_ID).child("PotholeDeleteRequestImage.jpg");
                reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        databaseReference.child(Pothole_AddedBy_UID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                {
                                    for(DataSnapshot ds : dataSnapshot.getChildren())
                                    {
                                        if(ds.getKey().contentEquals(Pothole_Delete_Req_ID))
                                        {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(),"Request Successfully Rejected!",Toast.LENGTH_LONG).show();
                                            ds.getRef().removeValue();
                                            Intent i = new Intent(DeleteReqDetailsAdmin.this,Admin_host.class);
                                            startActivity(i);
                                            finish();

                                        }
                                        else {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(),"Something is wrong! Please try again",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),"Something is wrong! Please try again",Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Database Error: "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}

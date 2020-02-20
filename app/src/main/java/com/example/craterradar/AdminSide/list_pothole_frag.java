package com.example.craterradar.AdminSide;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.craterradar.AdminSide.ListAdapters.PotholeListAdapter;
import com.example.craterradar.AdminSide.ListAdapters.UserListAdapter;
import com.example.craterradar.AdminSide.ModelClass.PotholeList;
import com.example.craterradar.AdminSide.ModelClass.UserList;
import com.example.craterradar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class list_pothole_frag extends Fragment {

    RecyclerView recyclerView;
    TextView textView;
    Context context;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ArrayList<PotholeList> potholeListArrayList = new ArrayList<>();
    PotholeListAdapter potholeListAdapter;

    String potholeID,potholeImageurl,pothole_lat,pothole_long,potholeDangerLevel,potholeDescription,potholeTimeStamp,potholeUploadedby;


    public list_pothole_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_pothole_frag, container, false);
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        progressBar = view.findViewById(R.id.progress_PotholeList);
        recyclerView = view.findViewById(R.id.pothole_list);
        textView = view.findViewById(R.id.no_pothole_text);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Potholes");

        potholeListArrayList.clear();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                { progressBar.setVisibility(View.VISIBLE);
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        potholeID = ds.getKey();
                        potholeImageurl = ds.child("potholeImageURL").getValue().toString();
                        pothole_lat = ds.child("location_Lat").getValue().toString();
                        pothole_long = ds.child("location_Long").getValue().toString();
                        potholeDangerLevel = ds.child("dangerLevel").getValue().toString();
                        potholeDescription = ds.child("description").getValue().toString();
                        potholeTimeStamp = ds.child("timeStamp").getValue().toString();
                        potholeUploadedby = ds.child("uid").getValue().toString();

                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.GONE);

                        //Log.e("Pothole LatLong",pothole_lat+" "+pothole_long);
                        potholeListArrayList.add(new PotholeList(potholeID,potholeImageurl,pothole_lat,pothole_long,potholeDangerLevel,potholeDescription,potholeTimeStamp,potholeUploadedby));

                    }
                    potholeListAdapter = new PotholeListAdapter(potholeListArrayList,context);
                    recyclerView.setAdapter(potholeListAdapter);
                    potholeListAdapter.notifyDataSetChanged();
                    potholeListAdapter.setOnClickListner(onClickListener);
                    if(potholeListArrayList.isEmpty())
                    {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"Error:"+databaseError,Toast.LENGTH_LONG).show();
            }
        });
    }
    public View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override

        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            //int position = viewHolder.getAdapterPosition();
        }
    };
}

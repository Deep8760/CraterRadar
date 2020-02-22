package com.example.craterradar.UserSide;


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
import android.widget.TextView;
import android.widget.Toast;

import com.example.craterradar.R;
import com.example.craterradar.UserSide.ListAdapters.AddedPotholeListAdapter;
import com.example.craterradar.UserSide.ModelClass.AddedPothole;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class List_of_All_Added_Pothole_frag extends Fragment {
    RecyclerView recyclerView;
    TextView emptyText;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String uid,potholeID,potholeImageURL,addedByUID,latitude,longitude,dangerLevel,description,timeStamp;
    Context context;
    ArrayList<AddedPothole> addedPotholeArrayList = new ArrayList<>();
    AddedPotholeListAdapter addedPotholeListAdapter;


    public List_of_All_Added_Pothole_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_of__all__added__pothole_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        recyclerView = view.findViewById(R.id.added_pothole_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        emptyText = view.findViewById(R.id.no_pothole_text);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Potholes");
        uid = firebaseAuth.getCurrentUser().getUid();
        addedPotholeArrayList.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {

                        if(ds.child("uid").getValue().toString().contentEquals(uid))
                        {
                           /* Log.e("DangerLevel:", ds.child("dangerLevel").getValue().toString());
                            Log.e("Description:", ds.child("description").getValue().toString());
                            Log.e("Lat:", ds.child("location_Lat").getValue().toString());
                            Log.e("Long:", ds.child("location_Long").getValue().toString());
                            Log.e("Timestamo:", ds.child("timeStamp").getValue().toString());*/

                            //location = new LatLng(Double.parseDouble(ds.child("location_Lat").getValue().toString()),Double.parseDouble(ds.child("location_Long").getValue().toString()));
                            //Log.e("Location",location.toString());
                            latitude = ds.child("location_Lat").getValue().toString();
                            longitude = ds.child("location_Long").getValue().toString();
                            dangerLevel = ds.child("dangerLevel").getValue().toString();
                            description = ds.child("description").getValue().toString();
                            timeStamp = ds.child("timeStamp").getValue().toString();
                            potholeID = ds.child("potholeid").getValue().toString();
                            potholeImageURL = ds.child("potholeImageURL").getValue().toString();
                            addedByUID = ds.child("uid").getValue().toString();
                            //Log.e("before Added in List:",location+"\n"+dangerLevel+"\n"+timeStamp+"\n"+description);
                            addedPotholeArrayList.add(new AddedPothole(potholeID,potholeImageURL,addedByUID,latitude,longitude,dangerLevel,timeStamp,description));

                        }
                    }
                    addedPotholeListAdapter = new AddedPotholeListAdapter(addedPotholeArrayList,context);
                    recyclerView.setAdapter(addedPotholeListAdapter);
                    addedPotholeListAdapter.notifyDataSetChanged();
                    addedPotholeListAdapter.setOnClickListner(onClickListener);
                    if(!addedPotholeArrayList.isEmpty())
                    {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyText.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"Data Error : "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
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

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
import com.example.craterradar.UserSide.ListAdapters.DeleteReqListAdapterUser;
import com.example.craterradar.UserSide.ModelClass.AddedPothole;
import com.example.craterradar.UserSide.ModelClass.DeleteReqData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DeleteRequestList_user extends Fragment {
    Context context;
    RecyclerView recyclerView;
    TextView textView;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String uid;
    String  DeleteReqID,
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

    ArrayList<DeleteReqData> deleteReqDataArrayList = new ArrayList<>();
    DeleteReqListAdapterUser deleteReqListAdapterUser;

    public DeleteRequestList_user() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_request_list_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        recyclerView = view.findViewById(R.id.deleteReq_list_user);
        textView = view.findViewById(R.id.no_data_deleteReq_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Pothole Delete Request");
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        deleteReqDataArrayList.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        if(uid.contentEquals(ds.getKey()))
                        {
                           for (DataSnapshot ds1 : ds.getChildren())
                           {
                               DeleteReqID = ds1.getKey();
                               Log.e("Delete REQ ID ",ds1.getKey());
                               Pothole_ID = ds1.child("pothole_ID").getValue().toString();
                               Pothole_Old_ImageURL = ds1.child("pothole_Old_ImageURL").getValue().toString();
                               Pothole_New_ImageURL = ds1.child("pothole_New_ImageURL").getValue().toString();
                               Pothole_Old_Latitude = ds1.child("pothole_Old_Latitude").getValue().toString();
                               Pothole_New_Latitude = ds1.child("pothole_New_Latitude").getValue().toString();
                               Pothole_Old_Longotide = ds1.child("pothole_Old_Longotide").getValue().toString();
                               Pothole_New_Longitude = ds1.child("pothole_New_Longitude").getValue().toString();
                               Pothole_Old_TimeStamp = ds1.child("pothole_Old_TimeStamp").getValue().toString();
                               Pothole_New_TimeStamp = ds1.child("pothole_New_TimeStamp").getValue().toString();
                               Pothole_Old_Description = ds1.child("pothole_Old_Description").getValue().toString();
                               Pothole_New_Description = ds1.child("pothole_New_Description").getValue().toString();
                               Pothole_AddedBy_UID = ds1.child("pothole_AddedBy_UID").getValue().toString();

                               deleteReqDataArrayList.add(new DeleteReqData(DeleteReqID,Pothole_ID,Pothole_Old_ImageURL,
                                       Pothole_New_ImageURL,
                                       Pothole_Old_Latitude,
                                       Pothole_New_Latitude,
                                       Pothole_Old_Longotide,
                                       Pothole_New_Longitude,
                                       Pothole_Old_TimeStamp,
                                       Pothole_New_TimeStamp,
                                       Pothole_Old_Description,
                                       Pothole_New_Description,
                                       Pothole_AddedBy_UID));
                           }
                        }
                    }
                    deleteReqListAdapterUser = new DeleteReqListAdapterUser(deleteReqDataArrayList,context);
                    recyclerView.setAdapter(deleteReqListAdapterUser);
                    deleteReqListAdapterUser.notifyDataSetChanged();
                    if(!deleteReqDataArrayList.isEmpty())
                    {
                        recyclerView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.GONE);
                    }
                }
                else {
                    textView.setVisibility(View.VISIBLE);
                    Toast.makeText(context,"Data not available!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"Data Error : "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}

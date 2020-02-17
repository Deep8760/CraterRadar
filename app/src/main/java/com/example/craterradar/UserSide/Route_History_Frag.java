package com.example.craterradar.UserSide;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.craterradar.R;
import com.example.craterradar.UserSide.ListAdapters.RouteHistoryListAdapter;
import com.example.craterradar.UserSide.ModelClass.RouteHistory;
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
public class Route_History_Frag extends Fragment {
    Context context;
    RecyclerView recyclerView;
    RouteHistoryListAdapter routeHistoryListAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String uid;
    TextView emptyText;
    ArrayList<RouteHistory> historyArrayList = new ArrayList<>();


    public Route_History_Frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_route__history_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        recyclerView = view.findViewById(R.id.searched_routes_list);
        emptyText = view.findViewById(R.id.no_history_text);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(uid);
        ((AppCompatActivity) getActivity()).getSupportActionBar();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot ds : dataSnapshot.child("SearchedRoutes").getChildren())
                    {

                        Log.e("Destination",ds.child("Destination").getValue().toString());
                       historyArrayList.add(new RouteHistory(ds.child("Destination").getValue().toString()));

                    }
                    routeHistoryListAdapter =new RouteHistoryListAdapter(historyArrayList,context);
                    recyclerView.setAdapter(routeHistoryListAdapter);
                    routeHistoryListAdapter.notifyDataSetChanged();
                    if(!historyArrayList.isEmpty())
                    {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyText.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}

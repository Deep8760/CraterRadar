package com.example.craterradar.UserSide.ListAdapters;

import android.content.Context;
import android.util.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.craterradar.R;
import com.example.craterradar.UserSide.ModelClass.RouteHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RouteHistoryListAdapter extends RecyclerView.Adapter<RouteHistoryListAdapter.ItemViewHolder>
{
    ArrayList<RouteHistory> routeHistoryArrayList;
    Context context;
    private View.OnClickListener itemClickListner;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String uid;
    private Object lock = new Object();

    public RouteHistoryListAdapter(ArrayList<RouteHistory> routeHistoryArrayList, Context context) {
        this.routeHistoryArrayList = routeHistoryArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_history_list_item,parent,false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        final RouteHistory routeHistory = routeHistoryArrayList.get(position);
        holder.routeHistoryText.setText(routeHistory.getDestination());
        holder.deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete Code
                //Toast.makeText(context,routeHistory.getDestination()+"Clicked!",Toast.LENGTH_LONG).show();
                firebaseAuth = FirebaseAuth.getInstance();
                uid = firebaseAuth.getCurrentUser().getUid();
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Users").child(uid).child("SearchedRoutes");
                synchronized (lock) {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if (ds.child("Destination").getValue().toString().contentEquals(routeHistory.getDestination())) {
                                        ds.getRef().removeValue();
                                        Toast.makeText(context, routeHistory.getDestination() + "deleted Successfully", Toast.LENGTH_LONG).show();
                                        routeHistoryArrayList.remove(position);
                                    }
                                }
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, "DataSnapShot doesn't exist!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(context, "DatabaseError :" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return routeHistoryArrayList.size();
    }




    public void setOnClickListner(View.OnClickListener clickListner)
    {
        itemClickListner = clickListner;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView routeHistoryText;
        Button deleteBTN;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            routeHistoryText = itemView.findViewById(R.id.history_item_text);
            deleteBTN = itemView.findViewById(R.id.history_item_deleteBtn);
            itemView.setOnClickListener(itemClickListner);
        }
    }
}

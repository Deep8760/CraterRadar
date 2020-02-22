package com.example.craterradar.UserSide.ListAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.craterradar.R;
import com.example.craterradar.UserSide.ModelClass.AddedPothole;
import com.example.craterradar.UserSide.PotholeDeleteReq_user;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class AddedPotholeListAdapter extends RecyclerView.Adapter<AddedPotholeListAdapter.ItemViewHolder> {
    Context context;
    ArrayList<AddedPothole> list;
    AddedPothole addedPothole_data;
    LatLng latLng;
    Bundle bundle;
    private View.OnClickListener itemClickListner;


    public AddedPotholeListAdapter(ArrayList<AddedPothole> addedPotholeArrayList, Context context) {
        this.context = context;
        list = addedPotholeArrayList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_pothole_list_item,parent,false);
        return new AddedPotholeListAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        addedPothole_data = list.get(position);
        holder.dangerLevel.setText(addedPothole_data.getDangerlevel());
        holder.timeStamp.setText(addedPothole_data.getTimestamp());
        holder.description.setText(addedPothole_data.getDescription());
        holder.potholeID = addedPothole_data.getPotholeID();
        holder.potholeImageURL = addedPothole_data.getPotholeImageURL();
        holder.addedByUID = addedPothole_data.getAddedByUID();

        latLng = new LatLng(Double.parseDouble(addedPothole_data.getLatitude()),Double.parseDouble(addedPothole_data.getLongitude()));
        holder.setMap(latLng);
        bundle = new Bundle();
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("PotholeID",holder.potholeID);
                bundle.putString("PotholeImageURL",holder.potholeImageURL);
                bundle.putString("PotholeLat",addedPothole_data.getLatitude());
                bundle.putString("PotholeLong",addedPothole_data.getLongitude());
                bundle.putString("PotholeDangerLevel",addedPothole_data.getDangerlevel());
                bundle.putString("PotholeDescription",addedPothole_data.getDescription());
                bundle.putString("PotholeTimeStamp",addedPothole_data.getTimestamp());
                bundle.putString("PotholeAddedByUID",addedPothole_data.getAddedByUID());
                Intent i = new Intent(context, PotholeDeleteReq_user.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtras(bundle);
                context.startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setOnClickListner(View.OnClickListener clickListner)
    {
        itemClickListner = clickListner;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        GoogleMap map;
        MapView mapView;
        TextView dangerLevel,timeStamp,description;
        Button delete_btn;
        LatLng latLng;
        String potholeID,potholeImageURL,addedByUID;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mapView = itemView.findViewById(R.id.pothole_list_item_map);
            dangerLevel = itemView.findViewById(R.id.pothole_list_item_danger_level);
            timeStamp = itemView.findViewById(R.id.pothole_list_item_timeStamp);
            description = itemView.findViewById(R.id.pothole_list_item_description);
            delete_btn = itemView.findViewById(R.id.potholeDelete_btn);

            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
            delete_btn.setOnClickListener(itemClickListner);
        }

        public void setMap(LatLng latLng) {
            this.latLng = latLng;
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.clear();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f));
            map.addMarker(new MarkerOptions().position(latLng));
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.getUiSettings().setMapToolbarEnabled(false);
        }
    }
}

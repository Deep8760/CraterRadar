package com.example.craterradar.UserSide.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.craterradar.R;
import com.example.craterradar.UserSide.ModelClass.AddedPothole;
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
    ArrayList<AddedPothole> addedPotholeArrayList = new ArrayList<>();
    AddedPothole addedPothole_data;

    public AddedPotholeListAdapter(ArrayList<AddedPothole> addedPotholeArrayList, Context context) {
        this.context = context;
        this.addedPotholeArrayList = addedPotholeArrayList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_pothole_list_item,parent,false);
        return new AddedPotholeListAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddedPotholeListAdapter.ItemViewHolder holder, int position) {
        if(holder == null)
        {
            return;
        }
        holder.bindView(position);
    }


    @Override
    public int getItemCount() {
        return addedPotholeArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        GoogleMap googleMap;
        MapView mapView;
        TextView dangerLevel,timeStamp,description;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mapView = itemView.findViewById(R.id.pothole_list_item_map);
            dangerLevel = itemView.findViewById(R.id.pothole_list_item_danger_level);
            timeStamp = itemView.findViewById(R.id.pothole_list_item_timeStamp);
            description = itemView.findViewById(R.id.pothole_list_item_description);
            if(mapView != null)
            {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(context);
            this.googleMap = googleMap;
            LatLng latLng = addedPothole_data.getLocation();

            // Add a marker for this item and set the camera
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f));
            this.googleMap.addMarker(new MarkerOptions().position(latLng));

            // Set the map type back to normal.
            this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        private void setMapLocation() {

        }

        public void bindView(int position)
        {
            addedPothole_data = addedPotholeArrayList.get(position);
            if(addedPothole_data.getLocation() != null)
            {
                setMapLocation();
                dangerLevel.setText(addedPothole_data.getDangerlevel());
                timeStamp.setText(addedPothole_data.getTimestamp());
                description.setText(addedPothole_data.getDescription());
            }
        }
    }
}

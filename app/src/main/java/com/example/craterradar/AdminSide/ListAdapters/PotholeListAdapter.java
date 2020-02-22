package com.example.craterradar.AdminSide.ListAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.craterradar.AdminSide.ModelClass.PotholeList;
import com.example.craterradar.AdminSide.PotholeDetailsAdmin;
import com.example.craterradar.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PotholeListAdapter extends RecyclerView.Adapter<PotholeListAdapter.ItemViewHolder> {
    Context context;
    ArrayList<PotholeList> potholeListArrayList;
    PotholeList potholeList;
    Uri profileImage;
    Bundle detailsBundle = new Bundle();
    private View.OnClickListener itemClickListner;

    public PotholeListAdapter(ArrayList<PotholeList> potholeListArrayList, Context context) {
        this.potholeListArrayList = potholeListArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pothole_list_item_admin,parent,false);
        return new PotholeListAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        potholeList = potholeListArrayList.get(position);
        profileImage = Uri.parse(potholeList.getPotholeImageurl());
        Picasso.get().load(profileImage).into(holder.potholeImage);
        holder.potholeLocation.setText("Lat:"+potholeList.getPothole_lat()+" Long:"+potholeList.getPothole_long());
        holder.potholeTimeStamp.setText(potholeList.getPotholeTimeStamp());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsBundle.putString("PotholeID",potholeListArrayList.get(position).getPotholeID());
                detailsBundle.putString("PotholeImageUrl",potholeListArrayList.get(position).getPotholeImageurl());
                detailsBundle.putString("PotholeLat",potholeListArrayList.get(position).getPothole_lat());
                detailsBundle.putString("PotholeLong",potholeListArrayList.get(position).getPothole_long());
                detailsBundle.putString("PotholeDangerLevel",potholeListArrayList.get(position).getPotholeDangerLevel());
                detailsBundle.putString("PotholeDescription",potholeListArrayList.get(position).getPotholeDescription());
                detailsBundle.putString("PotholeTimeStamp",potholeListArrayList.get(position).getPotholeTimeStamp());
                detailsBundle.putString("PotholeUploadedBy",potholeListArrayList.get(position).getPotholeUploadedby());
                Intent i = new Intent(context, PotholeDetailsAdmin.class);
                i.putExtras(detailsBundle);
                v.getContext().startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return potholeListArrayList.size();
    }
    public void setOnClickListner(View.OnClickListener clickListner)
    {
        itemClickListner = clickListner;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        CircleImageView potholeImage;
        TextView potholeLocation,potholeTimeStamp;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            potholeImage = itemView.findViewById(R.id.potholeImage_potholeList_admin);
            potholeLocation = itemView.findViewById(R.id.potholeLocation_list_admin);
            potholeTimeStamp = itemView.findViewById(R.id.pothoeTime_list_admin);
            itemView.setOnClickListener(itemClickListner);
        }
    }
}

package com.example.craterradar.AdminSide.ListAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.craterradar.AdminSide.DeleteReqDetailsAdmin;
import com.example.craterradar.AdminSide.ModelClass.DeleteRequestDataAdmin;
import com.example.craterradar.R;

import java.util.ArrayList;

public class DeleteReqListAdapter extends RecyclerView.Adapter<DeleteReqListAdapter.ItemViewHolder> {
    Context context;
    ArrayList<DeleteRequestDataAdmin> deleteRequestDataAdminArrayList;
    DeleteRequestDataAdmin deleteRequestDataAdmin;
    private View.OnClickListener itemClickListner;
    Bundle detailsBundle = new Bundle();

    public DeleteReqListAdapter(ArrayList<DeleteRequestDataAdmin> deleteReqDataArrayList, Context context) {
        this.deleteRequestDataAdminArrayList = deleteReqDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delete_req_list_item_admin,parent,false);
        return new DeleteReqListAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        deleteRequestDataAdmin = deleteRequestDataAdminArrayList.get(position);
        holder.potholeID.setText(deleteRequestDataAdmin.getPothole_ID());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsBundle.putString("Pothole_Delete_Req_ID",deleteRequestDataAdminArrayList.get(position).getPothole_Delete_Req_ID());
                detailsBundle.putString("Pothole_ID",deleteRequestDataAdminArrayList.get(position).getPothole_ID());
                detailsBundle.putString("Pothole_Old_ImageURL",deleteRequestDataAdminArrayList.get(position).getPothole_Old_ImageURL());
                detailsBundle.putString("Pothole_New_ImageURL",deleteRequestDataAdminArrayList.get(position).getPothole_New_ImageURL());
                detailsBundle.putString("Pothole_Old_Latitude",deleteRequestDataAdminArrayList.get(position).getPothole_Old_Latitude());
                detailsBundle.putString("Pothole_New_Latitude",deleteRequestDataAdminArrayList.get(position).getPothole_New_Latitude());
                detailsBundle.putString("Pothole_Old_Longitude",deleteRequestDataAdminArrayList.get(position).getPothole_Old_Longotide());
                detailsBundle.putString("Pothole_New_Longitude",deleteRequestDataAdminArrayList.get(position).getPothole_New_Longitude());
                detailsBundle.putString("Pothole_Old_TimeStamp",deleteRequestDataAdminArrayList.get(position).getPothole_Old_TimeStamp());
                detailsBundle.putString("Pothole_New_TimeStamp",deleteRequestDataAdminArrayList.get(position).getPothole_New_TimeStamp());
                detailsBundle.putString("Pothole_Old_Description",deleteRequestDataAdminArrayList.get(position).getPothole_Old_Description());
                detailsBundle.putString("Pothole_New_Description",deleteRequestDataAdminArrayList.get(position).getPothole_New_Description());
                detailsBundle.putString("Pothole_AddedBy_UID",deleteRequestDataAdminArrayList.get(position).getPothole_AddedBy_UID());
                Intent i = new Intent(context, DeleteReqDetailsAdmin.class);
                i.putExtras(detailsBundle);
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return deleteRequestDataAdminArrayList.size();
    }

    public void setOnClickListner(View.OnClickListener clickListner)
    {
        itemClickListner = clickListner;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView potholeID;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            potholeID = itemView.findViewById(R.id.pothole_id_list_item);
            itemView.setOnClickListener(itemClickListner);
        }
    }
}

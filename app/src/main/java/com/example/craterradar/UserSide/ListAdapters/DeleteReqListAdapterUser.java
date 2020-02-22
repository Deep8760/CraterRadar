package com.example.craterradar.UserSide.ListAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.craterradar.AdminSide.ModelClass.DeleteRequestDataAdmin;
import com.example.craterradar.R;
import com.example.craterradar.UserSide.ModelClass.DeleteReqData;

import java.util.ArrayList;

public class DeleteReqListAdapterUser extends RecyclerView.Adapter<DeleteReqListAdapterUser.ItemViewHolder> {

    ArrayList<DeleteReqData> deleteReqDataArrayList;
    DeleteReqData deleteReqData;
    Context context;
    String DeleteReqID,PotholeID,status= "Pending";

    public DeleteReqListAdapterUser(ArrayList<DeleteReqData> deleteReqDataArrayList, Context context) {
        this.deleteReqDataArrayList = deleteReqDataArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delete_req_list_item_user,parent,false);
        return new DeleteReqListAdapterUser.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
       deleteReqData = deleteReqDataArrayList.get(position);
       //holder.deleteReqID.setText();
       //holder.potholeID.setText(deleteReqDataArrayList.get(position).getPothole_ID());
       //holder.Status.setText("Pending");
        Log.i("Delete ID",deleteReqData.getPothole_Delete_Req_ID());
        Log.i("Pothole ID",deleteReqData.getPothole_ID());
        holder.deleteReqID.setText("Delete Request ID: "+deleteReqData.getPothole_Delete_Req_ID());
        holder.potholeID.setText("Pothole ID: "+deleteReqData.getPothole_ID());
        holder.Status.setText(status);
       //DeleteReqID = deleteReqDataArrayList.get(position).getPothole_Delete_Req_ID();
       //PotholeID = deleteReqDataArrayList.get(position).getPothole_ID();

    }

    @Override
    public int getItemCount() {
        return deleteReqDataArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView deleteReqID,potholeID,Status;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteReqID = itemView.findViewById(R.id.deleteReqId_user);
            potholeID = itemView.findViewById(R.id.pothole_id);
            Status = itemView.findViewById(R.id.deleteReq_status);
           /* deleteReqID.setText(DeleteReqID);
            potholeID.setText(PotholeID);
            Status.setText(status);*/
        }
    }
}

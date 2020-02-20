package com.example.craterradar.AdminSide.ListAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.craterradar.AdminSide.ModelClass.UserList;
import com.example.craterradar.AdminSide.UserDetailsAdmin;
import com.example.craterradar.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ItemViewHolder> {
    Context context;
    ArrayList<UserList> userListArrayList;
    UserList userList;
    Uri profileImage;
    Bundle detailsBundle = new Bundle();
    private View.OnClickListener itemClickListner;

    public UserListAdapter(ArrayList<UserList> userListArrayList, Context context) {
        this.context = context;
        this.userListArrayList = userListArrayList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item_admin,parent,false);
        return new UserListAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        userList = userListArrayList.get(position);
        profileImage = Uri.parse(userList.getUserProfilePicPath());
        Picasso.get().load(profileImage).into(holder.profilePic);
        holder.UserName.setText(userList.getUserName());
        holder.UserID.setText(userList.getUid());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsBundle.putString("userProfileImagePath",userListArrayList.get(position).getUserProfilePicPath());
                detailsBundle.putString("userName",userListArrayList.get(position).getUserName());
                detailsBundle.putString("userID",userListArrayList.get(position).getUid());
                detailsBundle.putString("userEmail",userListArrayList.get(position).getEmail());
                detailsBundle.putString("userPhoneNo",userListArrayList.get(position).getPhone());
                detailsBundle.putString("userPassword",userListArrayList.get(position).getPass());

                Intent i = new Intent(context, UserDetailsAdmin.class);
                i.putExtras(detailsBundle);
                v.getContext().startActivity(i);
                //Toast.makeText(context,userListArrayList.get(position).getUserName(),Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return userListArrayList.size();
    }

    public void setOnClickListner(View.OnClickListener clickListner)
    {
        itemClickListner = clickListner;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profilePic;
        TextView UserName,UserID;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profilePic_userList_admin);
            UserName = itemView.findViewById(R.id.username_list_admin);
            UserID = itemView.findViewById(R.id.userID_list_admin);
            itemView.setOnClickListener(itemClickListner);
        }
    }
}

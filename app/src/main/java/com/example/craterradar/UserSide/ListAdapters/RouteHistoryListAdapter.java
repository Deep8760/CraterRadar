package com.example.craterradar.UserSide.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.craterradar.R;
import com.example.craterradar.UserSide.ModelClass.RouteHistory;

import java.util.ArrayList;

public class RouteHistoryListAdapter extends RecyclerView.Adapter<RouteHistoryListAdapter.ItemViewHolder>
{
    ArrayList<RouteHistory> routeHistoryArrayList= new ArrayList<>();
    Context context;

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
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        RouteHistory routeHistory = routeHistoryArrayList.get(position);
        holder.routeHistoryText.setText(routeHistory.getDestination());
    }

    @Override
    public int getItemCount() {
        return routeHistoryArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView routeHistoryText;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            routeHistoryText = itemView.findViewById(R.id.history_item_text);
        }
    }
}

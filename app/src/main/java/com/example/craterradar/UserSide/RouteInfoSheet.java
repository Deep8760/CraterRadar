package com.example.craterradar.UserSide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.craterradar.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class RouteInfoSheet extends BottomSheetDialogFragment {
    TextView duration,distance,no_of_pothole,pothole_duration,pothole_distance,danger_level;
    Button MoreDetailsBtn;
    String Duration,Distance,No_Of_Pothole,Pothole_Duration,Pothole_Distance,Danger_Level;
    private static View bottomSheetInternal;
    private static BottomSheetBehavior bottomSheetBehavior;
    Context context;

    public RouteInfoSheet(String duration,String distance)
    {
        Duration = duration;
        Distance = distance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.activity_route_info_sheet, null, false);
        context = getActivity().getApplicationContext();
        duration = view.findViewById(R.id.duration_route_info);
        distance = view.findViewById(R.id.distance_route_info);
        no_of_pothole = view.findViewById(R.id.no_of_pothole_route_info);
        pothole_duration = view.findViewById(R.id.next_pothole_duration_info);
        pothole_distance = view.findViewById(R.id.next_pothole_distance_info);
        danger_level = view.findViewById(R.id.danger_level_pothole_info);
        MoreDetailsBtn = view.findViewById(R.id.more_details_pothole_info);

        duration.setText(Duration);
        distance.setText(Distance);

        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                CoordinatorLayout coordinatorLayout = d.findViewById(R.id.layout_bottom);
                bottomSheetInternal = d.findViewById(R.id.bottom_view);
                bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetInternal);
                bottomSheetBehavior.setHideable(false);
                //BottomSheetBehavior.from((View)ConstraintLayout.getParent()).setPeekHeight(bottomSheetInternal.getHeight());
                //BottomSheetBehavior.from((View)getView().getParent()).setPeekHeight(50);
                bottomSheetBehavior.setPeekHeight(bottomSheetInternal.getHeight());
                coordinatorLayout.getParent().requestLayout();

            }
        });

        MoreDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog moreDetails_Pothole = new AlertDialog.Builder(context).create();
                moreDetails_Pothole.setCancelable(true);
                moreDetails_Pothole.setTitle("More Details Of Pothole");
                LayoutInflater factory = LayoutInflater.from(context);
                final View alert_view = factory.inflate(R.layout.more_details_alert_dialog,null);
                moreDetails_Pothole.setView(alert_view);
                moreDetails_Pothole.show();
            }
        });
        return view;
    }
}

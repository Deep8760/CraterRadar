package com.example.craterradar.AdminSide;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.craterradar.R;
import com.example.craterradar.fragment_pager_adapter;
import com.google.android.material.tabs.TabLayout;


public class home_admin_side extends Fragment {

    ViewPager viewPager;
    fragment_pager_adapter FragmentPager_adapter;
    TabLayout tabLayout;
    public home_admin_side() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_admin_side, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tabLayout);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Admin Pannel");
        setFragmentPagerAdapter();
    }

    private void setFragmentPagerAdapter() {
        FragmentPager_adapter = new fragment_pager_adapter(getActivity().getSupportFragmentManager());
        FragmentPager_adapter.AddFragment(new list_user_frag(),"");
        FragmentPager_adapter.AddFragment(new list_pothole_frag(),"");
        FragmentPager_adapter.AddFragment(new list_deleteReq_frag(),"");
        viewPager.setAdapter(FragmentPager_adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("User List").setIcon(R.drawable.list_of_user_icon);
        tabLayout.getTabAt(1).setText("Pothole List").setIcon(R.drawable.pothole_icon);
        tabLayout.getTabAt(2).setText("Delete Reqest List").setIcon(R.drawable.delete_req_list_icon);
    }
}

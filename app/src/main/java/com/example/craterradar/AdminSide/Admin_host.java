package com.example.craterradar.AdminSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.craterradar.Main2Activity;
import com.example.craterradar.R;
import com.example.craterradar.UserSide.UserSide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Admin_host extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public Toolbar toolbar;
    public DrawerLayout HomeDrawerLayout;
    public NavigationView HomeNavigationView;
    public NavController navController;
    public TextView UserName,EmailID;
    public ImageView profileImage;
    public String userName,emailID,profilePhotoUrl;
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseUser firebaseUser;
    public DatabaseReference databaseReference;
    Uri imageuri;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_host);
        firebaseAuth = FirebaseAuth.getInstance();
        setUpNavigationDrawer();
    }

    private void setUpNavigationDrawer() {
        setDataInNavDrawer();
        toolbar = findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Pannel");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences =  getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        HomeDrawerLayout = findViewById(R.id.nav_drawer_admin);
        HomeNavigationView = findViewById(R.id.admin_home_navigationview);
        UserName = HomeNavigationView.getHeaderView(0).findViewById(R.id.admin_name_nav_drawer);
        EmailID = HomeNavigationView.getHeaderView(0).findViewById(R.id.admin_email_nav_drawer);
        profileImage = HomeNavigationView.getHeaderView(0).findViewById(R.id.admin_profile_photo);
        navController = Navigation.findNavController(this,R.id.nav_host_adminside_fragment);
        NavigationUI.setupActionBarWithNavController(this,navController,HomeDrawerLayout);
        NavigationUI.setupWithNavController(HomeNavigationView,navController);
        HomeNavigationView.setNavigationItemSelectedListener(this);
    }

    private void setDataInNavDrawer() {
        firebaseUser = firebaseAuth.getCurrentUser();
        final String uid = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Admin");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    userName = dataSnapshot.child(uid).child("AdminName").getValue().toString();
                    emailID = dataSnapshot.child(uid).child("AdminEmail").getValue().toString();
                    UserName.setText(userName);
                    EmailID.setText(emailID);
                    profilePhotoUrl = dataSnapshot.child(uid).child("ProfileImagePath").getValue().toString();

                    Picasso.get().load(profilePhotoUrl).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_adminside_fragment),HomeDrawerLayout) || super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        if (HomeDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            HomeDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        HomeDrawerLayout.closeDrawers();
        int SelectedItem = item.getItemId();
        switch (SelectedItem)
        {
            case R.id.home_nav_drawer_admin:
                HomeDrawerLayout.closeDrawers();
                navController.navigate(R.id.action_home_admin_side_self);
                //Toast.makeText(this,"home", Toast.LENGTH_LONG).show();
                break;
            case R.id.edit_profile_nav_drawer_admin:
                navController.navigate(R.id.action_home_admin_side_to_edit_profile_admin_frag);
                //Toast.makeText(this,"edit Profile",Toast.LENGTH_LONG).show();
                break;
            case  R.id.about_us_nav_draw_admin:
                navController.navigate(R.id.action_home_admin_side_to_about_us_admin_frag);
                break;
            case R.id.log_out_nav_draw_admin:
                Toast.makeText(this,"logout",Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Log out");
                builder.setMessage("Are you sure you want to logout your acccount ?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code
                                FirebaseAuth.getInstance().signOut();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("UserType","");
                                editor.commit();

                                Intent intent_signout = new Intent(Admin_host.this, Main2Activity.class);
                                startActivity(intent_signout);
                                finish();
                                dialog.dismiss();
                            }

                        })
                        .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        });
                builder.show();


                break;
        }
        return false;
    }
}

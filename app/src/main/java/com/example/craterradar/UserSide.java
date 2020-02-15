package com.example.craterradar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserSide extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_side);
        firebaseAuth = FirebaseAuth.getInstance();
        setUpNavigationDrawer();


    }

    public void setUpNavigationDrawer() {
        setDataInNavDrawer();
        toolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CraterRadar");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        HomeDrawerLayout = findViewById(R.id.nav_drawer_user);
        HomeNavigationView = findViewById(R.id.home_navigationview);
        UserName = HomeNavigationView.getHeaderView(0).findViewById(R.id.user_name_nav_drawer);
        EmailID = HomeNavigationView.getHeaderView(0).findViewById(R.id.user_email_nav_drawer);
        profileImage = HomeNavigationView.getHeaderView(0).findViewById(R.id.C_user_profile_photo);
        navController = Navigation.findNavController(this,R.id.nav_host_userSide_fragment);
        NavigationUI.setupActionBarWithNavController(this,navController,HomeDrawerLayout);
        NavigationUI.setupWithNavController(HomeNavigationView,navController);
        HomeNavigationView.setNavigationItemSelectedListener(this);
    }

    public void setDataInNavDrawer() {
        firebaseUser = firebaseAuth.getCurrentUser();
        final String uid = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    userName = dataSnapshot.child(uid).child("Fullname").getValue().toString();
                    emailID = dataSnapshot.child(uid).child("Email").getValue().toString();
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
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_userSide_fragment),HomeDrawerLayout) || super.onSupportNavigateUp();
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
            case R.id.home_nav_drawer:
                Toast.makeText(this,"home",Toast.LENGTH_LONG).show();
                break;
            case R.id.edit_profile_nav_drawer:
                //Toast.makeText(this,"edit Profile",Toast.LENGTH_LONG).show();
                navController.navigate(R.id.action_homeUserSide_to_edit_profile_frag);
                break;
            case R.id.list_of_added_pothole_nav_draw:
                Toast.makeText(this,"List of Added Pothole",Toast.LENGTH_LONG).show();
                break;
            case R.id.route_history_nav_draw:
                Toast.makeText(this,"Route History",Toast.LENGTH_LONG).show();
                break;
            case R.id.delete_account_nav_draw:
                Toast.makeText(this,"Delete Account",Toast.LENGTH_LONG).show();
                break;
            case R.id.log_out_nav_draw:
                //Toast.makeText(this,"logout",Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent_signout = new Intent(this, Main2Activity.class);
                startActivity(intent_signout);
                finish();
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.add_pothole:
                Intent i = new Intent(UserSide.this,AddPotholeActivity.class);
                startActivity(i);
                finish();
                break;
        }
        //Toast.makeText(this,msg+" is checked!",Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
}

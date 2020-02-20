package com.example.craterradar.AdminSide;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.craterradar.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailsAdmin extends AppCompatActivity {

    CircleImageView profilePic;
    TextView id,name,email,phone,pass;
    String Uid,Name,Email,Phone,Pass,Pic;
    Uri Imageuri;
    ActionBar actionBar;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_admin);
        actionBar = getSupportActionBar();
        progressBar = findViewById(R.id.userDetails_progress_admin);
        actionBar.setDisplayHomeAsUpEnabled(true);
        profilePic = findViewById(R.id.profilePic_userDetails_admin);
        id = findViewById(R.id.userId_detail_admin);
        name = findViewById(R.id.userName_detail_admin);
        email = findViewById(R.id.userEmail_detail_admin);
        phone = findViewById(R.id.userPhone_details_admin);
        pass = findViewById(R.id.userPassword_details_admin);

        getAndShowDetails();

    }
    public void getAndShowDetails()
    {
        progressBar.setVisibility(View.VISIBLE);
        Bundle data = getIntent().getExtras();
        Uid = data.getString("userID");
        Name = data.getString("userName");
        Email = data.getString("userEmail");
        Phone = data.getString("userPhoneNo");
        Pass = data.getString("userPassword");
        Pic = data.getString("userProfileImagePath");
        Imageuri = Uri.parse(Pic);
        actionBar.setTitle(Name);
        Picasso.get().load(Imageuri).into(profilePic);
        id.setText(Uid);
        name.setText(Name);
        email.setText(Email);
        phone.setText(Phone);
        pass.setText(Pass);
        progressBar.setVisibility(View.GONE);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}

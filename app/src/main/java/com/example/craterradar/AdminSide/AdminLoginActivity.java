package com.example.craterradar.AdminSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.craterradar.Main2Activity;
import com.example.craterradar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout Email_ID, Password;
    Button Login;
    String userID, pass;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;

   @Override
    public void onStart() {
        super.onStart();
        updateUI(firebaseUser);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        Email_ID = findViewById(R.id.admin_email_layout);
        Password = findViewById(R.id.admin_password_layout);
        Login = findViewById(R.id.admin_loginBtn);
        progressBar = findViewById(R.id.admin_login_progress);
        progressBar.setVisibility(View.GONE);
        sharedPreferences = getSharedPreferences("LoginData", Context.MODE_PRIVATE);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Admin");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == Login) {
            userID = Email_ID.getEditText().getText().toString();
            pass = Password.getEditText().getText().toString();
            if (!userID.isEmpty() && !pass.isEmpty()) {
                Login_for_Admin(userID, pass);
            } else {

            }
        }
    }

    private void Login_for_Admin(final String UserEmail,final String Password) {
        progressBar.setVisibility(View.VISIBLE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (UserEmail.contentEquals(ds.child("AdminEmail").getValue().toString())
                                && Password.contentEquals(ds.child("AdminPassword").getValue().toString())) {
                           firebaseAuth.signInWithEmailAndPassword(UserEmail, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        updateUI(firebaseUser);
                                        editor.putString("UserType","Admin");
                                        editor.commit();
                                        Intent i = new Intent(AdminLoginActivity.this, Admin_host.class);
                                        startActivity(i);
                                        finish();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "There is some Problem. Please Try Again!", Toast.LENGTH_LONG).show();
                                        //updateUI(null);
                                    }

                                }
                            });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Its not Admin Account!.Try with UserLogin!", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(AdminLoginActivity.this, Main2Activity.class);
                            startActivity(i);
                            //finish();
                        }
                    }
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error:"+ databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void updateUI(FirebaseUser user)
    {
        user = firebaseAuth.getCurrentUser();
        progressBar.setVisibility(View.GONE);
        if(user != null)
        {
            Intent i = new Intent(AdminLoginActivity.this, Admin_host.class);
            startActivity(i);
            finish();
        }
    }
}

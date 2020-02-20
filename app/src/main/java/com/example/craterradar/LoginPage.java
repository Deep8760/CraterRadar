package com.example.craterradar;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.craterradar.AdminSide.AdminLoginActivity;
import com.example.craterradar.UserSide.UserSide;
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


public class LoginPage extends Fragment implements View.OnClickListener {
    Context context;
    TextInputLayout Email_ID,Password ;
    Button Login,Forget,Signup,Adminlogin;
    String userID,pass;
    NavController navController;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    String admin_email,admin_pass;

    public LoginPage() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        //updateUI(null);
        updateUI(firebaseUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();
        sharedPreferences = context.getSharedPreferences("LoginData",Context.MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Admin");


        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        //Email ID
        Email_ID = view.findViewById(R.id.email_layout);
        //Password
        Password = view.findViewById(R.id.password_layout);
        //Buttons
        Login = view.findViewById(R.id.loginBtn);
        Forget = view.findViewById(R.id.forget_login);
        Signup = view.findViewById(R.id.signup_login);

        Adminlogin = view.findViewById(R.id.adminLogin_login);

        progressBar =view.findViewById(R.id.login_progress);
        progressBar.setVisibility(View.GONE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Login");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);


        Login.setOnClickListener(this);
        Forget.setOnClickListener(this);
        Signup.setOnClickListener(this);

        Adminlogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == Login)
        {
            userID = Email_ID.getEditText().getText().toString();
            pass = Password.getEditText().getText().toString();
            if(!userID.isEmpty() && !pass.isEmpty())
            {

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                admin_email = ds.child("AdminEmail").getValue().toString();
                                admin_pass = ds.child("AdminPassword").getValue().toString();

                                if (!userID.contentEquals(admin_email) && !pass.contentEquals(admin_pass)) {
                                    FirebaseLoginMethod(userID, pass);
                                } else {
                                    Toast.makeText(context, "This is admin login.Please go to Admin Login Page.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        else {
                            Toast.makeText(context, "Something is wrong! Try Again.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            else
            {
                Toast.makeText(context,"Something is missing!",Toast.LENGTH_LONG).show();
            }

        }
        else if(v == Adminlogin)
        {
            Intent i = new Intent(getContext(), AdminLoginActivity.class);
            startActivity(i);
            getActivity().finish();
        }
        else if(v == Forget)
        {
            navController.navigate(R.id.action_loginPage_to_forgetPassFrag);
        }
        else if(v == Signup)
        {
            navController.navigate(R.id.action_loginPage_to_signupFrag);
        }
    }
    public void FirebaseLoginMethod(String UserEmail,String Password)
    {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);//Block UI
        firebaseAuth.signInWithEmailAndPassword(UserEmail,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);//UnBlock UI
                if(task.isSuccessful())
                {
                    editor.putString("UserType","User");
                    editor.commit();
                    updateUI(firebaseUser);
                    Intent i = new Intent(context, UserSide.class);
                    startActivity(i);
                    getActivity().finish();
                    Toast.makeText(getActivity().getApplicationContext(),"Successfully Logged in!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),"There is some Problem. Please Try Again!", Toast.LENGTH_LONG).show();
                    updateUI(null);
                }

            }
        });

        //getActivity().finish();
    }



  public void updateUI(FirebaseUser user)
    {
        user = firebaseAuth.getCurrentUser();
        progressBar.setVisibility(View.GONE);
        if(user != null)
        {
            Intent i = new Intent(context,UserSide.class);
            startActivity(i);
            getActivity().finish();
        }
    }
}

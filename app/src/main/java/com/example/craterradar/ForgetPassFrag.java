package com.example.craterradar;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassFrag extends Fragment implements View.OnClickListener {
    Context context;
    TextInputLayout emailID;
    String email;
    Button SendLinkBTN;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;


    public ForgetPassFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forget_pass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Forget Password");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        emailID = view.findViewById(R.id.email_forget);
        SendLinkBTN = view.findViewById(R.id.send_forgetpass_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.forget_progress);
        progressBar.setVisibility(View.GONE);

        SendLinkBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == SendLinkBTN)
        {
            email = emailID.getEditText().getText().toString();
            if(!email.isEmpty())
            {
                sendLink();
            }
            else
                {
                    Toast.makeText(context,"Please Enter Email id!",Toast.LENGTH_LONG).show();
                }
        }
    }

    public void sendLink()
    {
        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);//Block UI
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);//UnBlock UI
                Toast.makeText(context,"Password reset link is send successfully!", Toast.LENGTH_LONG).show();
            }
        });

    }
}

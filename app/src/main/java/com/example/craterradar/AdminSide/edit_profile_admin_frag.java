package com.example.craterradar.AdminSide;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.craterradar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class edit_profile_admin_frag extends Fragment implements View.OnClickListener{
    Context context;
    NavController navController;
    ProgressBar progressBar;
    CircleImageView profilePic;
    EditText name_editProfile,email_editProfile,password_editProfile,confirm_editProfile;
    Button SaveBtn;
    String picPath,name,email,pass,confirm;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String uid;

    public Uri image_uri;
    private String Imageextention = "";

    public edit_profile_admin_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile_admin_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        navController = Navigation.findNavController(getActivity(),R.id.nav_host_adminside_fragment);

        progressBar = view.findViewById(R.id.editProfile_progress_admin);
        progressBar.setVisibility(View.GONE);

        profilePic = view.findViewById(R.id.admin_profile_photo_edit);
        profilePic.setImageResource(R.drawable.user_2);
        name_editProfile = view.findViewById(R.id.name_editProfile_admin);
        email_editProfile = view.findViewById(R.id.email_editProfile_admin);
        //phone_editProfile = view.findViewById(R.id.phone_editProfile_admin);
        password_editProfile = view.findViewById(R.id.password_editProfile_admin);
        confirm_editProfile = view.findViewById(R.id.confirm_editProfile_admin);
        SaveBtn = view.findViewById(R.id.save_btn_editProfile_admin);

        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Admin");


        getDataFromDB();

        profilePic.setOnClickListener(this);
        SaveBtn.setOnClickListener(this);
    }

    private void getDataFromDB() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    progressBar.setVisibility(View.GONE);

                    //Log.e("Datasnapshot", );
                    name_editProfile.setText(dataSnapshot.child(uid).child("AdminName").getValue().toString());
                    email_editProfile.setText(dataSnapshot.child(uid).child("AdminEmail").getValue().toString());
                    //phone_editProfile.setText(dataSnapshot.child(uid).child("Phoneno").getValue().toString());
                    String imageURL = dataSnapshot.child(uid).child("ProfileImagePath").getValue().toString();
                    Picasso.get().load(imageURL).into(profilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == profilePic)
        {
            SelectNewProfilePic();
        }
        else if(v == SaveBtn)
        {
            SaveDataInDB();
        }
    }

    private void SaveDataInDB() {
        final String EMAILID,PASSWORD,NAME,CONFORM;
        EMAILID = email_editProfile.getText().toString();
        PASSWORD=password_editProfile.getText().toString();
        NAME=name_editProfile.getText().toString();
        CONFORM = confirm_editProfile.getText().toString();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        progressBar.setVisibility(View.VISIBLE);
        if (!EMAILID.isEmpty()
                && !PASSWORD.isEmpty()
                && !NAME.isEmpty()
                && PASSWORD.contentEquals(CONFORM))
        {
            firebaseUser.updateEmail(EMAILID).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful())
                    {
                        firebaseUser.updatePassword(PASSWORD).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    //Toast.makeText(context,"Email & Password Changed Successfully !",Toast.LENGTH_LONG).show();
                                    databaseReference.child(uid).child("AdminEmail").setValue(EMAILID);
                                    databaseReference.child(uid).child("AdminPassword").setValue(PASSWORD);
                                    databaseReference.child(uid).child("AdminName").setValue(NAME);
                                    Toast.makeText(context,"Profile is Update Successfully !",Toast.LENGTH_LONG).show();
                                    reAuthUser(EMAILID,PASSWORD,firebaseUser);
                                    navController.navigate(R.id.action_edit_profile_admin_frag_to_home_admin_side);

                                }
                                else
                                {
                                    Toast.makeText(context,"Can't Change Password!",Toast.LENGTH_LONG).show();
                                }


                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(context,"Can't Change Email!",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context,"Please Fill all fields!",Toast.LENGTH_LONG).show();
        }
    }


    public void reAuthUser(String email, String password, FirebaseUser firebaseUser)
    {
        AuthCredential credential = EmailAuthProvider.getCredential(email,password);
        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(context,"User is reAuthenticated!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(context, AdminLoginActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }
            }
        });

    }



    /** Image Select or Capture using Camera or Gallery and Show on Image View START */
    private void getFileExtention(Uri uri)
    {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        Imageextention = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void SelectNewProfilePic()
    {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                                getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission, 1000);
                        } else {
                            openCamera();
                        }
                    } else {
                        openCamera();
                    }
                } else if (options[item].equals("Choose from Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");

                    startActivityForResult(Intent.createChooser(intent,"Select Image"), 2);

                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(takePictureIntent, 1);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    //permisiion from pop up was denied.
                    Toast.makeText(getActivity().getApplicationContext(), "Permission Denied...", Toast.LENGTH_LONG).show();
                }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    profilePic.setImageURI(image_uri);
                    getFileExtention(image_uri);
                    break;
                case 2:
                    //data.getData returns the content URI for the selected Image
                    image_uri = data.getData();
                    profilePic.setImageURI(image_uri);
                    getFileExtention(image_uri);
                    Log.i("ImageURI",image_uri.toString());
                    break;
            }

        }
    }
    /** Image Select or Capture using Camera or Gallery and Show on Image View END */

}

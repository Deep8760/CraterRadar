package com.example.craterradar.UserSide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.craterradar.R;
import com.example.craterradar.UserSide.ModelClass.Potholes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.UUID;

public class AddPotholeActivity extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbar;
    TextView timeStamp_text,location_text;
    EditText description;
    ImageView potholeImage;
    Spinner danger_level_spinner;
    Button add_Pothole_btn;

    final static String[] Danger_Level_Pothole = {"Select Danger Level of Pothole","0 - 25 %","25 - 50 %","50 - 75 %","75 - 100 %"};
    Uri pothole_image_uri;
    private String Imageextention = "";
    ProgressBar progressBar;

    //For Location
    String TimeAndDate="",Latitude,LatRef,Longitude,LongRef,Location="",DangerLevel_Selected,Description;
    Double lat,lon;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String uid,pothole_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pothole);
        toolbar = findViewById(R.id.add_pothole_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.add_pothole_progress);
        progressBar.setVisibility(View.GONE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Potholes");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        potholeImage = findViewById(R.id.add_pothole_image_f1);
        timeStamp_text = findViewById(R.id.add_pothole_time_stamp_f1);
        location_text = findViewById(R.id.add_pothole_location_f1);
        description = findViewById(R.id.add_pothole_description_f1);
        danger_level_spinner = findViewById(R.id.danger_level_pothole_f1);
        add_Pothole_btn = findViewById(R.id.add_pothole_btn);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,Danger_Level_Pothole);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        danger_level_spinner.setAdapter(adapter);
        danger_level_spinner.setSelection(0);
        danger_level_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                DangerLevel_Selected = danger_level_spinner.getSelectedItem().toString();
                //Log.i("Selected Item",Selected_Danger_Level);
                // Toast.makeText(context, danger_level_spinner.getSelectedItem() + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        potholeImage.setOnClickListener(this);
        timeStamp_text.setOnClickListener(this);
        location_text.setOnClickListener(this);
        add_Pothole_btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v == potholeImage)
        {

            getPotholeImage();
            //Toast.makeText(context, "Pothole Image Selected", Toast.LENGTH_SHORT).show();
        }
        else if(v == add_Pothole_btn)
        {
            Description = description.getText().toString();

            if (DangerLevel_Selected.contentEquals("Select Danger Level of Pothole"))
            {
                Toast.makeText(this,"Please select Danger Level from Drop Down",Toast.LENGTH_LONG).show();
            }
            else if(TimeAndDate.contentEquals("") || Location.contentEquals(""))
            {
                Toast.makeText(this,"This image has no location and timestamp.",Toast.LENGTH_LONG).show();
            }
            else if(Description.isEmpty())
            {
                Toast.makeText(this,"Please enter description of pothole.",Toast.LENGTH_LONG).show();
            }
            else {
                AddPothole();
                Toast.makeText(this,"Added",Toast.LENGTH_LONG).show();
            }

//
//            if(!DangerLevel_Selected.contentEquals("Select Danger Level of Pothole")
//                    && TimeAndDate!=""
//                    && Location != ""
//                    && Description != "")
//            {
//
//            }
//            else
//            {
//                Toast.makeText(this,"Fill all the fields!",Toast.LENGTH_LONG).show();
//            }
        }

    }

    private void AddPothole()
    {

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);//Block UI


        uid = firebaseAuth.getUid();
        pothole_id = UUID.randomUUID().toString();

        final StorageReference str_ref = storageReference.child("Potholes/"+uid+"/"+pothole_id).child("Pothole_Image."+Imageextention);
        str_ref.putFile(pothole_image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                progressBar.setVisibility(View.GONE);
                storageReference.child("Potholes/"+uid+"/"+pothole_id).child("Pothole_Image."+Imageextention).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        Log.i("Image URL",url);

                        Potholes potholes = new Potholes(uid,pothole_id,url,lat.toString(),lon.toString(),DangerLevel_Selected,TimeAndDate,Description);

                        FirebaseDatabase.getInstance().getReference("Potholes").child(pothole_id).setValue(potholes).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AddPotholeActivity.this, "Pothole Added Successfully!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AddPotholeActivity.this,UserSide.class);
                                startActivity(i);
                                finish();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddPotholeActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Method for Getting Image Path from URI
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getLocation()
    {
        ShowExif(pothole_image_uri);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void ShowExif(Uri imageURI)
    {
        if(imageURI != null) {
            String photopath = getPathFromUri(this,imageURI);

            try {
                ExifInterface exifInterface = new ExifInterface(photopath);

                TimeAndDate = exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP) +""+ exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
                Latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                LatRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                Longitude =  exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                LongRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);


                lat = ConvertToLat(Latitude, LatRef);
                lon = ConvertToLong(Longitude, LongRef);



                //Toast.makeText(context, "Lat:"+strLat +"\n"+"Long:"+Longitude, Toast.LENGTH_LONG).show();
                //name.setText("ImageName");
                timeStamp_text.setText(TimeAndDate);


                //latitude.setText(lat.toString());
                //longitude.setText(lon.toString());
                location_text.setText(lat.toString()+" "+lon.toString());
                Location = location_text.getText().toString();

                Log.i("Location: ",location_text.toString());

            } catch (Exception e) {
                //Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                Toast.makeText(this,"Image has no Location.Please choose Different Image.",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "photoUri not found!",
                    Toast.LENGTH_LONG).show();
        }
    }

    /** Convertin DMS to Decimal Lat **/
    public double ConvertToLat(String latitude,String latRef) {
        double d_Lat = 0;
        if(!latitude.isEmpty() && !latRef.isEmpty())
        {
            //String test = "12/1,45/1,37469/1000";
            //String test2 = "S";
            /** Latitude CONVERTION **/
            String str_deg = "",str_min = "",str_sec ="" ;
            //Log.i("Original Lat:",latitude);
            /**Splitting Original Lat in Degree , Minutes and Seconds **/
            String[] DMS = latitude.split(",");
            for(int i = 0; i<DMS.length; i ++)
            {

                str_deg = String.valueOf(Array.get(DMS,0));
                str_min = String.valueOf(Array.get(DMS,1));
                str_sec = String.valueOf(Array.get(DMS,2));
                break;
            }

            /** Remove /1 or /100 or /1000 0r /10000 from DMS **/
            String[] Deg = str_deg.split("/");
            String[] Min = str_min.split("/");
            String[] Sec = str_sec.split("/",2);

            /** Save and Parse Value in Float form **/
            float f1,f2,f_deg,f_min,f_second;
            f_deg = Integer.parseInt(Deg[0]);
            f_min = Integer.parseInt(Min[0]);
            f_min = f_min/60;
            f1 = Float.parseFloat(Sec[0]);
            f2 = Float.parseFloat(Sec[1]);
            f_second = Float.valueOf(f1/f2);

            /** Convertion Formula DMS to Decimal Degree **/
            d_Lat = f_deg + f_min + f_second/3600;

            /** Final Answer for Lat **/


            if(latRef.contentEquals("S"))
            {
                d_Lat = d_Lat * -1;
                Log.i("Lat with South :", String.valueOf(d_Lat));
            }
            else if(latRef.contentEquals("N"))
            {
                Log.i("Lat With North", String.valueOf(d_Lat));
            }
            return d_Lat;
        }
        else {
            return d_Lat;
        }
    }

    /** Convertin DMS to Decimal Long **/
    public double ConvertToLong(String longitude,String longRef)
    {
        double d_Long = 0;
        if(!longitude.isEmpty() && !longRef.isEmpty())
        {
            //String test = "12/1,45/1,37469/1000";
            //String test2 = "S";
            /** Latitude CONVERTION **/
            String str_deg = "",str_min = "",str_sec ="" ;
            //Log.i("Original Lat:",latitude);
            /**Splitting Original Long in Degree , Minutes and Seconds **/
            String[] DMS = longitude.split(",");
            for(int i = 0; i<DMS.length; i ++)
            {

                str_deg = String.valueOf(Array.get(DMS,0));
                str_min = String.valueOf(Array.get(DMS,1));
                str_sec = String.valueOf(Array.get(DMS,2));
                break;
            }

            /** Remove /1 or /100 or /1000 0r /10000 from DMS **/
            String[] Deg = str_deg.split("/");
            String[] Min = str_min.split("/");
            String[] Sec = str_sec.split("/",2);

            /** Save and Parse Value in Float form **/
            float f1,f2,f_deg,f_min,f_second;
            f_deg = Integer.parseInt(Deg[0]);
            f_min = Integer.parseInt(Min[0]);
            f_min = f_min/60;
            f1 = Float.parseFloat(Sec[0]);
            f2 = Float.parseFloat(Sec[1]);
            f_second = Float.valueOf(f1/f2);

            /** Convertion Formula DMS to Decimal Degree **/
            d_Long = f_deg + f_min + f_second/3600;

            /** Final Answer for Long **/
            if(longRef.contentEquals("W"))
            {
                d_Long = d_Long * -1;
                Log.i("Lat with South :", String.valueOf(d_Long));
            }
            else if(longRef.contentEquals("E"))
            {
                Log.i("Lat With North", String.valueOf(d_Long));
            }
            return d_Long;
        }
        else {
            return d_Long;
        }
    }





    /** METHOD TO GET IMAGE FILE EXTENTION LIKE .JPG , .PNG ,etc */
    private void getFileExtention(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        Imageextention = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    /** METHOD TO GET POTHOLE IMAGE FROM USER USING PHONE CAMERA OR GALLERY **/
    private void getPotholeImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission, 1000);
                        } else {
                            openCamera();
                        }
                    } else {
                        openCamera();
                    }
                }
                else if (options[item].equals("Choose from Gallery")) {
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
        pothole_image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pothole_image_uri);
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
                    Toast.makeText(getApplicationContext(), "Permission Denied...", Toast.LENGTH_LONG).show();
                }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        progressBar.setVisibility(View.VISIBLE);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    progressBar.setVisibility(View.GONE);
                    new EncodeImage(pothole_image_uri).execute();
                    potholeImage.setImageURI(pothole_image_uri);
                    getFileExtention(pothole_image_uri);
                    getLocation();
                    break;
                case 2:
                    progressBar.setVisibility(View.GONE);
                    //data.getData returns the content URI for the selected Image
                    pothole_image_uri = data.getData();
                    new EncodeImage(pothole_image_uri).execute();
                    potholeImage.setImageURI(pothole_image_uri);
                    getFileExtention(pothole_image_uri);
                    Log.i("ImageURI",pothole_image_uri.toString());
                    getLocation();
                    break;
                    default:
                        progressBar.setVisibility(View.GONE);
                        break;
            }

        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public class EncodeImage extends AsyncTask<Void, Void, String> {
        Uri uri;
        String encodedImage = "";
        public EncodeImage(Uri uri){
            this.uri = uri;
        }
        @Override
        protected String doInBackground(Void... params){

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), pothole_image_uri);
                InputStream input = getContentResolver().openInputStream(pothole_image_uri);
                ExifInterface ei;
                if (Build.VERSION.SDK_INT > 23) {
                    ei = new ExifInterface(input);
                }
                else {
                    ei = new ExifInterface(pothole_image_uri.getPath());
                }
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                Bitmap rotatedBitmap = null;
                switch(orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(bitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(bitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(bitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bitmap;
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
                byte[] byteArray = outputStream.toByteArray();
                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return encodedImage;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        //onBackPressed();
        Intent i = new Intent(this,UserSide.class);
        startActivity(i);
        finish();
    }
}

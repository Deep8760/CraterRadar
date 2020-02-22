package com.example.craterradar.UserSide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.craterradar.R;
import com.example.craterradar.UserSide.ModelClass.DeleteReqData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class PotholeDeleteReq_user extends AppCompatActivity implements View.OnClickListener {
    ProgressBar progressBar;
    CircleImageView DeleteReqImage;
    TextView Latitude,Longitude,TimeStamp;
    EditText Description;
    Button SendReqBtn;
    ActionBar actionBar;
    Bundle bundle;
    String potholeID,potholeImageURL,addedByUID,latitude,longitude,dangerlevel,timestamp,description;
    public Uri image_uri;
    private String Imageextention = "";

    FirebaseAuth firebaseAuth;
    FirebaseStorage storage;

    String uid ,DeleteReqID;

    //For Location
    String TimeAndDate,lat,LatRef,lng,LongRef,Desc;
    Double Lat,Lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pothole_delete_req_user);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Pothole Delete Request");

        bundle = getIntent().getExtras();

        progressBar = findViewById(R.id.deleteReq_progress);
        DeleteReqImage = findViewById(R.id.deleteReq_potholePic);
        Latitude = findViewById(R.id.lat_deleteReq_user);
        Longitude = findViewById(R.id.long_deleteReq_user);
        TimeStamp = findViewById(R.id.timeStamp_deleteReq_user);
        Description = findViewById(R.id.description_deleteReq_user);
        SendReqBtn = findViewById(R.id.send_req_btn);

        potholeID = bundle.getString("PotholeID");
        potholeImageURL = bundle.getString("PotholeImageURL");
        addedByUID = bundle.getString("PotholeAddedByUID");
        latitude = bundle.getString("PotholeLat");
        longitude = bundle.getString("PotholeLong");
        dangerlevel = bundle.getString("PotholeDangerLevel");
        timestamp = bundle.getString("PotholeTimeStamp");
        description = bundle.getString("PotholeDescription");

        DeleteReqImage.setOnClickListener(this);
        SendReqBtn.setOnClickListener(this);
        /*imageUri = Uri.parse(potholeImageURL);
        Picasso.get().load(imageUri).into(DeleteReqImage);
        Latitude.setText(latitude);
        Longitude.setText(longitude);
        TimeStamp.setText(timestamp);*/

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

    @Override
    public void onClick(View v) {
        if(v == DeleteReqImage)
        {
            SelectImage();
        }
        else if(v == SendReqBtn)
        {
            Desc = Description.getText().toString();
            SubmitDeleteRequest();
        }
    }



    /** METHOD FOR IMAGE FROM GALLERY OR CAMERA START **/
    private void getFileExtention(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        Imageextention = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void SelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PotholeDeleteReq_user.this);
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
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
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
                    Toast.makeText(getApplicationContext(), "Permission Denied...", Toast.LENGTH_LONG).show();
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
                    new EncodeImage(image_uri);
                    DeleteReqImage.setImageURI(image_uri);
                    getFileExtention(image_uri);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getDataFromImage(image_uri);
                    }
                    else {
                        Toast.makeText(PotholeDeleteReq_user.this,"It require KitKat or higher Version!",Toast.LENGTH_LONG).show();
                    }
                    break;
                case 2:
                    //data.getData returns the content URI for the selected Image
                    image_uri = data.getData();
                    new EncodeImage(image_uri);
                    DeleteReqImage.setImageURI(image_uri);
                    getFileExtention(image_uri);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getDataFromImage(image_uri);
                    }
                    else {
                        Toast.makeText(PotholeDeleteReq_user.this,"It require KitKat or higher Version!",Toast.LENGTH_LONG).show();
                    }
                    //Log.i("ImageURI",image_uri.toString());
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri);
                InputStream input = getContentResolver().openInputStream(image_uri);
                ExifInterface ei;
                if (Build.VERSION.SDK_INT > 23) {
                    ei = new ExifInterface(input);
                }
                else {
                    ei = new ExifInterface(image_uri.getPath());
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
    /** METHOD FOR IMAGE FROM GALLERY OR CAMERA END **/

    /** METHOD TO GET DETAILS FROM IMAGE START **/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getDataFromImage(Uri pothole_image_uri)
    {
        ShowExif(pothole_image_uri);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void ShowExif(Uri imageURI)
    {
        progressBar.setVisibility(View.VISIBLE);
        if(imageURI != null) {
            String photopath = getPathFromUri(this,imageURI);

            try {
                ExifInterface exifInterface = new ExifInterface(photopath);

                TimeAndDate = exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP) +""+ exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
                lat = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                LatRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                lng =  exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                LongRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

                Lat = ConvertToLat(lat, LatRef);
                Lon = ConvertToLong(lng, LongRef);

                progressBar.setVisibility(View.GONE);
                TimeStamp.setText(TimeAndDate);
                Latitude.setText(Lat.toString());
                Longitude.setText(Lon.toString());


            } catch (Exception e) {
                //Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                Toast.makeText(this,"Image has no Location.Please choose Different Image.",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "photoUri not found!",
                    Toast.LENGTH_LONG).show();
        }
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

/** METHOD TO GET DETAILS FROM IMAGE END **/

    /** Convertin DMS to Decimal Lat START**/

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

    /** Convertin DMS to Decimal Lat END**/

    /** Convertin DMS to Decimal Long START **/

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

    /** Convertin DMS to Decimal Long END **/


    /** METHOD FOR ADDING INFO IN DATABASE START **/

    private void SubmitDeleteRequest() {
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        storage = FirebaseStorage.getInstance();

        progressBar.setVisibility(View.VISIBLE);
        DeleteReqID = UUID.randomUUID().toString();

        final StorageReference storageReference = storage.getReference().child("Pothole Delete Request").child(uid).child(DeleteReqID).child("PotholeDeleteRequestImage."+Imageextention);
        storageReference.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                storage.getReference().child("Pothole Delete Request").child(uid).child(DeleteReqID).child("PotholeDeleteRequestImage."+Imageextention).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        //Log.i("Image URL",url);
                        DeleteReqData deleteReqData = new DeleteReqData(DeleteReqID,potholeID,potholeImageURL,url,latitude,Lat.toString(),longitude,Lon.toString(),timestamp,TimeAndDate,description,Desc,addedByUID);
                        FirebaseDatabase.getInstance().getReference().child("Pothole Delete Request").child(uid).child(DeleteReqID).setValue(deleteReqData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(PotholeDeleteReq_user.this, "Delete Request Sent Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(PotholeDeleteReq_user.this,UserSide.class);
                                    startActivity(i);
                                    finish();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(PotholeDeleteReq_user.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PotholeDeleteReq_user.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PotholeDeleteReq_user.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** METHOD FOR ADDING INFO IN DATABASE END **/
}

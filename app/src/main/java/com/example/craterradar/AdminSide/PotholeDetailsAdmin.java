package com.example.craterradar.AdminSide;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.craterradar.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PotholeDetailsAdmin extends AppCompatActivity implements OnMapReadyCallback{
    ActionBar actionBar;
    ProgressBar progress;
    CircleImageView potholeImage;
    MapView mapView;
    GoogleMap googleMap;
    TextView potholeID,potholeDangerLevel,potholeUploadedBy,potholeTimeStamp,potholeDescription;
    String PotholeImageURL,PotholeID,PotholeDangerLevel,PotholeUploadedBy,PotholeTimeStamp,PotholeDescription,PotholeLat,PotholeLong;
    Uri Imageuri;
    LatLng latLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pothole_details_admin);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        potholeImage = findViewById(R.id.potholePic_details_admin);
        mapView = findViewById(R.id.pothole_details_map_admin);
        potholeID = findViewById(R.id.potholeID_details_admin);
        potholeDangerLevel = findViewById(R.id.potholeDanger_details_admin);
        potholeUploadedBy = findViewById(R.id.pothoeUploadedBy_details_admin);
        potholeTimeStamp = findViewById(R.id.PotholeTimes_details_admin);
        potholeDescription = findViewById(R.id.potholeDescription_details_admin);
        progress = findViewById(R.id.potholeDetails_progress_admin);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        getDetails();
        mapView.getMapAsync(this);
    }

    private void getDetails() {
        progress.setVisibility(View.VISIBLE);
        Bundle data = getIntent().getExtras();
        PotholeImageURL = data.getString("PotholeImageUrl");
        PotholeID = data.getString("PotholeID");
        PotholeDangerLevel = data.getString("PotholeDangerLevel");
        PotholeUploadedBy = data.getString("PotholeUploadedBy");
        PotholeTimeStamp = data.getString("PotholeTimeStamp");
        PotholeDescription = data.getString("PotholeDescription");
        PotholeLat = data.getString("PotholeLat");
        PotholeLong = data.getString("PotholeLong");
        //String to Image URI and URI to Image View
        Imageuri = Uri.parse(PotholeImageURL);
        actionBar.setTitle(PotholeID);
        Picasso.get().load(Imageuri).into(potholeImage);
        //String latlong to LatLong Convertion
        latLng = new LatLng(Double.parseDouble(PotholeLat),Double.parseDouble(PotholeLong));
        potholeID.setText(PotholeID);
        potholeDangerLevel.setText(PotholeDangerLevel);
        potholeTimeStamp.setText(PotholeTimeStamp);
        potholeDescription.setText(PotholeDescription);
        potholeUploadedBy.setText(PotholeUploadedBy);
        progress.setVisibility(View.GONE);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,13f));
        this.googleMap.addMarker(new MarkerOptions().position(latLng));
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.getUiSettings().setMapToolbarEnabled(false);
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
